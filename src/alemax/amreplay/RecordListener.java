package alemax.amreplay;

import alemax.amreplay.actions.Action;
import alemax.amreplay.actions.BlockBreakAction;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class RecordListener implements Listener {

    ArrayList<Action> actions;
    long startupTime;
    public boolean sound;

    public RecordListener() {
        actions = new ArrayList<Action>();
        startupTime = System.currentTimeMillis();
        sound = true;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockBreakAction action = new BlockBreakAction(startupTime, block.getX(), block.getY(), block.getZ());
        actions.add(action);
        if(sound) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
        }
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
}
