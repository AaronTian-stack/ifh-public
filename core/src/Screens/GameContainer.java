package Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import Game.shared.Assets;
import Game.AppPreferences;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class GameContainer extends Game {
	
	public static SpriteBatch batch;

	public static ShapeDrawer shape;

	public static int WIDTH;

	public static int HEIGHT;

	public static TextureAtlas atlas;

	public FrameBuffer lightBuffer;

	public TextureRegion lightBufferRegion;

	public FrameBuffer gameBuffer;

	public TextureRegion gameBufferRegion;
	
	public FrameBuffer pauseBuffer;

	public TextureRegion pauseBufferRegion;

	public PlayScreen play;
	
	public LoadingScreen load;
	
	public PauseScreen pause;
	
	public static Assets assets;
	
	public static Skin skin;

	public static Image fadeActor;
	
	public OrthographicCamera Camera = new OrthographicCamera();
	
	public static AppPreferences pref = new AppPreferences();
	
	public void create() {
		
		batch = new SpriteBatch();
		WIDTH = 512;
		HEIGHT = 288;
		lightBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, WIDTH, HEIGHT, false);
		gameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, WIDTH, HEIGHT, false);

		assets = new Assets();
		
		assets.load();
		
		assets.manager.finishLoading();
		
		System.out.println("FINISHED LOADING ATLAS");
		
		atlas = assets.manager.get(assets.getAtlas());

		shape = new ShapeDrawer(batch, atlas.findRegion("white"));
		
		skin = assets.manager.get(assets.getSkin());

		load = new LoadingScreen(this);

		fadeActor = new Image(new TextureRegion(GameContainer.atlas.findRegion("white")));
		
		fadeActor.setColor(Color.BLACK); 
		
		Camera.setToOrtho(false, GameContainer.WIDTH, GameContainer.HEIGHT);

		setScreen(load);
		
		init();

		// play and pause screens are created in initial load
		
	}
	
	public void init() {

		Preferences prefs = Gdx.app.getPreferences("ifhPref");
		
		switch(prefs.getInteger("display", 0)) {
		case 0:
			switch(prefs.getInteger("resolution", 2)) {
			case 0: // 1024×576
				Gdx.graphics.setWindowedMode(1024, 576);
				break;
			case 1: // 1152×648
				Gdx.graphics.setWindowedMode(1152, 648);
				break;
			case 2: // 1280 720
				Gdx.graphics.setWindowedMode(1280, 720);
				break;
			case 3: // 1366×768
				Gdx.graphics.setWindowedMode(1366, 768);
				break;
			case 4: // 1600×900
				Gdx.graphics.setWindowedMode(1600, 900);
				break;
			case 5: // 1920×1080
				Gdx.graphics.setWindowedMode(1920, 1080);
				break;
			case 6: // 2560×1440
				Gdx.graphics.setWindowedMode(2560, 1440);
				break;
			case 7: // 3840×2160
				Gdx.graphics.setWindowedMode(3840, 2160);
				break;
			}
			break;
		case 1:
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			break;
		}
	}

	float accum;

	public static float alpha;

	public static float dt = 1/30f;

	int updates = 0;
	
	boolean trigger;

	public void render() {

		if (screen == play) {
			update();
			if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
					play.switchScreen();
			
		}
		if(screen == load) {
			load.render(Gdx.graphics.getDeltaTime());
		}
		if(screen == pause) {
			update();
		}
		

		super.render();	
		
		batch.begin();

		fadeActor.act(Gdx.graphics.getDeltaTime());

		fadeActor.draw(batch, 1);
		
		batch.end();
		
	}
	
	public void update() {
		accum += Gdx.graphics.getDeltaTime();
		while (accum >= dt) {
			if(screen == play)
				play.update(dt);
			if(screen == pause)
				pause.update(dt);
			accum -= dt;
		} 
		alpha = accum / dt;
	}

	public void resize(int width, int height) {

		this.screen.resize(width, height);
		
		fadeActor.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		
		if (lightBuffer != null)
			lightBuffer.dispose(); 
		
		lightBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, GameContainer.WIDTH, GameContainer.HEIGHT, false);
		lightBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);
		lightBufferRegion = new TextureRegion(lightBuffer.getColorBufferTexture(), 0, 0, GameContainer.WIDTH, GameContainer.HEIGHT);
		lightBufferRegion.flip(false, true);
		
		if(screen != pause) { // does not reflect new ratio on pause screen though
			if (gameBuffer != null)
				gameBuffer.dispose(); 
			
			gameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, GameContainer.WIDTH, GameContainer.HEIGHT, false);
			gameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);
			gameBufferRegion = new TextureRegion(gameBuffer.getColorBufferTexture(), 0, 0, GameContainer.WIDTH, GameContainer.HEIGHT);
			gameBufferRegion.flip(false, true);
			
			if (pauseBuffer != null)
				pauseBuffer.dispose(); 
			
		}	
		
		int scale = 2;
		
		pauseBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, GameContainer.WIDTH * scale, GameContainer.HEIGHT * scale, false);
		pauseBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);
		pauseBufferRegion = new TextureRegion(pauseBuffer.getColorBufferTexture(), 0, 0, GameContainer.WIDTH * scale, GameContainer.HEIGHT * scale);
		pauseBufferRegion.flip(false, true);
		
	}

	public void pause() {
		
	}

	public void dispose() {
		batch.dispose();
		atlas.dispose();
		if(play != null)
			play.dispose();
		load.dispose();
		if(pause != null)
			pause.dispose();
		lightBuffer.dispose();
		gameBuffer.dispose();
		assets.dispose();
	}

	
}
