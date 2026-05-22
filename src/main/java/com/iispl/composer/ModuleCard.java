package com.iispl.composer;

import org.zkoss.zk.ui.HtmlMacroComponent;

/**
 * Companion class for /reuseableComponents/moduleCard.zul
 *
 * Accepted attributes:
 *   icon        – emoji / text icon  (e.g. "👥")
 *   moduleTitle – card heading       (e.g. "User Management")
 *   moduleDesc  – one-line info      (e.g. "Manage system users, roles and permissions")
 *   moduleHref  – navigation URL     (e.g. "/admin-users.zul")
 *
 * ZUL registration:
 *   <?component name="modulecard"
 *               macroURI="/reuseableComponents/moduleCard.zul"
 *               class="com.iispl.composer.ModuleCard"?>
 */
public class ModuleCard extends HtmlMacroComponent {

    private static final long serialVersionUID = 1L;

    // ── Fields ────────────────────────────────────────────────────────────────

    private String icon        = "";
    private String moduleTitle = "";
    private String moduleDesc  = "";
    private String moduleHref  = "#";

    // ── Getters ───────────────────────────────────────────────────────────────

    public String getIcon()        { return icon;        }
    public String getModuleTitle() { return moduleTitle; }
    public String getModuleDesc()  { return moduleDesc;  }
    public String getModuleHref()  { return moduleHref;  }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setIcon(String icon) {
        this.icon = icon != null ? icon : "";
        invalidate();
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle != null ? moduleTitle : "";
        invalidate();
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc != null ? moduleDesc : "";
        invalidate();
    }

    public void setModuleHref(String moduleHref) {
        this.moduleHref = moduleHref != null ? moduleHref : "#";
        invalidate();
    }
}
