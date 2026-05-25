package com.iispl.components;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

import java.util.Arrays;
import java.util.List;

/**
 * WorkflowProgressBarComponent
 * =============================
 * Macro component that renders the CTS pipeline step bar.
 * Each step is DONE (green), ACTIVE (blue), or PENDING (gray).
 *
 * Usage in ZUL:
 *   <?component name="workflowBar" macroURI="/components/workflowProgressBar.zul"
 *               class="com.iispl.demo.WorkflowProgressBarComponent"?>
 *   <workflowBar id="wfBar"/>
 *
 * Setup from your page composer:
 *   wfBar.setActiveStep("checker-out")
 *   wfBar.render()   ← must call after setActiveStep
 *
 * setActiveStep(pageId) auto-marks:
 *   - all steps BEFORE active → DONE
 *   - the matching step       → ACTIVE
 *   - all steps AFTER active  → PENDING
 */
public class WorkflowProgressBarComponent extends HtmlMacroComponent {

    private static final long serialVersionUID = 1L;

    @Wire private Div mc_stepsContainer;

    private List<WorkflowStep> steps = DEFAULT_STEPS;

    // ── Full CTS pipeline (default) ───────────────────────────────────────

    public static final List<WorkflowStep> DEFAULT_STEPS = Arrays.asList(
        new WorkflowStep(1, "Scan",          "scan",          WorkflowStep.State.PENDING),
        new WorkflowStep(2, "Repair",        "repair",        WorkflowStep.State.PENDING),
        new WorkflowStep(3, "Checker Out",   "checker-out",   WorkflowStep.State.PENDING),
        new WorkflowStep(4, "File Process",  "dem",           WorkflowStep.State.PENDING),
        new WorkflowStep(5, "Inward Repair", "inward-repair", WorkflowStep.State.PENDING),
        new WorkflowStep(6, "Checker In",    "inward-verify", WorkflowStep.State.PENDING),
        new WorkflowStep(7, "CBS",           "cbs",           WorkflowStep.State.PENDING),
        new WorkflowStep(8, "Batch Status",  "view-batches",  WorkflowStep.State.PENDING)
    );

    // ── WorkflowStep inner class ──────────────────────────────────────────

    public static class WorkflowStep {
        public enum State { DONE, ACTIVE, PENDING }

        public final int    stepNum;
        public final String label;
        public final String pageId;
        public       State  state;

        public WorkflowStep(int stepNum, String label, String pageId, State state) {
            this.stepNum = stepNum;
            this.label   = label;
            this.pageId  = pageId;
            this.state   = state;
        }
    }

    public WorkflowProgressBarComponent() {
        compose();
    }

    @Override
    public void afterCompose() {
        super.afterCompose();
        // render() must be called explicitly after setActiveStep()
    }

    // ── Public API ────────────────────────────────────────────────────────

    /** Override with a custom step list */
    public void setSteps(List<WorkflowStep> steps) {
        this.steps = steps;
    }

    /**
     * Given the active page ID, auto-marks:
     *   - all steps before it → DONE
     *   - the matching step   → ACTIVE
     *   - all steps after it  → PENDING
     */
    public void setActiveStep(String activePageId) {
        boolean foundActive = false;
        for (WorkflowStep step : steps) {
            if (step.pageId.equals(activePageId)) {
                step.state  = WorkflowStep.State.ACTIVE;
                foundActive = true;
            } else if (!foundActive) {
                step.state  = WorkflowStep.State.DONE;
            } else {
                step.state  = WorkflowStep.State.PENDING;
            }
        }
    }

    /** Renders the step bar into the ZUL container */
    public void render() {
        mc_stepsContainer.getChildren().clear();

        for (int i = 0; i < steps.size(); i++) {
            WorkflowStep step  = steps.get(i);
            boolean      isLast = (i == steps.size() - 1);

            Div stepDiv = new Div();

            String css = "workflow-step";
            if (step.state == WorkflowStep.State.DONE)   css += " done";
            if (step.state == WorkflowStep.State.ACTIVE)  css += " active";
            if (isLast) css += " last";
            stepDiv.setSclass(css);

            Label numLbl = new Label("Step " + step.stepNum);
            numLbl.setSclass("wf-step-num");
            numLbl.setParent(stepDiv);

            Label nameLbl = new Label(step.label);
            nameLbl.setSclass("wf-step-label");
            nameLbl.setParent(stepDiv);

            Div dot = new Div();
            dot.setSclass("wf-step-dot");
            dot.setParent(stepDiv);

            stepDiv.setParent(mc_stepsContainer);
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────

    public List<WorkflowStep> getSteps() { return steps; }
}
