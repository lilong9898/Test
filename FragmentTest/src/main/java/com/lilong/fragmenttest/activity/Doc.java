/**
 * (1) 某个布局容器, 先加上fragmentA, 再加上fragmentB, 不会触发fragmentA的onPause/onStop等方法
 * 只有当fragmentA被detach, remove或所属的activity结束时才触发
 *
 * (2) 某个布局容器, 先加上fragmentA, 再加上fragmentB, 会依次将fragmentA和fragmentB的rootView加到容器里
 * 所以两者的rootView都响应触摸事件
 *
 * (3) 使用backstack, 要调{@link FragmentTransaction#addToBackStack(String)}, 当按back键时会将之前transaction的反向操作
 *     使用backstack会影响fragment操作时的生命周期, 与不用backstack时不同
 *
 * {@link FragmentManager}是抽象类, 它的实现类是{@link FragmentManagerImpl}
 * {@link FragmentTransaction}是抽象类, 它的实现是{@link BackStackRecord}
 * 同时{@link BackStackRecord}也实现了{@link FragmentManagerImpl.OpGenerator}接口
 *
 * {@link FragmentManager#beginTransaction()}方法返回的实际上是{@link BackStackRecord}
 *
 * 调用{@link FragmentTransaction#add(Fragment, String)}, 其中
 * (1) 在这个FragmentTransaction上进行的每个动作, 会一一对应为指令{@link BackStackRecord#Op}
 * (2) 调{@link BackStackRecord#addOp}将(1)中的所有指令加入ArrayList<BackStackRecord#Op>里
 * (3) 调{@link BackStackRecord#commit}最终调到{@link FragmentManager#enqueueAction}其中
 *     (3.1) BackStackRecord实现了FragmentManagerImpl.OpGenerator接口, 所以有将自己加入到外界提供的BackStackRecord容器的能力
 *     (3.2) 这个BackStackRecord被加入到FragmentManager内部的, 类型为ArrayList<OpGenerator>, 名叫mPendingActions的容器里
 *     (3.3) FragmentManager的scheduleCommit方法将一个[runnable发送到所属activity的mHandler上], 这个handler是activity初始化的时候就生成的
 *     (3.4) runnable的内容是执行{@link FragmentManagerImpl#execPendingActions}
 *
 * {@link FragmentManagerImpl#execPendingActions}中:
 * (1) FragmentManager中的mPendingActions中的所有BackStackRecord, 被转移到FragmentManager中的另一个容器mTmpRecords中, mPendingActions清空
 * (2) 调FragmentManager的removeRedundantOperationsAndExecute方法, 其中会对mTmpRecords中的BackStackRecord重新排序, 去掉重复的和非必要的, 并进行优化
 *     其中经过FragmentManager的executeOpsTogether方法
 *            ->FragmentManager的executeOps方法
 *            ->BackStackRecord的executeOps方法
 *            ->最终调到FragmentManager的moveToState方法
 *            ->执行fragment的生命周期方法
 *
 * FragmentManager的代码较乱, 用到大量状态机也很复杂, 总之最后调到FragmentManager的moveToState方法, 它调的fragment的生命周期方法
 */

