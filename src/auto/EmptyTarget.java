package auto;

public class EmptyTarget implements ITarget {
    public static final EmptyTarget EMPTY = new EmptyTarget();
    
    
    private EmptyTarget() {
    }
    
    
    @Override
    public void rclick(int modflags) {
	
    }
    
    @Override
    public void click(int button, int modflags) {
	
    }
    
    @Override
    public void interact() {
	
    }
    
    @Override
    public void highlight() {
	
    }
    
    @Override
    public void take() {
	
    }
    
    @Override
    public void putBack() {
	
    }
    
    @Override
    public boolean hasMenu() {
	return false;
    }
    
    @Override
    public boolean disposed() {
	return true;
    }
}
