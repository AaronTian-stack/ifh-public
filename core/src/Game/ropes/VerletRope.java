package Game.ropes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import Game.abstraction.Actor;
import Screens.GameContainer;
import Screens.PlayScreen;
import Tiles.Enums.GameObjectType;
import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class VerletRope extends Actor {
	
	protected Array<RopeSegment> segments = new Array<RopeSegment>();

	protected float ropeSegLen = 0.6f;

	protected int segmentLen = 6;

	protected float width = 0.25F;

	protected Color color = Color.BLUE;

	protected int constraint = 100;

	protected RopeSegment first;

	protected Vector2 velocityV = new Vector2();

	protected Vector2 gravity;

	protected Vector2 changeDir = new Vector2();

	protected Vector2 changeAmount = new Vector2();

	protected Vector2 changeAmountCopy = new Vector2();

	protected Vector2 sub = new Vector2();

	protected Vector2 firstCopy = new Vector2();

	protected Vector2 secondCopy = new Vector2();

	protected RopeSegment firstSegment,secondSegment;
	
	protected Vector2 renderPosition = new Vector2();
	
	//protected Vector2 renderPosition2 = new Vector2();

	protected Vector3 target = new Vector3();
	
	Array<Vector2> path = new Array<Vector2>(segmentLen);

	public VerletRope(Vector3 target) {
		type = GameObjectType.Rope;
		Vector2 ropeStart = new Vector2(target.x, target.y);
		for (int i = 0; i < segmentLen; i++) {
			segments.add(new RopeSegment(ropeStart)); 
			path.add(new Vector2());
		}	
		position = new Vector3();
		velocity = new Vector3();
		rect = new Rectangle(0, 0, width, width);
		gravity = new Vector2(0, -0.3f);
		this.target = target;
		
	}

	public VerletRope(Vector3 target, float gravity, float ropeSegLen, int segmentLen, float width, int constraint, Color color) {
		this.target = target;
		type = GameObjectType.Rope;
		Vector2 ropeStart = new Vector2(target.x, target.y);
		for (int i = 0; i < segmentLen; i++) {
			segments.add(new RopeSegment(ropeStart)); 
			path.add(new Vector2());
		}
		position = new Vector3();
		velocity = new Vector3();
		rect = new Rectangle(0, 0, width, width);
		this.gravity = new Vector2(0, gravity);
		this.ropeSegLen = ropeSegLen;
		this.segmentLen = segmentLen;
		this.width = width;
		this.color = color;
		this.constraint = constraint;
	}

	public void update(float dt) {
		int i;
		for (i = 0; i < segmentLen; i++) {
			first = segments.get(i);
			firstCopy.set(first.curPos);
			velocityV.set(firstCopy.sub(first.oldPos));
			velocityV.add(gravity);
			first.oldPos.set(first.curPos);
			position.set(first.curPos.x, first.curPos.y, 0);
			velocity.set(velocityV.x, velocityV.y, 0);
			physicsStep();
			first.curPos.set(position.x, position.y);
			velocityV.set(velocity.x, velocity.y);
		} 
		for (i = constraint; i >= 0; i--)
			ApplyConstraint(); 
	}

	private void ApplyConstraint() {
		segments.get(0).curPos.set(target.x, target.y);
		for (int i = 0; i < segmentLen - 1; i++) {
			firstSegment = segments.get(i);
			secondSegment = segments.get(i + 1);
			firstCopy.set(firstSegment.curPos);
			secondCopy.set(secondSegment.curPos);
			float dist = firstCopy.sub(secondSegment.curPos).len();
			float error = Math.abs(dist - ropeSegLen);
			changeDir.setZero();
			firstCopy.set(firstSegment.curPos);
			if (dist > ropeSegLen) {
				changeDir.set(firstCopy.sub(secondSegment.curPos).nor());
			} else if (dist < ropeSegLen) {
				changeDir.set(secondCopy.sub(firstSegment.curPos).nor());
			} 
			changeAmount.set(changeDir.x * error, changeDir.y * error);
			if (i != 0) {
				firstSegment.curPos.sub(changeAmount.cpy().scl(0.5f));
				changeAmount.set(changeDir.x * error, changeDir.y * error);
				secondSegment.curPos.add(changeAmount.cpy().scl(0.5f));
			} else {
				secondSegment.curPos.add(changeAmount);
			} 
		} 
	}

	public void render(SpriteBatch sb, ShapeDrawer sd) {

		for (int i = 0; i < segmentLen; i++) {

			interpolate(renderPosition, segments.get(i));

			path.get(i).set(renderPosition);
			
			debug(sd);
			
		} 
		
		sd.path(path, JoinType.POINTY, true);

	}
	
	public void debug(ShapeDrawer sd) {
		if(PlayScreen.debugMode) {
			sd.rectangle(renderPosition.x-width/2, renderPosition.y-width/2, width, width, Color.GREEN, PlayScreen.imageScale);
		}
	}
	
	public void interpolate(Vector2 renderPosition, RopeSegment seg) {
		renderPosition.x = seg.curPos.x * GameContainer.alpha + seg.oldPos.x * (1 - GameContainer.alpha);
		renderPosition.y = seg.curPos.y * GameContainer.alpha + seg.oldPos.y * (1 - GameContainer.alpha);
	}
	
	

	public void dipose() {}

	public float getRopeSegLen() {
		return ropeSegLen;
	}
	
	public Array<RopeSegment> getSegments() {
		return segments;
	}
	
	public RopeSegment getFirstSeg() {
		return segments.get(0);
	}

	public RopeSegment getLastSeg() {
		return segments.get(segmentLen - 1);
	}

	public Vector3 getTarget() {
		return target;
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}
}


