package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class RoleModel implements Serializable {
    private static final long serialVersionUID = -1527556396007807723L;
    private String roleId;
    private String roleName;
    private String serverName;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
