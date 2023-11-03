package org.colonelkai.publictransit.commands;

import org.colonelkai.publictransit.commands.cost.CostBetweenCommand;

interface Commands {

    CostBetweenCommand COST_BETWEEN = new CostBetweenCommand();
}
