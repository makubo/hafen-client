package auto;

import haven.*;
import haven.rx.Reactor;
import rx.functions.Action2;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class Bot implements Defer.Callable<Void> {
    private static final Object lock = new Object();
    private static Bot current;
    private final List<ITarget> targets;
    private final BotAction[] actions;
    private Defer.Future<Void> task;
    private boolean cancelled = false;
    private String message = null;
    private static final Object waiter = new Object();
    
    public Bot(List<ITarget> targets, BotAction... actions) {
	this.targets = targets;
	this.actions = actions;
    }
    
    public Bot(BotAction... actions) {
	this(Collections.singletonList(EmptyTarget.EMPTY), actions);
    }
    
    @Override
    public Void call() throws InterruptedException {
	targets.forEach(ITarget::highlight);
	for (ITarget target : targets) {
	    for (BotAction action : actions) {
		if(target.disposed()) {break;}
		action.call(target, this);
		checkCancelled();
	    }
	}
	synchronized (lock) {
	    if(current == this) {current = null;}
	}
	return null;
    }
    
    private void run(Action2<Boolean, String> callback) {
	task = Defer.later(this);
	task.callback(() -> callback.call(task.cancelled(),  message));
    }
    
    private void checkCancelled() throws InterruptedException {
	if(cancelled) {
	    throw new InterruptedException();
	}
    }
    
    private void markCancelled() {
	cancelled = true;
	task.cancel();
    }
    
    public void cancel(String message) {
	this.message = message;
	markCancelled();
    }
    
    public void cancel() {
	cancel(null);
    }
    
    public static void cancelCurrent() {
	setCurrent(null);
    }
    private static void setCurrent(Bot bot) {
	synchronized (lock) {
	    if(current != null) {
		current.cancel();
	    }
	    current = bot;
	}
    }
    
    static void start(Bot bot, UI ui) {
	start(bot, ui, false);
    }
    
    static void start(Bot bot, UI ui, boolean silent) {
	setCurrent(bot);
	bot.run((error, message) -> {
	    if(!silent && CFG.SHOW_BOT_MESSAGES.get() || error) {
		GameUI.MsgType type = error ? GameUI.MsgType.ERROR : GameUI.MsgType.INFO;
		if(message == null) {
		    message = error
			? "Task is cancelled."
			: "Task is completed.";
		    type = GameUI.MsgType.INFO;
		}
		ui.message(message, type);
	    }
	});
    }
    
    static CompletableFuture<Coord2d> mapPosOfMouse(GameUI gui) {
	return gui.map.hit(gui.ui.mc);
    }
    
    private static boolean isHeld(GameUI gui, String what) throws Loading {
	GameUI.DraggedItem drag = gui.hand();
	if(drag == null && what == null) {
	    return true;
	}
	if(drag != null && what != null) {
	    return drag.item.is2(what);
	}
	return false;
    }
    
    static boolean waitHeld(GameUI gui, String what) {
	if(Boolean.TRUE.equals(doWaitLoad(() -> isHeld(gui, what)))) {
	    return true;
	}
	if(waitHeldChanged(gui)) {
	    return Boolean.TRUE.equals(doWaitLoad(() -> isHeld(gui, what)));
	}
	return false;
    }
    
    static boolean waitHeldChanged(GameUI gui) {
	boolean result = true;
	try {
	    synchronized (gui.heldNotifier) {
		gui.heldNotifier.wait(5000);
	    }
	} catch (InterruptedException e) {
	    result = false;
	}
	return result;
    }
    
    private static <T> T doWaitLoad(Supplier<T> action) {
	T result = null;
	boolean ready = false;
	while (!ready) {
	    try {
		result = action.get();
		ready = true;
	    } catch (Loading e) {
		pause(100);
	    }
	}
	return result;
    }
    
    static BotAction doWait(long ms) {
	return (t, b) -> pause(ms);
    }
    
    static void pause(long ms) {
	synchronized (waiter) {
	    try {
		waiter.wait(ms);
	    } catch (InterruptedException ignore) {
	    }
	}
    }
    
    private static void unpause() {
	synchronized (waiter) { waiter.notifyAll(); }
    }
    
    static boolean isOnRadar(Gob gob) {
	if(!CFG.AUTO_PICK_ONLY_RADAR.get()) {return true;}
	Boolean onRadar = gob.isOnRadar();
	return onRadar == null || onRadar;
    }
    
    static double distanceToPlayer(Gob gob) {
	Gob p = gob.glob.oc.getgob(gob.glob.sess.ui.gui.plid);
	return p.rc.dist(gob.rc);
    }
    
    static double distanceToCoord(Coord2d c, Gob gob) {
	if(c == null) {return Double.MAX_VALUE;}
	return c.dist(gob.rc);
    }
    
    public static Comparator<Gob> byDistance = (o1, o2) -> {
	try {
	    Gob p = o1.glob.oc.getgob(o1.glob.sess.ui.gui.plid);
	    return Double.compare(p.rc.dist(o1.rc), p.rc.dist(o2.rc));
	} catch (Exception ignored) {}
	return Long.compare(o1.id, o2.id);
    };
    
    public static void rclick(GameUI gui) {
	click(3, gui);
    }
    
    public static void click(int btn, GameUI gui) {
	gui.map.wdgmsg("click", Coord.z, gui.map.player().rc.floor(OCache.posres), btn, 0);
    }
    
    static BotAction selectFlower(String... options) {
	return (target, bot) -> {
	    if(target.hasMenu()) {
		FlowerMenu.lastTarget(target);
		Reactor.FLOWER.first().subscribe(flowerMenu -> {
		    Reactor.FLOWER_CHOICE.first().subscribe(choice -> unpause());
		    flowerMenu.forceChoose(options);
		});
		pause(5000);
	    }
	};
    }
    
    static Predicate<Gob> startsWith(String text) {
	return gob -> {
	    try {
		return gob.getres().name.startsWith(text);
	    } catch (Exception ignored) {}
	    return false;
	};
    }
    
    public interface BotAction {
	void call(ITarget target, Bot bot) throws InterruptedException;
    }
    
}
