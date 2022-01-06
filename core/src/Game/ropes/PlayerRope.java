package Game.ropes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import Screens.GameContainer;
import Screens.PlayScreen;
import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class PlayerRope extends VerletRope {

	Vector2 diff = new Vector2();

	public PlayerRope(Vector3 target) {

		super(target);
		
	}

	public PlayerRope(Vector3 target, float gravity, float ropeSegLen, int segmentLen, float width, int constraint, Color color) {
		
		super(target, gravity, ropeSegLen, segmentLen, width, constraint, color);
		
	}

	public void render(SpriteBatch sb, ShapeDrawer sd) {

		interpolate(renderPosition, segments.get(0));

		diff.x = target.x - renderPosition.x;
		diff.y = target.y - renderPosition.y;

		for (int i = 0; i < segmentLen; i++) {

			interpolate(renderPosition, segments.get(i), diff);
			
			path.get(i).set(renderPosition);

			debug(sd);
		} 
		
		sd.setColor(color);
		sd.path(path, PlayScreen.imageScale, JoinType.NONE, true);
		sd.setColor(Color.WHITE);
		
		sd.filledCircle(renderPosition.x, renderPosition.y, width, Color.LIGHT_GRAY);

	}
	
	public void interpolate(Vector2 renderPosition, RopeSegment seg, Vector2 diff) {
		renderPosition.x = seg.curPos.x * GameContainer.alpha + seg.oldPos.x * (1 - GameContainer.alpha) + diff.x;
		renderPosition.y = seg.curPos.y * GameContainer.alpha + seg.oldPos.y * (1 - GameContainer.alpha) + diff.y;
	}


}

