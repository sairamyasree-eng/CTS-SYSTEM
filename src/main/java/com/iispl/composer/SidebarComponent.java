package com.iispl.composer;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

import com.iispl.entity.User;

/**
 * SidebarComponent
 * ================
 * Macro component that reads the current user's role from session and
 * renders only the menu items that role is allowed to access.
 *
 * Usage in ZUL:
 *   <?component name="sidebar" macroURI="/components/sidebar.zul"
 *               class="com.iispl.composer.SidebarComponent"?>
 *   <sidebar/>
 *
 * HOW LOCKING WORKS:
 *  - Accessible items → clickable, navigates to page
 *  - Locked items     → shown with "locked" style, not clickable
 *  - Admin role       → shows admin-only items (Users, Banks)
 *
 * NOTE: Role is an entity with a String name field.
 *       Role names used: "ADMIN", "MAKER_OUTWARD", "CHECKER_OUTWARD",
 *                        "MAKER_INWARD", "CHECKER_INWARD"
 */
public class SidebarComponent extends HtmlMacroComponent {

    private static final long serialVersionUID = 1L;

    @Wire private Div   mc_menuContainer;
    @Wire private Div   mc_itemDashboard;
    @Wire private Label mc_lblSection;

    // ── All pipeline steps: { stepNumber, pageId, label, allowedRoleNames[] } ──
    // allowedRoleNames → matches Role.getName() values stored in DB
    private static final Object[][] PIPELINE_STEPS = {
        {1, "scan",          "Scan Service",    new String[]{"MAKER_OUTWARD"}},
        {2, "repair",        "Reject & Repair", new String[]{"MAKER_OUTWARD"}},
        {3, "checker-out",   "Checker Outward", new String[]{"CHECKER_OUTWARD"}},
        {4, "dem",           "File Processing", new String[]{"CHECKER_OUTWARD", "MAKER_INWARD"}},
        {5, "inward-repair", "Inward Repair",   new String[]{"MAKER_INWARD"}},
        {6, "inward-verify", "Checker Inward",  new String[]{"CHECKER_INWARD"}},
        {7, "cbs",           "CBS Processing",  new String[]{"CHECKER_INWARD"}},
        {8, "view-batches",  "Batch Status",    new String[]{"CHECKER_OUTWARD", "MAKER_OUTWARD",
                                                              "MAKER_INWARD",   "CHECKER_INWARD"}},
    };

    // ── Admin-only items ──
    private static final Object[][] ADMIN_ITEMS = {
        {"admin-users", "User Management", "👥"},
        {"admin-banks", "Bank Management", "🏦"},
    };

    public SidebarComponent() {
        compose();
    }

    @Override
    public void afterCompose() {
        super.afterCompose();

        // Read User entity from ZK session
        // Stored by login controller as: session.setAttribute("currentUser", user)
        User   user       = (User)   Sessions.getCurrent().getAttribute("currentUser");
        String activePage = (String) Sessions.getCurrent().getAttribute("currentPage");

        if (user == null) return;

        // Get role name from Role entity → Role.getName()
        String roleName = (user.getRole() != null) ? user.getRole().getRoleName() : "";

        if ("ADMIN".equalsIgnoreCase(roleName)) {
            renderAdminMenu(activePage);
        } else {
            renderPipelineMenu(roleName, activePage);
        }

        if ("dashboard".equals(activePage)) {
            mc_itemDashboard.setSclass("sidebar-item active");
        }
    }

    // ── Renders pipeline steps for non-admin roles ────────────────────────

    private void renderPipelineMenu(String roleName, String activePage) {
        for (Object[] step : PIPELINE_STEPS) {
            int      stepNum = (int)      step[0];
            String   pageId  = (String)   step[1];
            String   label   = (String)   step[2];
            String[] allowed = (String[]) step[3];

            boolean canAccess = hasRole(roleName, allowed);
            boolean isActive  = pageId.equals(activePage);

            Div item = buildMenuItem(String.valueOf(stepNum), label, pageId, canAccess, isActive);
            item.setParent(mc_menuContainer);
        }
    }

    // ── Renders admin-only items ──────────────────────────────────────────

    private void renderAdminMenu(String activePage) {
        mc_lblSection.setValue("ADMINISTRATION");
        mc_itemDashboard.setVisible(true);

        for (Object[] adminItem : ADMIN_ITEMS) {
            String pageId    = (String) adminItem[0];
            String label     = (String) adminItem[1];
            String icon      = (String) adminItem[2];
            boolean isActive = pageId.equals(activePage);

            Div item = buildMenuItem(icon, label, pageId, true, isActive);
            item.setParent(mc_menuContainer);
        }
    }

    // ── Builds a single sidebar menu item div ─────────────────────────────

    private Div buildMenuItem(String stepLabel, String itemLabel,
                               String pageId, boolean accessible, boolean active) {
        Div item = new Div();

        String cssClass = "sidebar-item";
        if (!accessible) cssClass += " locked";
        if (active)      cssClass += " active";
        item.setSclass(cssClass);

        // Step number / icon badge
        Div stepBadge = new Div();
        stepBadge.setSclass("sidebar-step");
        new Label(stepLabel).setParent(stepBadge);
        stepBadge.setParent(item);

        // Menu label
        Label lbl = new Label(itemLabel);
        lbl.setSclass("sidebar-item-label");
        lbl.setParent(item);

        // Only accessible items navigate on click
        if (accessible) {
            item.addEventListener("onClick", event ->
                org.zkoss.zk.ui.Executions.sendRedirect("/" + pageId + ".zul")
            );
        }

        return item;
    }

    // ── Checks if the user's role name is in the allowed list ────────────

    private boolean hasRole(String roleName, String[] allowed) {
        for (String r : allowed) {
            if (r.equalsIgnoreCase(roleName)) return true;
        }
        return false;
    }
}