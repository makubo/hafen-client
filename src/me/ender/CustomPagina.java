package me.ender;

import haven.MenuGrid;
import haven.Resource;

public class CustomPagina extends MenuGrid.Pagina {
    public final String resName;
    
    public CustomPagina(MenuGrid scm, Resource.Named res, CustomPaginaAction action) {
	super(scm, res, res);
	this.resName = res.name;
	button(new CustomPagButton(this, action));
    }
    
    public static boolean isLocalPagina(MenuGrid.Pagina p) {
	if(p == null) {return false;}
	if(p instanceof CustomPagina) {
	    return true;
	}
	String resname = MenuGrid.Pagina.resname(p);
	if(resname.isEmpty()) {
	    return false;
	}
	if(resname.equals("paginae/act/add")) {
	    return true;
	}
	
	return isLocalPagina(p.parent());
    }
}
