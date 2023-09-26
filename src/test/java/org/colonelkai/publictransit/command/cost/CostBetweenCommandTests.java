package org.colonelkai.publictransit.command.cost;

import org.colonelkai.publictransit.NodeManager;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.cost.CostBetweenCommand;
import org.colonelkai.publictransit.fake.CommandLine;
import org.colonelkai.publictransit.fake.position.FakeSyncExactPosition;
import org.colonelkai.publictransit.line.CostType;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.colonelkai.publictransit.node.NodeType;
import org.core.command.argument.ArgumentCommand;
import org.core.source.command.ConsoleSource;
import org.core.world.WorldExtent;
import org.core.world.position.block.entity.commandblock.CommandBlock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

public class CostBetweenCommandTests {

    NodeManager manager;
    PublicTransit plugin;
    MockedStatic<PublicTransit> pluginStatic;

    @AfterEach
    public void end() {
        this.pluginStatic.close();
    }

    @Test
    public void runCommandInvalidFirstArgument() {
        //setup
        ConsoleSource consoleSource = Mockito.mock(ConsoleSource.class);
        ArgumentCommand argcCmd = new CostBetweenCommand();

        //act/assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> CommandLine.run(argcCmd, consoleSource, "cost", "line", "1", "last"));
    }

    @Test
    public void runCommandInvalidSecondArgument() {
        //setup
        ConsoleSource consoleSource = Mockito.mock(ConsoleSource.class);
        ArgumentCommand argcCmd = new CostBetweenCommand();

        //act/assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> CommandLine.run(argcCmd, consoleSource, "cost", "line", "first", "1"));
    }

    @Test
    public void runCommandValid() {
        //setup
        ConsoleSource consoleSource = Mockito.mock(ConsoleSource.class);
        ArgumentCommand argcCmd = new CostBetweenCommand();

        //act
        boolean result = CommandLine.run(argcCmd, consoleSource, "cost", "line", "first", "last");

        //assert
        Assertions.assertTrue(result);
        Mockito.verify(consoleSource, Mockito.atLeast(1)).sendMessage(Mockito.argThat(argument -> {
            String message = argument.toPlain();
            return message.equals("Your journey will cost 1.0");
        }));
    }

    @Test
    public void runCommandValidSameNode() {
        //setup
        ConsoleSource consoleSource = Mockito.mock(ConsoleSource.class);
        ArgumentCommand argcCmd = new CostBetweenCommand();

        //act
        boolean result = CommandLine.run(argcCmd, consoleSource, "cost", "line", "first", "0");

        //assert
        Assertions.assertTrue(result);
        Mockito.verify(consoleSource, Mockito.atLeast(1)).sendMessage(Mockito.argThat(argument -> {
            String message = argument.toPlain();
            return message.equals("Your journey will cost 0.0");
        }));
    }

    @BeforeEach
    public void setup() {
        this.pluginStatic = Mockito.mockStatic(PublicTransit.class);
        this.plugin = Mockito.mock(PublicTransit.class);
        this.manager = new NodeManager();

        this.pluginStatic.when(PublicTransit::getPlugin).thenReturn(this.plugin);

        Mockito.when(this.plugin.getNodeManager()).thenReturn(this.manager);

        WorldExtent world = Mockito.mock(WorldExtent.class);

        Line line = new LineBuilder()
                .setCostType(CostType.FLAT_RATE)
                .setCost(1)
                .setBiDirectional(true)
                .setIdentifier("line")
                .addNodes(new NodeBuilder().setName("first").setType(NodeType.STOP).setPosition(new FakeSyncExactPosition(world, 1, 2, 3)),
                          new NodeBuilder().setType(NodeType.TRANSITIONAL).setPosition(new FakeSyncExactPosition(world, 2, 2, 2)),
                          new NodeBuilder().setName("last").setType(NodeType.STOP).setPosition(new FakeSyncExactPosition(world, 3, 3, 3)))
                .build();

        this.manager.register(line);
    }
}
