package Game.ropes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import Screens.GameContainer;
import Screens.PlayScreen;
import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class PauseRope extends VerletRope {

	public PauseRope(Vector3 target, AtlasRegion letter) {
		super(target);
		original.set(target);
		
		target2.set(target.x * MathUtils.random(1.005f, 1.015f), target.y, target.z);
		
		reset();
		this.frame = new Sprite(letter);
		
	}
	
	Sprite box;

	public PauseRope(Vector3 target, float gravity, float ropeSegLen, int segmentLen, float width, int constraint, Color color, AtlasRegion letter) {
		super(target, gravity, ropeSegLen, segmentLen, width, constraint, color);
		original.set(target);
		
		target2.set(target.x * MathUtils.random(1.005f, 1.015f), target.y, target.z);
		
		reset();
		this.frame = new Sprite(letter);
		
		
		box = new Sprite(frame);
		
		box.setRegion(GameContainer.atlas.findRegion("white"));
		
	}
	
	Vector2 diff = new Vector2();
	
	Vector2 renderPosition2 = new Vector2();
	
	boolean direction;
	
	Vector3 original = new Vector3();

	Vector3 target2 = new Vector3();
	
	float timer;
	
	float time = MathUtils.random(1.4f, 1.6f);
	float scale = 3.2f;
	float offset = 0.5f;
	public void render(SpriteBatch sb, ShapeDrawer sd) {

		timer+=Gdx.graphics.getDeltaTime();

		if(timer<time) {
			if(!direction) {
				target.set(original);
				target.interpolate(target2, Math.min(1, timer / time), Interpolation.smooth);
			}
			else {
				target.set(target2);
				target.interpolate(original, Math.min(1, timer / time), Interpolation.smooth);
			}
		}
		else {
			timer = 0;
			direction = !direction;
		}
		
		for (int i = 0; i < segmentLen; i++) { 

			interpolate(renderPosition, segments.get(i));

			path.get(i).set(renderPosition);

			debug(sd);
			
		} 
		
		float angle = MathUtils.atan2(diff.y, diff.x) * MathUtils.radiansToDegrees + 90;
		
		//draw(box, angle - 90, 0.1f, 2f, box.getWidth() / 2, -box.getHeight() / 2, Color.GRAY, sb);
		
		sd.setColor(Color.WHITE);
		sd.path(path, width * 1.5f, JoinType.NONE, true);
		sd.setColor(color);
		sd.path(path, width, JoinType.NONE, true);

		sd.setColor(Color.WHITE);
		
		interpolate(renderPosition, segments.get(segmentLen - 1));
		interpolate(renderPosition2, segments.get(segmentLen - 2));

		diff.set(renderPosition);
		diff.sub(renderPosition2);

		draw(frame, angle, 3, 3, 0, -0.2f, Color.WHITE, sb);
		
		draw(frame, angle, 3, 3, 0, 0.2f, Color.WHITE, sb);
		
		draw(frame, angle, 3, 3, -0.2f, 0, Color.WHITE, sb);
		
		draw(frame, angle, 3, 3, 0.2f, 0, Color.WHITE, sb);

		draw(frame, angle, 3, 3, 0, 0, color, sb);

	}
	
	public void draw(Sprite sprite, float angle, float scaleX, float scaleY, float offsetX, float offsetY, Color color, SpriteBatch sb) {
		
		sprite.setRotation(angle);
		
		sprite.setOrigin(sprite.getWidth() / 2.0f + offsetX, sprite.getHeight() + offsetY);

		sprite.setOriginBasedPosition(renderPosition.x, renderPosition.y);
		
		sprite.setColor(color);
		sprite.setScale(scaleX, scaleY);
		sprite.draw(sb);
	}
	
	public void setAll(float x, float y) {
		for (int i = 0; i < segmentLen; i++) {
			segments.get(i).curPos.set(x, y + i * ropeSegLen);
			segments.get(i).oldPos.set(x, y + i * ropeSegLen);
		}
		timer = 0;
		direction = false;
	}
	
	float offsetX = 0.5f;
	
	public void reset() {
		
		int j = (original.x < GameContainer.WIDTH / 2 ? -1 : 1);
		
		for (int i = 0; i < segmentLen; i++) {
			segments.get(i).curPos.set(original.x + i * j * offsetX, original.y + i * ropeSegLen);
			segments.get(i).oldPos.set(original.x + i * j * offsetX, original.y + i * ropeSegLen);
		}
		timer = 0;
		direction = false;
	}
	
}
