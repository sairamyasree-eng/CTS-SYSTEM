package com.iispl.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

public class MakerOutwardController extends SelectorComposer<Component> {

    // ── KPI Labels ──
    @Wire private Label lblTotalScanned;
    @Wire private Label lblPendingRepair;
    @Wire private Label lblAutoApproved;
    @Wire private Label lblInBatch;
    @Wire private Label lblDemSent;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        loadKpis();
    }

    // ── Load KPI values (replace with real DB/service calls) ──
    private void loadKpis() {
        lblTotalScanned.setValue("0");
        lblPendingRepair.setValue("0");
        lblAutoApproved.setValue("0");
        lblInBatch.setValue("0");
        lblDemSent.setValue("0");
    }

    // ── Navigation ──
    // Called by onClick on mod-tiles via @command('navigate', page='...')
    // Note: @command needs MVVM — if you're using MVC (SelectorComposer),
    // switch tile onClick to forward="onNavigate" and handle below,
    // OR change to a Button with onClick wired via @Listen.

    public void onNavigate(String page) {
        switch (page) {
            case "scan":
                Executions.sendRedirect("/pages/scan.zul");
                break;
            case "repair":
                Executions.sendRedirect("/pages/repair.zul");
                break;
            case "view-batches":
                Executions.sendRedirect("/pages/viewBatches.zul");
                break;
            default:
                break;
        }
    }

    // ── New Scan Session button ──
    // Wire btnNewScan with @Listen if needed:
    // @Listen("onClick = #btnNewScan")
    // public void onNewScan() { ... }

}