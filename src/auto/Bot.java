package auto;

import haven.*;
import rx.functions.Action2;

import java.util.*;


public class Bot implements Defer.Callable<Void> {
    private static final Object lock = new Object();
    private static Bot current;
    private final List<ITarget> targets;
    private final BotAction[] actions;
    private Defer.Future<Void> task;
    private boolean cancelled = false;
    private String message = null;
    List<Runnable> setup = null;
    List<Runnable> cleanup = null;
    
    public Bot(List<ITarget> targets, BotAction... actions) {
	this.targets = targets;
	this.actions = actions;
    }
    
    public Bot(BotAction... actions) {
	this(Collections.singletonList(Targets.EMPTY), actions);
    }
    
    public Bot setup(Runnable... actions) {
	setup = Arrays.asList(actions);
	return this;
    }
    
    public Bot cleanup(Runnable... actions) {
	cleanup = Arrays.asList(actions);
	return this;
    }
    
    @Override
    public Void call() throws InterruptedException {
	if(setup != null) {
	    setup.forEach(Runnable::run);
	}
	targets.forEach(ITarget::highlight);
	for (ITarget target : targets) {
	    for (BotAction action : actions) {
		if(target.disposed()) {break;}
		action.call(target, this);
		checkCancelled();
	    }
	}
	if(cleanup != null) {
	    cleanup.forEach(Runnable::run);
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
    
    public interface BotAction {
	void call(ITarget target, Bot bot) throws InterruptedException;
    }
    
}
