package me.ender;

import haven.MenuGrid;

public class CustomPagButton extends MenuGrid.PagButton {
    
    private final CustomPaginaAction action;
    
    public CustomPagButton(MenuGrid.Pagina pag, CustomPaginaAction action) {
	super(pag);
	this.action = action;
    }
    
    @Override
    public void use() {
	action.perform(pag.button());
    }
    
    @Override
    public void use(MenuGrid.Interaction iact) {
	action.perform(pag.button());
    }
}
