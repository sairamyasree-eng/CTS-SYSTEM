package com.iispl.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;

// BUG-FIX: Must extend SelectorComposer<Component> to be usable as ZK apply= composer.
// Previously was a plain class — ZK would throw ClassCastException at runtime.
public class CheckerOutController extends SelectorComposer<Component>{

}
