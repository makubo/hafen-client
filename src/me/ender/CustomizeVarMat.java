package me.ender;

import haven.res.lib.vmat.VarSprite;

import static haven.CFG.*;

public class CustomizeVarMat {
    public static boolean NoMat(VarSprite sprite) {
	if(ResName.CUPBOARD.equals(sprite.res.name)) {
	    return DISPLAY_NO_MAT_CUPBOARDS.get();
	}
	return false;
    }
}
