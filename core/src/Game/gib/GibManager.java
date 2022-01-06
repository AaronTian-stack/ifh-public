package Game.gib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import Game.abstraction.GameObject;
import Screens.GameContainer;
import Screens.PlayScreen;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class GibManager extends GameObject { // manages and spawns gibs at its position
	
	private Pool<Gib> gibPool = Pools.get(Gib.class);;
	private Array<Gib> gibs = new Array<Gib>();;
	private ParticleEffect blood = new ParticleEffect();
	private ParticleEffectPool bloodPool = new ParticleEffectPool(blood, 0, 1000);
	private Array<ParticleEffectPool.PooledEffect> particles = new Array<PooledEffect>();
	
	PooledEffect effect;
	
	public GibManager() {
		position = new Vector3();
		blood.load(Gdx.files.internal("Particles/blood"), GameContainer.atlas);
		blood.scaleEffect(PlayScreen.imageScale / 2);
	}
	
	Gib gib;

	public void generate(int amount) { // generates some flesh
		
		for(int i=0;i<amount;i++) {
			gib = gibPool.obtain();
			
			gib.reset();
			
			gib.setPosition(position);
			
			gib.setVelocity(MathUtils.random(-1f, 1f), MathUtils.random(0.2f, 2f));
			
			gibs.add(gib);
		}		
		
	}
	
	public void freeAll() {
		gibPool.freeAll(gibs);
		gibs.clear();
		particles.clear();
	}

	public void update(float dt) {

		for (int i = gibs.size - 1; i >= 0; i--) {
			gib = gibs.get(i);
			if(gib.isFinished()) {
				gibPool.free(gib);
				gibs.removeIndex(i);
				gib.reset();
			}
			else
				gib.update(dt);
		}

	}

	public void render(SpriteBatch sb, ShapeDrawer sd) {
		
		for (int i = particles.size - 1; i >= 0; i--) {
			effect = particles.get(i);
			effect.update(Gdx.graphics.getDeltaTime());
			effect.draw(sb, Gdx.graphics.getDeltaTime());
			if (effect.isComplete()) {
				effect.free();
				particles.removeIndex(i);
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.G)) {
			position.set(PlayScreen.clickPos);
			generate(10);
		}

		
	}

	public void dipose() {
		// TODO Auto-generated method stub
		
	}

	public Array<Gib> getGibs() {
		return gibs;
	}

	public void setGibs(Array<Gib> gibs) {
		this.gibs = gibs;
	}

	public ParticleEffectPool getBloodPool() {
		return bloodPool;
	}

	public void setBloodPool(ParticleEffectPool bloodPool) {
		this.bloodPool = bloodPool;
	}

	public Array<ParticleEffectPool.PooledEffect> getParticles() {
		return particles;
	}

	public void setParticles(Array<ParticleEffectPool.PooledEffect> particles) {
		this.particles = particles;
	}

	public Pool<Gib> getGibPool() {
		return gibPool;
	}

	public void setGibPool(Pool<Gib> gibPool) {
		this.gibPool = gibPool;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
