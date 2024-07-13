package haven;

import java.util.ArrayList;
import java.util.List;

public class SquareRadiiOverlay {
    private final List<Area> areas = new ArrayList<>();
    private final List<MCache.Overlay> ols = new ArrayList<>();
    private final MCache map;
    private final MCache.OverlayInfo info;
    
    public SquareRadiiOverlay(Gob gob, float radius, MCache.OverlayInfo info) {
	this.map = gob.glob.map;
	this.info = info;
	buildAreas(gob.rc, radius);
    }
    
    private void buildAreas(Coord2d rc, float radius) {
	int steps = (int) (radius / MCache.tilesz.x);
	
	for (int i = -steps; i <= steps; i++) {
	    float k = (float) (i * MCache.tilesz.x);
	    int y = (int) (Math.sqrt(radius * radius - k * k) / MCache.tilesz.y);
	    areas.add(Area.sized(rc.floor(MCache.tilesz).sub(i, y), Coord.of(1, 2 * y + 1)));
	}
    }
    
    public void add() {
	synchronized (ols) {
	    if(!ols.isEmpty()) {return;}
	    for (Area area : areas) {
		ols.add(map.new Overlay(area, info));
	    }
	}
    }
    
    public void rem() {
	synchronized (ols) {
	    if(ols.isEmpty()) {return;}
	    for (MCache.Overlay ol : ols) {
		ol.destroy();
	    }
	    ols.clear();
	}
    }
}
