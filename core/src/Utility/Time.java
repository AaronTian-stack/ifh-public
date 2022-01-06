package Utility;

import Screens.PlayScreen;
import com.badlogic.gdx.graphics.Color;
import java.util.Calendar;

public class Time {
	private int hour;

	private int min;

	private Calendar cal;

	public Time(Calendar cal) {
		this.cal = cal;
		cal.set(11, 12);
		cal.set(12, 0);
		this.hour = 0;
		this.min = 0;
	}

	public int getHour() {
		return this.hour;
	}

	public int getMin() {
		return this.min;
	}

	static int[] T = new int[2];

	public void getTime(int scale) {
		this.cal.setTimeInMillis(PlayScreen.sky.getTimeLong() * scale);
		this.hour = this.cal.get(11);
		this.min = this.cal.get(12);
	}

	static Color c = Color.WHITE;

	public Color getTint() {
		double z = Math.cos((this.hour - 14) * Math.PI / 12.0D);
		float b = (float)(0.30000001192092896D + 0.699999988079071D * (z + 1.0D) / 2.0D);
		c.set(b, b, b, 1.0F);
		return c;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public Calendar getCal() {
		return this.cal;
	}

	public void setCal(Calendar cal) {
		this.cal = cal;
	}
}
