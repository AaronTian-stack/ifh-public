package Game.ropes;

import com.badlogic.gdx.math.Vector2;

public class RopeSegment {
	
	public Vector2 oldPos = new Vector2();

	public Vector2 curPos = new Vector2();

	public RopeSegment(Vector2 pos) {
		oldPos.set(pos);
		curPos.set(pos);
	}

	public void set(RopeSegment r) {
		oldPos.set(r.oldPos);
		curPos.set(r.curPos);
	}
}
