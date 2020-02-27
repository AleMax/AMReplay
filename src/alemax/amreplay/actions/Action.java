package alemax.amreplay.actions;

public abstract class Action {

    private long timeStamp;

    public Action(long startTimeStamp) {
        timeStamp = System.currentTimeMillis() - startTimeStamp;
    }

    public abstract void onReplay();

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public abstract String toString();
}
