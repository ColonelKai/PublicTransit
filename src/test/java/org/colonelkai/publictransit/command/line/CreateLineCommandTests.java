package org.colonelkai.publictransit.command.line;

import org.colonelkai.publictransit.NodeManager;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.cost.CostBetweenCommand;
import org.colonelkai.publictransit.commands.line.CreateLineCommand;
import org.colonelkai.publictransit.fake.CommandLine;
import org.colonelkai.publictransit.line.Line;
import org.core.TranslateCore;
import org.core.command.argument.ArgumentCommand;
import org.core.config.ConfigManager;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationStream;
import org.core.eco.CurrencyManager;
import org.core.platform.Platform;
import org.core.source.command.ConsoleSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Collection;

public class CreateLineCommandTests {

    private MockedStatic<PublicTransit> pluginStatic;
    private MockedStatic<TranslateCore> translateCoreStatic;

    private NodeManager nodeManager;
    private Platform platform;

    @AfterEach
    public void end() {
        for (Line line : nodeManager.getLines()) {
            nodeManager.unregister(line);
        }
        this.pluginStatic.close();
        this.translateCoreStatic.close();
    }

    @BeforeEach
    public void setup() {
        this.translateCoreStatic = Mockito.mockStatic(TranslateCore.class);
        this.pluginStatic = Mockito.mockStatic(PublicTransit.class);
        PublicTransit mockedPlugin = Mockito.mock(PublicTransit.class);
        this.pluginStatic.when(PublicTransit::getPlugin).thenReturn(mockedPlugin);
        nodeManager = new NodeManager();
        Mockito.when(mockedPlugin.getNodeManager()).thenReturn(nodeManager);
        platform = Mockito.mock(Platform.class);
        this.translateCoreStatic.when(TranslateCore::getPlatform).thenReturn(platform);
        Mockito.when(platform.getConfigFormat()).thenReturn(ConfigurationFormat.FORMAT_YAML);
        ConfigurationStream.ConfigurationFile configFile = Mockito.mock(ConfigurationStream.ConfigurationFile.class);
        translateCoreStatic.when(() -> TranslateCore.createConfigurationFile(Mockito.any(), Mockito.any())).thenReturn(configFile);

        ConfigManager config = Mockito.mock(ConfigManager.class);
        Mockito.when(config.getDefaultFormat()).thenReturn(ConfigurationFormat.FORMAT_YAML);
        translateCoreStatic.when(TranslateCore::getConfigManager).thenReturn(config);
    }

    @Test
    public void testRunCommand() {
        ConsoleSource consoleSource = Mockito.mock(ConsoleSource.class);
        ArgumentCommand argcCmd = new CreateLineCommand();

        //act
        CommandLine.run(argcCmd, consoleSource, "line", "create", "helloworld");

        //assert
        Collection<Line> lines = nodeManager.getLines();
        Assertions.assertFalse(lines.isEmpty());
        Assertions.assertEquals(1, lines.size());
        Assertions.assertEquals("helloworld", lines.iterator().next().getIdentifier());
    }
}
