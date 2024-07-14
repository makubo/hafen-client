package me.ender;

import haven.*;

import static haven.OptWnd.*;

public class OptColorPanel {
    public static void init(OptWnd wnd, OptWnd.Panel panel) {
	int STEP = UI.scale(25);
	int START;
	int x, y;
	int my = 0, tx;
	
	Widget title = panel.add(new Label("Color settings", LBL_FNT), 0, 0);
	START = title.sz.y + UI.scale(10);
	
	x = 0;
	y = START;
	panel.add(new CFGColorBtn(CFG.COLOR_MINE_SUPPORT_OVERLAY, "Mine support overlay", false), x, y);
	
	y+= STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_TILE_GRID, "Tile grid", true), x, y);
	
	my = Math.max(my, y);
	
	panel.add(wnd.new PButton(UI.scale(200), "Back", 27, wnd.main), new Coord(0, my + UI.scale(35)));
	panel.pack();
	title.c.x = (panel.sz.x - title.sz.x) / 2;
    }
}
