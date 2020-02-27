package alemax.amreplay;

import alemax.amreplay.actions.Action;
import alemax.amreplay.actions.ReplayCommand;
import org.bukkit.plugin.java.JavaPlugin;


public class AMReplay extends JavaPlugin {

    @Override
    public void onEnable() {

        RecordListener listener = new RecordListener();
        getServer().getPluginManager().registerEvents(listener, this);

        this.getCommand("replay").setExecutor(new ReplayCommand(listener));

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }


}
