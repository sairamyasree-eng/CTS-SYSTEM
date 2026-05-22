package com.iispl.composer;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

import com.iispl.dto.SessionUserDTO;

/**
 * TopbarComponent
 * ===============
 * Macro component that populates the top navigation bar with:
 *  - Logged-in user's name, branch, and role badge
 *  - CTS session info read from ZK session as plain strings
 *  - Sign Out button which clears session and redirects to login
 *
 * Usage in ZUL:
 *   <?component name="topbar" macroURI="/reuseableComponents/topbar.zul"
 *               class="com.iispl.composer.TopbarComponent"?>
 *   <topbar/>
 *
 * Session attributes expected (set by LoginController):
 *   SessionUserDTO.SESSION_KEY ("sessionUser") → SessionUserDTO
 *   "sessionId"   → String  (optional — CTS batch session ID)
 *   "sessionTime" → String  (optional — e.g. "09:00 - 11:00")
 *   "sessionDate" → String  (optional — e.g. "19-05-2026")
 */
public class TopbarComponent extends HtmlMacroComponent {

    private static final long serialVersionUID = 1L;

    @Wire private Div    mc_sessionBadge;
    @Wire private Label  mc_lblSessionId;
    @Wire private Label  mc_lblSessionTime;
    @Wire private Label  mc_lblSessionDate;

    @Wire private Label  mc_lblUserName;
    @Wire private Label  mc_lblUserBranch;
    @Wire private Label  mc_lblRoleBadge;

    @Wire private Button mc_btnSignOut;

    public TopbarComponent() {
        compose();
    }

    @Override
    public void afterCompose() {
        super.afterCompose();

        // BUG-FIX: Read SessionUserDTO (not User entity) from the correct session key.
        // LoginController stores SessionUserDTO under SessionUserDTO.SESSION_KEY ("sessionUser").
        // Old code read "currentUser" as User entity → ClassCastException / null → redirect loop.
        SessionUserDTO sessionUser = (SessionUserDTO) Sessions.getCurrent()
                .getAttribute(SessionUserDTO.SESSION_KEY);

        if (sessionUser == null) {
            Executions.sendRedirect("/login.zul");
            return;
        }

        // ── Populate user info ────────────────────────────────────────────

        String displayName = (sessionUser.getFullName() != null && !sessionUser.getFullName().isEmpty())
            ? sessionUser.getFullName()
            : sessionUser.getUsername();
        mc_lblUserName.setValue(displayName);

        mc_lblUserBranch.setValue(
            sessionUser.getBranch() != null ? sessionUser.getBranch() : ""
        );

        // ── Role badge ────────────────────────────────────────────────────
        String roleName  = sessionUser.getRoleName() != null ? sessionUser.getRoleName() : "UNKNOWN";
        String roleLabel = toRoleLabel(roleName);
        String roleCss   = toRoleCss(roleName);

        mc_lblRoleBadge.setValue(roleLabel);
        mc_lblRoleBadge.setSclass("topbar-role-badge " + roleCss);

        // ── CTS session info (optional — stored as plain strings) ─────────
        String sessionId   = (String) Sessions.getCurrent().getAttribute("sessionId");
        String sessionTime = (String) Sessions.getCurrent().getAttribute("sessionTime");
        String sessionDate = (String) Sessions.getCurrent().getAttribute("sessionDate");

        if (sessionId != null && !sessionId.isEmpty()) {
            mc_lblSessionId.setValue(sessionId);
            mc_lblSessionTime.setValue(sessionTime != null ? sessionTime : "");
            mc_lblSessionDate.setValue(sessionDate != null ? sessionDate : "");
            mc_sessionBadge.setVisible(true);
        } else {
            mc_sessionBadge.setVisible(false);
        }
    }

    // ── Sign Out ──────────────────────────────────────────────────────────

    @Listen("onClick = #mc_btnSignOut")
    public void signOut() {
        Sessions.getCurrent().invalidate();
        Executions.sendRedirect("/login.zul");
    }

    // ── Role name → human-readable label ─────────────────────────────────

    private String toRoleLabel(String roleName) {
        if (roleName == null) return "Unknown";
        switch (roleName.toUpperCase()) {
            case "ADMIN":            return "Admin";
            case "MAKER_OUTWARD":    return "Maker Outward";
            case "CHECKER_OUTWARD":  return "Checker Outward";
            case "MAKER_INWARD":     return "Maker Inward";
            case "CHECKER_INWARD":   return "Checker Inward";
            default:                 return roleName;
        }
    }

    // ── Role name → CSS badge class ───────────────────────────────────────

    private String toRoleCss(String roleName) {
        if (roleName == null) return "role-default";
        switch (roleName.toUpperCase()) {
            case "ADMIN":            return "role-admin";
            case "MAKER_OUTWARD":    return "role-maker-out";
            case "CHECKER_OUTWARD":  return "role-checker-out";
            case "MAKER_INWARD":     return "role-maker-in";
            case "CHECKER_INWARD":   return "role-checker-in";
            default:                 return "role-default";
        }
    }
}
