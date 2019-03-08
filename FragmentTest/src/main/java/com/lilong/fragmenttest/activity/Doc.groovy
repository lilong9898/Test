import android.app.FragmentManager
import android.app.FragmentTransaction

/**
 * (1) 某个布局容器, 先加上fragmentA, 再加上fragmentB, 不会触发fragmentA的onPause/onStop等方法
 * 只有当fragmentA被detach, remove或所属的activity结束时才触发
 *
 * (2) 某个布局容器, 先加上fragmentA, 再加上fragmentB, 会依次将fragmentA和fragmentB的rootView加到容器里
 * 所以两者的rootView都响应触摸事件
 *
 * {@link FragmentManager}是抽象类, 它的实现类是{@link FragmentManagerImpl}
 * {@link FragmentTransaction}是抽象类, 它的实现是{@link BackStackRecord}
 * 同时{@link BackStackRecord}也实现了{@link FragmentManagerImpl.OpGenerator}接口
 *
 * {@link FragmentManager#beginTransaction()}方法返回的实际上是{@link BackStackRecord}
 *
 * 调用{@link FragmentTransaction#add(Fragment, String)}, 其中
 * (1) 动作会转换为指令{@link BackStackRecord#Op}
 * (2) 调{@link BackStackRecord#addOp}将(1)中的指令加入ArrayList<BackStackRecord#Op>里
 * (3) 调{@link BackStackRecord#commit}最终调到{@link FragmentManager#enqueueAction}其中
 *     (3.1) 之前的ArrayList<BackStackRecord#Op>被转换成{@link FragmentManager.OpGenerator}
 *     (3.2) {@link FragmentManager.OpGenerator}被加到ArrayList<OpGenerator>的mPendingActions里
 *     (3.3) {@link FragmentManager#scheduleCommit}将一个runnable发送到所属activity的mHandler上, 这个handler是activity初始化的时候就生成的
 *           runnable的内容是执行{@link FragmentManagerImpl#execPendingActions}
 *
 * {@link FragmentManagerImpl#execPendingActions}中:
 */