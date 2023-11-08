package org.colonelkai.publictransit.command.line.option;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.line.option.ViewLineOptionCommand;
import org.colonelkai.publictransit.fake.CommandLine;
import org.colonelkai.publictransit.fake.CommandLineBuilder;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.colonelkai.publictransit.options.CommandOptionBuilder;
import org.colonelkai.publictransit.options.CommandOptionMeta;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.source.command.ConsoleSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;

public class ViewCommandTests {

    @Test
    public void lineBuilderCount(){
        //act
        Collection<? extends CommandOptionMeta<?>> meta = CommandOptionBuilder.buildFrom(LineBuilder.class);

        //assert
        Assertions.assertEquals(6, meta.size());
    }

    @Test
    public void canRunViewCommand() throws NoSuchMethodException {
        Line line = new LineBuilder().setIdentifier("lines").setBiDirectional(true).build();
        PublicTransit.getPlugin().getNodeManager().register(line);
        ConsoleSource consoleSource = Mockito.mock(ConsoleSource.class);
        ViewLineOptionCommand viewLineOptionCommand = new ViewLineOptionCommand(new ExactArgument(""), LineBuilder.class.getDeclaredMethod("weight"));

        boolean run = CommandLine.run(viewLineOptionCommand, consoleSource, "line view lines weight");

        Assertions.assertTrue(run, "Failed to run command");
    }


}
