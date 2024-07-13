/* Preprocessed source code */
/* $use: gfx/fx/bprad */
/* $use: ui/pag/toggle */

package haven.res.gfx.fx.msrad;

import java.awt.*;
import java.util.*;
import haven.*;
import haven.render.*;

/* >spr: MSRad */
@haven.FromResource(name = "gfx/fx/msrad", version = 16)
public class MSRad extends Sprite {
    public static boolean show = false;
    public static Collection<MSRad> current = new WeakList<>();
    final RenderTree.Node square;
    final ColoredRadius circle;
    final Collection<RenderTree.Slot> slots = new ArrayList<>(1);
    
    public MSRad(Owner owner, Resource res, float r, Color color1, Color color2) {
	super(owner, res);
	Gob gob = (Gob) owner;
	circle = new ColoredRadius(gob, r, color1, color2);
	square = new BaseColor(FColor.fromColorAndAlpha(color2, 0.5f))
	    .apply(SquareRadiiMesh.getMesh(r), false);
    }
    
    public MSRad(Owner owner, Resource res, float r, Color color) {
	this(owner, res, r, color, color);
    }
    
    public MSRad(Owner owner, Resource res, float r) {
	this(owner, res, r, new Color(128, 128, 128, 128), new Color(128, 192, 192));
    }
    
    public MSRad(Owner owner, Resource res, Message sdt) {
	this(owner, res, Utils.hfdec((short) sdt.int16()) * 11);
    }
    
    public MSRad(Owner owner, float r, Color color1, Color color2) {
	this(owner, null, r, color1, color2);
    }
    
    public static void show(boolean show) {
	for (MSRad spr : current)
	    spr.show1(show);
	MSRad.show = show;
    }
    
    public void show1(boolean show) {
	if(show) {
	    if(useSquares(((Gob) owner).resid())) {
		Loading.waitfor(() -> RUtils.multiadd(slots, square));
	    } else {
		Loading.waitfor(() -> RUtils.multiadd(slots, circle));
	    }
	} else {
	    for (RenderTree.Slot slot : slots)
		slot.clear();
	}
    }
    
    public void added(RenderTree.Slot slot) {
	if(show) {
	    if(useSquares(((Gob) owner).resid())) {
		slot.add(square);
	    } else {
		slot.add(circle);
	    }
	}
	if(slots.isEmpty())
	    current.add(this);
	slots.add(slot);
    }
    
    @Override
    public void gtick(Render g) {
	circle.gtick(g);
    }
    
    public void removed(RenderTree.Slot slot) {
	slots.remove(slot);
	if(slots.isEmpty())
	    current.remove(this);
    }
    
    private boolean useSquares(String resid){
	if(resid == null) {return false;}
	switch (resid) {
	    case "gfx/terobjs/minesupport":
	    case "gfx/terobjs/column":
	    case "gfx/terobjs/map/naturalminesupport":
	    case "gfx/terobjs/ladder":
	    case "gfx/terobjs/minebeam":
		return true;
	}
	
	return false;
    }
}

/* >pagina: ShowSupports$Fac */
