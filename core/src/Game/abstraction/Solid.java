package Game.abstraction;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import Collision.CollideManager;
import Screens.PlayScreen;
import Tiles.Enums.Direction;
import Tiles.Enums.GameObjectType;
import Game.CollideInfo;

public abstract class Solid extends GameObject{

	Vector3 velocitycopy = new Vector3();
	int physicsStep;
	Array<Direction> staticHold;
	CollideInfo test;
	
	public Solid() {
		type =  GameObjectType.Solid;
	}
	
	public void physicsStep() {
		
		if(parent != null) {
			if(parent.parent == null)
				parent = null;	
		}

		addPoints();

		physicsStep = Math.round(velocity.len() / PlayScreen.level.getTilesize() + 1);

		velocitycopy.set(velocity);
		velocitycopy.scl(1f / physicsStep);
		
		for (int i = 0; i < physicsStep; i++) {
			
			position.add(velocitycopy);
			rect.setCenter(position.x, position.y);
			
			//CollideManager.collideStatic(this, true);
			
			if(!CollideManager.overlap(this, 1f))
				continue;

			test = CollideManager.collideDynamic(this, true, false);

			if(test.collide!=null && !test.collide.dead) {

				if(test.directions.contains(Direction.Top, true) || test.directions.contains(Direction.Left, true) || test.directions.contains(Direction.Right, true)) {
					test.collide.parent = this;
				}

				
			}


		} 
		
		
	}
	
	public abstract void move(float dt); // behavior when actor is riding
	
	public void death(Direction testT, Direction staticT) {
		if(dead)
			return;
		
		if(test.directions.contains(testT, true)) {
			staticHold = CollideManager.collideStatic(this, false); //push back out for solid checks
			if(staticHold.contains(staticT, true))
				test.collide.dispose();
		}
	}
	
	public void dispose() {
		
	}
	
}
