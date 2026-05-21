package com.iispl.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.iispl.dto.SessionUserDTO;
import com.iispl.exceptions.AuthException;
import com.iispl.service.LoginService;

public class LoginController extends SelectorComposer<Component>{
	
	@Wire private Textbox txtUsername; 
	
	@Wire private Textbox txtPassword;
	
	@Wire private Listbox cmbRole;
	
	@Wire private Label lblError;
	
	@WireVariable
    private LoginService loginService;
	
	private static final String DASHBOARD_URL = "/dashboard.zul";
	
	@Listen("onClick = #btnSignIn")
	public void validate() {
		String username = txtUsername.getValue();
		String password = txtPassword.getValue();
		String roleValue = getSelectedRoleValue();
		
		hideError();
		
		try {
			SessionUserDTO sessionUser = loginService.authenticate(username,password,roleValue);
			
			Sessions.getCurrent().setAttribute(SessionUserDTO.SESSION_KEY, sessionUser);
			
			Sessions.getCurrent().setAttribute("currentPage", "dashboard");
			
			txtPassword.setValue("");
			
			Executions.sendRedirect(DASHBOARD_URL);
		}catch(AuthException e) {
			showError(e.getMessage());
		}
	}
	
	public String getSelectedRoleValue() {
		Listitem selected = cmbRole.getSelectedItem();
		if(selected == null) {
			return "";
		}
		
		Object val = selected.getValue();
		
		return val == null ? "" : val.toString();
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
