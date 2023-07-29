package camp.pvp.command.impl;

import camp.pvp.NetworkHelper;
import camp.pvp.command.CommandHandler;
import camp.pvp.command.framework.Command;
import camp.pvp.command.framework.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.List;

public class CommandInfoCommand {

    @Command(name = "commandinfo", aliases = {"cmdinfo"}, permission = "core.commands.commandinfo")
    public void commandInfo(CommandArgs args) {
        if (args.length() < 1) {
            args.getSender().sendMessage(ChatColor.RED + "Usage: /" + args.getLabel() + " <command>");
            return;
        }
        CommandHandler commandHandler = NetworkHelper.getInstance().getCommandHandler();
        String node = args.getArgs(0).toLowerCase();

        Object command = commandHandler.getCommand(node);

        //Check if command exists
        if (command == null) {
            args.getSender().sendMessage(ChatColor.RED + "Command not found.");
            return;
        }
        //Get permission of command
        String permission = commandHandler.getPermission(command, node);

        //Get all aliases of command
        List<String> aliases = commandHandler.getAliases(command, node);
        aliases.remove(node);

        //Return the name of the plugin that owns this command
        args.getSender().sendMessage(ChatColor.GOLD + "Command " + ChatColor.WHITE + node + ChatColor.GOLD + " belongs to the plugin " + ChatColor.WHITE + JavaPlugin.getProvidingPlugin(command.getClass()).getName() + ChatColor.GOLD + ".");
        if (!permission.isEmpty()) {
            args.getSender().sendMessage(ChatColor.GOLD + "Permission: " + ChatColor.WHITE + permission);
        }
        if (aliases.size() > 0) {
            args.getSender().sendMessage(ChatColor.GOLD + "Aliases: " + ChatColor.WHITE + join(aliases, ", "));
        }
        return;
    }

    public static String join(List<String> pieces, String separator) {
        StringBuilder buffer = new StringBuilder();
        Iterator iter = pieces.iterator();

        while(iter.hasNext()) {
            buffer.append((String)iter.next());
            if (iter.hasNext()) {
                buffer.append(separator);
            }
        }

        return buffer.toString();
    }
}
