package alemax.amreplay;

import alemax.amreplay.actions.Action;
import alemax.amreplay.actions.BlockBreakAction;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class RecordListener implements Listener {

    private ArrayList<Action> actions;
    private long startupTime;
    private String name;
    private Server server;

    private String dataFolderPath;
    private File folder;

    public RecordListener(Server server) {
        //dataFolderPath = "/" + AMReplay.PLUGIN_NAME + "/";
        actions = new ArrayList<Action>();
        startupTime = System.currentTimeMillis();
        this.server = server;
        LocalDateTime date = LocalDateTime.now();
        this.name = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth() + "-" + date.getHour() + "-" + date.getMinute() + "-" + date.getSecond();

        createFolder();
        copyWorld();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        event.getPlayer().sendMessage(event.getBlock().getType().name());
        BlockBreakAction action = new BlockBreakAction(startupTime, block.getX(), block.getY(), block.getZ());
        actions.add(action);

    }

    private void copyWorld() {
        World overworld = server.getWorlds().get(0);
        System.out.println(overworld.getName());
        String overworldPath = server.getWorldContainer().getAbsolutePath();
        overworldPath = overworldPath.substring(0, overworldPath.length() - 1) + overworld.getName();

        String newOverworldPath = folder.getAbsolutePath() + "/" + overworld.getName() + "/";
        File newOverworldFile = new File(newOverworldPath);
        newOverworldFile.mkdir();

        try {
            FileUtils.copyDirectory(Paths.get(overworldPath + "/"), Paths.get(newOverworldPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFolder() {
        File dataFolder = server.getPluginManager().getPlugin(AMReplay.PLUGIN_NAME).getDataFolder();
        dataFolderPath = dataFolder.getAbsolutePath() + "/";
        if(!dataFolder.isDirectory()) {
            dataFolder.mkdir();
        }
        folder = new File(dataFolderPath + name);
        if(!folder.mkdir()) {
            System.out.println("Replay folder couldn't be created!");
            server.shutdown();
        }
    }

    public void save() {

    }

    public void addAction(Action action) {
        this.actions.add(action);
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        File newFolder = new File(dataFolderPath + name);
        if(folder.renameTo(newFolder)) {
            this.name = name;
            this.folder = newFolder;
            return true;
        }
        return false;
    }

    public long getStartupTime() {
        return startupTime;
    }
}
