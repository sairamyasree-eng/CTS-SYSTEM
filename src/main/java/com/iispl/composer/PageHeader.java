package com.iispl.composer;

import org.zkoss.zk.ui.HtmlMacroComponent;

/**
 * Companion class for /reuseableComponents/pageHeader.zul
 *
 * Accepted attributes:
 *   breadcrumb   – e.g. "CTS / ADMIN / USERS"
 *   pageTitle    – e.g. "User Management"
 *   pageSubtitle – e.g. "Manage CTS system users, roles and branch assignments"
 *   btnLabel     – label for the primary action button; pass "" to hide it
 *
 * ZUL registration:
 *   <?component name="pageheader"
 *               macroURI="/reuseableComponents/pageHeader.zul"
 *               class="com.iispl.composer.PageHeader"?>
 */
public class PageHeader extends HtmlMacroComponent {

    private static final long serialVersionUID = 1L;

    // ── Fields ────────────────────────────────────────────────────────────────

    private String breadcrumb   = "";
    private String pageTitle    = "";
    private String pageSubtitle = "";
    private String btnLabel     = "";   // empty  →  button hidden in ZUL

    // ── Getters ───────────────────────────────────────────────────────────────

    public String getBreadcrumb()   { return breadcrumb;   }
    public String getPageTitle()    { return pageTitle;    }
    public String getPageSubtitle() { return pageSubtitle; }
    public String getBtnLabel()     { return btnLabel;     }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setBreadcrumb(String breadcrumb) {
        this.breadcrumb = breadcrumb != null ? breadcrumb : "";
        invalidate();
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle != null ? pageTitle : "";
        invalidate();
    }

    public void setPageSubtitle(String pageSubtitle) {
        this.pageSubtitle = pageSubtitle != null ? pageSubtitle : "";
        invalidate();
    }

    public void setBtnLabel(String btnLabel) {
        this.btnLabel = btnLabel != null ? btnLabel : "";
        invalidate();
    }
}
