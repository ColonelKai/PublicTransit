package org.colonelkai.publictransit.command.cost;

import net.kyori.adventure.text.Component;
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
import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.command.argument.ArgumentCommand;
import org.core.eco.Currency;
import org.core.eco.CurrencyManager;
import org.core.source.command.ConsoleSource;
import org.core.utils.ComponentUtils;
import org.core.world.WorldExtent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class CostBetweenCommandTests {

    CurrencyManager currencyManager;
    NodeManager manager;
    PublicTransit plugin;
    MockedStatic<PublicTransit> pluginStatic;
    MockedStatic<TranslateCore> translateCoreStatic;

    @AfterEach
    public void end() {
        this.pluginStatic.close();
        this.translateCoreStatic.close();
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
        Currency defaultCurrency = Mockito.mock(Currency.class);
        Mockito.when(this.currencyManager.getDefaultCurrency()).thenReturn(defaultCurrency);
        Mockito.when(defaultCurrency.asDisplay(Mockito.anyDouble())).thenAnswer(invocation -> {
            double num = invocation.getArgument(0);
            return Component.text(num + "");
        });
        ConsoleSource consoleSource = Mockito.mock(ConsoleSource.class);
        ArgumentCommand argcCmd = new CostBetweenCommand();

        //act
        boolean result = CommandLine.run(argcCmd, consoleSource, "cost", "line", "first", "last");

        //assert
        Assertions.assertTrue(result);
        Mockito.verify(consoleSource, Mockito.atLeast(1)).sendMessage(Mockito.argThat((Component argument) -> {
            String message = ComponentUtils.toPlain(argument);
            return message.equals("Your journey will cost 1.0");
        }));
    }

    @Test
    public void runCommandValidSameNode() {
        //setup
        Currency defaultCurrency = Mockito.mock(Currency.class);
        Mockito.when(this.currencyManager.getDefaultCurrency()).thenReturn(defaultCurrency);
        Mockito.when(defaultCurrency.asDisplay(Mockito.anyDouble())).thenAnswer(invocation -> {
            double num = invocation.getArgument(0);
            return Component.text(num + "");
        });

        ConsoleSource consoleSource = Mockito.mock(ConsoleSource.class);
        ArgumentCommand argcCmd = new CostBetweenCommand();

        //act
        boolean result = CommandLine.run(argcCmd, consoleSource, "cost", "line", "first", "0");

        //assert
        Assertions.assertTrue(result);
        Mockito.verify(consoleSource, Mockito.atLeast(1)).sendMessage(Mockito.argThat((Component argument) -> {
            String message = ComponentUtils.toPlain(argument);
            return message.equals("Your journey will cost 0.0");
        }));
    }

    @BeforeEach
    public void setup() {
        this.translateCoreStatic = Mockito.mockStatic(TranslateCore.class);
        this.currencyManager = Mockito.mock(CurrencyManager.class);
        this.pluginStatic = Mockito.mockStatic(PublicTransit.class);
        this.plugin = Mockito.mock(PublicTransit.class);

        this.pluginStatic.when(PublicTransit::getPlugin).thenReturn(this.plugin);
        this.translateCoreStatic.when(TranslateCore::getCurrencyManager).thenReturn(this.currencyManager);

        this.manager = new NodeManager();
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
