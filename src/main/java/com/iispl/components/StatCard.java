package com.iispl.components;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class StatCard extends HtmlMacroComponent {

	private static final long serialVersionUID = 1L;

	@Wire
	private Div mc_statCard;
	@Wire
	private Label mc_statNumber;
	@Wire
	private Label mc_statLabel;

	private String cardColor = "blue";
	private String statNumber = "0";
	private String statLabel = "";
	private String statId;

	public StatCard() {
		// DO NOT call compose() here — ZK calls it automatically
	}

	@Override
	public void afterCompose() {
		super.afterCompose(); // triggers @Wire — must be first
		mc_statCard.setSclass("stat-card " + cardColor);
		mc_statNumber.setValue(statNumber);
		mc_statLabel.setValue(statLabel);
	}

	public void setCardColor(String v) {
		this.cardColor = v != null ? v : "blue";
	}

	public void setStatNumber(String v) {
		this.statNumber = v != null ? v : "0";
	}

	public void setStatLabel(String v) {
		this.statLabel = v != null ? v : "";
	}

	public String getCardColor() {
		return cardColor;
	}

	public String getStatNumber() {
		return statNumber;
	}

	public String getStatLabel() {
		return statLabel;
	}

	public void setStatId(String statId) {
		this.statId = statId;
		this.setId(statId);
	}

	public String getStatId() {
		return statId;
	}
}