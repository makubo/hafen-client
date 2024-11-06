package me.ender.ui;

import haven.CFG;
import haven.HSlider;
import haven.Label;

public class CFGSlider extends HSlider {
    private final CFG<Integer> cfg;
    private final Label label;
    private final String format;

    public CFGSlider(int w, int min, int max, CFG<Integer> cfg, Label label, String format) {
	super(w, min, max, cfg.get());
	this.cfg = cfg;
	this.label = label;
	this.format = format;
    }

    @Override
    protected void attached() {
	super.attached();
	val = cfg.get();
	updateLabel();
    }

    public void changed() {
	cfg.set(val);
	updateLabel();
    }

    private void updateLabel() {
	label.settext(String.format(format, val));
    }
}
