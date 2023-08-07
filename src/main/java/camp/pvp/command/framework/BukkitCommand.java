package camp.pvp.command.framework;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Command Framework - BukkitCommand <br>
 * An implementation of Bukkit's Command class allowing for registering of
 * commands without plugin.yml
 *
 * @author minnymin3
 */
public class BukkitCommand extends org.bukkit.command.Command {

    private final Plugin owningPlugin;
    protected BukkitCompleter completer;
    private CommandExecutor executor;

    /**
     * A slimmed down PluginCommand
     *
     * @param name
     * @param owner
     */
    protected BukkitCommand(String label, CommandExecutor executor, Plugin owner) {
        super(label);
        this.executor = executor;
        this.owningPlugin = owner;
        this.usageMessage = "";
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        boolean success = false;

        if (!owningPlugin.isEnabled()) {
            return false;
        }

        if (!testPermission(sender)) {
            return true;
        }

        try {
            success = executor.onCommand(sender, this, commandLabel, args);
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin "
                    + owningPlugin.getDescription().getFullName(), ex);
        }

        if (!success && usageMessage.length() > 0) {
            for (String line : usageMessage.replace("<command>", commandLabel).split("\n")) {
                sender.sendMessage(line);
            }
        }

        return success;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args)
            throws CommandException, IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        List<String> completions = new ArrayList<>();

        int currentArg = args.length - 1;
        Bukkit.getOnlinePlayers().stream().filter(p -> !completions.contains(p.getName()) && p.getName().toLowerCase().startsWith(args[currentArg].toLowerCase())).forEach(p -> completions.add(p.getName()));

        return completions;
    }

}
