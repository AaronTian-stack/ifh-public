package Game.gib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool.Poolable;

import Game.abstraction.Actor;
import Screens.GameContainer;
import Screens.PlayScreen;
import Tiles.Coordinate;
import Tiles.Enums.GameObjectType;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Gib extends Actor implements Poolable{ // death body part (or interactive sprite), created from a gib manager

	public Gib() {
		
		position = new Vector3();
		prevPosition = new Vector3();
		renderPosition = new Vector3();
		velocity = new Vector3();
		coor = new Coordinate(0, 0);
		rect = new Rectangle();
		
		bounceVertical = -0.4f;
		bounceHorizontal = -0.9f;
	
		type = GameObjectType.Actor;
		
		switch(MathUtils.random(1, 3)) {
		case 1:
			frame = new Sprite(GameContainer.atlas.findRegion("gib1"));
			break;
			
		case 2:
			frame = new Sprite(GameContainer.atlas.findRegion("gib2"));
			break;
			
		case 3:
			frame = new Sprite(GameContainer.atlas.findRegion("gib3"));
			break;
		}
		
		
		frame.setScale(PlayScreen.imageScale);
		
		crushcheck = true;

		
	}
	
	public Gib(AtlasRegion frame) {
		
		position = new Vector3();
		prevPosition = new Vector3();
		renderPosition = new Vector3();
		velocity = new Vector3();
		coor = new Coordinate(0, 0);
		rect = new Rectangle();
		
		bounceVertical = -0.4f;
		bounceHorizontal = -0.9f;
	
		type = GameObjectType.Actor;
		this.frame = new Sprite(frame);
		this.frame.setScale(PlayScreen.imageScale);

		
	}
	
	
	float gravity = 0.2f;
	float slowdown = 0.6f;
	
	float life = MathUtils.random(2f, 4f);
	float counter;
	
	@Override
	public void update(float dt) {
		
		prevPosition.set(position);
		
		if(!ground) {
			velocity.y -= gravity;
		}
		else {
			velocity.x *= slowdown;
		}

		rect.set(frame.getBoundingRectangle());
		rect.width*=0.9f;
		rect.height*=0.9f;
		
		physicsStep();
		
		vZero();
	
	}
	
	PooledEffect effect;

	
	float pCounter;
	float frequency = MathUtils.random(0.1f, 0.4f);
	float fadeout = 0.01f;
	
	float alpha = 1;
	
	boolean finished;
	
	public void render(SpriteBatch sb, ShapeDrawer sd) {

		interpolate();
		
		frame.rotate(-velocity.x);
		
		frame.setOriginBasedPosition(renderPosition.x, renderPosition.y);

		frame.draw(sb);
		
		counter += Gdx.graphics.getDeltaTime();
		
		if(PlayScreen.debugMode)
			
			sd.rectangle(frame.getBoundingRectangle(), Color.RED, PlayScreen.imageScale);
		
		
		if(counter < life) {
			
			pCounter += Gdx.graphics.getDeltaTime();
			
			if(pCounter > frequency) {
				effect = PlayScreen.gibManager.getBloodPool().obtain();
				
				effect.setPosition(renderPosition.x, renderPosition.y);
			
				PlayScreen.gibManager.getParticles().add(effect);
				
				pCounter = 0;
			}

		}
		else { // TODO: Replace with actor actions
			
			alpha -= fadeout * Gdx.graphics.getDeltaTime() * 144f;

			alpha = MathUtils.clamp(alpha, 0, 1);

			frame.setColor(1, 1, 1, alpha);

			finished = alpha <= 0;
				
			
		}

		

	}

	@Override
	public void reset() {
		dead = false;
		finished = false;
		alpha = 1;
		counter = 0;
		life = MathUtils.random(2f, 4f);
		frame.setColor(Color.WHITE);
		frequency = MathUtils.random(0.1f, 0.4f);
	}

	@Override
	public void die() {
		dead = true;
		
	}

	public float getCounter() {
		return counter;
	}

	public void setCounter(float counter) {
		this.counter = counter;
	}

	public float getLife() {
		return life;
	}

	public void setLife(float life) {
		this.life = life;
	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	@Override
	public void climb() {
		// TODO Auto-generated method stub
		
	} 

}
