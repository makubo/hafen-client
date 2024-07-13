package haven;

import haven.render.*;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

public class SquareRadiiMesh extends FastMesh {
    private static final Map<Float, SquareRadiiMesh> cache = new HashMap<>();
    
    private SquareRadiiMesh(VertexBuf buf, ShortBuffer sa) {
	super(buf, sa);
    }
    
    @Override
    public void added(RenderTree.Slot slot) {
	slot.ostate(Pipe.Op.compose(MapMesh.postmap, new States.Facecull(States.Facecull.Mode.NONE), Clickable.No, Location.nullrot));
    }
    
    public static synchronized SquareRadiiMesh getMesh(float r) {
	if(cache.containsKey(r)) {
	    return cache.get(r);
	} else {
	    SquareRadiiMesh value = buildMesh(r);
	    cache.put(r, value);
	    return value;
	}
    }
    
    private static SquareRadiiMesh buildMesh(float r) {
	float h = 0.2f;
	float tsz = 11f;
	float d = 5.5f;
	int h_steps = (int) (r / tsz);
	int total = 2 * h_steps + 1;
	FloatBuffer vert = Utils.mkfbuf(12 * total);
	ShortBuffer ind = Utils.mksbuf(12 * total);
	short vIndex = 0;
	
	for (int i = -h_steps; i <= h_steps; i++) {
	    float k = i * tsz;
	    float v = ((int) (Math.sqrt(r * r - k * k) / tsz)) * tsz + d;
	    
	    vert.put(k - d).put(-v).put(h);
	    vert.put(k + d).put(-v).put(h);
	    vert.put(k + d).put(v).put(h);
	    vert.put(k - d).put(v).put(h);
	    
	    ind.put(vIndex).put((short) (vIndex + 1)).put((short) (vIndex + 2));
	    ind.put(vIndex).put((short) (vIndex + 2)).put((short) (vIndex + 3));
	    vIndex = (short) (vIndex + 4);
	}
	
	return new SquareRadiiMesh(new VertexBuf(new VertexBuf.VertexData(vert), new VertexBuf.NormalData(vert)), ind);
    }
}
