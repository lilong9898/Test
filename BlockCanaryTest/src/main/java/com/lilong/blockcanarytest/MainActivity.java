package com.lilong.blockcanarytest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消息循环的关键在{@link MessageQueue}里:
 * (1) 其构造方法中调JNI方法nativeInit(), 在native层创建了Looper和MessageQueue(功能跟java层的不同, 仅仅是为了通过管道实现休眠/唤醒功能)
 * (2) native层的Looper的构造函数里, 会创建一个管道
 *     管道的读端mWakeReadPipeFd由JNI方法nativePollOnce(long ptr, int timeoutMillis)来读
 *     管道的写端mWakeWritePipeFd由JNI方法nativeWake(long ptr)来写
 *     综上, 管道是在native层, 但它的读写动作都是由java层触发的
 * (3) 消息队列是一个由{@link Message}串联成的单链表, 链表头是mMessages
 * (4) 读消息队列:
 *     next()方法, 其中:
 *     (4.1) 如果当前消息队列为空, nativePollOnce的timeout会在上个消息处理时被设置成-1,
 *           于是nativePollOnce会读取管道中的唤醒消息, 如果一直没有唤醒, 则会停在这个方法上(timeout为-1表示无限大)
 *           但此时主线程还是RUNNABLE状态, 未被阻塞, 只是不占用CPU资源了
 *     (4.2) 如果当前消息队列里还有未处理的消息, nativePollOnce的timeout会在上个消息处理时被设置为0, 于是这个方法直接timeout跳过去了
 *           后面会沿着链表寻找下一个{@link Message}
 *     (4.3) 如果有延时消息, nativePollOnce的timeout会被设置为这个延时, 使得延时消息时间每到时, 一直等待唤醒, 时间一到就timeout, 开始处理延时消息
 *     (4.4) 消息处理完(即发送给对应handler)后, 被从链表中移除
 * (5) 写消息队列:
 *     enqueueMessage(Message msg, long when)方法, 其中:
 *     (5.1) 将消息放入链表中的合适位置
 *     (5.2) 如果当前消息队列停在nativePollOnce()上了, 调nativeWake(), 向管道中写入一个字符"W"
 *           nativePollOnce作为管道的读端, 就会返回, (4.1)中的next()方法会继续执行, 处理链表中的消息
 * */
public class MainActivity extends Activity {

    private static final String TAG = "BTest";

    private Button btnSendMsgDelayed;
    private TextView tvMessageQueue;
    private Button btnClearMsgs;
    private Button btnSendBlockRunnable;
    private Handler handler;
    private SimpleDateFormat sdf;
    private Date date;


    private static final int MSG_TEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSendMsgDelayed = findViewById(R.id.btnSendMsgDelayed);
        tvMessageQueue = findViewById(R.id.tvMessageQueue);
        btnClearMsgs = findViewById(R.id.btnClearMsgs);
        btnSendBlockRunnable = findViewById(R.id.btnSendBlockRunnable);
        handler = new Handler();
        sdf = new SimpleDateFormat("HH:mm:ss");
        date = new Date();
        // 防止button自身的UI有关runnable(主要是跟click有关的)被发送到消息队列中去, 干扰消息队列信息的显示, 这里探测到DOWN事件就触发显示
        btnSendMsgDelayed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    handler.sendEmptyMessageDelayed(MSG_TEST, 3000);
                    tvMessageQueue.setText(getMessageQueueInfo());
                }
                return true;
            }
        });
        btnClearMsgs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    handler.removeMessages(MSG_TEST);
                    tvMessageQueue.setText(getMessageQueueInfo());
                }
                return true;
            }
        });
        btnSendBlockRunnable.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Thread.sleep(3000);
                            }catch (Exception e){}
                        }
                    });
                }
                return true;
            }
        });
    }

    private String getMessageQueueInfo(){
        Looper looper = handler.getLooper();
        StringBuilder messageQueueInfo = new StringBuilder();
            Message messageHead = getMessageHead(looper);
            if(messageHead != null){
                date.setTime(messageHead.getWhen());
                String when = sdf.format(date);
                messageQueueInfo.append("what = " + messageHead.what + " @ uptime = " + when);
                messageQueueInfo.append("\n");
                Message curMessage = messageHead;
                while(getMessageNext(curMessage) != null){
                    curMessage = getMessageNext(curMessage);
                    messageQueueInfo.append("what = " + curMessage.what + " @ uptime = " + when);
                    messageQueueInfo.append("\n");
                }
            }
        return messageQueueInfo.toString();
    }

    private Message getMessageHead(Looper looper){
        Message messageHead = null;
        try{
            Field messageQueueField = Looper.class.getDeclaredField("mQueue");
            messageQueueField.setAccessible(true);
            Object messageQueue = messageQueueField.get(looper);
            Field messageHeadField = messageQueue.getClass().getDeclaredField("mMessages");
            messageHeadField.setAccessible(true);
            messageHead = (Message) messageHeadField.get(messageQueue);
        }catch (Exception e){}
        return messageHead;
    }

    private Message getMessageNext(Message message){
        Message messageNext = null;
        try{
            Field messageNextField = Message.class.getDeclaredField("next");
            messageNextField.setAccessible(true);
            messageNext = (Message) messageNextField.get(message);
        }catch (Exception e){
        }
        return messageNext;
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(MSG_TEST);
        super.onDestroy();
    }
}
