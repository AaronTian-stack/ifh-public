package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Queue;

import Game.abstraction.GameObject;
import Screens.PlayScreen;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class LineTrail extends GameObject {
	
	Queue<Vector3> positionQueue = new Queue<Vector3>();
	int inMaxSize; // non frame dependent value. Length of trail (number of lines)
	int maxStackSize;
	float size, drawSize; // size of line at beginning
	Pool<Vector3> vectorPool = Pools.get(Vector3.class);
	Color trailColor, trans;
	Color trailDrawColor = new Color();
	Interpolation in;
	
	public LineTrail(int inMaxSize, float size, Color trailColor, Interpolation in) {
		this.inMaxSize = inMaxSize;
		this.trailColor = trailColor;
		this.size = size;
		drawSize = size;
		trans = new Color(trailColor.r, trailColor.g, trailColor.b, 0);
		this.in = in;
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}
	
	public void removeLast() {
		if(positionQueue.notEmpty())
			positionQueue.removeLast();
	}
	
	public void emptyQueue() {
		while(positionQueue.size > 0) { // free all vectors and empty queue
			vectorPool.free(positionQueue.last());
			positionQueue.removeLast();
		}
	}
	
	public void updateQueue(Vector3 position) {
		
		positionQueue.addFirst(vectorPool.obtain().set(position));
		
		while(positionQueue.size == maxStackSize) {
			vectorPool.free(positionQueue.last());
			positionQueue.removeLast();
		}
		
	}

	@Override
	public void render(SpriteBatch sb, ShapeDrawer sd) {
		maxStackSize = Math.round(inMaxSize * (Gdx.graphics.getFramesPerSecond() / 60f));
		// Draw the trail
	
		for(int i=0;i<positionQueue.size-1;i++) {
			//sd.setColor(trailDrawColor);
			sd.line(positionQueue.get(i).x, positionQueue.get(i).y, 
					positionQueue.get(i+1).x, positionQueue.get(i+1).y,
					trailColor, PlayScreen.imageScale * drawSize);
			//trailDrawColor.set(trailColor);
			//trailDrawColor.lerp(trans, (i / 1f / positionQueue.size)); // TODO: Use coloraction to have different interpolation
			drawSize = in.apply(size, 0, i / 1f / positionQueue.size);

		}
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
