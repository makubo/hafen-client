package me.ender;

import haven.*;
import haven.Button;
import haven.Label;

import java.awt.*;

public class CFGColorWnd extends WindowX {
    private static final Coord BOX = UI.scale(Coord.of(45));
    private static final int PAD = UI.scale(5);
    private static final int BTN_W = UI.scale(50);
    private static final int HEX_W = UI.scale(70);
    private static final int TEXT_W = UI.scale(30);
    
    private final TextEntry txtHex, txtR, txtG, txtB, txtA;
    private Color col;
    private final boolean hasAlpha;
    
    public CFGColorWnd(CFG<Color> cfg, boolean hasAlpha) {
	super(Coord.z, "Set Color");
	justclose = true;
	skipInitPos = skipSavePos = true;
	this.hasAlpha = hasAlpha;
	col = cfg.get();
	
	Composer composer = new Composer(this).hmrgn(PAD).vmrgn(PAD).hpad(BOX.x + PAD);
	
	txtR = new TextField(TEXT_W, Integer.toString(col.getRed()), this::rgbUpdated);
	txtG = new TextField(TEXT_W, Integer.toString(col.getBlue()), this::rgbUpdated);
	txtB = new TextField(TEXT_W, Integer.toString(col.getGreen()), this::rgbUpdated);
	composer.addr(
	    new Label("R:"), txtR,
	    new Label("G:"), txtG,
	    new Label("B:"), txtB
	);
	
	txtHex = new TextField(HEX_W, ClientUtils.color2hex(col, this.hasAlpha).toUpperCase(), this::hexUpdated);
	Label label = new Label("Hex:");
	
	if(this.hasAlpha) {
	    txtA = new TextField(TEXT_W, Integer.toString(col.getAlpha()), this::rgbUpdated);
	    composer.addr(label, txtHex, new Label("A:"), txtA);
	} else {
	    txtA = null;
	    composer.addr(label, txtHex);
	}
	composer.hpad(0);
	
	composer.addr(
	    new Button(BTN_W, "Apply", () -> {
		cfg.set(col);
	    }),
	    new Button(BTN_W, "Save", () -> {
		cfg.set(col);
		close();
	    }));
	
	pack();
    }
    
    private void rgbUpdated() {
	col = new Color(
	    ClientUtils.str2cc(txtR.text()),
	    ClientUtils.str2cc(txtG.text()),
	    ClientUtils.str2cc(txtB.text()),
	    hasAlpha ? ClientUtils.str2cc(txtA.text()) : 255
	);
	txtHex.settext(ClientUtils.color2hex(col, hasAlpha).toUpperCase());
    }
    
    private void hexUpdated() {
	col = ClientUtils.hex2color(txtHex.text(), col);
	txtR.settext(Integer.toString(col.getRed()));
	txtG.settext(Integer.toString(col.getGreen()));
	txtB.settext(Integer.toString(col.getBlue()));
	if(txtA != null) {
	    txtA.settext(Integer.toString(col.getAlpha()));
	}
    }
    
    @Override
    public void cdraw(GOut og) {
	super.cdraw(og);
	og.chcolor(col);
	og.frect2(Coord.z, BOX);
	og.chcolor();
    }
    
    private static class TextField extends TextEntry {
	private final Runnable changed;
	
	public TextField(int w, String deftext, Runnable changed) {
	    super(w, deftext);
	    this.changed = changed;
	}
	
	@Override
	public void activate(String text) {
	    if(changed != null) {changed.run();}
	}
	
	@Override
	public void changed(ReadLine buf) {
	    super.changed(buf);
	    if(changed != null) {changed.run();}
	}
    }
}
