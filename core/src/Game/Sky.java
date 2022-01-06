package Game;

import java.util.GregorianCalendar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import Utility.Time;

public class Sky { // Manages day night cycle
	
	private Color currentSkyColor = new Color();

	private Color DaySkyColor = new Color(143/255f, 215/255f, 1f, 1f);

	private Color NightSkyColor = new Color(0.28235295f, 0.2901961f, 0.46666667f, 1f);

	private Color AmbientColor = new Color();

	private Color AmbientDay = new Color(1f, 1f, 1f, 1f);

	private Color AmbientNight = new Color(0.05f, 0.05f, 0.05f, 1f);
	
	private Time time = new Time(new GregorianCalendar());
	
	private int timeScale = 360;

	private long timeLong = (61206480 / timeScale);
	
	private Vector3 oh = new Vector3(); //interpolation

	private Vector3 th = new Vector3();
	
	public Sky() {
		
	}
	
	public void update() {
		timeCycle();
	}
	
	public void timeCycle() {
		/*if (Gdx.input.isKeyPressed((Keys.UP)))
			timeScale = (int)(timeScale + Gdx.graphics.getDeltaTime() * 144); 
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			timeScale = (int)(timeScale - Gdx.graphics.getDeltaTime() * 144);*/ // TODO: Add some other way to adjust time in game
		timeLong = (long)(timeLong + Gdx.graphics.getDeltaTime() * 1000);
		time.getTime(timeScale);
		if (time.getHour() >= 12) {
			float fx = (time.getHour() + time.getMin() / 60f - 12) / 12;
			currentSkyColor.set(DaySkyColor);
			AmbientColor.set(AmbientDay);
			interpolate(currentSkyColor, NightSkyColor, fx, Interpolation.pow3In);
			interpolate(AmbientColor, AmbientNight, fx, Interpolation.pow3In);
		} else {
			float fx = (time.getHour() + time.getMin() / 60f) / 12;
			currentSkyColor.set(NightSkyColor);
			AmbientColor.set(AmbientNight);
			interpolate(currentSkyColor, DaySkyColor, fx, Interpolation.slowFast);
			interpolate(AmbientColor, AmbientDay, fx, Interpolation.slowFast);
		} 
	}
	
	public void interpolate(Color og, Color target, float fx, Interpolation i) {
		oh.set(og.r, og.g, og.b);
		th.set(target.r, target.g, target.b);
		oh.interpolate(th, fx, i);
		og.set(oh.x, oh.y, oh.z, 1f);
	}

	public Color getCurrentSkyColor() {
		return currentSkyColor;
	}

	public void setCurrentSkyColor(Color currentSkyColor) {
		this.currentSkyColor = currentSkyColor;
	}

	public int getTimeScale() {
		return timeScale;
	}

	public void setTimeScale(int timeScale) {
		this.timeScale = timeScale;
	}

	public Color getAmbientColor() {
		return AmbientColor;
	}

	public void setAmbientColor(Color ambientColor) {
		AmbientColor = ambientColor;
	}

	public long getTimeLong() {
		return timeLong;
	}

	public void setTimeLong(long timeLong) {
		this.timeLong = timeLong;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

}
