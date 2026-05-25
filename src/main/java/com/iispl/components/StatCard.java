package com.iispl.components;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class StatCard extends HtmlMacroComponent {

    private static final long serialVersionUID = 1L;

    @Wire private Div   mc_statCard;
    @Wire private Label mc_statNumber;
    @Wire private Label mc_statLabel;

    private String cardColor  = "blue";
    private String cardSize   = "";
    private String statNumber = "0";
    private String statLabel  = "";

    public StatCard() {
        compose();
    }

    @Override
    public void afterCompose() {
        super.afterCompose();
        String css = "stat-card " + cardColor;
        if (cardSize != null && !cardSize.isEmpty()) css += " " + cardSize;
        mc_statCard.setSclass(css);
        mc_statNumber.setValue(statNumber);
        mc_statLabel.setValue(statLabel);
    }

    public String getCardColor()  { return cardColor;  }
    public String getCardSize()   { return cardSize;   }
    public String getStatNumber() { return statNumber; }
    public String getStatLabel()  { return statLabel;  }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor != null ? cardColor : "blue";
    }

    public void setCardSize(String cardSize) {
        this.cardSize = cardSize != null ? cardSize : "";
    }

    public void setStatNumber(String statNumber) {
        this.statNumber = statNumber != null ? statNumber : "0";
    }

    public void setStatLabel(String statLabel) {
        this.statLabel = statLabel != null ? statLabel : "";
    }
}