package haven;

import java.awt.*;
import java.awt.image.BufferedImage;

import static haven.PUtils.*;

public class WindowX extends Window {
    public WindowX(Coord sz, String cap, boolean lg, Deco deco, boolean defdeco) {
	super(sz, cap, lg, deco, defdeco);
    }
    
    public WindowX(Coord sz, String cap, boolean lg, Deco deco) {
	super(sz, cap, lg, deco, false);
    }
    
    public WindowX(Coord sz, String cap, boolean lg) {
	super(sz, cap, lg, null, true);
    }
    
    public WindowX(Coord sz, String cap) {
	super(sz, cap, false);
    }
    
    protected Deco makedeco() {
	return(new DecoX(this.large));
    }
    
/*
    public static class Slim implements Decorator {
	private static final Tex bg = Resource.loadtex("gfx/hud/wnd/bgtex");
	private static final Tex cl = Resource.loadtex("gfx/hud/wnd/cleft");
	private static final TexI cm = new TexI(Resource.loadsimg("gfx/hud/wnd/cmain"));
	private static final Tex cr = Resource.loadtex("gfx/hud/wnd/cright");
	private static final int capo = UI.scale(2), capio = UI.scale(1);
	private static final Coord mrgn = UI.scale(1, 1);
	private static final Text.Furnace cf = new Text.Imager(new PUtils.TexFurn(new Text.Foundry(Text.serif.deriveFont(Font.BOLD, UI.scale(14))).aa(true), ctex)) {
	    protected BufferedImage proc(Text text) {
		return (rasterimg(blurmask2(text.img.getRaster(), UI.rscale(0.75), UI.rscale(1.0), Color.BLACK)));
	    }
	};
	
	public static final BufferedImage[] cbtni = new BufferedImage[]{
	    Resource.loadsimg("gfx/hud/btn-close"),
	    Resource.loadsimg("gfx/hud/btn-close-d"),
	    Resource.loadsimg("gfx/hud/btn-close-h")
	};
	
	private static final IBox wbox = new IBox("gfx/hud/wnd", "tl", "tr", "bl", "br", "extvl", "extvr", "extht", "exthb") {
	    final Coord co = UI.scale(3, 3), bo = UI.scale(2, 2);
	    
	    public Coord btloff() {return (super.btloff().sub(bo));}
	    
	    public Coord ctloff() {return (super.ctloff().sub(co));}
	    
	    public Coord bisz() {return (super.bisz().sub(bo.mul(2)));}
	    
	    public Coord cisz() {return (super.cisz().sub(co.mul(2)));}
	};
	
	
	public void drawbg(WindowX wnd, GOut g) {
	    g.chcolor(new Color(55, 64, 32, 200));
	    g.frect(wnd.cptl.add(mrgn.mul(2)), wnd.wsz.sub(mrgn.mul(2)));
	    g.chcolor();
	}
	
	public void drawframe(WindowX wnd, GOut g) {
	    wbox.draw(g, wnd.cptl, wnd.wsz);
	}
	
	public void drawtitle(WindowX wnd, GOut g) {
	    if(wnd.cap != null) {
		int w = wnd.cap.sz().x;
		int y = wnd.cptl.y + capo;
		g.aimage(cl, new Coord(wnd.cptl.x, y), 0, 0.5);
		g.aimage(cm, new Coord(wnd.cptl.x + cl.sz().x, y), 0, 0.5, new Coord(w, cm.sz().y));
		g.aimage(cr, new Coord(wnd.cptl.x + w + cl.sz().x, y), 0, 0.5);
		g.aimage(wnd.cap.tex(), new Coord(wnd.cptl.x + cl.sz().x, y - capo - capio), 0, 0.5);
	    }
	}
	
	public boolean checkhit(WindowX wnd, Coord c) {
	    if(wnd.decohide)
		return (c.isect(wnd.atl, wnd.asz));
	    return (c.isect(wnd.cptl, wnd.wsz)
		|| c.isect(wnd.cptl.addy(-cm.sz.y), wnd.cpsz));
	}
	
	public void resize(WindowX wnd, Coord sz) {
	    wnd.asz = sz;
	    wnd.csz = wnd.asz.add(mrgn.mul(2));
	    wnd.wsz = wnd.csz.add(wbox.bisz()).addy(cm.sz().y / 2);
	    wnd.cptl = new Coord(wnd.tlo.x, Math.max(wnd.tlo.y, capo) + cm.sz().y / 2);
	    wnd.cpsz = new Coord(cl.sz().x + cm.sz.x + cr.sz().x, cm.sz.y);
	    wnd.sz = wnd.wsz.add(wnd.cptl).add(wnd.rbo);
	    wnd.ctl = wnd.cptl.add(wbox.btloff()).add(0, cm.sz().y / 2);
	    wnd.atl = wnd.ctl.add(mrgn);
	    wnd.cbtn.c = wnd.xlate(new Coord(wnd.wsz.x - wnd.cbtn.sz.x, wnd.atl.y - wnd.cbtn.sz.y), false);
	}
    }
*/

/*
    private static class OldSchool implements Decorator {
	
	@Override
	public void drawbg(WindowX wnd, GOut g) {
	    Coord bgc = new Coord();
	    Coord cbr = wnd.ctl.add(wnd.csz);
	    for (bgc.y = wnd.ctl.y; bgc.y < cbr.y; bgc.y += bg.sz().y) {
		for (bgc.x = wnd.ctl.x; bgc.x < cbr.x; bgc.x += bg.sz().x)
		    g.image(bg, bgc, wnd.ctl, cbr);
	    }
	    bgc.x = wnd.ctl.x;
	    for (bgc.y = wnd.ctl.y; bgc.y < cbr.y; bgc.y += bgl.sz().y)
		g.image(bgl, bgc, wnd.ctl, cbr);
	    bgc.x = cbr.x - bgr.sz().x;
	    for (bgc.y = wnd.ctl.y; bgc.y < cbr.y; bgc.y += bgr.sz().y)
		g.image(bgr, bgc, wnd.ctl, cbr);
	}
	
	@Override
	public void drawframe(WindowX wnd, GOut g) {
	    Coord mdo, cbr;
	    g.image(cl, wnd.tlo);
	    mdo = wnd.tlo.add(cl.sz().x, 0);
	    cbr = mdo.add(wnd.cmw, cm.sz().y);
	    for (int x = 0; x < wnd.cmw; x += cm.sz().x)
		g.image(cm, mdo.add(x, 0), Coord.z, cbr);
	    g.image(cr, wnd.tlo.add(cl.sz().x + wnd.cmw, 0));
	    g.image(wnd.cap.tex(), wnd.tlo.add(cpo));
	    mdo = wnd.tlo.add(cl.sz().x + wnd.cmw + cr.sz().x, 0);
	    cbr = wnd.tlo.add(wnd.wsz.add(-tr.sz().x, tm.sz().y));
	    for (; mdo.x < cbr.x; mdo.x += tm.sz().x)
		g.image(tm, mdo, Coord.z, cbr);
	    g.image(tr, wnd.tlo.add(wnd.wsz.x - tr.sz().x, 0));
	    
	    mdo = wnd.tlo.add(0, cl.sz().y);
	    cbr = wnd.tlo.add(lm.sz().x, wnd.wsz.y - bl.sz().y);
	    if(cbr.y - mdo.y >= lb.sz().y) {
		cbr.y -= lb.sz().y;
		g.image(lb, new Coord(wnd.tlo.x, cbr.y));
	    }
	    for (; mdo.y < cbr.y; mdo.y += lm.sz().y)
		g.image(lm, mdo, Coord.z, cbr);
	    
	    mdo = wnd.tlo.add(wnd.wsz.x - rm.sz().x, tr.sz().y);
	    cbr = wnd.tlo.add(wnd.wsz.x, wnd.wsz.y - br.sz().y);
	    for (; mdo.y < cbr.y; mdo.y += rm.sz().y)
		g.image(rm, mdo, Coord.z, cbr);
	    
	    g.image(bl, wnd.tlo.add(0, wnd.wsz.y - bl.sz().y));
	    mdo = wnd.tlo.add(bl.sz().x, wnd.wsz.y - bm.sz().y);
	    cbr = wnd.tlo.add(wnd.wsz.x - br.sz().x, wnd.wsz.y);
	    for (; mdo.x < cbr.x; mdo.x += bm.sz().x)
		g.image(bm, mdo, Coord.z, cbr);
	    g.image(br, wnd.tlo.add(wnd.wsz.sub(br.sz())));
	}
    
	@Override
	public boolean checkhit(WindowX wnd, Coord c) {
	    if(wnd.decohide)
		return (c.isect(wnd.atl, wnd.asz));
	    Coord cpc = c.sub(wnd.cptl);
	    return (c.isect(wnd.ctl, wnd.csz)
		|| (c.isect(wnd.cptl, wnd.cpsz) && (cm.back.getRaster().getSample(cpc.x % cm.back.getWidth(), cpc.y, 3) >= 128)));
	}
    
	@Override
	public void apply(WindowX wnd) {
	    wnd.cbtn.images(Window.cbtni[0], Window.cbtni[1], Window.cbtni[2]);
	    Decorator.super.apply(wnd);
	}
    
	@Override
	public void resize(WindowX wnd, Coord sz) {
	    wnd.asz = sz;
	    wnd.csz = wnd.asz.add(wnd.mrgn.mul(2));
	    wnd.wsz = wnd.csz.add(tlm).add(brm);
	    wnd.sz = wnd.wsz.add(wnd.tlo).add(wnd.rbo);
	    wnd.ctl = wnd.tlo.add(tlm);
	    wnd.atl = wnd.ctl.add(wnd.mrgn);
	    wnd.cmw = (wnd.cap == null) ? 0 : wnd.cap.sz().x;
	    wnd.cmw = Math.max(wnd.cmw, wnd.wsz.x / 4);
	    wnd.cptl = new Coord(wnd.ctl.x, wnd.tlo.y);
	    wnd.cpsz = wnd.tlo.add(cpo.x + wnd.cmw, cm.sz().y).sub(wnd.cptl);
	    wnd.cmw = wnd.cmw - (cl.sz().x - cpo.x) - UI.scale(5);
	    wnd.cbtn.c = wnd.xlate(wnd.tlo.add(wnd.wsz.x - wnd.cbtn.sz.x, 0), false);
	}
    
	@Override
	public Text.Furnace captionFont() {
	    return Window.cf;
	}
    
	@Override
	public void placetwdgs(WindowX wnd) {
	    int x = wnd.sz.x - UI.scale(20);
	    for (Widget ch : wnd.twdgs) {
		if(ch.visible) {
		    ch.c = wnd.xlate(new Coord(x -= ch.sz.x + UI.scale(5), wnd.ctl.y - ch.sz.y / 2), false);
		}
	    }
	}
    }*/
}
