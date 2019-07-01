package com.lilong.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

/**
 * {@link Messenger}实现跨进程通信（服务端）
 * 本质上也是根据{@link IMessenger#Stub}类
 *      (1) 在客户端生成一个代理对象(调asInterface(IBinder)方法创建Stub.Proxy)
 *      (2) 在服务端生成一个委托对象(调onBind方法返回一个Stub的实现类的对象)
 *      所有代码都预装在framework中，所以不需要像aidl那样在编译时生成这些代码到app里
 * */
public class TestMessengerService extends Service {

    private static final int MSG_IPC_ADD_CALL_SERVER = 1;
    private static final int MSG_IPC_ADD_REPLY_CLIENT = 2;

    class ServerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_IPC_ADD_CALL_SERVER:
                    int number1 = msg.arg1;
                    int number2 = msg.arg2;
                    int result = number1 + number2;
                    Message replyMsg = Message.obtain();
                    replyMsg.what = MSG_IPC_ADD_REPLY_CLIENT;
                    replyMsg.arg1 = result;
                    try{
                        msg.replyTo.send(replyMsg);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private Handler serverHandler = new ServerHandler();
    private Messenger serverMessenger = new Messenger(serverHandler);

    @Override
    public IBinder onBind(Intent intent) {
        return serverMessenger.getBinder();
    }
}
