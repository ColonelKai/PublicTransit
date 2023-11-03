package org.colonelkai.publictransit.commands;

import org.colonelkai.publictransit.commands.cost.CostBetweenCommand;
import org.colonelkai.publictransit.commands.line.CreateLineCommand;
import org.colonelkai.publictransit.commands.line.DeleteLineCommand;
import org.colonelkai.publictransit.commands.line.option.ViewLineOptionCommand;
import org.colonelkai.publictransit.commands.line.option.cost.ViewLineCostCommand;
import org.colonelkai.publictransit.commands.line.option.cost.ViewLineDirectionCommand;
import org.colonelkai.publictransit.commands.line.option.cost.ViewLineWeightCommand;
import org.colonelkai.publictransit.commands.node.create.CreateStopNodeCommand;
import org.colonelkai.publictransit.commands.node.create.CreateTransitionalNodeCommand;
import org.colonelkai.publictransit.commands.node.delete.DeleteNodeCommand;
import org.colonelkai.publictransit.commands.node.move.CloneNodeCommand;
import org.colonelkai.publictransit.commands.node.move.MoveNodeCommand;

interface Commands {

    //line
    CreateLineCommand CREATE_LINE = new CreateLineCommand();
    DeleteLineCommand DELETE_LINE = new DeleteLineCommand();

    //line option
    ViewLineCostCommand VIEW_LINE_COST = new ViewLineCostCommand();
    ViewLineDirectionCommand VIEW_LINE_DIRECTION = new ViewLineDirectionCommand();
    ViewLineWeightCommand VIEW_LINE_WEIGHT = new ViewLineWeightCommand();

    //node
    CloneNodeCommand CLONE_NODE = new CloneNodeCommand();
    MoveNodeCommand MOVE_NODE = new MoveNodeCommand();
    DeleteNodeCommand DELETE_NODE = new DeleteNodeCommand();
    CreateStopNodeCommand CREATE_STOP = new CreateStopNodeCommand();
    CreateTransitionalNodeCommand CREATE_TRANSITIONAL = new CreateTransitionalNodeCommand();

    //misc
    CostBetweenCommand COST_BETWEEN = new CostBetweenCommand();
}
