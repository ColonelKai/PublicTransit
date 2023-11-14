package org.colonelkai.publictransit.utils;

import org.core.permission.CorePermission;

public final class Permissions {

    public static final CorePermission CREATE_LINE = new CorePermission(false, "publictransit", "cmd", "line", "create");
    public static final CorePermission DELETE_LINE = new CorePermission(false, "publictransit", "cmd", "line", "delete");

    public static final CorePermission COST_VIEW = new CorePermission(true, "publictransit", "cmd", "line", "cost", "view");
    public static final CorePermission VIEW_LINE_OPTION = new CorePermission(false, "publictransit", "cmd", "line", "view", "option");
    public static final CorePermission SET_LINE_OPTION = new CorePermission(false, "publictransit", "cmd", "line", "set", "option");

    public static final CorePermission VIEW_NODE_OPTION = new CorePermission(false, "publictransit", "cmd", "node", "view", "option");
    public static final CorePermission SET_NODE_OPTION = new CorePermission(false, "publictransit", "cmd", "node", "set", "option");
    public static final CorePermission CREATE_NODE = new CorePermission(false, "publictransit", "cmd", "node", "create");
    public static final CorePermission DELETE_NODE = new CorePermission(false, "publictransit", "cmd", "node", "delete");
    public static final CorePermission MOVE_NODE = new CorePermission(false, "publictransit", "cmd", "node", "move");
    public static final CorePermission COPY_NODE = new CorePermission(false, "publictransit", "cmd", "node", "copy");
    public static final CorePermission TRAVEL = new CorePermission(true, "publictransit", "cmd", "travel");

    private Permissions() {
        throw new RuntimeException("Should not run");
    }
}
