package org.colonelkai.publictransit.commands.line.option;

import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.line.Line;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;
import org.core.source.command.CommandSource;

import javax.swing.text.View;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class ViewLineOptionCommand implements ArgumentCommand {

    private final ExactArgument LINE_ARGUMENT = new ExactArgument("line");
    private final ExactArgument VIEW_ARGUMENT = new ExactArgument("view");
    private final LineArgument LINE_NAME_ARGUMENT;
    private final ExactArgument argument;

    protected abstract void sendMessage(CommandSource source, Line line);

    public ViewLineOptionCommand(ExactArgument argument) {
        this.argument = argument;
        LINE_NAME_ARGUMENT = new LineArgument("linename");
    }

    public ViewLineOptionCommand(ExactArgument argument, Function<Stream<Line>, Stream<Line>> filter) {
        this.argument = argument;
        LINE_NAME_ARGUMENT = new LineArgument("linename", filter);
    }

    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(LINE_ARGUMENT, VIEW_ARGUMENT, LINE_NAME_ARGUMENT, argument);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        Line line = commandContext.getArgument(this, LINE_NAME_ARGUMENT);
        sendMessage(commandContext.getSource(), line);
        return true;
    }
}
