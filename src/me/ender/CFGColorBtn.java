package me.ender;

import haven.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CFGColorBtn extends IButton implements CFG.Observer<Color> {
    private static final BufferedImage box = Resource.loadsimg("gfx/hud/color/box");
    private static final Coord isz = Coord.of(box.getWidth(), box.getHeight());
    private static final Coord pad = UI.scale(Coord.of(2));
    private static final Coord csz = isz.sub(pad.mul(2));
    private final CFG<Color> cfg;
    private final String title;
    private final boolean hasAlpha;
    private boolean dis;
    
    public CFGColorBtn(CFG<Color> cfg, String title, boolean hasAlpha) {
	super(drawUp(cfg, title), drawDown(cfg, title), drawHover(cfg, title));
	this.title = title;
	this.hasAlpha = hasAlpha;
	recthit = true;
	
	this.cfg = cfg;
	cfg.observe(this);
    }
    
    @Override
    public boolean mousedown(Coord c, int button) {
	if(dis) {return false;}
	return super.mousedown(c, button);
    }
    
    @Override
    public boolean mouseup(Coord c, int button) {
	if(dis) {return false;}
	return super.mouseup(c, button);
    }
    
    @Override
    public void mousemove(Coord c) {
	if(dis) {return;}
	super.mousemove(c);
    }
    
    @Override
    public void click() {
	if(dis) {return;}
	Widget root = ui.gui;
	if(root == null) {
	    root = ui.root;
	}
	dis = true;
	root.add(new CFGColorWnd(cfg, hasAlpha), ui.mc.add(10, -45)).onDestroyed(widget -> dis = false);
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
	g.fillRect(pad.x, pad.y, csz.x, csz.y);
	
	g.setColor(textColor);
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
