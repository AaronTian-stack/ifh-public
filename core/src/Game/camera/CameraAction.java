package Game.camera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;

import Screens.PlayScreen;

public class CameraAction {

	private Interpolation moveInterpolation, zoomInterpolation;
	
	private float moveDuration, zoomDuration;
	
	private float zoom, startZoom;
	
	private Vector3 startPosition;
	
	private Action action; // optional action

	public CameraAction() {

		startPosition = new Vector3();
		
	}
	
	public void set(float moveDuration, float zoomDuration, float zoom, Interpolation moveInterpolation, Interpolation zoomInterpolation) {
		
		this.moveInterpolation = moveInterpolation;
		this.moveDuration = moveDuration;
		this.zoomDuration = zoomDuration;
		this.zoom = zoom;
		this.zoomInterpolation = zoomInterpolation;
		this.startZoom = PlayScreen.gameCamera.getCamera().zoom;
		this.startPosition.set(PlayScreen.gameCamera.getCamera().position);
		
	}
	
	public void set(float moveDuration, float zoomDuration, float zoom, Interpolation moveInterpolation, Interpolation zoomInterpolation, Action action) {
		
		this.moveInterpolation = moveInterpolation;
		this.moveDuration = moveDuration;
		this.zoomDuration = zoomDuration;
		this.zoom = zoom;
		this.zoomInterpolation = zoomInterpolation;
		this.action = action;
		this.startZoom = PlayScreen.gameCamera.getCamera().zoom;
		this.startPosition.set(PlayScreen.gameCamera.getCamera().position);
		
	}

	public Interpolation getMoveInterpolation() {
		return moveInterpolation;
	}

	public void setMoveInterpolation(Interpolation moveInterpolation) {
		this.moveInterpolation = moveInterpolation;
	}

	public Interpolation getZoomInterpolation() {
		return zoomInterpolation;
	}

	public void setZoomInterpolation(Interpolation zoomInterpolation) {
		this.zoomInterpolation = zoomInterpolation;
	}

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	public float getMoveDuration() {
		return moveDuration;
	}

	public void setMoveDuration(float moveDuration) {
		this.moveDuration = moveDuration;
	}

	public float getZoomDuration() {
		return zoomDuration;
	}

	public void setZoomDuration(float zoomDuration) {
		this.zoomDuration = zoomDuration;
	}

	public float getStartZoom() {
		return startZoom;
	}

	public void setStartZoom(float startZoom) {
		this.startZoom = startZoom;
	}

	public Vector3 getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(Vector3 startPosition) {
		this.startPosition.set(startPosition);
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

}
