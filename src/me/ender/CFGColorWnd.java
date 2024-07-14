package me.ender;

import haven.*;
import haven.Button;

import java.awt.*;

public class CFGColorWnd extends WindowX {
    private final TextEntry entry;
    private Color col;
    
    public CFGColorWnd(CFG<Color> cfg) {
	super(Coord.z, "Set Color");
	justclose = true;
	skipInitPos = skipSavePos = true;
	col = cfg.get();
	entry = add(new TextEntry(60, ClientUtils.color2hex(col)) {
	    @Override
	    public void activate(String text) {
		update();
	    }
	    
	    @Override
	    protected void changed() {
		super.changed();
		update();
	    }
	}, 50, 0);
	
	add(new Button(60, "Save", () -> {
	    update();
	    cfg.set(col);
	    close();
	}), 50, 25);
	
	pack();
    }
    
    private void update() {
	col = ClientUtils.hex2color(entry.text(), col);
    }
    
    @Override
    public void cdraw(GOut og) {
	super.cdraw(og);
	og.chcolor(col);
	og.frect2(Coord.of(5, 5), Coord.of(40, 40));
	og.chcolor();
    }
}
