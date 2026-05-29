package com.iispl.components;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

import com.iispl.dto.SessionUserDTO;

public class SidebarComponent extends HtmlMacroComponent {

    private static final long serialVersionUID = 1L;

    @Wire private Label mc_lblSection;

    // ── Admin items ───────────────────────────────────────────────────────
    @Wire private Div mc_itemDashboard;
    @Wire private Div mc_itemUserMgmt;
    @Wire private Div mc_itemBankMgmt;

    // ── Pipeline items ────────────────────────────────────────────────────
    @Wire private Div mc_item1;
    @Wire private Div mc_item2;
    @Wire private Div mc_item3;
    @Wire private Div mc_item4;
    @Wire private Div mc_item5;
    @Wire private Div mc_item6;
    @Wire private Div mc_item7;
    @Wire private Div mc_item8;

    // ── Attributes ────────────────────────────────────────────────────────
    private boolean itemUserMgmt = false;
    private boolean itemBankMgmt = false;
    private boolean item1        = false;
    private boolean item2        = false;
    private boolean item3        = false;
    private boolean item4        = false;
    private boolean item5        = false;
    private boolean item6        = false;
    private boolean item7        = false;
    private boolean item8        = false;
    private String  activeItem   = "";

    public SidebarComponent() {
        // do not call compose() here
    	compose();
    }

    @Override
    public void afterCompose() {
        super.afterCompose(); // renders template + injects @Wire fields — must be first

        SessionUserDTO sessionUser = (SessionUserDTO) Sessions.getCurrent()
                .getAttribute(SessionUserDTO.SESSION_KEY);

        if (sessionUser != null) {
            String roleName = sessionUser.getRoleName();
            mc_lblSection.setValue(
                "ADMIN".equalsIgnoreCase(roleName) ? "ADMINISTRATION" : "PIPELINE"
            );
        }

        // Dashboard — always enabled
        //enableItem(mc_itemDashboard, "/admin-dashboard.zul", "dashboard");
        enableItem(mc_itemDashboard, getDashboardUrl(), "dashboard");

        // Admin items
        applyItem(mc_itemUserMgmt, itemUserMgmt, "/admin-users.zul", "admin-users");
        applyItem(mc_itemBankMgmt, itemBankMgmt, "/admin-banks.zul", "admin-banks");

        // Pipeline items
        applyItem(mc_item1, item1, "/scan.zul",         "scan");
        applyItem(mc_item2, item2, "/repair.zul",        "repair");
        applyItem(mc_item3, item3, "/checker-out.zul",   "checker-out");
        applyItem(mc_item4, item4, "/dem.zul",           "dem");
        applyItem(mc_item5, item5, "/inward-repair.zul", "inward-repair");
        applyItem(mc_item6, item6, "/inward-verify.zul", "inward-verify");
        applyItem(mc_item7, item7, "/cbs.zul",           "cbs");
        applyItem(mc_item8, item8, "/view-batches.zul",  "view-batches");
    }

    private void applyItem(Div item, boolean enabled, String url, String pageId) {
        if (enabled) {
            enableItem(item, url, pageId);
        } else {
            item.setSclass("sidebar-item sidebar-disabled");
        }
    }

    private void enableItem(Div item, String url, String pageId) {
        boolean isActive = pageId.equals(activeItem);
        item.setSclass("sidebar-item" + (isActive ? " active" : ""));
        item.addEventListener("onClick", e ->
            org.zkoss.zk.ui.Executions.sendRedirect(url)
        );
    }
    
    private String getDashboardUrl() {

        SessionUserDTO sessionUser =
            (SessionUserDTO) Sessions.getCurrent()
                .getAttribute(SessionUserDTO.SESSION_KEY);

        if (sessionUser == null) {
            return "/login.zul";
        }

        String role = sessionUser.getRoleName();

        switch (role) {

        case "ADMIN":
            return "/admin-dashboard.zul";

        case "MAKER_OUTWARD":
            return "/maker-outward-dashboard.zul";

        case "CHECKER_OUTWARD":
            return "/checker-outward-dashboard.zul";

        case "MAKER_INWARD":
            return "/maker-inward-dashboard.zul";

        case "CHECKER_INWARD":
            return "/checker-inward-dashboard.zul";

        default:
            return "/login.zul";
        }
    }
    

    // ── Getters & Setters ─────────────────────────────────────────────────
    public boolean isItemUserMgmt() { return itemUserMgmt; }
    public boolean isItemBankMgmt() { return itemBankMgmt; }
    public boolean isItem1()        { return item1; }
    public boolean isItem2()        { return item2; }
    public boolean isItem3()        { return item3; }
    public boolean isItem4()        { return item4; }
    public boolean isItem5()        { return item5; }
    public boolean isItem6()        { return item6; }
    public boolean isItem7()        { return item7; }
    public boolean isItem8()        { return item8; }
    public String  getActiveItem()  { return activeItem; }

    public void setItemUserMgmt(boolean v) { this.itemUserMgmt = v; }
    public void setItemBankMgmt(boolean v) { this.itemBankMgmt = v; }
    public void setItem1(boolean v)        { this.item1 = v; }
    public void setItem2(boolean v)        { this.item2 = v; }
    public void setItem3(boolean v)        { this.item3 = v; }
    public void setItem4(boolean v)        { this.item4 = v; }
    public void setItem5(boolean v)        { this.item5 = v; }
    public void setItem6(boolean v)        { this.item6 = v; }
    public void setItem7(boolean v)        { this.item7 = v; }
    public void setItem8(boolean v)        { this.item8 = v; }
    public void setActiveItem(String v)    { this.activeItem = v != null ? v : ""; }
}