package alemax.amreplay.actions;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;

public abstract class Action {

    protected long timeStamp;

    public Action() {}

    public Action(long startTimeStamp) {
        timeStamp = System.currentTimeMillis() - startTimeStamp;
    }

    public abstract byte getActionID();

    public abstract void onReplay();

    public abstract byte[] toBytes();

    public abstract Action fromBytes(Integer index, byte[] bytes);

    @Override
    public abstract String toString();

}
