package com.iispl.components;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class ModuleCard extends HtmlMacroComponent {

    private static final long serialVersionUID = 1L;

    @Wire private Div   mc_moduleCard;
    @Wire private Label mc_icon;
    @Wire private Label mc_moduleTitle;
    @Wire private Label mc_moduleDesc;
    @Wire private A     mc_moduleLink;

    private String icon        = "";
    private String moduleTitle = "";
    private String moduleDesc  = "";
    private String moduleHref  = "#";

    public ModuleCard() {
        compose();
    }

    @Override
    public void afterCompose() {
        super.afterCompose();
        mc_icon.setValue(icon);
        mc_moduleTitle.setValue(moduleTitle);
        mc_moduleDesc.setValue(moduleDesc);
        mc_moduleLink.setHref(moduleHref);
    }

    public String getIcon()        { return icon;        }
    public String getModuleTitle() { return moduleTitle; }
    public String getModuleDesc()  { return moduleDesc;  }
    public String getModuleHref()  { return moduleHref;  }

    public void setIcon(String icon) {
        this.icon = icon != null ? icon : "";
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle != null ? moduleTitle : "";
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc != null ? moduleDesc : "";
    }

    public void setModuleHref(String moduleHref) {
        this.moduleHref = moduleHref != null ? moduleHref : "#";
    }
}