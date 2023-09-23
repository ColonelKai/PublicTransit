package org.colonelkai.publictransit.command.argument.line;

import org.colonelkai.publictransit.commands.arguments.NodeArgument;
import org.colonelkai.publictransit.fake.CommandLine;
import org.colonelkai.publictransit.fake.CommandLineBuilder;
import org.colonelkai.publictransit.fake.position.FakeSyncExactPosition;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.colonelkai.publictransit.node.NodeType;
import org.core.source.command.ConsoleSource;
import org.core.world.WorldExtent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class NodeArgumentTests {

    private static Node firstNode;
    private static List<Node> nodes;
    private static Node secondNode;
    private static Node thirdNode;

    @Test
    public void testFailsMissingResult() {
        //setup
        ConsoleSource source = Mockito.mock(ConsoleSource.class);
        NodeArgument nodeArgument = new NodeArgument("example", (context, arg) -> nodes);

        //act
        CommandLine line = new CommandLineBuilder().setArguments(nodeArgument).setExecute((cmd, context, strings) -> {
            context.getArgument(cmd, nodeArgument);
            Assertions.fail("Should not hit");
            return false;
        }).build();

        //assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> line.run(source, "missing"));
    }

    @Test
    public void testReturnCorrectIndex() {
        //setup
        ConsoleSource source = Mockito.mock(ConsoleSource.class);
        NodeArgument nodeArgument = new NodeArgument("example", (context, arg) -> nodes);

        //act
        CommandLine line = new CommandLineBuilder().setArguments(nodeArgument).setExecute((cmd, context, strings) -> {
            Node node = context.getArgument(cmd, nodeArgument);
            Assertions.assertEquals(secondNode, node);
            return true;
        }).build();

        //assert
        Assertions.assertTrue(line.run(source, "1"));
    }

    @Test
    public void testReturnCorrectName() {
        //setup
        ConsoleSource source = Mockito.mock(ConsoleSource.class);
        NodeArgument nodeArgument = new NodeArgument("example", (context, arg) -> nodes);

        //act
        CommandLine line = new CommandLineBuilder().setArguments(nodeArgument).setExecute((cmd, context, strings) -> {
            Node node = context.getArgument(cmd, nodeArgument);
            Assertions.assertEquals(thirdNode, node);
            return true;
        }).build();

        //assert
        Assertions.assertTrue(line.run(source, "first-not"));
    }

    @Test
    public void testSuggestNoArgs() {
        //setup
        ConsoleSource source = Mockito.mock(ConsoleSource.class);
        NodeArgument nodeArgument = new NodeArgument("example", (context, arg) -> nodes);
        CommandLine line = new CommandLineBuilder().setArguments(nodeArgument).build();

        //act
        Collection<String> suggestions = line.suggest(source, "");

        //assert
        Assertions.assertEquals(5, suggestions.size());
        Assertions.assertInstanceOf(List.class, suggestions);
        List<String> sortedSuggestions = (List<String>) suggestions;
        Assertions.assertEquals("0", sortedSuggestions.get(0));
        Assertions.assertEquals("1", sortedSuggestions.get(1));
        Assertions.assertEquals("2", sortedSuggestions.get(2));
        Assertions.assertEquals("first", sortedSuggestions.get(3));
        Assertions.assertEquals("first-not", sortedSuggestions.get(4));
    }

    @Test
    public void testSuggestOneLetter() {
        //setup
        ConsoleSource source = Mockito.mock(ConsoleSource.class);
        NodeArgument nodeArgument = new NodeArgument("example", (context, arg) -> nodes);
        CommandLine line = new CommandLineBuilder().setArguments(nodeArgument).build();

        //act
        Collection<String> suggestions = line.suggest(source, "f");

        //assert
        Assertions.assertEquals(2, suggestions.size());
        Assertions.assertInstanceOf(List.class, suggestions);
        List<String> sortedSuggestions = (List<String>) suggestions;
        Assertions.assertEquals("first", sortedSuggestions.get(0));
        Assertions.assertEquals("first-not", sortedSuggestions.get(1));
    }

    @Test
    public void testSuggestOneNumberWithFilter() {
        //setup
        ConsoleSource source = Mockito.mock(ConsoleSource.class);
        NodeArgument nodeArgument = new NodeArgument("example", (context, arg) -> nodes, stream -> stream.filter(n -> NodeType.TRANSITIONAL == n.getNodeType()));
        CommandLine line = new CommandLineBuilder().setArguments(nodeArgument).build();

        //act
        Collection<String> suggestions = line.suggest(source, "1");

        //assert
        Assertions.assertEquals(1, suggestions.size());
        Assertions.assertInstanceOf(List.class, suggestions);
        List<String> sortedSuggestions = (List<String>) suggestions;
        Assertions.assertEquals("1", sortedSuggestions.get(0));
    }

    @Test
    public void testSuggestInvalidOneLetter() {
        //setup
        ConsoleSource source = Mockito.mock(ConsoleSource.class);
        NodeArgument nodeArgument = new NodeArgument("example", (context, arg) -> nodes);
        CommandLine line = new CommandLineBuilder().setArguments(nodeArgument).build();

        //act
        Collection<String> suggestions = line.suggest(source, "z");

        //assert
        Assertions.assertEquals(0, suggestions.size());
    }

    @BeforeAll
    public static void setup() {
        WorldExtent world = Mockito.mock(WorldExtent.class);

        firstNode = new NodeBuilder().setPosition(new FakeSyncExactPosition(world, 1, 2, 3)).setType(NodeType.STOP).setName("first").build();
        secondNode = new NodeBuilder().setPosition(new FakeSyncExactPosition(world, 2, 2, 3)).setType(NodeType.TRANSITIONAL).build();
        thirdNode = new NodeBuilder().setPosition(new FakeSyncExactPosition(world, 3, 2, 3)).setType(NodeType.STOP).setName("first-not").build();
        nodes = Arrays.asList(firstNode, secondNode, thirdNode);
    }

}
