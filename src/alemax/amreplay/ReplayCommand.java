package alemax.amreplay;

import alemax.amreplay.RecordListener;
import alemax.amreplay.actions.Action;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ReplayCommand implements CommandExecutor {

    AMReplay plugin;
    RecordListener recordListener;
    Server server;

    public ReplayCommand(RecordListener recordListener, Server server, AMReplay plugin) {
        this.recordListener = recordListener;
        this.server = server;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "\"/replay\"" + ChatColor.RESET + " " + ChatColor.GOLD + "Usage:\n"
                    + ChatColor.WHITE + "/replay " + ChatColor.GREEN + "setname " + ChatColor.RED + "<name>\n"
                    + ChatColor.WHITE + "/replay " + ChatColor.GREEN + "list\n"
                    + ChatColor.WHITE + "/replay " + ChatColor.GREEN + "currentActions\n"
                    + ChatColor.WHITE + "/replay " + ChatColor.GREEN + "stop\n"
                    + ChatColor.WHITE + "/replay " + ChatColor.GREEN + "load " + ChatColor.RED + "<name>" + ChatColor.RED + " <world>\n"
                    + ChatColor.WHITE + "/replay " + ChatColor.GREEN + "teleport"
                        );
            return true;
        } else if(args[0].equals("setname")) {
            if(args.length > 1) {
                String newName = args[1];
                if(recordListener.setName(args[1])) {
                    sender.sendMessage(ChatColor.GREEN + "Name successfully changed to " + args[1]);
                } else
                    sender.sendMessage(ChatColor.RED + "Could not change the name to " + args[1]);
            } else
                sender.sendMessage(ChatColor.RED + "Please specify a new name");
            return true;
        } else if(args[0].equals("currentActions")) {
            for(Action action : recordListener.getActions()) {
                sender.sendMessage(action.toString());
            }
            return true;
        } else if(args[0].equals("stop")) {

            recordListener.save();
            recordListener.recordingMode = false;
            return true;

        } else if(args[0].equals("load")) {
            if(args.length > 2) {
                if(plugin.replay != null) {
                    plugin.replay.closeReplay();
                }

                plugin.replay = new RecordListener(server, false, plugin);
                plugin.replay.loadReplay(args[1], args[2]);

            } else
                sender.sendMessage(ChatColor.RED + "Please specify a name and a world to load");
            return true;
        } else if(args[0].equals("teleport")) {

            if(sender instanceof Player) {
                if(plugin.replay != null) {
                    plugin.replay.teleportToReplay((Player) sender);
                }
            }

        } else if(args[0].equals("watch")) {
            if(plugin.replay != null) {
                plugin.replay.watchReplay();
            }

        } else if(args[0].equals("endwatch")) {
            if(plugin.replay != null) {
                plugin.replay.closeReplay();
            }

        } else if(args[0].equals("world")) {
            WorldCreator creator = new WorldCreator("worldB");
            creator.createWorld();
            World world = server.createWorld(creator);
            sender.sendMessage( "" + server.getWorlds().size());
            ((Player) sender).teleport(new Location(Bukkit.getWorld("worldB"), ((Player) sender).getLocation().getX(), ((Player) sender).getLocation().getY(), ((Player) sender).getLocation().getZ()));
        }
        return false;
    }
}
