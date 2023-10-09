package org.colonelkai.publictransit.fake;

import org.colonelkai.publictransit.utils.lamda.ThrowableTriPredicate;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLineBuilder {

    private List<CommandArgument<?>> arguments = new ArrayList<>();
    private ThrowableTriPredicate<CommandLine, CommandContext, String[], NotEnoughArguments> execute;

    public List<CommandArgument<?>> arguments() {
        return this.arguments;
    }

    public CommandLine build() {
        return new CommandLine(this);
    }

    public ThrowableTriPredicate<CommandLine, CommandContext, String[], NotEnoughArguments> execute() {
        return this.execute;
    }

    public CommandLineBuilder setArguments(CommandArgument<?>... arguments) {
        return this.setArguments(Arrays.asList(arguments));
    }

    public CommandLineBuilder setArguments(List<CommandArgument<?>> arguments) {
        this.arguments = arguments;
        return this;
    }

    public CommandLineBuilder setExecute(ThrowableTriPredicate<CommandLine, CommandContext, String[], NotEnoughArguments> execute) {
        this.execute = execute;
        return this;
    }
}
