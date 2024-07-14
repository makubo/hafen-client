package me.ender;

import haven.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CFGColorBtn extends IButton implements CFG.Observer<Color> {
    private static final BufferedImage box = Resource.loadsimg("gfx/hud/color/box");
    private static final Coord isz = Coord.of(box.getWidth(), box.getHeight());
    private final CFG<Color> cfg;
    private final String title;
    
    public CFGColorBtn(CFG<Color> cfg, String title) {
	super(drawUp(cfg, title), drawDown(cfg, title), drawHover(cfg, title));
	this.title = title;
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
    
    private static BufferedImage draw(CFG<Color> cfg, String title, Color textColor) {
	BufferedImage ret = TexI.mkbuf(isz);
	Graphics g = ret.getGraphics();
	
	g.setColor(cfg.get());
	g.fillRect(2, 2, isz.x-4, isz.y-4);
	
	g.setColor(Color.WHITE);
	g.drawImage(box, 0,0, null);
	
	g.dispose();
	
	return ItemInfo.catimgsh(UI.scale(5), ret, Text.render(title, textColor).img);
    }
    
    private static BufferedImage drawUp(CFG<Color> cfg, String title) {
	return draw(cfg, title, Color.WHITE);
    }
    
    private static BufferedImage drawDown(CFG<Color> cfg, String title) {
	return draw(cfg, title, Color.LIGHT_GRAY);
    }
    
    private static BufferedImage drawHover(CFG<Color> cfg, String title) {
	return draw(cfg, title, Color.YELLOW);
    }
    
    @Override
    public void updated(CFG<Color> cfg) {
	up = drawUp(cfg, title);
	down = drawDown(cfg, title);
	hover = drawHover(cfg, title);
	redraw();
    }
}
