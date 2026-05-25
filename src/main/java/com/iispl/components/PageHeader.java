package com.iispl.components;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class PageHeader extends HtmlMacroComponent {

    private static final long serialVersionUID = 1L;

    @Wire private Label  mc_breadcrumb;
    @Wire private Label  mc_pageTitle;
    @Wire private Label  mc_pageSubtitle;
    @Wire private Div    mc_btnPanel;
    @Wire private Button mc_btnAction;

    private String breadcrumb   = "";
    private String pageTitle    = "";
    private String pageSubtitle = "";
    private String btnLabel     = "";

    public PageHeader() {
        compose();
    }

    @Override
    public void afterCompose() {
        super.afterCompose();
        mc_breadcrumb.setValue(breadcrumb);
        mc_pageTitle.setValue(pageTitle);
        mc_pageSubtitle.setValue(pageSubtitle);
        if (btnLabel != null && !btnLabel.isEmpty()) {
            mc_btnAction.setLabel(btnLabel);
            mc_btnPanel.setVisible(true);
        } else {
            mc_btnPanel.setVisible(false);
        }
    }

    public String getBreadcrumb()   { return breadcrumb;   }
    public String getPageTitle()    { return pageTitle;    }
    public String getPageSubtitle() { return pageSubtitle; }
    public String getBtnLabel()     { return btnLabel;     }

    public void setBreadcrumb(String breadcrumb) {
        this.breadcrumb = breadcrumb != null ? breadcrumb : "";
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle != null ? pageTitle : "";
    }

    public void setPageSubtitle(String pageSubtitle) {
        this.pageSubtitle = pageSubtitle != null ? pageSubtitle : "";
    }

    public void setBtnLabel(String btnLabel) {
        this.btnLabel = btnLabel != null ? btnLabel : "";
    }
}