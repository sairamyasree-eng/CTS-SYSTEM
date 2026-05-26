package com.iispl.controller;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Borderlayout;

import com.iispl.components.StatCard;

public class CheckerOutwardDashboardController
        extends SelectorComposer<Borderlayout> {

    private static final long serialVersionUID = 1L;

    @Wire private StatCard lblAutoApproved;
    @Wire private StatCard lblAwaitingReview;
    @Wire private StatCard lblApproved;
    @Wire private StatCard lblRejected;
    @Wire private StatCard lblReferred;

    @Override
    public void doAfterCompose(Borderlayout comp) throws Exception {
        super.doAfterCompose(comp);

        Sessions.getCurrent().setAttribute("currentPage", "dashboard");

        // Wire real counts from service layer here.
        // Example:
        //   OutwardSummaryDTO s = checkerOutwardService.getTodaySummary();
        //   lblAutoApproved.setStatNumber(String.valueOf(s.getAutoApproved()));
        //   lblAwaitingReview.setStatNumber(String.valueOf(s.getAwaitingReview()));
        //   lblApproved.setStatNumber(String.valueOf(s.getApproved()));
        //   lblRejected.setStatNumber(String.valueOf(s.getRejected()));
        //   lblReferred.setStatNumber(String.valueOf(s.getReferred()));
    }
}