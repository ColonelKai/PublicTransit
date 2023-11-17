package org.colonelkai.publictransit.commands;

import org.colonelkai.publictransit.commands.cost.CostBetweenCommand;
import org.colonelkai.publictransit.commands.line.CreateLineCommand;
import org.colonelkai.publictransit.commands.line.DeleteLineCommand;
import org.colonelkai.publictransit.commands.line.info.LineInfoCommand;
import org.colonelkai.publictransit.commands.node.create.CreateStopNodeCommand;
import org.colonelkai.publictransit.commands.node.create.CreateTransitionalNodeCommand;
import org.colonelkai.publictransit.commands.node.delete.DeleteNodeCommand;
import org.colonelkai.publictransit.commands.node.info.NodeInfoCommand;
import org.colonelkai.publictransit.commands.node.move.CloneNodeCommand;
import org.colonelkai.publictransit.commands.node.move.MoveNodeCommand;
import org.colonelkai.publictransit.commands.travel.TravelCommand;

interface Commands {

    //line
    CreateLineCommand CREATE_LINE = new CreateLineCommand();
    DeleteLineCommand DELETE_LINE = new DeleteLineCommand();
    LineInfoCommand LINE_INFO = new LineInfoCommand();

    //node
    CloneNodeCommand CLONE_NODE = new CloneNodeCommand();
    MoveNodeCommand MOVE_NODE = new MoveNodeCommand();
    DeleteNodeCommand DELETE_NODE = new DeleteNodeCommand();
    CreateStopNodeCommand CREATE_STOP = new CreateStopNodeCommand();
    CreateTransitionalNodeCommand CREATE_TRANSITIONAL = new CreateTransitionalNodeCommand();
    NodeInfoCommand NODE_INFO = new NodeInfoCommand();

    //misc
    CostBetweenCommand COST_BETWEEN = new CostBetweenCommand();

    @ForCommand(name = PublicTransitCommandLauncher.TRAVEL)
    TravelCommand TRAVEL = new TravelCommand();
}
