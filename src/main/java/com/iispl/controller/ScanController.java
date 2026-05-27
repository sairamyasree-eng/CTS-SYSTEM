package com.iispl.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;

public class ScanController extends SelectorComposer<Component>{
	@Wire
	public Textbox cheque_count;
	
	@Wire
	public Textbox total_amount;
	
	@Wire
	public Textbox batch_id;
	
	@Wire
	public Textbox folder_upload;
	
	@Wire
	public Textbox processing_date;
	
	@Listen("onClick=#close_button")
	public void closeScan() {
		Executions.sendRedirect("/maker-outward-dashboard.zul");
	}
	
	@Listen("onClick=#scanButton")
	public void startScan() {
		Executions.sendRedirect("/scanService.zul");
	}
	
	@Listen("onClick=#cancelButton")
	public void cancelScan() {
		Executions.sendRedirect("/maker-outward-dashboard.zul");
	}
	
	
}
