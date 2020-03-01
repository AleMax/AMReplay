package alemax.amreplay.actions;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import org.bukkit.World;

public abstract class Action {

    protected long timeStamp;

    public Action() {}

    public Action(long startTimeStamp) {
        timeStamp = System.currentTimeMillis() - startTimeStamp;
    }

    public abstract byte getActionID();

    public abstract void onReplay(World world);

    public abstract byte[] toBytes();

    public abstract Action fromBytes(Integer index, byte[] bytes);

    @Override
    public abstract String toString();

    public long getTimeStamp() {
        return timeStamp;
    }
}
