package org.colonelkai.publictransit.command.node.options;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.NodeManager;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.node.option.ViewNodeOptionCommand;
import org.colonelkai.publictransit.fake.CommandLine;
import org.colonelkai.publictransit.fake.position.FakeSyncExactPosition;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.colonelkai.publictransit.node.NodeType;
import org.colonelkai.publictransit.options.CommandOptionBuilder;
import org.colonelkai.publictransit.options.CommandOptionMeta;
import org.core.TranslateCore;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationStream;
import org.core.platform.Platform;
import org.core.source.command.ConsoleSource;
import org.core.utils.ComponentUtils;
import org.core.world.WorldExtent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Collection;

public class ViewNodeOptionsCommandTests {

    private NodeManager nodeManager;
    private Platform platform;
    private MockedStatic<PublicTransit> pluginStatic;
    private MockedStatic<TranslateCore> translateCoreStatic;

    @Test
    public void canRunViewCommand() throws NoSuchMethodException {
        WorldExtent extent = Mockito.mock(WorldExtent.class);
        Line line = new LineBuilder()
                .setIdentifier("lines")
                .setBiDirectional(true)
                .addNodes(new NodeBuilder().setName("testName").setType(NodeType.STOP).setPosition(new FakeSyncExactPosition(extent, 1, 1, 1)))
                .build();
        PublicTransit.getPlugin().getNodeManager().register(line);
        ConsoleSource consoleSource = Mockito.mock(ConsoleSource.class);
        var order = Mockito.inOrder(consoleSource);
        ViewNodeOptionCommand viewLineOptionCommand = new ViewNodeOptionCommand(new ExactArgument("time"), NodeBuilder.class.getDeclaredMethod("time"));

        boolean run = CommandLine.run(viewLineOptionCommand, consoleSource, "line", "view", "lines", "0", "time");

        Assertions.assertTrue(run, "Failed to run command");
        order.verify(consoleSource, Mockito.calls(1)).sendMessage(Mockito.argThat(new ArgumentMatcher<Component>() {
            @Override
            public boolean matches(Component argument) {
                String message = ComponentUtils.toPlain(argument);
                return message.equals("time: none");
            }
        }));
    }

    @AfterEach
    public void end() {
        for (Line line : this.nodeManager.getLines()) {
            this.nodeManager.unregister(line);
        }
        this.pluginStatic.close();
        this.translateCoreStatic.close();
    }

    @Test
    public void nodeBuilderCount() {
        //act
        Collection<? extends CommandOptionMeta<?>> meta = CommandOptionBuilder.buildFrom(NodeBuilder.class);

        //assert
        Assertions.assertEquals(2, meta.size());
    }

    @BeforeEach
    public void setup() {
        this.translateCoreStatic = Mockito.mockStatic(TranslateCore.class);
        this.pluginStatic = Mockito.mockStatic(PublicTransit.class);
        PublicTransit mockedPlugin = Mockito.mock(PublicTransit.class);
        this.pluginStatic.when(PublicTransit::getPlugin).thenReturn(mockedPlugin);
        this.nodeManager = new NodeManager();
        Mockito.when(mockedPlugin.getNodeManager()).thenReturn(this.nodeManager);
        this.platform = Mockito.mock(Platform.class);
        this.translateCoreStatic.when(TranslateCore::getPlatform).thenReturn(this.platform);
        Mockito.when(this.platform.getConfigFormat()).thenReturn(ConfigurationFormat.FORMAT_YAML);
        ConfigurationStream.ConfigurationFile configFile = Mockito.mock(ConfigurationStream.ConfigurationFile.class);
        this.translateCoreStatic.when(() -> TranslateCore.createConfigurationFile(Mockito.any(), Mockito.any())).thenReturn(configFile);
    }


}
