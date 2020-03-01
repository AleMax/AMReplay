package alemax.amreplay.actions;

import alemax.amreplay.AMInteger;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.event.block.BlockDamageEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BlockPlaceAction extends Action {

    private int x;
    private int y;
    private int z;
    private String blockType;
    private ArrayList<Byte> additionalBytes;

    public BlockPlaceAction() {}

    public BlockPlaceAction(long startTimeStamp, int x, int y, int z, Block block) {
        super(startTimeStamp);
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockType = block.getType().name();
        additionalBytes = new ArrayList<Byte>();

        /*
        0x01: Rotatable: 1 byte --> Lookup
        0x02: Orientable: 1 byte --> lookup
        0x03: Directional: 1 byte --> lookup
        0x04: Bisected: 1 byte --> lookup
        0x05: Slab: 1 byte --> lookup
         */
        BlockData data = block.getBlockData();
        if(data instanceof Rotatable) {
            additionalBytes.add((byte) 0x01);
            BlockFace blockFace = ((Rotatable) data).getRotation();
            switch(blockFace) {
                case NORTH: additionalBytes.add((byte) 0x01); break;
                case EAST: additionalBytes.add((byte) 0x02); break;
                case SOUTH: additionalBytes.add((byte) 0x03); break;
                case WEST: additionalBytes.add((byte) 0x04); break;
                case UP: additionalBytes.add((byte) 0x05); break;
                case DOWN: additionalBytes.add((byte) 0x06); break;
                case NORTH_EAST: additionalBytes.add((byte) 0x07); break;
                case NORTH_WEST: additionalBytes.add((byte) 0x08);
                case SOUTH_EAST: additionalBytes.add((byte) 0x09); break;
                case SOUTH_WEST: additionalBytes.add((byte) 0x0A); break;
                case WEST_NORTH_WEST: additionalBytes.add((byte) 0x0B); break;
                case NORTH_NORTH_WEST: additionalBytes.add((byte) 0x0C); break;
                case NORTH_NORTH_EAST: additionalBytes.add((byte) 0x0D); break;
                case EAST_NORTH_EAST: additionalBytes.add((byte) 0x0E); break;
                case EAST_SOUTH_EAST: additionalBytes.add((byte) 0x0F); break;
                case SOUTH_SOUTH_EAST: additionalBytes.add((byte) 0x10); break;
                case SOUTH_SOUTH_WEST: additionalBytes.add((byte) 0x11); break;
                case WEST_SOUTH_WEST: additionalBytes.add((byte) 0x12); break;
                case SELF: additionalBytes.add((byte) 0x13); break;
                default: additionalBytes.add((byte) 0x01); break;
            }

        }
        if(data instanceof Orientable) {
            additionalBytes.add((byte) 0x02);
            Axis axis = ((Orientable) data).getAxis();
            switch(axis) {
                case X: additionalBytes.add((byte) 0x01); break;
                case Y: additionalBytes.add((byte) 0x02); break;
                case Z: additionalBytes.add((byte) 0x03); break;
                default: additionalBytes.add((byte) 0x01); break;
            }
        }
        if(data instanceof Directional) {
            additionalBytes.add((byte) 0x03);
            BlockFace blockFace = ((Directional) data).getFacing();
            switch(blockFace) {
                case NORTH: additionalBytes.add((byte) 0x01); break;
                case EAST: additionalBytes.add((byte) 0x02); break;
                case SOUTH: additionalBytes.add((byte) 0x03); break;
                case WEST: additionalBytes.add((byte) 0x04); break;
                case UP: additionalBytes.add((byte) 0x05); break;
                case DOWN: additionalBytes.add((byte) 0x06); break;
                case NORTH_EAST: additionalBytes.add((byte) 0x07); break;
                case NORTH_WEST: additionalBytes.add((byte) 0x08);
                case SOUTH_EAST: additionalBytes.add((byte) 0x09); break;
                case SOUTH_WEST: additionalBytes.add((byte) 0x0A); break;
                case WEST_NORTH_WEST: additionalBytes.add((byte) 0x0B); break;
                case NORTH_NORTH_WEST: additionalBytes.add((byte) 0x0C); break;
                case NORTH_NORTH_EAST: additionalBytes.add((byte) 0x0D); break;
                case EAST_NORTH_EAST: additionalBytes.add((byte) 0x0E); break;
                case EAST_SOUTH_EAST: additionalBytes.add((byte) 0x0F); break;
                case SOUTH_SOUTH_EAST: additionalBytes.add((byte) 0x10); break;
                case SOUTH_SOUTH_WEST: additionalBytes.add((byte) 0x11); break;
                case WEST_SOUTH_WEST: additionalBytes.add((byte) 0x12); break;
                case SELF: additionalBytes.add((byte) 0x13); break;
                default: additionalBytes.add((byte) 0x01); break;
            }
        }
        if(data instanceof Bisected) {
            additionalBytes.add((byte) 0x04);
            Bisected.Half half = ((Bisected) data).getHalf();
            switch(half) {
                case TOP: additionalBytes.add((byte) 0x01); break;
                case BOTTOM: additionalBytes.add((byte) 0x02); break;
                default: additionalBytes.add((byte) 0x02); break;
            }
        }
        if(data instanceof Slab) {
            additionalBytes.add((byte) 0x05);
            Slab.Type type = ((Slab) data).getType();
            switch(type) {
                case TOP: additionalBytes.add((byte) 0x01); break;
                case BOTTOM: additionalBytes.add((byte) 0x02); break;
                case DOUBLE: additionalBytes.add((byte) 0x03); break;
                default: additionalBytes.add((byte) 0x02); break;
            }
        }
        if(data instanceof Stairs) {
            additionalBytes.add((byte) 0x06);
            Stairs.Shape shape = ((Stairs) data).getShape();
            switch(shape) {
                case STRAIGHT: additionalBytes.add((byte) 0x01); break;
                case INNER_LEFT: additionalBytes.add((byte) 0x02); break;
                case INNER_RIGHT: additionalBytes.add((byte) 0x03); break;
                case OUTER_LEFT: additionalBytes.add((byte) 0x04); break;
                case OUTER_RIGHT: additionalBytes.add((byte) 0x05); break;
                default: additionalBytes.add((byte) 0x01); break;
            }
        }

        additionalBytes.add((byte) 0x00);

    }

    @Override
    public byte getActionID() {
        return 2;
    }

    @Override
    public void onReplay(World world) {
        Block block = world.getBlockAt(x, y ,z);
        block.setType(Material.getMaterial(blockType));
        if(additionalBytes.size() > 0) {
            int index = 0;
            BlockData data = block.getBlockData();
            while(additionalBytes.get(index) != 0) {
                if(additionalBytes.get(index) == 1) {
                    switch(additionalBytes.get(index + 1)) {
                        case 0x01: ((Rotatable) data).setRotation(BlockFace.NORTH); break;
                        case 0x02: ((Rotatable) data).setRotation(BlockFace.EAST); break;
                        case 0x03: ((Rotatable) data).setRotation(BlockFace.SOUTH); break;
                        case 0x04: ((Rotatable) data).setRotation(BlockFace.WEST); break;
                        case 0x05: ((Rotatable) data).setRotation(BlockFace.UP); break;
                        case 0x06: ((Rotatable) data).setRotation(BlockFace.DOWN); break;
                        case 0x07: ((Rotatable) data).setRotation(BlockFace.NORTH_EAST); break;
                        case 0x08: ((Rotatable) data).setRotation(BlockFace.NORTH_WEST); break;
                        case 0x09: ((Rotatable) data).setRotation(BlockFace.SOUTH_EAST); break;
                        case 0x0A: ((Rotatable) data).setRotation(BlockFace.SOUTH_WEST); break;
                        case 0x0B: ((Rotatable) data).setRotation(BlockFace.WEST_NORTH_WEST); break;
                        case 0x0C: ((Rotatable) data).setRotation(BlockFace.NORTH_NORTH_WEST); break;
                        case 0x0D: ((Rotatable) data).setRotation(BlockFace.NORTH_NORTH_EAST); break;
                        case 0x0E: ((Rotatable) data).setRotation(BlockFace.EAST_NORTH_EAST); break;
                        case 0x0F: ((Rotatable) data).setRotation(BlockFace.EAST_SOUTH_EAST); break;
                        case 0x10: ((Rotatable) data).setRotation(BlockFace.SOUTH_SOUTH_EAST); break;
                        case 0x11: ((Rotatable) data).setRotation(BlockFace.SOUTH_SOUTH_WEST); break;
                        case 0x12: ((Rotatable) data).setRotation(BlockFace.WEST_SOUTH_WEST); break;
                        case 0x13: ((Rotatable) data).setRotation(BlockFace.SELF); break;
                        default: ((Rotatable) data).setRotation(BlockFace.NORTH); break;
                    }

                    index += 2;
                } else if(additionalBytes.get(index) == 2) {
                    switch(additionalBytes.get(index + 1)) {
                        case 0x01: ((Orientable) data).setAxis(Axis.X); break;
                        case 0x02: ((Orientable) data).setAxis(Axis.Y); break;
                        case 0x03: ((Orientable) data).setAxis(Axis.Z); break;
                        default: ((Orientable) data).setAxis(Axis.X); break;
                    }
                    index += 2;
                } else if(additionalBytes.get(index) == 3) {
                    switch(additionalBytes.get(index + 1)) {
                        case 0x01: ((Directional) data).setFacing(BlockFace.NORTH); break;
                        case 0x02: ((Directional) data).setFacing(BlockFace.EAST); break;
                        case 0x03: ((Directional) data).setFacing(BlockFace.SOUTH); break;
                        case 0x04: ((Directional) data).setFacing(BlockFace.WEST); break;
                        case 0x05: ((Directional) data).setFacing(BlockFace.UP); break;
                        case 0x06: ((Directional) data).setFacing(BlockFace.DOWN); break;
                        case 0x07: ((Directional) data).setFacing(BlockFace.NORTH_EAST); break;
                        case 0x08: ((Directional) data).setFacing(BlockFace.NORTH_WEST); break;
                        case 0x09: ((Directional) data).setFacing(BlockFace.SOUTH_EAST); break;
                        case 0x0A: ((Directional) data).setFacing(BlockFace.SOUTH_WEST); break;
                        case 0x0B: ((Directional) data).setFacing(BlockFace.WEST_NORTH_WEST); break;
                        case 0x0C: ((Directional) data).setFacing(BlockFace.NORTH_NORTH_WEST); break;
                        case 0x0D: ((Directional) data).setFacing(BlockFace.NORTH_NORTH_EAST); break;
                        case 0x0E: ((Directional) data).setFacing(BlockFace.EAST_NORTH_EAST); break;
                        case 0x0F: ((Directional) data).setFacing(BlockFace.EAST_SOUTH_EAST); break;
                        case 0x10: ((Directional) data).setFacing(BlockFace.SOUTH_SOUTH_EAST); break;
                        case 0x11: ((Directional) data).setFacing(BlockFace.SOUTH_SOUTH_WEST); break;
                        case 0x12: ((Directional) data).setFacing(BlockFace.WEST_SOUTH_WEST); break;
                        case 0x13: ((Directional) data).setFacing(BlockFace.SELF); break;
                        default: ((Directional) data).setFacing(BlockFace.NORTH); break;
                    }
                    index += 2;
                } else if(additionalBytes.get(index) == 4) {
                    switch(additionalBytes.get(index + 1)) {
                        case 0x01: ((Bisected) data).setHalf(Bisected.Half.TOP); break;
                        case 0x02: ((Bisected) data).setHalf(Bisected.Half.BOTTOM); break;
                        default: ((Bisected) data).setHalf(Bisected.Half.BOTTOM); break;
                    }
                    index += 2;
                } else if(additionalBytes.get(index) == 5) {
                    switch(additionalBytes.get(index + 1)) {
                        case 0x01: ((Slab) data).setType(Slab.Type.TOP); break;
                        case 0x02: ((Slab) data).setType(Slab.Type.BOTTOM); break;
                        case 0x03: ((Slab) data).setType(Slab.Type.DOUBLE); break;
                        default: ((Slab) data).setType(Slab.Type.BOTTOM); break;
                    }
                    index += 2;
                } else if(additionalBytes.get(index) == 6) {
                    switch(additionalBytes.get(index + 1)) {
                        case 0x01: ((Stairs) data).setShape(Stairs.Shape.STRAIGHT); break;
                        case 0x02: ((Stairs) data).setShape(Stairs.Shape.INNER_LEFT); break;
                        case 0x03: ((Stairs) data).setShape(Stairs.Shape.INNER_RIGHT); break;
                        case 0x04: ((Stairs) data).setShape(Stairs.Shape.OUTER_LEFT); break;
                        case 0x05: ((Stairs) data).setShape(Stairs.Shape.OUTER_RIGHT); break;
                        default: ((Stairs) data).setShape(Stairs.Shape.STRAIGHT); break;
                    }
                    index += 2;
                }
            }
            block.setBlockData(data);
        }
    }

    /**
     * (ActionID: 0x01)
     * 8 bytes: timestamp
     * 4 bytes: x
     * 4 bytes: y
     * 4 bytes: z
     * 1 byte: Blocktype String length
     * x bytes: String
     * 4 bytes: additional data length
     * y bytes: additional data
     */
    @Override
    public byte[] toBytes() {
        byte[] bytes = new byte[21 + blockType.length() + 4 + additionalBytes.size()];

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

        bytes[20] = (byte) blockType.length();

        byte[] stringbytes = blockType.getBytes();

        for(int i = 0; i < stringbytes.length; i++) {
            bytes[21 + i] = stringbytes[i];
        }

        bytes[21 + stringbytes.length] = (byte) (additionalBytes.size() >>> 24);
        bytes[22 + stringbytes.length] = (byte) (additionalBytes.size() >>> 16);
        bytes[23 + stringbytes.length] = (byte) (additionalBytes.size() >>> 8);
        bytes[24 + stringbytes.length] = (byte) (additionalBytes.size() >>> 0);

        for(int i = 0; i < additionalBytes.size(); i++) {
            bytes[21 + 4 + stringbytes.length + i] = additionalBytes.get(i);
        }

        return bytes;
    }

    @Override
    public BlockPlaceAction fromBytes(AMInteger index, byte[] bytes) {
        BlockPlaceAction action = new BlockPlaceAction();

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

        int length = bytes[index.value + 20];

        byte[] stringbytes = new byte[length];
        for(int i = 0; i < length; i++) {
            stringbytes[i] = bytes[index.value + 21 + i];
        }
        action.blockType = new String(stringbytes);

        length = 0;
        length = ((bytes[index.value + 21 + stringbytes.length] & 0xFF) << 24) |
                ((bytes[index.value + 22 + stringbytes.length] & 0xFF) << 16) |
                ((bytes[index.value + 23 + stringbytes.length] & 0xFF) << 8) |
                ((bytes[index.value + 24 + stringbytes.length] & 0xFF) << 0);


        action.additionalBytes = new ArrayList<Byte>();
        for(int i = 0; i < length; i++) {
            action.additionalBytes.add((bytes[index.value + 21 + 4 + stringbytes.length + i]));
        }

        index.value += 21 + 4 + stringbytes.length + action.additionalBytes.size();

        return action;
    }

    @Override
    public String toString() {
        return "BlockPlaceAction after " + (timeStamp / 1000.0) + "s at " + x + " " + y + " " + z + " (" + blockType + ")";
    }

}
