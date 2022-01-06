package Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import Screens.GameContainer;

public class Light {
	private Color color;

	private Vector3 position = new Vector3();

	private Sprite light = new Sprite(GameContainer.atlas.findRegion("softlight"));

	private Vector2 scale = new Vector2();

	float time;

	public Light(float x, float y, float scale) {
		this.position.x = x;
		this.position.y = y;
		this.color = Color.WHITE;
		this.scale.set(scale / 6f, scale / 6f);
	}

	public void render(SpriteBatch sb) {
		sb.setColor(this.color);
		light.setScale(scale.x, scale.y);
		light.setBounds(0, 0, light.getRegionWidth(), light.getRegionHeight());
		light.setCenter(position.x, position.y);
		light.setOrigin(light.getWidth() / 2.0F, light.getHeight() / 2.0F);
		light.draw(sb);
		sb.setColor(Color.WHITE);
	}

	public void setPos(float x, float y) {
		this.position.x = x;
		this.position.y = y;
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Vector3 getPosition() {
		return this.position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}
}
