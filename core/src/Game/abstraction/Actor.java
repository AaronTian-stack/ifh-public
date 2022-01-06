package Game.abstraction;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import Collision.CollideManager;
import Screens.PlayScreen;
import Tiles.Enums.Direction;
import Tiles.Enums.GameObjectType;
import Game.CollideInfo;

public abstract class Actor extends GameObject {

	Vector3 velocitycopy = new Vector3();
	int physicsStep;
	Array<Direction> staticHold;
	CollideInfo test;
	
	protected Vector3 parentVelocity = new Vector3();
	
	protected boolean crushcheck;
	
	public Actor() {
		 type = GameObjectType.Actor;
	}

	Vector3 testV = new Vector3();
	
	boolean hold;
	
	public void physicsStep() {

		if(type != GameObjectType.Rope) 
			addPoints();
	
		if(parent != null) {
			
			//physicsStep = Math.round(parent.velocity.len() / PlayScreen.level.getTilesize() + 2);
			//move(physicsStep, parent.velocity);
			position.add(parent.velocity); // dont actually need to use move method

			if(!CollideManager.overlapSingle(this, parent, 1.3f)) {
				parentVelocity.set(parent.velocity);
				parent.parent = null;
				parent = null;
				
			}
		}

		
		physicsStep = Math.round(velocity.len() / PlayScreen.level.getTilesize() + 1);
		
		move(physicsStep, velocity);
		
	}
	
	protected boolean dynamicClimb;
	
	public void move(int physicsStep, Vector3 velocity) {
		velocitycopy.set(velocity);
		velocitycopy.scl(1f / physicsStep);
		for (int i = 0; i < physicsStep; i++) {
			
			position.add(velocitycopy);
			rect.setCenter(position.x, position.y);
			staticHold = CollideManager.collideStatic(this, true);
			
			if(!dead) {
				
				test = CollideManager.collideDynamic(this, true, true);

				if(test.collide != null) {
					
					hold = ground;

					death(Direction.Bottom, Direction.Top);
					death(Direction.Top, Direction.Bottom);
					death(Direction.Left, Direction.Right);
					death(Direction.Right, Direction.Left);

					ground = hold;
					
					if(test.directions.contains(Direction.Top, true)) {
						parent = test.collide;
						test.collide.parent = this;
					}
					if(test.directions.contains(Direction.Right, true) || test.directions.contains(Direction.Left, true)) {
						
						if(test.directions.contains(Direction.Right, true))
							orientation = true;
						else
							orientation = false;
						
						parent = test.collide;
						test.collide.parent = this;
						climb();
						dynamicClimb = true;
					}
					else
						dynamicClimb = false;
				
				}

			}

		}
	}
	
	public void death(Direction testT, Direction staticT) {
		if(dead)
			return;
		
		if(test.directions.contains(testT, true)) {
			staticHold = CollideManager.collideStatic(this, true); //push back out for solid checks
			if(staticHold.contains(staticT, true))
				die();
		}
	}
	
	public void dispose() {
		die();
	}
	
	public abstract void die();
	
	public abstract void climb();

	public Vector3 getParentVelocity() {
		return parentVelocity;
	}

}
