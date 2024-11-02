package me.ender.minimap;

import haven.MapFile;
import haven.Message;
import haven.StreamMessage;

import java.io.IOException;
import java.io.InputStream;

import static haven.MapFile.*;

public class MapFileUtils {
    public interface ILoader {
	void Load(StreamMessage data);
    }
    
    public static void load(MapFile file, ILoader loader, String name, Object... args) {
	InputStream fp;
	try {
	    fp = file.sfetch(name, args);
	} catch (IOException e) {
	    return;
	}
	String fname = String.format(name, args);
	try (StreamMessage data = new StreamMessage(fp)) {
	    loader.Load(data);
	} catch (Message.BinError e) {
	    warn(e, "error when loading '%s': %s", fname, e);
	}
    }
}
