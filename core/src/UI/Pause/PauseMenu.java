package UI.Pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Game.ropes.PauseRope;
import Game.ropes.VerletRope;
import Screens.GameContainer;
import Screens.PauseScreen;
import Screens.PlayScreen;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class PauseMenu {
	
	GameContainer game;
	
	public Stage stage;
	
	private Viewport viewport;
	
	float gravity = -1f;
	
	float ropeSegLen = 8.8f; 
	
	int segmentLen = 8;
	
	float width = 2f;
	
	int constraint = 40; 
	
	Color color = Color.BLACK;
	
	/*
	PauseRope letterP = new PauseRope(new Vector3(GameContainer.WIDTH * 0.25f,GameContainer.HEIGHT, 0), gravity, ropeSegLen, segmentLen, width, constraint, color, GameContainer.atlas.findRegion("letterP"));
	PauseRope letterA = new PauseRope(new Vector3(GameContainer.WIDTH * 0.35f,GameContainer.HEIGHT, 0), gravity, ropeSegLen, segmentLen, width, constraint, color, GameContainer.atlas.findRegion("letterA"));
	PauseRope letterU = new PauseRope(new Vector3(GameContainer.WIDTH * 0.45f,GameContainer.HEIGHT, 0), gravity, ropeSegLen, segmentLen, width, constraint, color, GameContainer.atlas.findRegion("letterU"));
	PauseRope letterS = new PauseRope(new Vector3(GameContainer.WIDTH * 0.55f,GameContainer.HEIGHT, 0), gravity, ropeSegLen, segmentLen, width, constraint, color, GameContainer.atlas.findRegion("letterS"));
	PauseRope letterE = new PauseRope(new Vector3(GameContainer.WIDTH * 0.65f,GameContainer.HEIGHT, 0), gravity, ropeSegLen, segmentLen, width, constraint, color, GameContainer.atlas.findRegion("letterE"));
	PauseRope letterD = new PauseRope(new Vector3(GameContainer.WIDTH * 0.75f,GameContainer.HEIGHT, 0), gravity, ropeSegLen, segmentLen, width, constraint, color, GameContainer.atlas.findRegion("letterD"));
	*/
	
	public InputMultiplexer inputMultiplexer = new InputMultiplexer();
	
	NinePatch patch = new NinePatch(new TextureRegion(GameContainer.atlas.findRegion("base")), 2, 2, 2, 6);
	
	NinePatchDrawable patchD = new NinePatchDrawable(patch);;
	
	TextButton escape;
	
	private Menu menu;
	
	Video video;
	private Settings settings;
	Audio audio;
	
	Warning videoWarn;
	
	Warning exitWarn;
	
	public PauseMenu(SpriteBatch sb) {
		
		viewport = new StretchViewport(GameContainer.WIDTH * 2, GameContainer.HEIGHT * 2);
		
		//viewport = new ScreenViewport();
		
		video = new Video(viewport, sb, this, patchD);
		
		settings = new Settings(viewport, sb, this, patchD);
		
		audio = new Audio(viewport, sb, this, patchD);
		
		ChangeListener Vyes = new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				menu = settings;
				hide();
				//transition();
			}
		};
		
		ChangeListener Vno = new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				menu = video;
				transition();
			}
		};
		
		ChangeListener Eyes = new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		};
		
		ChangeListener Eno = new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				menu = settings;
				hide();
				
			}
		};
		
		videoWarn = new Warning("You have unsaved changes. Exit anyway?", Vyes, Vno, viewport, sb, this, patchD);
		
		exitWarn = new Warning("Exit application?", Eyes, Eno, viewport, sb, this, patchD);
		
		menu = settings;
		
		hide();
		
	}
	
	public void transition() {
		stage = menu.stage;
		stage.getRoot().getActions().clear();
		stage.getRoot().setPosition(0, -50);
		stage.getRoot().addAction(Actions.moveTo(0, 0, 0.4f, Interpolation.swingOut));
		stage.getRoot().getColor().a = 0;
		stage.getRoot().addAction(Actions.fadeIn(0.2f, Interpolation.smooth2));
	}

	
	public void update(float dt) {
		if(menu == settings) {
			/*letterP.update(dt);
			letterA.update(dt);
			letterU.update(dt);
			letterS.update(dt);
			letterE.update(dt);
			letterD.update(dt);*/
		}
	}
	
	public void render(SpriteBatch sb, ShapeDrawer sd) {
		
		inputMultiplexer.setProcessors(stage);
		
		Gdx.input.setInputProcessor(inputMultiplexer);

		reset();
		stage.act();

		stage.draw();

		if(menu == settings) {
			
			sb.setProjectionMatrix(game.Camera.combined);

			sb.begin();

			/*letterP.render(sb, sd);
			letterU.render(sb, sd);
			letterE.render(sb, sd);
			letterA.render(sb, sd);
			letterS.render(sb, sd);
			letterD.render(sb, sd);*/
			
			sb.end();

			
		}


	}
	
	public void reset() {
		
		menu.update();
		stage = menu.stage;
		
	}
	
	public void show() {
		
		settings.show();
		
		stage.getRoot().addAction(Actions.fadeIn(0.5f, Interpolation.smooth2));
		
		stage.getRoot().addAction(Actions.moveTo(0, 0, 0.5f, Interpolation.swingOut));
	}

	public void hide() {
		/*letterP.reset();
		letterA.reset();
		letterU.reset();
		letterS.reset();
		letterE.reset();
		letterD.reset();*/
		
		menu = settings;
		
		transition();
	}
	
	public void dispose() {
		settings.dispose();
		video.dispose();
		audio.dispose();
		videoWarn.dispose();
		exitWarn.dispose();
	}

	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	public void setGame(GameContainer game) {
		this.game = game;
		
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Settings getSettings() {
		return settings;
	}

}
