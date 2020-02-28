package alemax.amreplay.actions;

import alemax.amreplay.RecordListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplayCommand implements CommandExecutor {

    RecordListener recordListener;

    public ReplayCommand(RecordListener recordListener) {
        this.recordListener = recordListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(args.length == 0) {
                sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "\"/replay\"" + ChatColor.RESET + " " + ChatColor.GOLD + "Usage:\n"
                    + ChatColor.WHITE + "/replay " + ChatColor.GREEN + "setname " + ChatColor.RED + "<name>\n"
                    + ChatColor.WHITE + "/replay " + ChatColor.GREEN + "list\n"
                    + ChatColor.WHITE + "/replay " + ChatColor.GREEN + "currentActions\n"
                    + ChatColor.WHITE + "/replay " + ChatColor.GREEN + "sounds " + ChatColor.RED + "<on/off>"
                        );
                return true;
            } else if(args[0].equals("currentActions")) {
                for(Action action : recordListener.getActions()) {
                    sender.sendMessage(action.toString());
                }
                return true;
            } else if(args[0].equals("sounds")) {
                if(args.length > 1) {
                    if(args[1].equals("off")) {
                        if(recordListener.sound) {
                            recordListener.sound = false;
                            sender.sendMessage(ChatColor.RED + "Disabled Sounds!");
                        } else {
                            sender.sendMessage(ChatColor.GOLD + "Sounds already disabled");
                        }
                        return true;
                    } else if(args[1].equals("on")) {
                        if(!recordListener.sound) {
                            recordListener.sound = true;
                            sender.sendMessage(ChatColor.GREEN + "Enabled Sounds!");
                        } else {
                            sender.sendMessage(ChatColor.GOLD + "Sounds already enabled");
                        }
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
