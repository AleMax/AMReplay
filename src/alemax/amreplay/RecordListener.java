package alemax.amreplay;

import alemax.amreplay.actions.Action;
import alemax.amreplay.actions.BlockBreakAction;
import alemax.amreplay.actions.BlockPlaceAction;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.FileUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class RecordListener implements Listener {

    AMReplay plugin;
    private ArrayList<Action> actions;
    private long startupTime;
    private String name;
    private Server server;

    private String dataFolderPath;
    private File folder;

    public boolean recordingMode;
    private int replayIndex;
    private int ticksPassed;
    static int TicksMillisecondsMultiplier = 50;

    public String worldFolderPath;
    public String worldName;

    private int taskID;

    public RecordListener(Server server, boolean recordingMode, AMReplay plugin) {
        actions = new ArrayList<Action>();
        this.plugin = plugin;

        this.server = server;
        this.recordingMode = recordingMode;

        if(recordingMode) {
            startupTime = System.currentTimeMillis();
            LocalDateTime date = LocalDateTime.now();
            this.name = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth() + "-" + date.getHour() + "-" + date.getMinute() + "-" + date.getSecond();
            createFolder();
            copyWorld();
        }


    }

    public void loadReplay(String name, String worldName) {
        if(!recordingMode) {
            this.name = name;
            File dataFolder = server.getPluginManager().getPlugin(AMReplay.PLUGIN_NAME).getDataFolder();
            dataFolderPath = dataFolder.getAbsolutePath() + "/";
            folder = new File(dataFolderPath + name);
            this.worldName = "AMReplay-" + name + "-" + worldName;
            worldFolderPath = server.getWorldContainer().getAbsolutePath();
            worldFolderPath = worldFolderPath.substring(0, worldFolderPath.length() - 1) + this.worldName;


            try {
                FileUtils.copyDirectory(Paths.get(folder.getAbsolutePath()+ "/world/"), Paths.get(worldFolderPath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            WorldCreator creator = new WorldCreator(this.worldName);
            creator.createWorld();
            server.createWorld(creator);
            //((Player) sender).teleport(new Location(Bukkit.getWorld("worldB"), ((Player) sender).getLocation().getX(), ((Player) sender).getLocation().getY(), ((Player) sender).getLocation().getZ()));

            try {
                File file = new File(folder + "/" + "replay.rpl");
                FileInputStream fos = new FileInputStream(file);
                try {

                    byte[] data = new byte[(int) file.length()];
                    fos.read(data);


                    if(data[0] == 65 && data[1] == 77 && data[2] == 82 && data[3] == 101
                            && data[4] == 112 && data[5] == 108 && data[6] == 97 && data[7] == 121) {
                        AMInteger index = new AMInteger(8);

                        Action[] actionLookupTable = {new BlockBreakAction(), new BlockPlaceAction()};
                        actions.clear();

                        while (index.value < data.length) {
                            for (Action action : actionLookupTable) {
                                if (data[index.value] == action.getActionID()) {
                                    index.value++;
                                    Action newAction = action.fromBytes(index, data);
                                    actions.add(newAction);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    fos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void watchReplay() {
        World world = server.getWorld(worldName);

        taskID = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if(replayIndex < actions.size()) {
                    while (actions.get(replayIndex).getTimeStamp() < ticksPassed * TicksMillisecondsMultiplier) {
                        actions.get(replayIndex).onReplay(world);
                        replayIndex++;
                        if (replayIndex == actions.size()) break;
                    }
                    ticksPassed++;
                }
            }
        }, 0l, 1l).getTaskId();
    }

    public void stopWatch() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public void teleportToReplay(Player player) {
        World replayWorld = server.getWorld(worldName);
        if(replayWorld != null) {
            player.teleport(new Location(replayWorld, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
        }
    }

    public void closeReplay() {
        stopWatch();
        World replayWorld = server.getWorld(worldName);
        if(replayWorld.getPlayers() != null) {
            for (Player player : replayWorld.getPlayers()) {
                player.teleport(new Location(server.getWorlds().get(0), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
            }
        }
        server.unloadWorld(worldName, false);
        FileUtils.deleteDirectory(new File(worldFolderPath));
    }




    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(recordingMode) {
            Block block = event.getBlock();
            BlockBreakAction action = new BlockBreakAction(startupTime, block.getX(), block.getY(), block.getZ());
            actions.add(action);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(recordingMode) {
            Block block = event.getBlock();
            BlockPlaceAction action = new BlockPlaceAction(startupTime, block.getX(), block.getY(), block.getZ(), block);
            actions.add(action);
        }
    }


    private void copyWorld() {
        World overworld = server.getWorlds().get(0);
        String overworldPath = server.getWorldContainer().getAbsolutePath();
        overworldPath = overworldPath.substring(0, overworldPath.length() - 1) + overworld.getName();

        String newOverworldPath = folder.getAbsolutePath() + "/" + overworld.getName() + "/";
        File newOverworldFile = new File(newOverworldPath);
        newOverworldFile.mkdir();

        try {
            FileUtils.copyDirectory(Paths.get(overworldPath + "/"), Paths.get(newOverworldPath));
            File uidat = new File(newOverworldPath + "/uid.dat");
            uidat.delete();
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
        if(recordingMode) {
            try {
                FileOutputStream fos = new FileOutputStream(folder + "/" + "replay.rpl");

                try {

                    fos.write(new byte[]{65,77,82,101,112,108,97,121}); //AMReplay

                    for(Action action : actions) {
                        fos.write(action.getActionID());
                        fos.write(action.toBytes());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    fos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            recordingMode = false;
        }
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
