package me.ender;

import haven.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CFGColorBtn extends IButton implements CFG.Observer<Color> {
    private static final BufferedImage box = Resource.loadsimg("gfx/hud/color/box");
    private static final Coord isz = Coord.of(box.getWidth(), box.getHeight());
    private final CFG<Color> cfg;
    
    public CFGColorBtn(CFG<Color> cfg) {
	super(drawUp(cfg), drawDown(cfg), drawHover(cfg));
	recthit = true;
	
	this.cfg = cfg;
	cfg.observe(this);
    }
    
    @Override
    public void click() {
	Widget root = ui.gui;
	if(root == null) {
	    root = ui.root;
	}
	//TODO: disable button while window is open
	root.add(new CFGColorWnd(cfg), 100, 100); //TODO: set position close to button?
    }
    
    @Override
    public void destroy() {
	cfg.unobserve(this);
	super.destroy();
    }
    
    private static BufferedImage drawUp(CFG<Color> cfg) {
	BufferedImage ret = TexI.mkbuf(isz);
	Graphics g = ret.getGraphics();
	
	g.setColor(cfg.get());
	g.fillRect(2, 2, isz.x-4, isz.y-4);
	
	g.setColor(Color.WHITE);
	g.drawImage(box, 0,0, null);
	
	g.dispose();
	
	return ret;
    }
    
    private static BufferedImage drawDown(CFG<Color> cfg) {
	return drawUp(cfg);
    }
    
    private static BufferedImage drawHover(CFG<Color> cfg) {
	return drawUp(cfg);
    }
    
    @Override
    public void updated(CFG<Color> cfg) {
	up = drawUp(cfg);
	down = drawDown(cfg);
	hover = drawHover(cfg);
	redraw();
    }
}
