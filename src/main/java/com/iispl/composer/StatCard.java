package com.iispl.composer;

import org.zkoss.zk.ui.HtmlMacroComponent;

/**
 * Companion class for /reuseableComponents/statCard.zul
 *
 * Accepted attributes (set in ZUL via attribute names):
 *   cardColor  – CSS colour modifier: blue | green | purple | teal  (default: "blue")
 *   cardSize   – extra CSS modifier : "small" | ""                  (default: "")
 *   statNumber – the big number on the card                         (default: "0")
 *   statLabel  – the label below the number                         (default: "")
 *
 * ZUL registration:
 *   <?component name="statcard"
 *               macroURI="/reuseableComponents/statCard.zul"
 *               class="com.iispl.composer.StatCard"?>
 */
public class StatCard extends HtmlMacroComponent {

    private static final long serialVersionUID = 1L;

    // ── Fields with sensible defaults ─────────────────────────────────────────

    private String cardColor  = "blue";
    private String cardSize   = "";
    private String statNumber = "0";
    private String statLabel  = "";

    // ── Getters ───────────────────────────────────────────────────────────────

    public String getCardColor()  { return cardColor; }
    public String getCardSize()   { return cardSize;  }
    public String getStatNumber() { return statNumber; }
    public String getStatLabel()  { return statLabel;  }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor != null ? cardColor : "blue";
        invalidate(); // re-render macro when value changes
    }

    public void setCardSize(String cardSize) {
        this.cardSize = cardSize != null ? cardSize : "";
        invalidate();
    }

    public void setStatNumber(String statNumber) {
        this.statNumber = statNumber != null ? statNumber : "0";
        invalidate();
    }

    public void setStatLabel(String statLabel) {
        this.statLabel = statLabel != null ? statLabel : "";
        invalidate();
    }
}
