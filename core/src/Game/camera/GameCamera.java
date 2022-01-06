package Game.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Game.abstraction.GameObject;
import Screens.GameContainer;
import Screens.PlayScreen;
import Utility.Mouse;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class GameCamera { // Manages camera and viewports
	
	private OrthographicCamera camera;

	private Viewport viewPort;
	
	private Vector3 position;
	
	private float viewW, viewH;
	
	private float cameraZoom;

	private float speed = 0.1f;
	
	private float scrollSpeed = 0.1f;
	
	private Mouse mouse;
	
	private float min = 0.5f; // 0.5 - 0.0625
	
	float max;
	float maxO = 1f; // 0.25 - 1
	float maxE = 1f; // 0.25 - 1

	public GameCamera() {
		position = new Vector3();

		camera = new OrthographicCamera();

		int scale = 4;
		
		//viewPort = new ExtendViewport(GameContainer.WIDTH / scale, GameContainer.HEIGHT / scale, camera); // in WORLD UNITS try to divide by factor of 4
		
		//may be better to use strechviewport and just adjust container settings instead if there are locked resolutions
		
		viewPort = new StretchViewport(GameContainer.WIDTH / scale, GameContainer.HEIGHT / scale, camera); // in WORLD UNITS try to divide by factor of 4
		
		//viewPort = new FitViewport(1920, 1080, camera);
		
		camera.setToOrtho(false, viewPort.getScreenWidth(), viewPort.getScreenHeight());

		camera.zoom = maxO;
		
	}
	
	private GameObject target; // the thing the camera will track when action queue is empty
	
	private Pool<CameraAction> actionPool = Pools.get(CameraAction.class);
	
	private Array<CameraAction> actionQueue = new Array<CameraAction>(); // should only track static objects. Might mess up if the target is moving.
	CameraAction action, lastAction;
	float timer;
	
	Vector3 targetPosition = new Vector3();
	
	public void render() {

		if (PlayScreen.editMode) {
			max = maxE;
		} else {
			max = maxO;
		} 
		
		if(actionQueue.size > 0) {
			
			timer += Gdx.graphics.getDeltaTime();

			lastAction = action;
			
			action = actionQueue.get(0);
			
			if(lastAction != action) {
				action.setStartZoom(camera.zoom);
				action.setStartPosition(camera.position);
				if (action.getAction() != null)
					PlayScreen.fadeActor.addAction(action.getAction());
				
			}
			
			
			position.set(action.getStartPosition());
			
			position.interpolate(target.getRenderPosition(), Math.min(1, timer / action.getMoveDuration()), action.getMoveInterpolation());

			camera.zoom = action.getZoomInterpolation().apply(action.getStartZoom(), action.getZoom(), Math.min(1, timer / action.getZoomDuration()));
			
			if(timer > Math.max(action.getMoveDuration(), action.getZoomDuration()) && GameContainer.fadeActor.getActions().size == 0) {
				
				timer = 0;
				actionQueue.removeIndex(0);
				actionPool.free(action);
				
			}

			
		}
		
		else {
			//timer = 0;
			position.interpolate(target.getRenderPosition(), speed * Gdx.graphics.getDeltaTime() * 144, Interpolation.linear);
			
			camera.rotate(MathUtils.lerp(getCameraRotation() - 180, 0, 0.1f));
		}

		if(shakeTimer < shakeDuration) {
			
			camera.rotate(MathUtils.random(-1, 1) * shakeAngle * Gdx.graphics.getDeltaTime() * 144 * ((shakeDuration - shakeTimer) / shakeDuration));
			
			shakeTimer += Gdx.graphics.getDeltaTime();
			
			float bruh = shakeScale * Gdx.graphics.getDeltaTime() * 144 * ((shakeDuration - shakeTimer) / shakeDuration);
			
			position.add(MathUtils.random(-1, 1) * bruh, MathUtils.random(-1, 1) * bruh, 0);

		}
		
		zoom();

		camera.position.set(position);
		
		clamp();
		
		camera.update();
		
		if(Gdx.input.isKeyJustPressed(Keys.F))
			shake(1f, 1, 1);
		
	}
	
	public void zoom() { // manages camera zoom and also interpolates zoom
		
		if(actionQueue.size == 0) {
			
			if (mouse.getTarget() < min) {
				mouse.setTarget(min);
				//camera.zoom = mouse.getTarget();
			} 
			if (mouse.getTarget() > max) {
				mouse.setTarget(max);
				//camera.zoom = mouse.getTarget();
			} 

			camera.zoom = MathUtils.lerp(camera.zoom, mouse.getTarget(), scrollSpeed * Gdx.graphics.getDeltaTime() * 144);

			
			if(MathUtils.isEqual(mouse.getTarget(), camera.zoom, 0.001f))
				camera.zoom = mouse.getTarget(); 
		}

		viewW = camera.viewportWidth * camera.zoom;
		viewH = camera.viewportHeight * camera.zoom;
		
	}
	
	public void clamp() {
		camera.position.x = MathUtils.clamp(camera.position.x, viewW / 2f, 1.0E9F);
		camera.position.y = MathUtils.clamp(camera.position.y, viewH / 2f, 1.0E9F);
	}

	public CameraAction createAction() {
		return actionPool.obtain();
	}

	float shakeScale;
	float shakeAngle;
	float shakeDuration;
	float shakeTimer = 999;

	public void shake(float angle, float rotation, float duration) {
		shakeScale = angle;
		shakeAngle = rotation;
		shakeDuration = duration;
		shakeTimer = 0;
	}
	
	public float getCameraRotation()
	{                       
	     return -(float)Math.atan2(camera.up.x, camera.up.y)*MathUtils.radiansToDegrees + 180;    
	     
	}
	
	public void resize(int width, int height) {
		viewPort.update(width, height, true);
	}

	public void dipose() {
		
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public Viewport getViewPort() {
		return viewPort;
	}

	public void setViewPort(Viewport viewPort) {
		this.viewPort = viewPort;
	}

	public float getCameraZoom() {
		return cameraZoom;
	}

	public void setCameraZoom(float cameraZoom) {
		this.cameraZoom = cameraZoom;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Mouse getMouse() {
		return mouse;
	}

	public void setMouse(Mouse mouse) {
		this.mouse = mouse;
	}

	public GameObject getTarget() {
		return target;
	}

	public void setTarget(GameObject target) {
		this.target = target;
	}

	public Array<CameraAction> getActionQueue() {
		return actionQueue;
	}

	public void setActionQueue(Array<CameraAction> actionQueue) {
		this.actionQueue = actionQueue;
	}

	public Pool<CameraAction> getActionPool() {
		return actionPool;
	}

	public void setActionPool(Pool<CameraAction> actionPool) {
		this.actionPool = actionPool;
	}

	public float getMaxE() {
		return maxE;
	}

	public float getMin() {
		return min;
	}

}
