package auto;

import haven.Coord2d;
import haven.GameUI;
import haven.Gob;
import haven.GobTag;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GobHelper {
    static List<ITarget> getNearestTargets(GameUI gui, String name, int limit, double distance) {
	return gui.ui.sess.glob.oc.stream()
	    .filter(gobIs(name))
	    .filter(gob -> Bot.distanceToPlayer(gob) <= distance)
	    .sorted(Bot.byDistance)
	    .limit(limit)
	    .map(GobTarget::new)
	    .collect(Collectors.toList());
    }
    
    static List<ITarget> getNearestTargets(GameUI gui, GobTag tag, int limit, double distance) {
	return getNearestTargets(gui, tag, limit, Bot::distanceToPlayer, distance);
    }
    
    static List<ITarget> getNearestTargets(GameUI gui, GobTag tag, int limit, Coord2d pos, double distance) {
	return getNearestTargets(gui, tag, limit, g -> Bot.distanceToCoord(pos, g), distance);
    }
    
    private static List<ITarget> getNearestTargets(GameUI gui, GobTag tag, int limit, Function<Gob, Double> meter, double distance) {
	return gui.ui.sess.glob.oc.stream()
	    .filter(gobIs(tag))
	    .filter(gob -> meter.apply(gob) <= distance)
	    .sorted(Comparator.comparingDouble(meter::apply))
	    .limit(limit)
	    .map(GobTarget::new)
	    .collect(Collectors.toList());
    }
    
    static Bot.BotAction waitGobNoPose(Gob gob, long timeout, String... poses) {
	return (t, b) -> {
	    final long started = System.currentTimeMillis();
	    while (System.currentTimeMillis() - started < timeout
		&& gob != null && !gob.disposed() && gob.hasPose(poses)) {
		Bot.pause(100);
	    }
	};
    }
    
    static Bot.BotAction waitGobPose(Gob gob, long timeout, String... poses) {
	return (t, b) -> {
	    final long started = System.currentTimeMillis();
	    while (System.currentTimeMillis() - started < timeout
		&& gob != null && !gob.disposed() && !gob.hasPose(poses)) {
		Bot.pause(100);
	    }
	};
    }
    
    private static Predicate<Gob> gobIs(String what) {
	return g -> {
	    if(g == null) {return false;}
	    String id = g.resid();
	    if(id == null) {return false;}
	    return id.contains(what);
	};
    }
    
    private static Predicate<Gob> gobIs(GobTag what) {
	return g -> {
	    if(g == null) {return false;}
	    return g.is(what);
	};
    }
    
    static Predicate<Gob> has(GobTag tag) {
	return gob -> gob.is(tag);
    }
}
