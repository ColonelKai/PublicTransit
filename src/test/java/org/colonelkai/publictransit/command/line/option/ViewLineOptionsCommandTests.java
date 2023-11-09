package org.colonelkai.publictransit.command.line.option;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.NodeManager;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.line.option.ViewLineOptionCommand;
import org.colonelkai.publictransit.fake.CommandLine;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.colonelkai.publictransit.options.CommandOptionBuilder;
import org.colonelkai.publictransit.options.CommandOptionMeta;
import org.core.TranslateCore;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.config.ConfigManager;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationStream;
import org.core.platform.Platform;
import org.core.source.command.ConsoleSource;
import org.core.utils.ComponentUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Collection;

public class ViewLineOptionsCommandTests {

    private NodeManager nodeManager;
    private Platform platform;
    private MockedStatic<PublicTransit> pluginStatic;
    private MockedStatic<TranslateCore> translateCoreStatic;

    @Test
    public void canRunViewCommand() throws NoSuchMethodException {
        Line line = new LineBuilder().setIdentifier("lines").setBiDirectional(true).build();
        PublicTransit.getPlugin().getNodeManager().register(line);
        ConsoleSource consoleSource = Mockito.mock(ConsoleSource.class);
        var order = Mockito.inOrder(consoleSource);
        ViewLineOptionCommand viewLineOptionCommand = new ViewLineOptionCommand(new ExactArgument("weight"), LineBuilder.class.getDeclaredMethod("weight"));

        boolean run = CommandLine.run(viewLineOptionCommand, consoleSource, "line", "view", "lines", "weight");

        Assertions.assertTrue(run, "Failed to run command");
        order.verify(consoleSource, Mockito.calls(1)).sendMessage(Mockito.argThat((ArgumentMatcher<Component>) argument -> {
            String message = ComponentUtils.toPlain(argument);
            return message.equals("weight: none");
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
    public void lineBuilderCount() {
        //act
        Collection<? extends CommandOptionMeta<?>> meta = CommandOptionBuilder.buildFrom(LineBuilder.class);

        //assert
        Assertions.assertEquals(6, meta.size());
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

        ConfigManager config = Mockito.mock(ConfigManager.class);
        Mockito.when(config.getDefaultFormat()).thenReturn(ConfigurationFormat.FORMAT_YAML);
        Mockito.when(config.read(Mockito.any())).thenReturn(configFile);
        translateCoreStatic.when(TranslateCore::getConfigManager).thenReturn(config);
    }


}
