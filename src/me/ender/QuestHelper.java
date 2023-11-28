package me.ender;

import haven.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuestHelper extends GameUI.Hidewnd {
    private final TaskList taskList;
    
    public QuestHelper() {
	super(Coord.z, "Quest Helper");
	taskList = add(new TaskList(UI.scale(220), 13));
	pack();
    }
    
    public void processQuest(List<CharWnd.Quest.Condition> conditions, int id, boolean isCredo) {
	if(!visible()) {return;}
	synchronized (taskList) {
	    taskList.tasks.removeIf(q -> q.id == id);
	    long left = conditions.stream().filter(q -> q.done != 1).count();
	    for (CharWnd.Quest.Condition condition : conditions) {
		String name = condition.desc;
		TaskState status = TaskState.ACTIVE;
		
		if(isCredo) {name = "\uD83D\uDD6E " + name;}
		
		if(condition.done == 1) {//this task is done
		    continue;
		} else if(left <= 1) { //this is the last task
		    status = TaskState.LAST;
		    name = "★ " + name;
		}
		taskList.tasks.add(new Task(name, status, id, isCredo));
	    }
	    
	    taskList.tasks.sort(taskList.comp);
	    taskList.change(-1);
	}
    }
    
    @Override
    public void toggle() {
	super.toggle();
	if(visible()) {refresh();}
    }
    
    public void refresh() {
	synchronized (taskList) {
	    taskList.tasks.clear();
	    taskList.refresh = true;
	}
    }
    
    private enum TaskState {
	ACTIVE, LAST
    }
    
    private static class TaskList extends Listbox<Task> {
	public static final int ITEM_H = UI.scale(20);
	public static final Coord TEXT_C = Coord.of(0, ITEM_H / 2);
	public static final Color BGCOLOR = new Color(0, 0, 0, 120);
	public List<Task> tasks = new ArrayList<>(50);
	public boolean refresh = true;
	private long lastUpdateTime = System.currentTimeMillis();
	private final Comparator<Task> comp = Comparator.comparing(a -> a.name);
	private CharWnd.Quest.Info currentQuest, prevQuest;
	
	public TaskList(int w, int h) {
	    super(w, h, ITEM_H);
	    bgcolor = BGCOLOR;
	}
	
	@Override
	public void tick(double dt) {
	    if(!refresh) {prevQuest = currentQuest;}
	    currentQuest = null;
	    GameUI gui = ui.gui;
	    if(gui == null || gui.chrwdg == null) {return;}
	    CharWnd chrwdg = gui.chrwdg;
	    currentQuest = chrwdg.quest;
	    
	    if(!tvisible()) {return;}
	    
	    if(!refresh) {
		if(prevQuest != currentQuest) {
		    for (Task item : tasks) {
			item.current = currentQuest != null && currentQuest.questid() == item.id;
		    }
		    Collections.sort(tasks);
		}
		return;
	    }
	    if(System.currentTimeMillis() - lastUpdateTime < 500) {return;}
	    
	    
	    refresh = false;
	    lastUpdateTime = System.currentTimeMillis();
	    synchronized (this) {
		tasks.clear();
		
		boolean changed = false;
		for (CharWnd.Quest quest : chrwdg.cqst.quests) {
		    //currently selected quest will be selected last
		    if(currentQuest != null && currentQuest.questid() == quest.id) {continue;}
		    chrwdg.wdgmsg("qsel", quest.id);
		    changed = true;
		}
		if(currentQuest != null) {
		    chrwdg.wdgmsg("qsel", currentQuest.questid());
		    //if the only quest in the log is currently selected - send selection again to re-select it
		    if(!changed) {chrwdg.wdgmsg("qsel", currentQuest.questid());}
		} else {
		    chrwdg.wdgmsg("qsel", (Object) null);
		}
		Collections.sort(tasks);
	    }
	}
	
	protected Task listitem(int idx) {
	    return tasks.get(idx);
	}
	
	@Override
	protected int listitems() {
	    return tasks.size();
	}
	
	@Override
	protected void drawitem(GOut g, Task item, int idx) {
	    Color color;
	    if(item.status == TaskState.LAST) {
		color = item.current ? Color.CYAN : Color.GREEN;
	    } else {
		color = item.current ? Color.WHITE : Color.LIGHT_GRAY;
	    }
	    g.chcolor(color);
	    g.atext(item.name, TEXT_C, 0, 0.5);
	}
	
	public void change(Task item) {
	    if(item == null) {return;}
	    if(ui.gui.chrwdg.quest != null && ui.gui.chrwdg.quest.questid() == item.id) {
		ui.gui.chrwdg.wdgmsg("qsel", (Object) null);
	    } else {
		ui.gui.chrwdg.wdgmsg("qsel", item.id);
	    }
	}
    }
    
    private static class Task implements Comparable<Task> {
	private final String name;
	private final TaskState status;
	private final int id;
	private final boolean credo;
	private boolean current = false;
	
	public Task(String name, TaskState status, int id, boolean credo) {
	    this.name = name;
	    this.status = status;
	    this.id = id;
	    this.credo = credo;
	}
	
	public int compareTo(Task o) {
	    int result = -Boolean.compare(current, o.current);
	    if(result == 0) {
		result = status.compareTo(o.status);
	    }
	    if(result == 0) {
		result = -Boolean.compare(credo, o.credo);
	    }
	    if(result == 0) {
		result = name.compareTo(o.name);
	    }
	    return result;
	}
    }
}
