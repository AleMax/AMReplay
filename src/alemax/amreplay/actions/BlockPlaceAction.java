package alemax.amreplay.actions;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockPlaceAction extends Action {

    private int x;
    private int y;
    private int z;

    public BlockPlaceAction() {}

    public BlockPlaceAction(long startTimeStamp, int x, int y, int z) {
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
    public BlockPlaceAction fromBytes(Integer index, byte[] bytes) {
        BlockPlaceAction action = new BlockPlaceAction();

        action.timeStamp = ((bytes[index++] & 0xFF) << 56) |
                ((bytes[index++] & 0xFF) << 48) |
                ((bytes[index++] & 0xFF) << 40) |
                ((bytes[index++] & 0xFF) << 32) |
                ((bytes[index++] & 0xFF) << 24) |
                ((bytes[index++] & 0xFF) << 16) |
                ((bytes[index++] & 0xFF) << 8) |
                ((bytes[index++] & 0xFF) << 0);

        action.x = ((bytes[index++] & 0xFF) << 24) |
                ((bytes[index++] & 0xFF) << 16) |
                ((bytes[index++] & 0xFF) << 8) |
                ((bytes[index++] & 0xFF) << 0);

        action.y = ((bytes[index++] & 0xFF) << 24) |
                ((bytes[index++] & 0xFF) << 16) |
                ((bytes[index++] & 0xFF) << 8) |
                ((bytes[index++] & 0xFF) << 0);

        action.z = ((bytes[index++] & 0xFF) << 24) |
                ((bytes[index++] & 0xFF) << 16) |
                ((bytes[index++] & 0xFF) << 8) |
                ((bytes[index++] & 0xFF) << 0);

        //For the last byte
        index++;

        return action;
    }

    @Override
    public String toString() {
        return "BlockBreakAction after " + (timeStamp / 1000.0) + "s at " + x + " " + y + " " + z;
    }

}
