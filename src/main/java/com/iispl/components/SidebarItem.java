package com.iispl.components;

import java.io.IOException;
import java.io.Writer;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.sys.ComponentCtrl;

public class SidebarItem extends AbstractComponent {

    private static final long serialVersionUID = 1L;

    private String label  = "";
    private String pageId = "";
    private String icon   = "";
    private String roles  = "";

    public String getLabel()            { return label; }
    public void   setLabel(String v)    { this.label = v; }

    public String getPageId()           { return pageId; }
    public void   setPageId(String v)   { this.pageId = v; }

    public String getIcon()             { return icon; }
    public void   setIcon(String v)     { this.icon = v; }

    public String getRoles()            { return roles; }
    public void   setRoles(String v)    { this.roles = v; }

    public String[] getRolesArray() {
        if (roles == null || roles.isBlank()) return new String[0];
        return roles.split(",");
    }

    // ── Renders nothing to browser ──
    @Override
    public void redraw(Writer out) throws IOException {
        // intentionally empty — SidebarComponent reads our properties
        // and builds the actual Div elements itself
    }
}