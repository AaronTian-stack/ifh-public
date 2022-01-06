package Screens;

import UI.GameHud;
import UI.LevelEditorUI;
import Utility.Mouse;
import Game.Box;
import Game.MovingPlatform;
import Game.Player;
import Game.Sky;

import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import Game.abstraction.GameObject;
import Game.camera.GameCamera;
import Game.gib.GibManager;
import Game.level.Level;
import Game.ropes.VerletRope;
import Game.shared.GameSprites;
import Tiles.Enums.GameObjectType;

public class PlayScreen implements Screen {
	private GameContainer game;

	//public static ShaderProgram outlineShader;

	private GameHud GameHud = new GameHud(GameContainer.batch);

	public static LevelEditorUI LevelEditor = new LevelEditorUI(GameContainer.batch);

	public static boolean editMode = true;

	public static boolean debugMode = false;

	public static boolean lighting = false;

	public static Level level = new Level();

	public static Player player;

	public static Mouse mouse;

	public static final float imageScale = 0.25f;

	public InputMultiplexer inputMultiplexer;

	Vector3 diff = new Vector3();

	public static Sky sky = new Sky();
	
	public static GameCamera gameCamera;

	public PlayScreen(GameContainer game) {

		inputMultiplexer = new InputMultiplexer();

		this.game = game;
		
		gameCamera = new GameCamera();

		fadeActor.setColor(0,0,0,0);
		
		player = new Player();

		gameCamera.setTarget(player);
		
		mouse = new Mouse(gameCamera.getCamera());
		
		gameCamera.setMouse(mouse);

		inputMultiplexer.addProcessor(mouse);
		inputMultiplexer.addProcessor(LevelEditor.stage);

		GameSprites.init();
		
		//gameObjects.add(player);
		
		//outlineShader = new ShaderProgram(Gdx.files.internal("Shaders/outlineVertex.glsl"), Gdx.files.internal("Shaders/outlineFragment.glsl"));
		//ShaderProgram.pedantic = false;
		//if (!outlineShader.isCompiled())
		//	throw new GdxRuntimeException("Couldn't compile shader: " + outlineShader.getLog()); 

		
	}

	public static GibManager gibManager = new GibManager();
	
	public static Array<GameObject> gameObjects = new Array<GameObject>(); // TODO: ADD POOLS FOR ALL gameObjects

	public static Pool<MovingPlatform> boxPool = Pools.get(MovingPlatform.class);

	public static Array<GameObject> rendered = new Array<GameObject>();

	public void update(float dt) {

		level.update(dt);
		LevelEditor.update();

		for (GameObject gameOb : gameObjects) {
			gameOb.addPoints();
		}
		
		gibManager.update(dt);


		for (GameObject gameOb : gameObjects) {
			gameOb.update(dt);
		}
		
		player.update(dt);

		
	}

	public static Image fadeActor = new Image(new TextureRegion(GameContainer.atlas.findRegion("white")));

	public static Vector3 clickPos = new Vector3();
	public static Vector3 oldClickPos = new Vector3();

	public static float stageScale = 0.05f;

	public static GameObject dragging;

	public void render(float alpha) {
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
		//Gdx.gl.glEnable(GL20.GL_BLEND);
		//Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.input.setInputProcessor(inputMultiplexer);
		
		gameCamera.render();

		oldClickPos.set(clickPos);
		
		clickPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		
		gameCamera.getCamera().unproject(clickPos);

		sky.update();
		
		if(!Gdx.input.isButtonPressed(Buttons.LEFT))
			dragging = null;

		renderGame();

		if (debugMode) {
			GameHud.update(level.getCount());
			GameHud.render();
			
		} 
		
		LevelEditor.render();

		if (Gdx.input.isKeyJustPressed(Keys.E)) {
			editMode = !editMode; 
			LevelEditor.move();
			for (GameObject gameOb : gameObjects) {
				gameOb.editReset();
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.L))
			lighting = !lighting; 
		if (Gdx.input.isKeyJustPressed(Keys.M))
			debugMode = !debugMode; 

		GameContainer.batch.begin();

		fadeActor.act(Gdx.graphics.getDeltaTime());
		
		fadeActor.draw(GameContainer.batch, 1);
		
		GameContainer.batch.end();

	}

	public void renderGame() {
		
		//rendered.clear();
		game.gameBuffer.begin();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		//Gdx.gl.glEnable(GL20.GL_BLEND);
		//Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		Gdx.gl.glClearColor(sky.getCurrentSkyColor().r, sky.getCurrentSkyColor().g,
				sky.getCurrentSkyColor().b, sky.getCurrentSkyColor().a);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		GameContainer.batch.setProjectionMatrix(gameCamera.getCamera().combined);
		GameContainer.shape.update();
		//GameContainer.batch.enableBlending();
		GameContainer.batch.begin();

		level.render(GameContainer.batch, GameContainer.shape);
		
		rendered.sort();
		
		for(GameObject g : rendered)
			g.render(GameContainer.batch, GameContainer.shape);
		
		rendered.clear();
		
		player.render(GameContainer.batch, GameContainer.shape);
		
		gibManager.render(GameContainer.batch, GameContainer.shape);
		
		GameContainer.batch.end();
		game.gameBuffer.end(gameCamera.getViewPort().getScreenX(),gameCamera.getViewPort().getScreenY(),
				gameCamera.getViewPort().getScreenWidth(),gameCamera.getViewPort().getScreenHeight());
		
		renderFrame(true);
	}
	
	public void renderFrame(boolean input) {
		if(!input)
			Gdx.input.setInputProcessor(null);
		GameContainer.batch.setProjectionMatrix(game.Camera.combined);
		GameContainer.batch.begin();
		GameContainer.batch.draw(game.gameBufferRegion, 0, 0, GameContainer.WIDTH, GameContainer.HEIGHT);
		GameContainer.batch.end();
		if (lighting)
			renderFBO(); 
	}

	int oldS, oldD;
	
	public void renderFBO() {
		oldS = GameContainer.batch.getBlendSrcFunc();
		oldD = GameContainer.batch.getBlendDstFunc();
		game.lightBuffer.begin();
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glClearColor(sky.getAmbientColor().r, sky.getAmbientColor().g,
				sky.getAmbientColor().b, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		GameContainer.batch.setProjectionMatrix(gameCamera.getCamera().combined);
		GameContainer.batch.begin();
		player.renderLights(GameContainer.batch);
		GameContainer.batch.end();
		game.lightBuffer.end();
		GameContainer.batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
		GameContainer.batch.setProjectionMatrix(game.Camera.combined);
		GameContainer.batch.begin();
		GameContainer.batch.draw(game.lightBufferRegion, 0, 0, GameContainer.WIDTH, GameContainer.HEIGHT); //LIGHTBUFFERREGION
		GameContainer.batch.setBlendFunction(oldS, oldD);
		GameContainer.batch.end();
	}

	public void resize(int width, int height) {
		
		gameCamera.resize(width, height);
		GameHud.resize(width, height);
		LevelEditor.resize(width, height);
		fadeActor.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//stage.getViewport().update(width, height, true);
		
		
	}

	public void pause() {
		
	}

	public void resume() {
		
	}
	
	boolean wasLight;

	public void show() {
		lighting = wasLight;
		GameContainer.fadeActor.addAction(Actions.fadeOut(0.5f));
	}

	SequenceAction sequenceAction = new SequenceAction();
	public void switchScreen() {
		//sequenceAction.addAction(Actions.fadeIn(0.5f));
	    //sequenceAction.addAction(Actions.delay(1f));
	    sequenceAction.addAction(Actions.run(new Runnable() {
	       @Override
	        public void run() {
	            game.setScreen(game.pause);
	        }
	    }));
	    GameContainer.fadeActor.addAction(sequenceAction);
	    wasLight = lighting;
	    lighting = true;
	}
	
	public void hide() {
		
	}

	public void dispose() {
		level.dispose();
		player.dispose();
		GameHud.dispose();
		LevelEditor.dispose();
	}


}

class SortbyType implements Comparator<GameObject> {

	@Override
	public int compare(GameObject arg0, GameObject arg1) { // return 1 if arg0 greater
		if(arg0.getType() == GameObjectType.Actor && arg1.getType() == GameObjectType.Solid)
			return -1;
		if(arg1.getType() == GameObjectType.Actor && arg0.getType() == GameObjectType.Solid)
			return 1;
		return 0;
	}
}
