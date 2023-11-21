package auto;

import haven.Equipory;
import haven.GameUI;
import haven.WItem;
import haven.Widget;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InvHelper {
    
    private static List<WItem> items(Widget inv) {
	return inv != null ? new ArrayList<>(inv.children(WItem.class)) : new LinkedList<>();
    }
    
    static Optional<WItem> findFirstThatContains(String what, Collection<Supplier<List<WItem>>> where) {
	for (Supplier<List<WItem>> place : where) {
	    Optional<WItem> w = place.get().stream()
		.filter(contains(what))
		.findFirst();
	    if(w.isPresent()) {
		return w;
	    }
	}
	return Optional.empty();
    }
    
    private static Predicate<WItem> contains(String what) {
	return w -> w.contains.get().is(what);
    }
    
    static float countItems(String what, Supplier<List<WItem>> where) {
	return where.get().stream()
	    .filter(wItem -> wItem.is(what))
	    .map(wItem -> wItem.quantity.get())
	    .reduce(0f, Float::sum);
    }
    
    static Optional<WItem> findFirstItem(String what, Supplier<List<WItem>> where) {
	return where.get().stream()
	    .filter(wItem -> wItem.is(what))
	    .findFirst();
    }
    
    static Supplier<List<WItem>> INVENTORY(GameUI gui) {
	return () -> items(gui.maininv);
    }
    
    static Supplier<List<WItem>> BELT(GameUI gui) {
	return () -> {
	    Equipory e = gui.equipory;
	    if(e != null) {
		WItem w = e.slots[Equipory.SLOTS.BELT.idx];
		if(w != null) {
		    return items(w.item.contents);
		}
	    }
	    return new LinkedList<>();
	};
    }
    
    static Supplier<List<WItem>> HANDS(GameUI gui) {
	return () -> {
	    List<WItem> items = new LinkedList<>();
	    if(gui.equipory != null) {
		WItem slot = gui.equipory.slots[Equipory.SLOTS.HAND_LEFT.idx];
		if(slot != null) {
		    items.add(slot);
		}
		slot = gui.equipory.slots[Equipory.SLOTS.HAND_RIGHT.idx];
		if(slot != null) {
		    items.add(slot);
		}
	    }
	    return items;
	};
    }
}
