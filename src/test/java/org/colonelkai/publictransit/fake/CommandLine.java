package org.colonelkai.publictransit.fake;

import org.colonelkai.publictransit.utils.lamda.ThrowableTriPredicate;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.context.CommandArgumentContext;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;
import org.core.source.command.CommandSource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CommandLine implements ArgumentCommand {

    private List<CommandArgument<?>> arguments;
    private ThrowableTriPredicate<CommandLine, CommandContext, String[], NotEnoughArguments> executor;

    CommandLine(CommandLineBuilder builder) {
        this.executor = builder.execute();
        this.arguments = builder.arguments();
    }

    @Override
    public List<CommandArgument<?>> getArguments() {
        return this.arguments;
    }

    @Override
    public String getDescription() {
        return "Testing purposes only";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.empty();
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        return this.executor.accept(this, commandContext, args);
    }

    public boolean run(CommandSource source, String... arguments) {
        return run(this, source, arguments);
    }

    public <T> Collection<String> suggest(CommandSource source, String arguments) throws NotEnoughArguments {
        return suggest(this, source, arguments);
    }

    public static boolean run(ArgumentCommand cmd, CommandSource source, String... arguments) {
        CommandContext commandContext = new CommandContext(source, Collections.singleton(cmd), arguments);
        try {
            return cmd.run(commandContext, arguments);
        } catch (NotEnoughArguments e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Collection<String> suggest(ArgumentCommand cmd, CommandSource source, String arguments) throws NotEnoughArguments {
        CommandContext commandContext = new CommandContext(source, Collections.singleton(cmd), arguments);
        var cmdArguments = cmd.getArguments();
        if (1 == cmdArguments.size()) {
            var arg = (CommandArgument<T>) cmdArguments.get(0);
            return arg.suggest(commandContext, new CommandArgumentContext<>(arg, 0, arguments));
        }
        return commandContext.getSuggestions(cmd);
    }
}
