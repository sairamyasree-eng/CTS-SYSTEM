package com.iispl.components;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class ModuleCard extends HtmlMacroComponent {

	private static final long serialVersionUID = 1L;

	@Wire
	private Div mc_moduleCard;
	@Wire
	private Label mc_icon;
	@Wire
	private Label mc_stepLabel;
	@Wire
	private Label mc_moduleTitle;
	@Wire
	private Label mc_moduleDesc;
	@Wire
	private A mc_moduleLink;

	private String icon = "";
	private String stepLabel = "";
	private String moduleTitle = "";
	private String moduleDesc = "";
	private String moduleHref = "#";
	private String moduleAction = "Open →";
	private String moduleId;

	public ModuleCard() {
		// DO NOT call compose() here — ZK calls it automatically
	}

	@Override
	public void afterCompose() {
		super.afterCompose(); // triggers @Wire — must be first
		mc_icon.setValue(icon);
		mc_stepLabel.setValue(stepLabel);
		mc_moduleTitle.setValue(moduleTitle);
		mc_moduleDesc.setValue(moduleDesc);
		mc_moduleLink.setHref(moduleHref);
		mc_moduleLink.setLabel(moduleAction);
	}

	public void setIcon(String v) {
		this.icon = v != null ? v : "";
	}

	public void setStepLabel(String v) {
		this.stepLabel = v != null ? v : "";
	}

	public void setModuleTitle(String v) {
		this.moduleTitle = v != null ? v : "";
	}

	public void setModuleDesc(String v) {
		this.moduleDesc = v != null ? v : "";
	}

	public void setModuleHref(String v) {
		this.moduleHref = v != null ? v : "#";
	}

	public void setModuleAction(String v) {
		this.moduleAction = v != null ? v : "Open →";
	}

	public String getIcon() {
		return icon;
	}

	public String getStepLabel() {
		return stepLabel;
	}

	public String getModuleTitle() {
		return moduleTitle;
	}

	public String getModuleDesc() {
		return moduleDesc;
	}

	public String getModuleHref() {
		return moduleHref;
	}

	public String getModuleAction() {
		return moduleAction;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
		this.setId(moduleId);
	}
}