package com.iispl.controller;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Borderlayout;

import com.iispl.components.StatCard;
import com.iispl.dto.SessionUserDTO;

public class MakerInwardDashboardController
        extends SelectorComposer<Borderlayout> {

    private static final long serialVersionUID = 1L;

    // Stat cards — ids match statId attribute in ZUL
    @Wire private StatCard lblBpxfStatus;
    @Wire private StatCard lblParsed;
    @Wire private StatCard lblSentChecker;

    @Override
    public void doAfterCompose(Borderlayout comp) throws Exception {
        super.doAfterCompose(comp);

        // Mark current page for sidebar highlighting
        Sessions.getCurrent().setAttribute("currentPage", "dashboard");

        // Load real counts from service layer here.
        // Defaults already set in ZUL ("Pending", 0, 0).
        // Example when service is wired:
        //   InwardSummaryDTO summary = inwardService.getTodaySummary();
        //   lblBpxfStatus.setStatNumber(summary.getBpxfStatus());
        //   lblParsed.setStatNumber(String.valueOf(summary.getParsedCount()));
        //   lblSentChecker.setStatNumber(String.valueOf(summary.getSentToCheckerCount()));
    }
}