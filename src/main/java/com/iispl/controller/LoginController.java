package com.iispl.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.iispl.dto.SessionUserDTO;
import com.iispl.exceptions.AuthException;
import com.iispl.service.LoginService;

public class LoginController extends SelectorComposer<Component> {

    @Wire private Textbox txtUsername;
    @Wire private Textbox txtPassword;
    @Wire private Label   lblError;

    private final LoginService loginService = new LoginService();

    @Listen("onClick = #btnSignIn")
    public void validate() {
        String username = txtUsername.getValue().trim();
        String password = txtPassword.getValue().trim();

        hideError();

        try {
            SessionUserDTO sessionUser = loginService.authenticate(username, password);

            Sessions.getCurrent().setAttribute(SessionUserDTO.SESSION_KEY, sessionUser);
            Sessions.getCurrent().setAttribute("currentPage", "dashboard");

            txtPassword.setValue("");

            redirectByRole(sessionUser.getRoleName());

        } catch (AuthException e) {
            showError(e.getMessage());
        }
    }

    @Listen("onClick = #btnGoRegister")
    public void onGoRegister() {
        Executions.sendRedirect("/register.zul");
    }

    private void redirectByRole(String roleName) {
        String url;
        switch (roleName.toUpperCase()) {
            case "ADMIN":           url = "/admin-dashboard.zul";           break;
            case "MAKER_OUTWARD":   url = "/maker-outward-dashboard.zul";   break;
            case "CHECKER_OUTWARD": url = "/checker-outward-dashboard.zul"; break;
            case "MAKER_INWARD":    url = "/maker-inward-dashboard.zul";    break;
            case "CHECKER_INWARD":  url = "/checker-inward-dashboard.zul";  break;
            default:
                showError("Unknown role. Please contact administrator.");
                return;
        }
        Executions.sendRedirect(url);
    }

    private void showError(String message) {
        lblError.setValue(message);
        lblError.setVisible(true);
    }

    private void hideError() {
        lblError.setVisible(false);
        lblError.setValue("");
    }
}