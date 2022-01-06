package Game;

import Screens.GameContainer;
import Screens.PlayScreen;
import Tiles.Coordinate;
import Tiles.Enums.GameObjectType;
import Tiles.Enums.State;
import Tiles.Enums.TileType;
import UI.RadialSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Queue;

import Collision.CollideManager;
import Game.abstraction.Actor;
import Game.abstraction.GameObject;
import Game.camera.CameraAction;
import Game.gib.Gib;
import Game.ropes.PlayerRope;
import Game.shared.GameSprites;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Stack;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Player extends Actor {

	private Animation<TextureRegion> idle = new Animation<TextureRegion>(0.15f,
			GameContainer.atlas.findRegions("idle"), Animation.PlayMode.LOOP);

	private Animation<TextureRegion> run = new Animation<TextureRegion>(0.06f,
			GameContainer.atlas.findRegions("run"), Animation.PlayMode.LOOP);

	private Animation<TextureRegion> jumpAnim = new Animation<TextureRegion>(0.04f,
			GameContainer.atlas.findRegions("jump"), Animation.PlayMode.NORMAL);
	
	private Animation<TextureRegion> fallAnim = new Animation<TextureRegion>(0.05f,
			GameContainer.atlas.findRegions("fall"), Animation.PlayMode.LOOP);;

	private Animation<TextureRegion> crouchIdle = new Animation<TextureRegion>(0.3f,
			GameContainer.atlas.findRegions("crouch"), Animation.PlayMode.LOOP);
	
	private Animation<TextureRegion> crouchWalk = new Animation<TextureRegion>(0.1f,
			GameContainer.atlas.findRegions("walk"), Animation.PlayMode.LOOP);

	private Animation<TextureRegion> climbA = new Animation<TextureRegion>(0.1f,
			GameContainer.atlas.findRegions("climb"), Animation.PlayMode.LOOP);

	private Animation<TextureRegion> climbIdle = new Animation<TextureRegion>(0.15f,
			GameContainer.atlas.findRegions("climbIdle"), Animation.PlayMode.LOOP);

	private Animation<TextureRegion> currentAnim = idle;

	private float width = 2f;

	private float height = 3f;

	private float cWidth = 2f;

	private float cHeight = 2f;

	float[][] idleOffset, runOffset, jumpOffset, fallOffset, crouchWalkOffset, crouchIdleOffset, climbOffset, climbIdleOffset;

	boolean turned;

	float runSpeed = 0.6f;
	
	float crouchSpeed = 0.2f;
	
	float climbSpeed = 0.5f;
	
	float maxStamina = 0.8f; // total stamina (seconds)

	float COspeed = 1.2f; // flying in edit mode

	float airslow = 0.85f; // factor x velocity is slowed when not pressing buttons in air
	
	float airtime;

	float staminaTime;

	float runTime, standTime, climbTime, climbIdleTime;

	float acceleration = 0.2f;

	float jumpV = 0.5f;
	
	float gravity = 0.2f; //0.2f
	
	float flygravity = 0.08f;
	
	float rotation;

	boolean jump,justJump;

	State state = State.Idle;

	float velocityapply;

	Sound climbSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Player/climb.wav")); 
	
	Sound jumpSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Player/jump.wav"));
	
	Sound impact = Gdx.audio.newSound(Gdx.files.internal("Sounds/Player/impact.wav"));
	
	Sound impact2 = Gdx.audio.newSound(Gdx.files.internal("Sounds/Player/impact2.wav"));

	Sound walk1 = Gdx.audio.newSound(Gdx.files.internal("Sounds/Player/walk1.wav"));
	
	Sound walk2 = Gdx.audio.newSound(Gdx.files.internal("Sounds/Player/walk2.wav"));

	float slowTime = 0.2f;

	float saved, savedCrouch;

	float jumpTime;

	float stateTime;

	//float staminaAnimation; // for green bar

	TextureRegion currentFrame;

	PooledEffect effect;
	
	ParticleEffect runSmoke = new ParticleEffect();
	
	ParticleEffect jumpSmoke = new ParticleEffect();
	
	ParticleEffect sweat = new ParticleEffect();
	
	ParticleEffect skyTrail = new ParticleEffect();

	ParticleEffectPool runSmokePool, jumpSmokePool, sweatPool, skyTrailPool;

	Array<ParticleEffectPool.PooledEffect> effects = new Array<PooledEffect>();

	public void offsets() {
		
		idleOffset = new float[idle.getKeyFrames().length][2];
		runOffset = new float[run.getKeyFrames().length][2];
		jumpOffset = new float[jumpAnim.getKeyFrames().length][2];
		fallOffset = new float[fallAnim.getKeyFrames().length][2];
		crouchWalkOffset = new float[crouchWalk.getKeyFrames().length][2];
		crouchIdleOffset = new float[crouchIdle.getKeyFrames().length][2];
		climbOffset = new float[climbA.getKeyFrames().length][2];
		climbIdleOffset = new float[climbIdle.getKeyFrames().length][2];
		
		FileHandle file = Gdx.files.internal("Sprites/wire.txt");
		String text = file.readString();
		String[] textA = text.split("\\r?\\n");
		String[] idleS = textA[0].split(" ");
		String[] runS = textA[1].split(" ");
		String[] jumpS = textA[2].split(" ");
		String[] fallS = textA[3].split(" ");
		String[] crouchWalkS = textA[4].split(" ");
		String[] crouchIdleS = textA[5].split(" ");
		String[] climbS = textA[6].split(" ");
		String[] climbIdleS = textA[7].split(" ");
		
		int count = 0;
		int i;
		for (i = 0; i < idleOffset.length; i++) {
			idleOffset[i][0] = Float.parseFloat(idleS[count]) * PlayScreen.imageScale;
			count++;
			idleOffset[i][1] = Float.parseFloat(idleS[count]) * PlayScreen.imageScale;
			count++;
		}
		count = 0;
		for (i = 0; i < runOffset.length; i++) {
			runOffset[i][0] = Float.parseFloat(runS[count]) * PlayScreen.imageScale;
			count++;
			runOffset[i][1] = Float.parseFloat(runS[count]) * PlayScreen.imageScale;
			count++;
		}
		count = 0;
		for (i = 0; i < jumpOffset.length; i++) {
			jumpOffset[i][0] = Float.parseFloat(jumpS[count]) * PlayScreen.imageScale;
			count++;
			jumpOffset[i][1] = Float.parseFloat(jumpS[count]) * PlayScreen.imageScale;
			count++;
		}
		count = 0;
		for (i = 0; i < fallOffset.length; i++) {
			fallOffset[i][0] = Float.parseFloat(fallS[count]) * PlayScreen.imageScale;
			count++;
			fallOffset[i][1] = Float.parseFloat(fallS[count]) * PlayScreen.imageScale;
			count++;
		}
		count = 0;
		for (i = 0; i < crouchWalkOffset.length; i++) {
			crouchWalkOffset[i][0] = Float.parseFloat(crouchWalkS[count]) * PlayScreen.imageScale;
			count++;
			crouchWalkOffset[i][1] = Float.parseFloat(crouchWalkS[count]) * PlayScreen.imageScale;
			count++;
		}
		count = 0;
		for (i = 0; i < crouchIdleOffset.length; i++) {
			crouchIdleOffset[i][0] = Float.parseFloat(crouchIdleS[count]) * PlayScreen.imageScale;
			count++;
			crouchIdleOffset[i][1] = Float.parseFloat(crouchIdleS[count]) * PlayScreen.imageScale;
			count++;
		}
		count = 0;
		for (i = 0; i < climbOffset.length; i++) {
			climbOffset[i][0] = Float.parseFloat(climbS[count]) * PlayScreen.imageScale;
			count++;
			climbOffset[i][1] = Float.parseFloat(climbS[count]) * PlayScreen.imageScale;
			count++;
		}
		count = 0;
		for (i = 0; i < climbIdleOffset.length; i++) {
			climbIdleOffset[i][0] = Float.parseFloat(climbIdleS[count]) * PlayScreen.imageScale;
			count++;
			climbIdleOffset[i][1] = Float.parseFloat(climbIdleS[count]) * PlayScreen.imageScale;
			count++;
		}
		
		System.out.println(Arrays.deepToString(idleOffset));
		System.out.println(Arrays.deepToString(runOffset));
		System.out.println(Arrays.deepToString(jumpOffset));
		System.out.println(Arrays.deepToString(fallOffset));
		System.out.println(Arrays.deepToString(crouchWalkOffset));
		System.out.println(Arrays.deepToString(crouchIdleOffset));
		System.out.println(Arrays.deepToString(climbOffset));
		System.out.println(Arrays.deepToString(climbIdleOffset));

	}


	public Player() {
		
		position = new Vector3(0, 0, 0);
		prevPosition = new Vector3(0, 0, 0);
		renderPosition = new Vector3(0, 0, 0);
		rect = new Rectangle(0, 0, width, height);
		frame = new Sprite();
		
		velocity = new Vector3(0, 0, 0);

		coor = new Coordinate(0, 0);

		color = new Color(Color.WHITE);

		runSmoke.load(Gdx.files.internal("Particles/runSmoke"), GameContainer.atlas);
		jumpSmoke.load(Gdx.files.internal("Particles/jump"), GameContainer.atlas);
		sweat.load(Gdx.files.internal("Particles/sweat"), GameContainer.atlas);
		skyTrail.load(Gdx.files.internal("Particles/skyTrail"), GameContainer.atlas);

		runSmoke.scaleEffect(PlayScreen.imageScale);
		jumpSmoke.scaleEffect(PlayScreen.imageScale);
		sweat.scaleEffect(PlayScreen.imageScale);
		skyTrail.scaleEffect(PlayScreen.imageScale);
		
		runSmokePool = new ParticleEffectPool(runSmoke, 1, 20);
		jumpSmokePool = new ParticleEffectPool(jumpSmoke, 1, 3);
		sweatPool = new ParticleEffectPool(sweat, 1, 10);
		skyTrailPool = new ParticleEffectPool(skyTrail, 1, 50);

		frame.setScale(PlayScreen.imageScale);
		scaleX = PlayScreen.imageScale;
		scaleY = PlayScreen.imageScale;
		
		
		reset.setRunnable(new Runnable() {
		@Override
		public void run () {
			reset();
		}
		});
		
		zIndex = -1;
		
		bodyparts.addAll(torso, leftLeg, rightLeg, leftArm, rightArm);
		
		offsets();
	}
	
	float fallSpeed = runSpeed;
	boolean hit, crouch;
	
	float sc = 0.53f; // magic number
	
	public void update(float dt) {
			
		prevPosition.set(position);
		
		if (dead) {
			parent = null;
			gib.update(dt);
			gib.setPosition(position);
			for(Gib g : bodyparts)
				g.update(dt);
		}
		
		if (PlayScreen.editMode) {
			editControl(dt);
		} 
		else {
			
			if (!ground) {
				
				airtime += dt;
				if(state != State.ClimbIdle && state != State.Climb) {
					if(state == State.Fly) {
						velocity.y -= flygravity;
						parentVelocity.y -= flygravity;
					}
					else {
						velocity.y -= gravity;
						parentVelocity.y -= gravity;
					}
				}
				
				if (state == State.Jump) {
					justJump = true;
					fallSpeed = runSpeed;
				}
				if (velocity.y < -0.4f && state != State.Fly) {

					if(state == State.CrouchIdle || state == State.CrouchWalk)
						fallSpeed = crouchSpeed;
					else if (state != State.Fall)
						fallSpeed = runSpeed;
					
					state = State.Fall;
					standTime = 0;
				}
	
				switch (state) {
				case ClimbIdle:
					climbIdle(dt);
					break;
				case Climb:
					climb(dt);
					break;
				case Fall:
					fall(dt);
					break;
				case Jump:
					jump(dt);
					break;
				case Fly:
					fly(dt);
					break;
				}
				
				
				velocity.x += parentVelocity.x * sc;
				if(parentVelocity.y > 0) 
					velocity.y += parentVelocity.y * 0.4f;
				
				stamina = MathUtils.clamp(stamina, 0, maxStamina);
			
			} else if (ground) {
				
				flyDisabled = false;
				flyTimer = 0;

				parentVelocity.setZero();
				
				stamina = 0;
				factor = regen;
				coyote = true;
				airtime = 0;
				if (state == State.Jump || state == State.Fall || state == State.Fly) {
					if (right || left) {
						state = State.Run;
					} else {
						state = State.Idle;
						saved = 0;
					}
					impact2.play(GameContainer.pref.getSoundVolume());
					hit = true;
					effect = jumpSmokePool.obtain();
					effect.setPosition(rect.x, renderPosition.y - rect.height / 2);
					effects.add(effect);
					
				}
				
				
				if((state == State.CrouchIdle || state == State.CrouchWalk) && !crouch) {
					setCrouch();
					crouch = true;
				}
	
				if((state != State.CrouchIdle && state != State.CrouchWalk) && crouch) {
					setNormal();
					crouch = false;
				}

				switch (state) {
				case CrouchWalk:
					crouchWalk(dt);
					break;
				case CrouchIdle:
					crouchIdle(dt);
					break;
				case Idle:
					idle(dt);
					break;
				case Run:
					run(dt, 0);
					break;
				}
			}
		}
		
		//velocity.add();
		
		vZero();
	
		physicsStep();
		
		//rope.update(dt);

	}
	

	Interpolation sl = Interpolation.pow5Out;

	public void idle(float dt) {
		standTime += dt;
		runTime = 0;
		airtime = 0;
		currentAnim = idle;
		state = State.Idle;
		if (down) {
			stateTime = 0;
			crouchIdle(dt);
		}
		if ((left && !right && leftCheck()) || (right && !left && rightCheck())) 
			run(dt, 0);
		if (jumpKey) {
			velocity.y = 0;
			if(!justJump)
				jump(dt);
		}
		if ((!(left || right) || (left && right))) {
			velocityapply = sl.apply(saved, 0, Math.min(1, standTime / slowTime));
			velocity.x = velocityapply;
		}
	}
	
	public void run(float dt, float start) {

		standTime = 0;
		state = State.Run;
		//if (ramp) {
		//	velocity.scl(0.8f);
		//}
		if (jumpKey) {
			velocity.y = 0;
			if(!justJump)
				jump(dt);
		}
		if(down) {
			savedCrouch = velocity.x * 1.5f;
			runTime=0;
			crouchWalk(dt);
			return;
		}
		turn(dt, start, runSpeed, run);
		if ((!right && !left) || (left && right) || (right && !rightCheck()) || (left && !leftCheck())) {
			saved = velocity.x;
			idle(dt);
		}
		
	}

	public void jump(float dt) {

		if(checkClimb()) {
			climbIdle(dt);
			return;
		}
		
		if(flyTrigger(dt)) return;
		
		if (jumpKey && jumpTime < 0.1f)
			velocity.y += jumpV;
		jumpTime += dt;
		airstrafe(dt);
		currentAnim = jumpAnim;
		if (state != State.Jump) {
			stateTime = 0;
			jumpSound.stop();
			jumpSound.play(0.5f * GameContainer.pref.getSoundVolume());
			
		}	
		state = State.Jump;
		
	}

	
	float regen = 3f; //original value!
	float factor = regen;
	float factorPly = 0.5f;
	
	float minStamina = maxStamina * 0.5f;
	
	public void fall(float dt) {

		state = State.Fall;
		ground = false;
		
		if(dead)
			return;
		
		if(flyTrigger(dt)) return;

		if (checkClimb() && stamina < minStamina) {
			setDimensionNormal();
			climbIdle(dt);
			return;
		}
		
		if (velocity.y < 0)
			stamina -= dt * factor;
		
		airstrafe(dt);
	}
	
	boolean coyote;

	float coyoteTime = 0.1f;

	public void airstrafe(float dt) {

		ground = false;

		turn(dt, 0, fallSpeed, run);
		
		if(state == State.Fall)
			currentAnim = fallAnim;
		if (jumpKey && accurateAir < coyoteTime && coyote && !justJump) {
			justJump = false;
			coyote = false;
			velocity.y = 0;
			jumpTime = 0;
			if (wallJump)
				stamina += maxStamina * 0.3f;
			jump(dt);
		}
		if (accurateAir > coyoteTime)
			wallJump = false;
		if (!right && !left) {
			velocity.x *= airslow;
			parentVelocity.x *= airslow;
		}
		
	}
	
	Interpolation ac = Interpolation.sineOut;

	public void turn(float dt, float start, float speed, Animation<TextureRegion> anim) {

		if (left && !right) {
			runTime += dt;
			currentAnim = anim;
			if (!orientation) {
				runTime = 0;
				turned = true;
				savedCrouch = 0;
				if (state == State.CrouchWalk) //FIX FOR: When you switch direction during transition from run to crouch walk it interpolates from wrong speed 
					start=0;
			}
			velocityapply = ac.apply(start, -speed, Math.min(1, runTime / acceleration));
			velocity.x = velocityapply;
			velocity.x += parentVelocity.x * sc;
			orientation = left;
		}
		if (right && !left) {
			runTime += dt;
			currentAnim = anim;
			if (orientation) {
				runTime = 0;
				turned = true;
				savedCrouch = 0;
				if (state == State.CrouchWalk)
					start=0;
			}
			velocityapply = ac.apply(start, speed, Math.min(1, runTime / acceleration));
			velocity.x = velocityapply;
			velocity.x += parentVelocity.x * sc;
			orientation = left;
		}
	}

	public void crouchIdle(float dt) {
		standTime += dt;
		runTime = 0;
		airtime = 0;
		currentAnim = crouchIdle;
		state = State.CrouchIdle;
		justJump = false;
		
		if(!down && crouchCheck())
			idle(dt);
		if ((left && !right && leftCheck()) || (right && !left && rightCheck())) {
			savedCrouch = 0;
			crouchWalk(dt);
		}
		if (!(left || right) || (left && right)) {
			velocityapply = sl.apply(saved, 0, Math.min(1, standTime / slowTime));
			velocity.x = velocityapply;
		}
	}
	
	public void crouchWalk(float dt) {
		standTime = 0;
		state = State.CrouchWalk;
		
		turn(dt, savedCrouch, crouchSpeed, crouchWalk);
		//if (ramp) {
		//	velocity.scl(0.8f);
		//	velocity.y = -2;
		//}
		if (!down && crouchCheck()) {
			runTime = 0;
			run(dt, velocity.x);
		}
		if ((!right && !left) || (left && right) || (right && !rightCheck()) || (left && !leftCheck())) {
			saved = velocity.x;
			crouchIdle(dt);
		}
	}
	

	public void climbIdle(float dt) {
		
		parentVelocity.setZero();
		justJump = false;
		currentAnim = climbIdle;
		climbTime = 0;
		climbIdleTime += dt;
		state = State.ClimbIdle;
		airtime = 0;
		accurateAir = 0;
		velocity.x = 0;

		if ((!orientation && left) || (orientation && right) || !checkClimb() || stamina >= maxStamina) {
			exitClimb(dt);
			return;
		}
		
		if(up ^ (down && CollideManager.getCellType(position.x, position.y - rect.height / 2 - 0.01f) == TileType.Empty))
			climb(dt);

		if (!(up || down) || (up && down)) {
			velocityapply = sl.apply(saved, 0, Math.min(1, climbIdleTime / slowTime));
			velocity.y = velocityapply;
		}

	

	}
	
	public void climb() { // actor climb

		if(parent != null && (stamina < minStamina) && dynamicClimb) {
			climbIdle(GameContainer.dt);
		}
		else {
			parent = null;
		}
		
	}

	Interpolation climbIn = Interpolation.slowFast;
	public void climb(float dt) {
		
		parentVelocity.setZero();
		climbIdleTime = 0;
		justJump = false;
		currentAnim = climbA;
		state = State.Climb;
		airtime = 0;
		accurateAir = 0;
		velocity.x = 0;

		if ((!orientation && left) || (orientation && right) || !checkClimb() || stamina >= maxStamina 
				|| (CollideManager.getCellType(position.x, position.y - rect.height / 2 - 0.01f) != TileType.Empty && down)) {
			exitClimb(dt);
			return;
		}

		climber(dt, 0, Math.max(climbSpeed * 0.2f, climbIn.apply(climbSpeed, 0, Math.min(1, stamina / maxStamina))));
		
		if ((!up && !down) || (up && down)) {
			saved = velocity.y;
			climbIdle(dt);
		}

	}
	
	boolean wallJump;
	Vector3 velocitycopy = new Vector3();
	boolean chuckle;

	public void exitClimb(float dt) {

		coyote = stamina < maxStamina;
		wallJump = true;
		factor *= factorPly;
		
		if (coyote) 
			velocity.y += 0.5f;
		else
			justJump = true;

		position.x -= 0.25f * (orientation ? -1 : 1);

		if(parent != null) {
			
			velocitycopy.set(parent.getVelocity());

			parentVelocity.add(velocitycopy.scl(1)); // in case you want to scale
			
			dynamicClimb = false; // hack to disable climbing so you don't get caught again

		}

		parent = null;
		
		fall(dt);

	}
	
	float stamina;

	public void climber(float dt, float start, float speed) {
		if (up && !down) {
			climbTime += dt;
			stamina += dt;
			velocityapply = ac.apply(start, speed, Math.min(1, climbTime / acceleration));
			velocity.y = velocityapply;
			
		}
		if (down && !up) {
			climbTime += dt;
			stamina += dt;
			velocityapply = ac.apply(start, -speed, Math.min(1, climbTime / acceleration));
			velocity.y = velocityapply;
			
		}
	}

	public boolean flyTrigger(float dt) {
		

		if(!justLeft && flyKey) {
			velocity.scl(0.6f);
			flyTrail.emptyQueue();
			canExit = false;
			fly(dt);
			return true;
		}

		
		return false;
	}
	
	float rotationSpeed = 11;
	float rotateSpeed;
	float maxDown = -4f; // needed velocity for max rotation
	
	boolean flyDisabled;
	float disableTimer, flyTimer;
	float flyDisable = 0.7f; // how long disabled flight lasts
	float slowFly = 0.7f; // timer for disabled flight. If you try up pull up without enough this timer will trigger 
	
	float flyMin = 0.8f; // minimum factor for rotation
	float flyScale = 0.96f; // how much velocity is scaled while rotating
	
	Vector2 velocity2D = new Vector2();
	Vector2 parentVelocity2D = new Vector2();
	
	boolean canExit;
	boolean justLeft;

	public void fly(float dt) {

		if(!flyKey)
			canExit = true;
		
		if(canExit && flyKey) {
			flyKey = false;
			justLeft = true;
			fall(dt);
			return;
		}

		//System.out.println(velocity.len());
		state = State.Fly;
		
		velocity2D.set(velocity.x, velocity.y);

		//rotateSpeed = rotationSpeed * (Math.min(Math.max(flyMin, 1 - (maxDown - velocity.y) / maxDown), 1));
		
		if(velocity.y > 0)
			rotateSpeed = rotationSpeed * (Math.min(Math.max(flyMin, (90 - rotation) / 90), 1)) * Math.max(1, (velocity.len() / 1.5f));
		else
			rotateSpeed = rotationSpeed * Math.max(1, velocity.y / maxDown);
		
		//if(rotateSpeed == rotationSpeed * flyMin && up && velocity.len2() < 2) { // maybe also overall velocity speed because this triggers too often
		if((up || down) && velocity.y > -0.5f && velocity.len() < 1) { // maybe also overall velocity speed because this triggers too often
			disableTimer += dt;
			if(disableTimer > slowFly) {
				flyDisabled = true;
				disableTimer = 0;
			}
		}
		else if(velocity.y < -1) {
			disableTimer = 0;
		}
		
		if(flyDisabled) {
			flyTimer += dt;
			if(a.getActions().size == 0) {
				colorTrans.addAction(Actions.color(flash, 0.07f));
				colorTrans.addAction(Actions.color(white, 0.07f));
				a.addAction(colorTrans);
			}
			if(flyTimer > flyDisable) {
				flyTimer = 0;
				flyDisabled = false;
			}
				
		}
		else {
			if((up && !down && !orientation) || (down && !up && orientation)) {
				velocity2D.rotateDeg(rotateSpeed);
				parentVelocity2D.rotateDeg(rotateSpeed);
			}
			if((down && !up && !orientation) || (up && !down && orientation)) {
				velocity2D.rotateDeg(-rotateSpeed);
				parentVelocity2D.rotateDeg(-rotateSpeed);
			}
		}
		
		velocity.set(velocity2D, 0);
		parentVelocity.set(parentVelocity2D, 0);
		
		if(up && velocity.y > 0 && velocity.len() > 0.5f) {
			velocity.scl(flyScale);
			parentVelocity.scl(flyScale);
		}
		
		
		rotation = MathUtils.radiansToDegrees * MathUtils.atan2(velocity.y, velocity.x); // -90 straight down

		
	}

	public void editControl(float dt) {
		
		climb = false;
		if (left && !right)
			if (shift) {
				velocity.x = -COspeed * 3;
			} else {
				velocity.x = -COspeed;
			}
		if (right && !left)
			if (shift) {
				velocity.x = COspeed * 3;
			} else {
				velocity.x = COspeed;
			}
		if (down && !up)
			if (shift) {
				velocity.y = -COspeed * 3;
			} else {
				velocity.y = -COspeed;
			}
		if (up && !down)
			if (shift) {
				velocity.y = COspeed * 3;
			} else {
				velocity.y = COspeed;
			}
		if (control) {
			velocity.scl(0.2f);
		} else {
			velocity.scl(0.75f);
		}
		if (up || left || down || right) {
			currentAnim = run;
			state = State.Run;
		} else {
			state = State.Idle;
			currentAnim = idle;
		}
		vZero();
	}

	public void setCrouch() {
		
		rect.height = cHeight;
		rect.width = cWidth;
		position.y -= (height - cHeight) / 2;
		stateTime=0;
		
		
	}
	
	public void setNormal() {
		
		if(dead)
			return; // failsafe
		
		if(crouchCheck()) {
			setDimensionNormal();
			position.y += (height - cHeight) / 2;
			prevPosition.y = position.y;

		}
	}
	
	public void setDimensionNormal() {
		rect.height = height;
		rect.width = width;
	}
	
	public boolean crouchCheck() {
		return CollideManager.getCellType(position.x, position.y + rect.height/2) == TileType.Empty 
				&& CollideManager.getCellType(position.x - rect.width/2 - 0.01f, position.y + rect.height/2) == TileType.Empty 
				&& CollideManager.getCellType(position.x + rect.width/2, position.y + rect.height/2) == TileType.Empty;
	}
	
	public void dynamicClimb() {

		if(stamina >= maxStamina)
			return;
		
		if(parent.getPosition().x > position.x)
			position.x = parent.getPosition().x - parent.getRect().width / 2 - rect.width / 2;
		if(parent.getPosition().x < position.x)
			position.x = parent.getPosition().x + parent.getRect().width / 2 + rect.width / 2;
	}
	
	public boolean checkClimb() {
		
		if(parent != null && dynamicClimb && CollideManager.yOverlap(this, parent) > 0.5f) {
			return true;
		}
		
		if (orientation && CollideManager.getCellType(position.x - rect.width / 2 - 0.01f, position.y) == TileType.Solid) {
			orientation = true;
			return true;
		}
		
		if (!orientation && CollideManager.getCellType(position.x + rect.width / 2, position.y) == TileType.Solid) {
			orientation = false;
			return true;
		}
			
		return false;

	}
	
	public boolean leftCheck() {
		return CollideManager.getCellType(position.x - rect.width / 2 - 0.01f, position.y) == TileType.Empty;
	}
	
	public boolean rightCheck() {
		return CollideManager.getCellType(position.x + rect.width / 2, position.y) == TileType.Empty;
	}
	
	CameraAction action;
	RunnableAction reset = new RunnableAction();
	SequenceAction overallSequence = new SequenceAction();
	
	public void die() {
		
		if(dead)
			return;

		dead = true;
		PlayScreen.gibManager.setPosition(position);
		PlayScreen.gibManager.generate(5);
		
		setCrouch();
		
		rect.setSize(1f, 1f);
		
		velocity.x = MathUtils.random(-2f, 2f);

		velocity.y = MathUtils.random(0.5f, 2f);
		
		PlayScreen.editMode = false;
		
		action = PlayScreen.gameCamera.createAction();
		
		action.set(0.5f, 2, 0.0625f, Interpolation.smooth, Interpolation.exp5Out);

		PlayScreen.gameCamera.getActionQueue().add(action);

		//overallSequence.reset();

		overallSequence.addAction(Actions.fadeIn(0.5f));
		overallSequence.addAction(Actions.delay(1.5f));
		overallSequence.addAction(Actions.run(reset.getRunnable()));
		overallSequence.addAction(Actions.fadeOut(0.5f));
		
		action = PlayScreen.gameCamera.createAction();
		
		action.set(2, 2, 0.125f, Interpolation.smooth, Interpolation.smooth, overallSequence);

		PlayScreen.gameCamera.getActionQueue().add(action);
		
		PlayScreen.gameCamera.getMouse().setTarget(0.125f);

		resetInput();
		
		for(Gib g : bodyparts) {
			g.setPositionAll(position);
			g.setLife(MathUtils.random(4f, 6f));
			g.setVelocity(MathUtils.random(-2f, 2f), MathUtils.random(0.5f, 2f));
		}
		
		state = State.Fall;
		
		fall(GameContainer.dt);
		
	}
	
	public void reset() {
		dead = false;
		rect.setSize(width, height);
		velocity.setZero();
		setPositionAll(0, 5);
		for(Gib g : bodyparts) { 
			g.reset();
		}
		
		frame.setRotation(0);
		
		PlayScreen.gibManager.freeAll();
	}

	Interpolation squash = Interpolation.swingIn;

	Interpolation impactSquash = Interpolation.swingOut;

	Interpolation fall = Interpolation.pow2In;
	
	Interpolation elastic = Interpolation.elasticOut;

	float accurateAir, accurateGround, accurateFly;

	float scaleX, scaleY;

	float saveX, saveY;

	TextureRegion death = new TextureRegion(GameContainer.atlas.findRegion("deadhead"));
	
	Gib gib = new Gib();
	
	Gib torso = new Gib(GameContainer.atlas.findRegion("deadtorso"));
	Gib leftLeg = new Gib(GameContainer.atlas.findRegion("deadlegleft"));
	Gib rightLeg = new Gib(GameContainer.atlas.findRegion("deadlegright"));
	Gib leftArm = new Gib(GameContainer.atlas.findRegion("deadarmleft"));
	Gib rightArm = new Gib(GameContainer.atlas.findRegion("deadarmright"));
	
	Array<Gib> bodyparts = new Array<Gib>();
	
	LineTrail flyTrail = new LineTrail(25, 1.5f, new Color(Color.WHITE), Interpolation.sineIn);
	
	float rotationAnim;
	
	public void render(SpriteBatch sb, ShapeDrawer sd) {
	
		interpolate();
		
		if(state == State.Climb && stamina > maxStamina * 0.25f)
			stateTime += Gdx.graphics.getDeltaTime() * (stamina / maxStamina);
		else
			stateTime += Gdx.graphics.getDeltaTime();
		
		if(!dead) {
			input();
			currentFrame = currentAnim.getKeyFrame(Math.max(0, stateTime));
		}
		else {
			currentFrame = death;
			gib.setCounter(0);
			gib.setPosition(position);
			gib.getFrame().setScale(0);
			gib.render(sb, sd);
			
			for(Gib g: bodyparts) {
				g.render(sb, sd);
			}
			
		}

		frame.setRegion(currentFrame);
		
		frame.setBounds(0, 0, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
		
		if(state == State.ClimbIdle)
			
			frame.setOrigin(frame.getWidth() / 2.0f - PlayScreen.imageScale * 1.25f * (orientation ? -1 : 1), frame.getHeight() / 2.0f);
			
		else
		
			frame.setOrigin(frame.getWidth() / 2.0f, frame.getHeight() / 2.0f);
		

		if (!ground) {
			accurateAir += Gdx.graphics.getDeltaTime();
			if (accurateAir > 0.33f)
				accurateGround = 0;
			if(state == State.Fly)
				accurateFly += Gdx.graphics.getDeltaTime();
			else
				accurateFly = 0;
		} else {
			accurateGround += Gdx.graphics.getDeltaTime();
			accurateAir = 0;
		}

		switch (state) {
		case Idle:
		case Run:
			scaleX = impactSquash.apply(PlayScreen.imageScale * 2f, PlayScreen.imageScale, Math.min(accurateGround * 3.5f, 1));
			scaleY = impactSquash.apply(PlayScreen.imageScale * 0.25f, PlayScreen.imageScale, Math.min(accurateGround * 3.5f, 1));
			break;
		case Fall:
			scaleX = fall.apply(PlayScreen.imageScale, 0.5f * PlayScreen.imageScale, Math.min(accurateAir, 1));
			scaleY = fall.apply(PlayScreen.imageScale, 1.2f * PlayScreen.imageScale, Math.min(accurateAir, 1));
			break;
		case Jump:
			scaleX = squash.apply(1.3f * PlayScreen.imageScale, PlayScreen.imageScale, Math.min(stateTime * 3.5f, 1));
			scaleY = squash.apply(0.8f * PlayScreen.imageScale, PlayScreen.imageScale, Math.min(stateTime * 3.5f, 1));
			break;
		case CrouchWalk:
		case CrouchIdle:	
			scaleX = elastic.apply(PlayScreen.imageScale, 1f * PlayScreen.imageScale, Math.min(stateTime, 1));
			scaleY = elastic.apply(PlayScreen.imageScale, 0.9f * PlayScreen.imageScale, Math.min(stateTime, 1)); 
			break;
		case Climb:
		case ClimbIdle:
			scaleX = PlayScreen.imageScale;
			scaleY = PlayScreen.imageScale;
			break;
		case Fly:
			scaleX = impactSquash.apply(PlayScreen.imageScale * 2f, PlayScreen.imageScale, Math.min(accurateFly * 1.5f, 1));
			scaleY = impactSquash.apply(PlayScreen.imageScale * 0.25f, PlayScreen.imageScale, Math.min(accurateFly * 1.5f, 1));
			break;
		}
		if (PlayScreen.editMode) {
			scaleX = PlayScreen.imageScale;
			scaleY = PlayScreen.imageScale;
		}
	
		frame.setScale(scaleX, scaleY);
		frame.setFlip(orientation, false);
		
		if(!dead)
			frame.setCenter(renderPosition.x,
					renderPosition.y + ((frame.getBoundingRectangle()).height - rect.height) / 2.0f);
		else
			frame.setCenter(renderPosition.x,
					renderPosition.y);

		sounds();
		flash();

		frame.setColor(color);
		
		if(parent != null) // For debug 
			frame.setColor(Color.GREEN);
		
		if(dead) 
			rotationAnim -= velocity.x;
		else {
			if(state == State.Fly) {
				if(!orientation) {
					rotationAnim = rotation;
					//rotationAnim = MathUtils.lerpAngleDeg(rotationAnim, 0, 0.4f);
				}
				else {
					if(rotation < -90 || (rotation < 90 && rotation > -90))
						rotationAnim = rotation + 180;
					else if(rotation > 90)
						rotationAnim = rotation - 180;
				}
			}
			else {
				rotationAnim = MathUtils.lerpAngleDeg(rotationAnim, 0, 0.4f);
				if(MathUtils.isEqual(rotationAnim, 0, 0.1f))
					rotationAnim = 0;
			}
		}
		
		if(state == State.Fly)
			flyTrail.updateQueue(renderPosition);
		else
			flyTrail.removeLast();
		
		flyTrail.render(sb, sd);

		frame.setRotation(rotationAnim);
		
		frame.draw(sb);

		if (PlayScreen.debugMode)
			debug(sd);
		
		

		particles(sb);

		if(!dead)
			renderRope(sb, sd);
		
	}
	
	float counter;
	
	Image a = new Image();
	
	SequenceAction colorTrans = new SequenceAction();

	Color white = new Color(Color.WHITE);
	Color flash = new Color(232/255f, 59/255f, 59/255f, 1);

	public void flash() {

		if (stamina > maxStamina * 0.6f) {
			
			if(a.getActions().size == 0) {
				colorTrans.addAction(Actions.color(flash, 0.07f));
				colorTrans.addAction(Actions.color(white, 0.07f));
				a.addAction(colorTrans);
			}
			
			counter += Gdx.graphics.getDeltaTime();
			
			if (state == State.Climb || state == State.ClimbIdle) {
				
				//play stamina sound
				//lowStamina.stop();
				//lowStamina.play();
				
				if (counter > 0.25f) {
					effect = sweatPool.obtain();
					effect.setPosition(renderPosition.x, renderPosition.y);
					effects.add(effect);
					counter = 0;
				}
				
				
			}	
					
		}
		else if(stamina > maxStamina * 0.2f) {
			if(a.getActions().size == 0) {
				colorTrans.addAction(Actions.color(flash, 0.25f));
				colorTrans.addAction(Actions.color(white, 0.25f));
				a.addAction(colorTrans);
			}
		}
		else {
			//a.addAction(Actions.color(white, 0.1f, Interpolation.smooth));
		}	
		
		a.act(Gdx.graphics.getDeltaTime());
		
		color.set(a.getColor());
	}
	
	float walkVolume = 0.7f;
	
	boolean stop;
	
	public void sounds() {
		
		if (!stop && state == State.Climb && currentAnim.getKeyFrameIndex(stateTime) == 2) {
			climbSound.play(GameContainer.pref.getSoundVolume());
			stop = true;
		}
		else if (state == State.Climb && currentAnim.getKeyFrameIndex(stateTime) != 2)
			stop = false;

		if (state == State.Run) {
			if(currentAnim.getKeyFrameIndex(stateTime) == 2) {
				walk1.stop();
				walk1.play(walkVolume * GameContainer.pref.getSoundVolume());
			}
			if(currentAnim.getKeyFrameIndex(stateTime) == 6) {
				walk2.stop();
				walk2.play(walkVolume * GameContainer.pref.getSoundVolume());
			}
		}
		
		if (state == State.CrouchWalk) {
			if(currentAnim.getKeyFrameIndex(stateTime) == 1) {
				walk1.stop();
				walk1.play(walkVolume * 0.5f * GameContainer.pref.getSoundVolume());
			}
			if(currentAnim.getKeyFrameIndex(stateTime) == 3) {
				walk2.stop();
				walk2.play(walkVolume * 0.5f * GameContainer.pref.getSoundVolume());
			}
		}
		
	}
	
	PlayerRope rope = new PlayerRope(new Vector3(), 0.5f, 0.4f, 4, 0.25f, 100, GameSprites.color1);

	float cornerX, cornerY;

	float offX, offY;

	public void renderRope(SpriteBatch sb, ShapeDrawer sd) {
		cornerY = frame.getBoundingRectangle().y + frame.getBoundingRectangle().height;
		if (orientation) {
			cornerX = frame.getBoundingRectangle().x + frame.getBoundingRectangle().width;
		} else {
			cornerX = frame.getBoundingRectangle().x;
		}
		switch (state) {
		case ClimbIdle:
			offX = climbIdleOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), climbIdleOffset.length - 1)][0]
					* (orientation ? -1 : 1);
			offY = climbIdleOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), climbIdleOffset.length - 1)][1];	
			break;
		case Climb:
			offX = climbOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), climbOffset.length - 1)][0]
					* (orientation ? -1 : 1);
			offY = climbOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), climbOffset.length - 1)][1];	
			break;
		case CrouchWalk:
			offX = crouchWalkOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), crouchWalkOffset.length - 1)][0]
					* (orientation ? -1 : 1);
			offY = crouchWalkOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), crouchWalkOffset.length - 1)][1];	
			break;
		case CrouchIdle:
			offX = crouchIdleOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), crouchIdleOffset.length - 1)][0]
					* (orientation ? -1 : 1);
			offY = crouchIdleOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), crouchIdleOffset.length - 1)][1];	
			break;
		case Idle:
			offX = idleOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), idleOffset.length - 1)][0]
					* (orientation ? -1 : 1);
			offY = idleOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), idleOffset.length - 1)][1];			
			break;
		case Run:
			offX = runOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), runOffset.length - 1)][0]
					* (orientation ? -1 : 1);
			offY = runOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), runOffset.length - 1)][1];		
			break;
		case Jump:
			offX = jumpOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), jumpOffset.length - 1)][0]
					* (orientation ? -1 : 1);
			offY = jumpOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), jumpOffset.length - 1)][1];		
			break;
		case Fall:
			offX = fallOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), fallOffset.length - 1)][0]
					* (orientation ? -1 : 1);
			offY = fallOffset[Math.min(currentAnim.getKeyFrameIndex(stateTime), fallOffset.length - 1)][1];	
			break;
		case Fly:
			
			break;
		}
		
	
		offX *= frame.getScaleX() / PlayScreen.imageScale;
		offY *= frame.getScaleY() / PlayScreen.imageScale;
		
		rope.getTarget().set(cornerX + offX, cornerY - offY, 0);
		rope.render(sb, sd);

	}

	public void debug(ShapeDrawer sd) {
		
		
		sd.rectangle(frame.getBoundingRectangle(), Color.RED, PlayScreen.imageScale);
		renderBox(sd, Color.BLUE);
		sd.rectangle(position.x - rect.width/2, position.y-rect.height/2, rect.width, rect.height, Color.LIME, PlayScreen.imageScale);
			
		
	}
	

	boolean left, down, right, up, jumpKey, shift, control, flyKey;
	
	public void input() {

		jumpKey = Gdx.input.isKeyPressed(Keys.W);
		up = Gdx.input.isKeyPressed(Keys.W);
		down = Gdx.input.isKeyPressed(Keys.S);
		left = Gdx.input.isKeyPressed(Keys.A);
		right = Gdx.input.isKeyPressed(Keys.D);
		shift = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT);
		control = Gdx.input.isKeyPressed(Keys.CONTROL_LEFT);
		flyKey = Gdx.input.isKeyPressed(Keys.SPACE);

		if (justJump && !jumpKey)
			justJump = false;
		
		if(justLeft && !flyKey)
			justLeft = false;
		
	}
	
	public void resetInput() {
		
		jumpKey = false;
		flyKey = false;
		up = false;
		down = false;
		left = false;
		right = false;
		shift = false;
		control = false;
		canExit = false;
		
	}
	
	
	boolean resetSmoke;

	public void smoke() {
		
		if (state != State.Jump) {
			resetSmoke = false;
		}
		
		if (!resetSmoke && state == State.Jump && !coyote && accurateAir < 0.01f && !wallJump) {
			
			effect = jumpSmokePool.obtain();
			
			effect.setPosition(renderPosition.x, rect.y);
			
			effects.add(effect);
			
			resetSmoke = true;
			
		}

	}
	
	float comp, uyh;
	float sDistance = 3f;

	public void particles(SpriteBatch sb) {
		
		smoke();
		
		uyh += Gdx.graphics.getDeltaTime();
		if (ground && runTime > 0.07f && state != State.CrouchWalk) {	
			
			if (ramp) comp = 0.08f;
			else comp = 0.04f;
			
			if (uyh > comp) {
				effect = runSmokePool.obtain();
				effect.setPosition(renderPosition.x - rect.width / sDistance * (orientation ? -1 : 1) , renderPosition.y - rect.height / 2);
				effects.add(effect);
								
				uyh = 0;
			}

			
		}
		

		for (int i = effects.size - 1; i >= 0; i--) {
			effect = effects.get(i);
			effect.update(Gdx.graphics.getDeltaTime());
			effect.draw(sb, Gdx.graphics.getDeltaTime());
			if (effect.isComplete()) {
				effect.free();
				effects.removeIndex(i);
			}
		}
		
		
		
		
	}

	public void dipose() {
		runSmoke.dispose();
		//lowStamina.dispose();
		climbSound.dispose();
		jumpSound.dispose();
		impact.dispose();
		impact2.dispose();
		walk1.dispose();
		walk2.dispose();
	}
	
	Light light = new Light(100,100,1);

	public void renderLights(SpriteBatch sb) {
		light.setPos(
				(rope.getLastSeg()).curPos.x * GameContainer.alpha
						+ (rope.getLastSeg()).oldPos.x * (1.0F - GameContainer.alpha),
				(rope.getLastSeg()).curPos.y * GameContainer.alpha
						+ (rope.getLastSeg()).oldPos.y * (1.0F - GameContainer.alpha));
		light.render(sb);
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}


	public float getMaxStamina() {
		return maxStamina;
	}


	public void setMaxStamina(float maxStamina) {
		this.maxStamina = maxStamina;
	}


	public float getStamina() {
		return stamina;
	}


	public void setStamina(float stamina) {
		this.stamina = stamina;
	}

}
