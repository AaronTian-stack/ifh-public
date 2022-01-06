package Utility;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

import Screens.PlayScreen;

public class Mouse implements InputProcessor {
	private float target;

	public Mouse(OrthographicCamera camera) {
		this.target = camera.zoom;
	}

	public boolean keyDown(int keycode) {
		return false;
	}

	public boolean keyUp(int keycode) {
		return false;
	}

	public boolean keyTyped(char character) {
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	public boolean scrolled(float amountX, float amountY) {
		//System.out.println(amountY * PlayScreen.gameCamera.getMin());
		this.target -= amountY * PlayScreen.gameCamera.getMin();
		//System.out.println(target);
		return true;
	}

	public float getTarget() {
		return this.target;
	}

	public void setTarget(float target) {
		this.target = target;
	}
}
