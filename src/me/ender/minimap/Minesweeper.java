package me.ender.minimap;

import haven.*;
import haven.render.Homo3D;
import haven.render.Pipe;
import haven.render.RenderTree;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static haven.MapFile.*;

public class Minesweeper {
    private static final String INDEX = "ender-ms-index";
    private static final String GRID_NAME = "ender-ms-grid-%x";
    private static final int TILES = MCache.cmaps.x * MCache.cmaps.y;
    private static final Coord2d TILE_CENTER = MCache.tilesz.div(2);
    public static final RenderTree.Node NIL = RenderTree.Node.nil;
    
    private final Map<Long, byte[]> values = new HashMap<>();
    private final Map<Long, SweeperNode[]> cuts = new HashMap<>();
    private final MapFile file;
    
    public Minesweeper(MapFile file) {
	this.file = file;
	MapFileUtils.load(file, this::load, INDEX);
    }
    
    public static void process(Sprite.Owner owner, int count) {
	if(!(owner instanceof Gob)) {return;}
	Gob gob = (Gob) owner;
	
	GameUI gui = gob.context(GameUI.class);
	if(gui == null) {return;}
	
	Coord gc = gob.rc.floor(MCache.tilesz);
	MCache.Grid grid = gob.glob.map.getgridt(gc);
	if(grid == null) {return;}
	
	Coord tc = gc.sub(grid.gc.mul(MCache.cmaps));
	long id = grid.id;
	
	Map<Long, byte[]> grids = gui.minesweeper.values;
	byte[] values;
	if(grids.containsKey(id)) {
	    values = grids.get(id);
	} else {
	    values = new byte[TILES];
	    grids.put(id, values);
	    gui.minesweeper.storeIndex();
	}
	values[index(tc)] = (byte) count;
	gui.minesweeper.storeGrid(id, values);
    }
    
    private static int index(Coord tc) {
	return tc.x + tc.y * MCache.cmaps.x;
    }
    
    public static RenderTree.Node getcut(UI ui, Coord cc) {
	if(!CFG.SHOW_MINESWEEPER_OVERLAY.get()) {return NIL;}
	GameUI gui = ui.gui;
	if(gui == null) {return NIL;}
	Minesweeper minesweeper = gui.minesweeper;
	if(minesweeper == null) {return NIL;}
	
	return minesweeper.getcut(ui.sess.glob.map.getgrid(cc.div(MCache.cutn)), cc.mod(MCache.cutn));
    }
    
    private RenderTree.Node getcut(MCache.Grid grid, Coord cc) {
	SweeperNode[] nodes;
	if(!cuts.containsKey(grid.id)) {
	    if(!values.containsKey(grid.id)) {return NIL;}
	    nodes = new SweeperNode[MCache.cutn.x * MCache.cutn.y];
	    cuts.put(grid.id, nodes);
	} else {
	    nodes = cuts.get(grid.id);
	}
	int index = cc.x + cc.y * MCache.cutn.x;
	
	if(nodes[index] == null) {
	    nodes[index] = new SweeperNode(values.get(grid.id), cc);
	}
	
	return nodes[index];
    }
    
    private void storeIndex() {
	OutputStream fp;
	try {
	    fp = file.sstore(INDEX);
	} catch (IOException e) {
	    throw (new StreamMessage.IOError(e));
	}
	Set<Long> grids = values.keySet();
	try (StreamMessage out = new StreamMessage(fp)) {
	    out.adduint8(1);
	    for (Long id : grids) {
		out.addint64(id);
	    }
	}
    }
    
    private void storeGrid(long id, byte[] grid) {
	OutputStream fp;
	try {
	    fp = file.sstore(GRID_NAME, id);
	} catch (IOException e) {
	    throw (new StreamMessage.IOError(e));
	}
	try (StreamMessage out = new StreamMessage(fp)) {
	    out.adduint8(1);
	    for (byte v : grid) {
		out.adduint8(v);
	    }
	}
    }
    
    private void load(StreamMessage data) {
	int ver = data.uint8();
	if(ver == 1) {
	    while (!data.eom()) {
		loadGrid(data.int64());
	    }
	} else {
	    warn("unknown mapfile ender-minesweeper version: %d", ver);
	}
    }
    
    private void loadGrid(long id) {
	MapFileUtils.load(file, data -> loadGrid(data, id), GRID_NAME, id);
    }
    
    private void loadGrid(StreamMessage data, long id) {
	int ver = data.uint8();
	if(ver == 1) {
	    values.put(id, data.bytes(TILES));
	} else {
	    values.remove(id);
	    warn("unknown mapfile ender-minesweeper-grid %d version: %d", id, ver);
	}
    }
    
    private static class SweeperNode implements RenderTree.Node, PView.Render2D {
	private static final Text.Foundry TEXT_FND = new Text.Foundry(Text.sansbold, 12);
	private static final Color[] COLORS = new Color[]{
	    new Color(136, 226, 255),
	    new Color(102, 255, 217),
	    new Color(102, 255, 127),
	    new Color(233, 255, 34),
	    new Color(250, 195, 56),
	    new Color(255, 150, 65),
	    new Color(234, 61, 83),
	    new Color(213, 77, 249),
	};
	private static final Map<Byte, Tex> CACHE = new HashMap<>();
	
	private final byte[] values;
	private final Coord cc;
	
	public SweeperNode(byte[] values, Coord cc) {
	    this.values = values;
	    this.cc = cc;
	}
	
	private static Tex getTex(byte val) {
	    if(val <= 0) {return null;}
	    if(!CACHE.containsKey(val)) {
		Color color = COLORS[Utils.clip(val - 1, 0, COLORS.length - 1)];
		CACHE.put(val, Text.renderstroked(String.valueOf(val), color, Color.BLACK, TEXT_FND).tex());
	    }
	    return CACHE.get(val);
	}
	
	public Coord3f origin(Coord tc) {
	    Coord2d mc = tc.mul(MCache.tilesz).add(TILE_CENTER);
	    return new Coord3f((float) mc.x, (float) -mc.y, 1f);
	}
	
	@Override
	public void draw(GOut g, Pipe state) {
	    Coord ul = cc.mul(MCache.cutsz);
	    Coord o = new Coord();
	    for (o.x = 0; o.x < MCache.cutsz.x; o.x++) {
		for (o.y = 0; o.y < MCache.cutsz.y; o.y++) {
		    
		    Tex tex = getTex(values[index(ul.add(o))]);
		    if(tex == null) {continue;}
		    
		    Coord sc = Homo3D.obj2view(origin(o), state, Area.sized(g.sz())).round2();
		    if(!sc.isect(Coord.z, g.sz())) {continue;}
		    
		    
		    g.aimage(tex, sc, 0.5f, 0.5f);
		}
	    }
	}
    }
}
