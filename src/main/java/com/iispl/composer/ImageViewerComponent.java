package com.iispl.composer;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

/**
 * ImageViewerComponent
 * ====================
 * Macro component for Front / Back / Grayscale tab switching for cheque images.
 * Optionally shows a signature comparison panel.
 *
 * Usage in ZUL:
 *   <?component name="imageViewer" macroURI="/components/imageViewerPanel.zul"
 *               class="com.iispl.demo.ImageViewerComponent"?>
 *   <imageViewer id="viewer"/>
 *
 * Call sequence from your page composer:
 *   viewer.setFrontImagePath(path)
 *   viewer.setBackImagePath(path)        [optional]
 *   viewer.setGrayscaleImagePath(path)   [optional]
 *   viewer.setShowSignature(true)        [optional]
 *   viewer.setSignaturePath(path)
 *   viewer.setBankSignaturePath(path)
 *   viewer.setSignatureMatch(true/false/null)  null = not evaluated
 *   viewer.refresh()
 */
public class ImageViewerComponent extends HtmlMacroComponent {

    private static final long serialVersionUID = 1L;

    // ── Tab elements ──
    @Wire private Div   mc_tabFront;
    @Wire private Div   mc_tabBack;
    @Wire private Div   mc_tabGrayscale;

    // ── Panels ──
    @Wire private Div   mc_panelFront;
    @Wire private Div   mc_panelBack;
    @Wire private Div   mc_panelGrayscale;

    // ── Images ──
    @Wire private Image mc_imgFront;
    @Wire private Image mc_imgBack;
    @Wire private Image mc_imgGrayscale;

    // ── Placeholders ──
    @Wire private Div   mc_placeholderFront;
    @Wire private Div   mc_placeholderBack;
    @Wire private Div   mc_placeholderGrayscale;

    // ── Signature panel ──
    @Wire private Div   mc_signaturePanel;
    @Wire private Image mc_imgSig;
    @Wire private Image mc_imgBankSig;
    @Wire private Div   mc_placeholderSig;
    @Wire private Div   mc_placeholderBankSig;
    @Wire private Div   mc_sigMatchBadge;
    @Wire private Label mc_lblSigMatch;

    // ── Configuration (set before afterCompose / refresh) ──
    private String  frontImagePath     = null;
    private String  backImagePath      = null;
    private String  grayscaleImagePath = null;
    private boolean showSignature      = false;
    private String  signaturePath      = null;
    private String  bankSignaturePath  = null;
    private Boolean signatureMatch     = null; // null = not evaluated yet

    // ── State ──
    private String activeTab = "front";

    public ImageViewerComponent() {
        compose();
    }

    @Override
    public void afterCompose() {
        super.afterCompose();
        refresh();
    }

    // ── Public setters ────────────────────────────────────────────────────

    public void setFrontImagePath(String path)     { this.frontImagePath = path; }
    public void setBackImagePath(String path)      { this.backImagePath = path; }
    public void setGrayscaleImagePath(String path) { this.grayscaleImagePath = path; }
    public void setShowSignature(boolean show)     { this.showSignature = show; }
    public void setSignaturePath(String path)      { this.signaturePath = path; }
    public void setBankSignaturePath(String path)  { this.bankSignaturePath = path; }

    /** @param match true=matched, false=mismatch, null=not evaluated */
    public void setSignatureMatch(Boolean match)   { this.signatureMatch = match; }

    /** Reset to front tab and clear all images */
    public void reset() {
        frontImagePath = null; backImagePath = null; grayscaleImagePath = null;
        signaturePath  = null; bankSignaturePath = null; signatureMatch = null;
        activeTab      = "front";
        showSignature  = false;
    }

    /** Apply all configuration to the UI — call after any setter changes */
    public void refresh() {
        loadImages();
        applyTab(activeTab);
        renderSignaturePanel();
    }

    // ── Tab switching ─────────────────────────────────────────────────────

    @Listen("onClick = #mc_tabFront")
    public void onTabFront()     { applyTab("front"); }

    @Listen("onClick = #mc_tabBack")
    public void onTabBack()      { applyTab("back"); }

    @Listen("onClick = #mc_tabGrayscale")
    public void onTabGrayscale() { applyTab("grayscale"); }

    private void applyTab(String tab) {
        activeTab = tab;
        mc_tabFront.setSclass("img-tab"     + ("front".equals(tab)     ? " active" : ""));
        mc_tabBack.setSclass("img-tab"       + ("back".equals(tab)      ? " active" : ""));
        mc_tabGrayscale.setSclass("img-tab"  + ("grayscale".equals(tab) ? " active" : ""));
        mc_panelFront.setVisible("front".equals(tab));
        mc_panelBack.setVisible("back".equals(tab));
        mc_panelGrayscale.setVisible("grayscale".equals(tab));
    }

    // ── Load images ───────────────────────────────────────────────────────

    private void loadImages() {
        setImage(mc_imgFront,     mc_placeholderFront,     frontImagePath);
        setImage(mc_imgBack,      mc_placeholderBack,      backImagePath);
        setImage(mc_imgGrayscale, mc_placeholderGrayscale, grayscaleImagePath);
    }

    private void setImage(Image imgComp, Div placeholder, String path) {
        if (path != null && !path.isEmpty()) {
            imgComp.setSrc(path);
            imgComp.setVisible(true);
            placeholder.setVisible(false);
        } else {
            imgComp.setVisible(false);
            placeholder.setVisible(true);
        }
    }

    // ── Signature panel ───────────────────────────────────────────────────

    private void renderSignaturePanel() {
        mc_signaturePanel.setVisible(showSignature);
        if (!showSignature) return;

        setImage(mc_imgSig,     mc_placeholderSig,     signaturePath);
        setImage(mc_imgBankSig, mc_placeholderBankSig, bankSignaturePath);

        if (signatureMatch != null) {
            mc_sigMatchBadge.setVisible(true);
            if (signatureMatch) {
                mc_lblSigMatch.setValue("✓  Signature Matched");
                mc_sigMatchBadge.setSclass("sig-match-badge match");
            } else {
                mc_lblSigMatch.setValue("✕  Signature Mismatch");
                mc_sigMatchBadge.setSclass("sig-match-badge mismatch");
            }
        } else {
            mc_sigMatchBadge.setVisible(false);
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────

    public String  getActiveTab()      { return activeTab; }
    public Boolean getSignatureMatch() { return signatureMatch; }
    public String  getFrontImagePath() { return frontImagePath; }
    public String  getBackImagePath()  { return backImagePath; }
    public boolean isShowSignature()   { return showSignature; }
}
