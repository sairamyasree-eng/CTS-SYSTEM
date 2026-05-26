package com.iispl.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;

import com.iispl.dto.SessionUserDTO;

/**
 * AdminDashboardController — MVC SelectorComposer.
 *
 * Applied to admin-dashboard.zul window.
 * Guards the page so only ADMIN role can access.
 * Provides navigation handlers for module tiles.
 */
public class AdminDashboardController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        SessionUserDTO sessionUser = (SessionUserDTO) Sessions.getCurrent()
                .getAttribute(SessionUserDTO.SESSION_KEY);

        if (sessionUser == null) {
            Executions.sendRedirect("/login.zul");
            return;
        }

        // Only ADMIN may access this page
        if (!"ADMIN".equalsIgnoreCase(sessionUser.getRoleName())) {
            Executions.sendRedirect("/login.zul");
            return;
        }

        Sessions.getCurrent().setAttribute("currentPage", "dashboard");
    }

    @Listen("onClick = #btnGoUsers")
    public void onGoUsers() {
        Executions.sendRedirect("/admin-users.zul");
    }

    @Listen("onClick = #btnGoBanks")
    public void onGoBanks() {
        Executions.sendRedirect("/admin-banks.zul");
    }
}