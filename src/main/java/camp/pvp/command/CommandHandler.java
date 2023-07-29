package camp.pvp.command;

import camp.pvp.NetworkHelper;
import camp.pvp.command.framework.Command;
import camp.pvp.command.framework.CommandFramework;
import camp.pvp.command.impl.CommandInfoCommand;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler {

    private NetworkHelper plugin;
    private CommandFramework commandFramework;
    private List<Object> cmds;

    public CommandHandler(NetworkHelper plugin) {
        this.plugin = plugin;
        commandFramework = new CommandFramework(plugin);
        cmds = new ArrayList<>();
        registerCommand(new CommandInfoCommand());
    }

    public void registerCommand(Object o) {
        commandFramework.registerCommands(o);
        cmds.add(o);
    }

    public Object getCommand(String name) {
        for (Object o : cmds) {
            for (Method method : o.getClass().getMethods()) {
                if (method.getAnnotation(Command.class) != null) {
                    Command command = method.getAnnotation(Command.class);
                    List<String> list = new ArrayList<>(Arrays.asList(command.aliases()));
                    if (command.name().equalsIgnoreCase(name) || list.contains(name)) {
                        return o;
                    }
                }
            }
        }
        return null;
    }

    public List<String> getAliases(Object o, String name) {
        List<String> aliases = new ArrayList<>();
        for (Method method : o.getClass().getMethods()) {
            if (method.getAnnotation(Command.class) != null) {
                Command command = method.getAnnotation(Command.class);
                List<String> list = new ArrayList<>(Arrays.asList(command.aliases()));
                if (command.name().equalsIgnoreCase(name) || list.contains(name)) {
                    aliases.add(command.name().toLowerCase());
                    Arrays.stream(command.aliases()).forEach(a -> aliases.add(a.toLowerCase()));
                }
            }
        }
        return aliases;
    }

    public String getPermission(Object o, String name) {
        for (Method method : o.getClass().getMethods()) {
            if (method.getAnnotation(Command.class) != null) {
                Command command = method.getAnnotation(Command.class);
                List<String> list = new ArrayList<>(Arrays.asList(command.aliases()));
                if (command.name().equalsIgnoreCase(name) || list.contains(name)) {
                    return command.permission();
                }
            }
        }
        return null;
    }
}
