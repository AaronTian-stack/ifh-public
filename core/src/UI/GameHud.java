package UI;

import Screens.GameContainer;
import Screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.text.DecimalFormat;

public class GameHud {
	public Stage stage;

	private Viewport viewport;

	private BitmapFont font;

	DecimalFormat f;

	Label chunkLabel;

	Label timeLabel;

	Label cameraLabel;

	Label draw;

	Label playervelocity;

	Label playerpos;

	Label framerate;
	
	Label stamina;
	
	Label parent;

	float fontscale = 0.5f;

	public GameHud(SpriteBatch sb) {
		f = new DecimalFormat("##.0000");
		viewport = new ExtendViewport(GameContainer.WIDTH, GameContainer.HEIGHT);
		stage = new Stage(viewport, sb);
		Table table = new Table();
		table.setFillParent(true);
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("UI/LanaPixel.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.color = Color.WHITE;
		fontParameter.borderColor = Color.BLACK;
		fontParameter.borderWidth = 0.8f;
		fontParameter.size = 12;
		font = fontGenerator.generateFont(fontParameter);
		font.setUseIntegerPositions(false);
		framerate = new Label("", new Label.LabelStyle(font, font.getColor()));
		timeLabel = new Label("", new Label.LabelStyle(font, font.getColor()));
		chunkLabel = new Label("", new Label.LabelStyle(font, font.getColor()));
		cameraLabel = new Label("", new Label.LabelStyle(font, font.getColor()));
		parent = new Label("", new Label.LabelStyle(font, font.getColor()));
		draw = new Label("", new Label.LabelStyle(font, font.getColor()));
		playervelocity = new Label("", new Label.LabelStyle(font, font.getColor()));
		playerpos = new Label("", new Label.LabelStyle(font, font.getColor()));
		stamina = new Label("", new Label.LabelStyle(font, font.getColor()));
		framerate.setFontScale(fontscale);
		chunkLabel.setFontScale(fontscale);
		timeLabel.setFontScale(fontscale);
		parent.setFontScale(fontscale);
		cameraLabel.setFontScale(fontscale);
		draw.setFontScale(fontscale);
		playervelocity.setFontScale(fontscale);
		playerpos.setFontScale(fontscale);
		stamina.setFontScale(fontscale);
		timeLabel.setWrap(true);
		table.add(framerate).align(Align.left).row();
		
		table.add(chunkLabel).align(Align.left).row();
		
		table.add(cameraLabel).align(Align.left).row();
		
		table.add(timeLabel).align(Align.left).row();
		
		table.add(playervelocity).align(Align.left).row();
		
		table.add(parent).align(Align.left).row();

		table.add(playerpos).align(Align.left).row();
		
		table.add(draw).align(Align.left).row();
		
		table.add(stamina).align(Align.left).row();
		stage.addActor(table);
		table.left().top();
		fontGenerator.dispose();
	}
	
	//StringBuilder rendered = new StringBuilder("Rendered chunk(s)");

	public void update(int chunks) {

		if (PlayScreen.editMode)
			chunkLabel.setText("Rendered " + chunks + " chunk(s)"); 
		timeLabel.setText("Time: " + PlayScreen.sky.getTime().getCal().get(11) + " : " + PlayScreen.sky.getTime().getCal().get(12));
		cameraLabel.setText("Camera Zoom: " + f.format(PlayScreen.gameCamera.getCamera().zoom) + " Camera Pos: " + PlayScreen.gameCamera.getCamera().position.toString());
		draw.setText(String.valueOf(GameContainer.batch.renderCalls) + " draw calls");
		playervelocity.setText("Velocity: " + PlayScreen.player.getVelocity().toString() + " and " + PlayScreen.player.getState());
		playerpos.setText("Position: " + PlayScreen.player.getPosition().toString());
		framerate.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
		stamina.setText("Stamina: " + PlayScreen.player.getStamina() + " / " + PlayScreen.player.getMaxStamina());
		parent.setText("ParentV: " + PlayScreen.player.getParentVelocity());
	}
	
	public void render() {
		stage.act();
		//stage.getViewport().apply();
		stage.draw();
	}

	public void dispose() {
		font.dispose();
		stage.dispose();
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
