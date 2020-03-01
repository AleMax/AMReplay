package alemax.amreplay;

import org.bukkit.plugin.java.JavaPlugin;


public class AMReplay extends JavaPlugin {

    public static String PLUGIN_NAME = "AMReplay";

    RecordListener listener;
    ReplayCommand command;

    public RecordListener replay;

    @Override
    public void onEnable() {

        System.out.println("Setting up Replay");

        listener = new RecordListener(getServer(), true, this);
        getServer().getPluginManager().registerEvents(listener, this);

        command = new ReplayCommand(listener, getServer(), this);
        this.getCommand("replay").setExecutor(command);
    }

    @Override
    public void onDisable() {
        System.out.println("Saving replay");
        if(replay != null) {
            replay.closeReplay();
        }
        listener.save();
    }


}
