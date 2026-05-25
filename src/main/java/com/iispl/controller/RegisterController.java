package com.iispl.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.iispl.exceptions.AuthException;
import com.iispl.service.RegisterService;

public class RegisterController extends SelectorComposer<Component> {

    @Wire private Textbox txtUsername;
    @Wire private Textbox txtPassword;
    @Wire private Textbox txtConfirmPassword;
    @Wire private Listbox cmbRole;
    @Wire private Label   lblError;
    @Wire private Label   lblSuccess;

    private final RegisterService registerService = new RegisterService();

    @Listen("onClick = #btnRegister")
    public void onRegister() {
        hideMessages();

        String username        = txtUsername.getValue().trim();
        String password        = txtPassword.getValue().trim();
        String confirmPassword = txtConfirmPassword.getValue().trim();
        String roleName        = getSelectedRoleValue();

        try {
            registerService.register(username, password, confirmPassword, roleName);
            showSuccess("User registered successfully!");
            clearForm();
        } catch (AuthException e) {
            showError(e.getMessage());
        }
    }

    @Listen("onClick = #btnGoLogin")
    public void onGoLogin() {
        Executions.sendRedirect("/login.zul");
    }

    private String getSelectedRoleValue() {
        Listitem selected = cmbRole.getSelectedItem();
        if (selected == null) return "";
        Object val = selected.getValue();
        return val == null ? "" : val.toString();
    }

    private void clearForm() {
        txtUsername.setValue("");
        txtPassword.setValue("");
        txtConfirmPassword.setValue("");
        cmbRole.setSelectedIndex(0);
    }

    private void showError(String message) {
        lblError.setValue(message);
        lblError.setVisible(true);
    }

    private void showSuccess(String message) {
        lblSuccess.setValue(message);
        lblSuccess.setVisible(true);
    }

    private void hideMessages() {
        lblError.setVisible(false);
        lblError.setValue("");
        lblSuccess.setVisible(false);
        lblSuccess.setValue("");
    }
}