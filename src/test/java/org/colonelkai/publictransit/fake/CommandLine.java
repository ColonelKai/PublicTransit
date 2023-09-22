package org.colonelkai.publictransit.fake;

import org.colonelkai.publictransit.utils.lamda.ThrowableTriPredicate;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
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
        CommandContext commandContext = new CommandContext(source, Collections.singleton(this), arguments);
        try {
            return run(commandContext, arguments);
        } catch (NotEnoughArguments e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<String> suggest(CommandSource source, String arguments) {
        CommandContext commandContext = new CommandContext(source, Collections.singleton(this), arguments);
        return commandContext.getSuggestions(this);
    }
}
