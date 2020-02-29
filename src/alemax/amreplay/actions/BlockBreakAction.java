package alemax.amreplay.actions;

public class BlockBreakAction extends Action {

    private int x;
    private int y;
    private int z;

    public BlockBreakAction(long startTimeStamp, int x, int y, int z) {
        super(startTimeStamp);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void onReplay() {

    }

    @Override
    public byte getActionID() {
        return 1;
    }

    @Override
    public String toString() {
        return "BlockBreakAction after " + (getTimeStamp() / 1000.0) + "s at " + x + " " + y + " " + z;
    }
}
