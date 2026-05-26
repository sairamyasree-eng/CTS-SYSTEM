package com.iispl.controller;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Borderlayout;

import com.iispl.components.StatCard;

public class CheckerInwardDashboardController
        extends SelectorComposer<Borderlayout> {

    private static final long serialVersionUID = 1L;

    @Wire private StatCard lblPendingVerification;
    @Wire private StatCard lblVerified;
    @Wire private StatCard lblCbsProcessed;

    @Override
    public void doAfterCompose(Borderlayout comp) throws Exception {
        super.doAfterCompose(comp);

        Sessions.getCurrent().setAttribute("currentPage", "dashboard");

        // Wire real counts from service layer here.
        // Example:
        //   InwardSummaryDTO s = checkerInwardService.getTodaySummary();
        //   lblPendingVerification.setStatNumber(String.valueOf(s.getPendingVerification()));
        //   lblVerified.setStatNumber(String.valueOf(s.getVerified()));
        //   lblCbsProcessed.setStatNumber(String.valueOf(s.getCbsProcessed()));
    }
}