package com.lilong.designpattern;

/**
 * 备忘录模式
 * 用一个备忘录对象去保存目标对象的状态,以便新的目标对象可以读取这些状态
 * 备忘录对象由专门的存储器对象来保存
 */

public class PatternMemo {
    public static void main(String[] args) {

        Instance oldInstance = new Instance();
        oldInstance.setProgress(66);
        oldInstance.saveInstanceState();
        System.out.println(oldInstance);

        Instance newInstance = new Instance();
        newInstance.restoreInstanceState();
        System.out.println(newInstance);
    }
}

/**
 * 抽象的待备忘录的目标对象
 */
interface Memoable {
    void saveInstanceState();

    void restoreInstanceState();
}

class Instance implements Memoable {

    private int progress;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public void saveInstanceState() {
        Memo memo = MemoStore.getInstance().getMemo();
        memo.setProgress(getProgress());
        MemoStore.getInstance().storeMemo(memo);
    }

    @Override
    public void restoreInstanceState() {
        int progress = MemoStore.getInstance().getMemo().getProgress();
        setProgress(progress);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + hashCode() + ", progress = " + getProgress();
    }
}

class Memo {
    private int progress;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}

class MemoStore {

    private volatile static MemoStore memoStore;

    private MemoStore() {
    }

    public static MemoStore getInstance() {
        if (memoStore == null) {
            synchronized (MemoStore.class) {
                if (memoStore == null) {
                    memoStore = new MemoStore();
                }
            }
        }
        return memoStore;
    }

    private Memo memo;

    public Memo getMemo() {
        return memo == null ? new Memo() : memo;
    }

    public void storeMemo(Memo memo) {
        this.memo = memo;
    }
}




