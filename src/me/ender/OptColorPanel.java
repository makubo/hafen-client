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
	
	y+= STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_HBOX_SOLID, "Hit box: Solid", true), x, y);
	
	y+= STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_HBOX_PASSABLE, "Hit box: Passable", true), x, y);
	
	my = Math.max(my, y);
	
	x += UI.scale(250);
	y = START;
	
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_READY, "Workstation ready", true), x, y);
	
	y+= STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_FULL, "Container: Full", true), x, y);
	
	y+= STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_EMPTY, "Container: Empty", true), x, y);
	
	y+= STEP;
	
	//combat
	y+= STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_SELF, "Combat highlight: Self", true), x, y);
	
	y+= STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_LEADER, "Combat highlight: Leader", true), x, y);
	
	y+= STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_PARTY, "Combat highlight: Party", true), x, y);
	
	y+= STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_IN_COMBAT, "Combat highlight: Enemy", true), x, y);
	
	y+= STEP;
	panel.add(new CFGColorBtn(CFG.COLOR_GOB_COMBAT_TARGET, "Combat highlight: Current target", true), x, y);
	
	my = Math.max(my, y);
	
	panel.add(wnd.new PButton(UI.scale(200), "Back", 27, wnd.main), new Coord(0, my + UI.scale(35)));
	panel.pack();
	title.c.x = (panel.sz.x - title.sz.x) / 2;
    }
}
