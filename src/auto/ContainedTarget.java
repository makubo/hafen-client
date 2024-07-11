package auto;

import haven.Coord;

public class ContainedTarget implements ITarget {
    public final InvHelper.ContainedItem contained;
    
    public ContainedTarget(InvHelper.ContainedItem contained) {
	this.contained = contained;
    }
    
    
    @Override
    public void rclick(int modflags) {
	if(disposed()) {return;}
	contained.item.rclick(modflags);
    }
    
    @Override
    public void click(int button, int modflags) {
	if(disposed()) {return;}
	contained.item.mouseclick(Coord.z, button, modflags);
    }
    
    @Override
    public void interact() {
    }
    
    @Override
    public void highlight() {
    }
    
    @Override
    public void take() {
	if(disposed()) {return;}
	contained.take();
    }
    
    @Override
    public void putBack() {
	if(disposed()) {return;}
	contained.putBack();
    }
    
    @Override
    public boolean hasMenu() {
	//TODO: we can return true if item itself has menu?
	return false;
    }
    
    @Override
    public boolean disposed() {
	return contained == null || contained.itemDisposed();
    }
}
