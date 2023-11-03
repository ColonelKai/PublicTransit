package org.colonelkai.publictransit.commands;

import org.colonelkai.publictransit.PublicTransit;
import org.core.command.ArgumentLauncher;
import org.core.command.CommandLauncher;
import org.core.command.argument.ArgumentCommand;
import org.core.platform.plugin.Plugin;
import org.core.source.command.CommandSource;
import org.core.utils.Singleton;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PublicTransitCommandLauncher implements ArgumentLauncher, CommandLauncher {

    private final Singleton<Set<ArgumentCommand>> commands;

    private final String name;

    public static final String PUBLIC_TRANSIT = "publictransit";
    public static final String TRAVEL = "travel";

    public PublicTransitCommandLauncher(String name) {
        this.name = name;
        this.commands = new Singleton<>(() -> Arrays.stream(Commands.class.getDeclaredFields()).filter(field -> {
            ForCommand cmdMeta = field.getAnnotation(ForCommand.class);
            if (null == cmdMeta) {
                return name.equals(PUBLIC_TRANSIT);
            }
            return name.equals(cmdMeta.name());
        }).map(field -> {
            try {
                return field.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).filter(cmd -> cmd instanceof ArgumentCommand).map(cmd -> (ArgumentCommand) cmd).collect(Collectors.toSet()));
    }

    @Override
    public Set<ArgumentCommand> getCommands() {
        return this.commands.get();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "all public transit commands";
    }

    @Override
    public Plugin getPlugin() {
        return PublicTransit.getPlugin();
    }

    @Override
    public boolean hasPermission(CommandSource source) {
        return this.getCommands().stream().anyMatch(cmd -> cmd.hasPermission(source));
    }
}
