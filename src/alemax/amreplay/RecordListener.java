package alemax.amreplay;

import alemax.amreplay.actions.Action;
import alemax.amreplay.actions.BlockBreakAction;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class RecordListener implements Listener {

    ArrayList<Action> actions;
    long startupTime;

    public RecordListener() {
        actions = new ArrayList<Action>();
        startupTime = System.currentTimeMillis();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockBreakAction action = new BlockBreakAction(startupTime, block.getX(), block.getY(), block.getZ());
        actions.add(action);
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
}
