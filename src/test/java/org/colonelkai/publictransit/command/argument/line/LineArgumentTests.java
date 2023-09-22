package org.colonelkai.publictransit.command.argument.line;

import org.colonelkai.publictransit.NodeManager;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.fake.CommandLineBuilder;
import org.colonelkai.publictransit.line.CostType;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.core.source.command.ConsoleSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

public class LineArgumentTests {

    private final Line firstLine = new LineBuilder().setCost(1).setIdentifier("first").setCostType(CostType.FLAT_RATE).setBiDirectional(true).build();
    private final Line secondLine = new LineBuilder().setCost(2).setIdentifier("second").setCostType(CostType.FLAT_RATE).setBiDirectional(true).build();
    private final Line thirdLine = new LineBuilder().setCost(3).setIdentifier("last").setCostType(CostType.FLAT_RATE).setBiDirectional(true).build();

    private MockedStatic<PublicTransit> staticPlugin;

    @Test
    public void TestFailsMissingResult() {
        //setup
        LineArgument lineArgument = new LineArgument("example");
        ConsoleSource source = Mockito.mock(ConsoleSource.class);

        //act
        var line = new CommandLineBuilder().setArguments(lineArgument).setExecute((cmd, commandContext, strings) -> {
            commandContext.getArgument(cmd, lineArgument);
            Assertions.fail("Should not hit");
            return false;
        }).build();

        //assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> line.run(source, "missing"));
    }

    @Test
    public void TestReturnCorrectDefault() {
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
    public void TestReturnIncorrectResult() {
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

}
