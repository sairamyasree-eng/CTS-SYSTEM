package com.iispl.controller;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Borderlayout;

import com.iispl.components.StatCard;

public class MakerOutwardController
        extends SelectorComposer<Borderlayout> {

    private static final long serialVersionUID = 1L;

    // Stat cards — ids match statId attribute in ZUL
    @Wire private StatCard lblChequesLoaded;
    @Wire private StatCard lblAutoApproved;
    @Wire private StatCard lblIqaFailed;
    @Wire private StatCard lblRepaired;
    @Wire private StatCard lblReferredBack;

    @Override
    public void doAfterCompose(Borderlayout comp) throws Exception {
        super.doAfterCompose(comp);

        // Mark current page for sidebar highlighting
        Sessions.getCurrent().setAttribute("currentPage", "dashboard");

        // Load real counts from service layer here.
        // Defaults already set in ZUL.
        // Example when service is wired:
        //   OutwardSummaryDTO summary = outwardService.getTodaySummary();
        //   lblChequesLoaded.setStatNumber(String.valueOf(summary.getLoaded()));
        //   lblAutoApproved.setStatNumber(String.valueOf(summary.getAutoApproved()));
        //   lblIqaFailed.setStatNumber(String.valueOf(summary.getIqaFailed()));
        //   lblRepaired.setStatNumber(String.valueOf(summary.getRepaired()));
        //   lblReferredBack.setStatNumber(String.valueOf(summary.getReferredBack()));
    }

    @Listen("onClick = #btnNewScan")
    public void openScanDialog() {
        org.zkoss.zk.ui.Executions.sendRedirect("/scanPopup.zul");
    }
        
    
}