package alemax.amreplay.actions;

import alemax.amreplay.AMInteger;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

import java.nio.ByteBuffer;

public class BlockBreakAction extends Action {

    private int x;
    private int y;
    private int z;

    public BlockBreakAction() {}

    public BlockBreakAction(long startTimeStamp, int x, int y, int z) {
        super(startTimeStamp);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public byte getActionID() {
        return 1;
    }

    @Override
    public void onReplay(World world) {
        Block block = world.getBlockAt(x, y ,z);
        block.setType(Material.AIR);
        block.getBlockData();
    }

    /**
     * (ActionID: 0x01)
     * 8 bytes: timestamp
     * 4 bytes: x
     * 4 bytes: y
     * 4 bytes: z
     * 1 byte: 0x00 (for later additions)
     * --- 21
     */
    @Override
    public byte[] toBytes() {
        byte[] bytes = new byte[21];

        bytes[0] = (byte) (timeStamp >>> 56);
        bytes[1] = (byte) (timeStamp >>> 48);
        bytes[2] = (byte) (timeStamp >>> 40);
        bytes[3] = (byte) (timeStamp >>> 32);
        bytes[4] = (byte) (timeStamp >>> 24);
        bytes[5] = (byte) (timeStamp >>> 16);
        bytes[6] = (byte) (timeStamp >>> 8);
        bytes[7] = (byte) (timeStamp >>> 0);

        bytes[8] = (byte) (x >>> 24);
        bytes[9] = (byte) (x >>> 16);
        bytes[10] = (byte) (x >>> 8);
        bytes[11] = (byte) (x >>> 0);

        bytes[12] = (byte) (y >>> 24);
        bytes[13] = (byte) (y >>> 16);
        bytes[14] = (byte) (y >>> 8);
        bytes[15] = (byte) (y >>> 0);

        bytes[16] = (byte) (z >>> 24);
        bytes[17] = (byte) (z >>> 16);
        bytes[18] = (byte) (z >>> 8);
        bytes[19] = (byte) (z >>> 0);

        bytes[20] = 0;

        return bytes;
    }

    @Override
    public BlockBreakAction fromBytes(AMInteger index, byte[] bytes) {
        BlockBreakAction action = new BlockBreakAction();

        action.timeStamp = ((bytes[index.value] & 0xFF) << 56) |
                ((bytes[index.value + 1] & 0xFF) << 48) |
                ((bytes[index.value + 2] & 0xFF) << 40) |
                ((bytes[index.value + 3] & 0xFF) << 32) |
                ((bytes[index.value + 4] & 0xFF) << 24) |
                ((bytes[index.value + 5] & 0xFF) << 16) |
                ((bytes[index.value + 6] & 0xFF) << 8) |
                ((bytes[index.value + 7] & 0xFF) << 0);

        action.x = ((bytes[index.value + 8] & 0xFF) << 24) |
                ((bytes[index.value + 9] & 0xFF) << 16) |
                ((bytes[index.value + 10] & 0xFF) << 8) |
                ((bytes[index.value + 11] & 0xFF) << 0);

        action.y = ((bytes[index.value + 12] & 0xFF) << 24) |
                ((bytes[index.value + 13] & 0xFF) << 16) |
                ((bytes[index.value + 14] & 0xFF) << 8) |
                ((bytes[index.value + 15] & 0xFF) << 0);

        action.z = ((bytes[index.value + 16] & 0xFF) << 24) |
                ((bytes[index.value + 17] & 0xFF) << 16) |
                ((bytes[index.value + 18] & 0xFF) << 8) |
                ((bytes[index.value + 19] & 0xFF) << 0);

        index.value += 21;

        return action;
    }

    @Override
    public String toString() {
        return "BlockBreakAction after " + (timeStamp / 1000.0) + "s at " + x + " " + y + " " + z;
    }

}
