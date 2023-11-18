package org.colonelkai.publictransit.command.argument.line;

import org.colonelkai.publictransit.NodeManager;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.fake.CommandLine;
import org.colonelkai.publictransit.fake.CommandLineBuilder;
import org.colonelkai.publictransit.line.CostType;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.core.exceptions.NotEnoughArguments;
import org.core.source.command.ConsoleSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class LineArgumentTests {

    private final Line firstLine = new LineBuilder().setCost(1).setIdentifier("first").setCostType(CostType.FLAT_RATE).setBiDirectional(true).build();
    private final Line secondLine = new LineBuilder().setCost(2).setIdentifier("second").setCostType(CostType.FLAT_RATE).setBiDirectional(true).build();
    private final Line thirdLine = new LineBuilder().setCost(3).setIdentifier("first-not").setCostType(CostType.FLAT_RATE).setBiDirectional(true).build();

    private MockedStatic<PublicTransit> staticPlugin;

    @BeforeEach
    public void setup() {
        PublicTransit transitMock = Mockito.mock(PublicTransit.class);
        Mockito.when(transitMock.getConfigFolder()).thenReturn(new File("test"));
        this.staticPlugin = Mockito.mockStatic(PublicTransit.class);
        this.staticPlugin.when(PublicTransit::getPlugin).thenReturn(transitMock);

        NodeManager manager = new NodeManager();
        manager.register(this.firstLine);
        manager.register(this.secondLine);
        manager.register(this.thirdLine);

        Mockito.when(transitMock.getNodeManager()).thenReturn(manager);
    }

    @AfterEach
    public void shutdown() {
        this.staticPlugin.close();
    }

    @Test
    public void testFailsMissingResult() {
        //setup
        LineArgument lineArgument = new LineArgument("example");
        ConsoleSource source = Mockito.mock(ConsoleSource.class);

        //act
        CommandLine line = new CommandLineBuilder().setArguments(lineArgument).setExecute((cmd, commandContext, strings) -> {
            commandContext.getArgument(cmd, lineArgument);
            Assertions.fail("Should not hit");
            return false;
        }).build();

        //assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> line.run(source, "missing"));
    }

    @Test
    public void testReturnCorrectDefault() {
        //setup
        LineArgument lineArgument = new LineArgument("example");
        ConsoleSource source = Mockito.mock(ConsoleSource.class);

        //act
        boolean result = new CommandLineBuilder().setArguments(lineArgument).setExecute((cmd, commandContext, strings) -> {
            Line line = commandContext.getArgument(cmd, lineArgument);
            Assertions.assertEquals(this.firstLine, line);
            return line.equals(this.firstLine);
        }).build().run(source, "first");
        Assertions.assertTrue(result);
    }

    @Test
    public void testReturnIncorrectResult() {
        //setup
        LineArgument lineArgument = new LineArgument("example");
        ConsoleSource source = Mockito.mock(ConsoleSource.class);

        //act
        boolean result = new CommandLineBuilder().setArguments(lineArgument).setExecute((cmd, commandContext, strings) -> {
            Line line = commandContext.getArgument(cmd, lineArgument);
            Assertions.assertNotEquals(this.firstLine, line);
            return line.equals(this.firstLine);
        }).build().run(source, "second");
        Assertions.assertFalse(result);
    }

    @Test
    public void testSuggestNoArgs() throws NotEnoughArguments {
        LineArgument lineArgument = new LineArgument("example");
        ConsoleSource source = Mockito.mock(ConsoleSource.class);

        //act
        Collection<String> suggestions = new CommandLineBuilder().setArguments(lineArgument).build().suggest(source, "");

        //assert
        Assertions.assertEquals(3, suggestions.size());
        Assertions.assertInstanceOf(List.class, suggestions);
        List<String> sortedSuggestions = (List<String>) suggestions;
        Assertions.assertEquals("first", sortedSuggestions.get(0));
        Assertions.assertEquals("first-not", sortedSuggestions.get(1));
        Assertions.assertEquals("second", sortedSuggestions.get(2));
    }

    @Test
    public void testSuggestionOneCharacterArgs() throws NotEnoughArguments {
        LineArgument lineArgument = new LineArgument("example");
        ConsoleSource source = Mockito.mock(ConsoleSource.class);

        //act
        Collection<String> suggestions = new CommandLineBuilder().setArguments(lineArgument).build().suggest(source, "f");

        //assert
        Assertions.assertEquals(2, suggestions.size());
        Assertions.assertInstanceOf(List.class, suggestions);
        List<String> sortedSuggestions = (List<String>) suggestions;
        Assertions.assertEquals("first", sortedSuggestions.get(0));
        Assertions.assertEquals("first-not", sortedSuggestions.get(1));
    }

    @Test
    public void testSuggestionOneInvalidCharacterArgs() throws NotEnoughArguments {
        LineArgument lineArgument = new LineArgument("example");
        ConsoleSource source = Mockito.mock(ConsoleSource.class);

        //act
        Collection<String> suggestions = new CommandLineBuilder().setArguments(lineArgument).build().suggest(source, "z");

        //assert
        Assertions.assertEquals(0, suggestions.size());
    }

}
