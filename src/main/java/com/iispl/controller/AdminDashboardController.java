package com.iispl.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;

import com.iispl.dto.SessionUserDTO;

public class AdminDashboardController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    @Wire
    private Include topbarInclude;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        SessionUserDTO sessionUser = (SessionUserDTO) Sessions.getCurrent()
                .getAttribute(SessionUserDTO.SESSION_KEY);

        if (sessionUser == null) {
            Executions.sendRedirect("/login.zul");
            return;
        }

        if (!"ADMIN".equalsIgnoreCase(sessionUser.getRoleName())) {
            Executions.sendRedirect("/login.zul");
            return;
        }

        Sessions.getCurrent().setAttribute("currentPage", "dashboard");
    }

    // Fires after the included topbar.zul finishes rendering
    @Listen("onFulfill = #topbarInclude")
    public void onTopbarReady() {
        Button btnSignOut = (Button) topbarInclude.getFellow("mc_btnSignOut");
        btnSignOut.addEventListener(Events.ON_CLICK, event -> signOut());
    }

    private void signOut() {
        Sessions.getCurrent().invalidate();
        Executions.sendRedirect("/login.zul");
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