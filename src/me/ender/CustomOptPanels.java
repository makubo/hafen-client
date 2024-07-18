package me.ender;

import haven.*;

import static haven.OptWnd.*;

public class CustomOptPanels {
    public static void init(OptWnd wnd, OptWnd.Panel panel) {
	int STEP = UI.scale(25);
	int H_STEP = UI.scale(10);
	int START;
	int x, y;
	int my = 0, tx;
	Widget w;
	
	Widget title = panel.add(new Label("Color settings", LBL_FNT), 0, 0);
	START = title.sz.y + UI.scale(10);
	
	x = 0;
	y = START;
	panel.add(new CFGColorBtn(CFG.COLOR_MINE_SUPPORT_OVERLAY, "Mine support overlay", false), x, y);
	
	y += STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_TILE_GRID, "Tile grid", true), x, y);
	
	y += STEP;
	panel.add(new Label("Hit Box:"), x, y);
	
	y += STEP;
	tx = H_STEP;
	tx += panel.add(new CFGColorBtn(CFG.COLOR_HBOX_SOLID, "Solid", true), tx, y).sz.x;
	panel.add(new CFGColorBtn(CFG.COLOR_HBOX_PASSABLE, "Passable", true), tx + H_STEP, y);
	
	y += STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_RIDGE_BOX, "Ridge highlight", true), x, y);
	
	my = Math.max(my, y);
	
	x += UI.scale(200);
	y = START;
	
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_READY, "Workstation ready", true), x, y);
	
	y += STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_FULL, "Container: Full", true), x, y);
	
	y += STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_EMPTY, "Container: Empty", true), x, y);
	
	y += STEP;
	
	//combat
	panel.add(new Label("Combat highlights:"), x, y);
	
	tx = x + H_STEP;
	y += STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_SELF, "Self", true), tx, y);
	
	y += STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_LEADER, "Leader", true), tx, y);
	
	y += STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_PARTY, "Party", true), tx, y);
	
	y += STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_IN_COMBAT, "Enemy", true), tx, y);
	
	y += STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_COMBAT_TARGET, "Current target", true), tx, y);
	
	my = Math.max(my, y);
	
	w = panel.add(wnd.new PButton(UI.scale(200), "Back", 27, wnd.main), new Coord(0, my + UI.scale(35)));
	panel.pack();
	title.c.x = (panel.sz.x - title.sz.x) / 2;
	w.c.x = (panel.sz.x - w.sz.x) / 2;
    }
}
