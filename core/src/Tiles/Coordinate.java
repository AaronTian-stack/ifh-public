package Tiles;

import java.util.Objects;

public final class Coordinate {
	private int x;

	private int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int hashCode() {
		return Objects.hash(new Object[] { Integer.valueOf(this.x), Integer.valueOf(this.y) });
	}

	public boolean equals(Object o) {
		if (o == null)
			return false; 
		if (this == o)
			return true; 
		if (getClass() != o.getClass())
			return false; 
		Coordinate other = (Coordinate)o;
		return (this.x == other.x && this.y == other.y);
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public String toString() {
		return String.valueOf(this.x) + " " + this.y;
	}
	
	public boolean equals(Coordinate c) {
		return this.x == c.x && this.y == c.y;
	}
	
	public void set (Coordinate c) {
		this.x = c.x;
		this.y = c.y;
	}
	
	public void set(int x, int y) {
		this.x = x; this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
