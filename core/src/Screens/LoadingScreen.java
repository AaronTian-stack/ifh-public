package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen implements Screen {

	GameContainer game;

	public LoadingScreen(GameContainer game) {
		this.game = game;

		camera.setToOrtho(false, viewport.getScreenWidth(), viewport.getScreenHeight());
		stage = new Stage(viewport);

		logo.setOrigin(Align.center);
		
		Table table = new Table();
		table.setFillParent(true);
		
		table.add(logo);

		SequenceAction s = new SequenceAction();
		
		s.addAction(Actions.scaleTo(0, 0));
		
		s.addAction(Actions.delay(2f, Actions.scaleTo(1, 1, 2, Interpolation.elasticOut)));

		s.addAction(Actions.delay(2f));

		logo.addAction(s);
		
		stage.addActor(table);

		//stage.setDebugAll(true);
		
	}

	Stage stage;
	
	OrthographicCamera camera = new OrthographicCamera();

	Viewport viewport = new StretchViewport(GameContainer.WIDTH, GameContainer.HEIGHT, camera);

	float initial = 2f;
	
	Image logo = new Image(new TextureRegion(GameContainer.atlas.findRegion("titleLogo")));
	
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0,0,0,1); //  clear the screen 
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
    	stage.act();
    	
    	stage.draw();
    	
    	finished();

	}
	
	boolean trigger;
	
	public void finished() {
		if(!trigger && GameContainer.assets.manager.update() && !logo.hasActions()) {
			font();
			switchScreen();
			trigger = true;
			System.out.println("SWITCHING");
		}
	}
	
	public void font() {
		
		/* Set default font for high resolution. Scale down as needed. */
		
		BitmapFont lana24 = GameContainer.assets.manager.get("lana24.ttf", BitmapFont.class);

		GameContainer.skin.get(TextButtonStyle.class).font = lana24;
		GameContainer.skin.get(ListStyle.class).font = lana24;
		GameContainer.skin.get(LabelStyle.class).font = lana24;
		GameContainer.skin.get(TextFieldStyle.class).font = lana24;
		GameContainer.skin.get(LabelStyle.class).font = lana24;
		GameContainer.skin.get(WindowStyle.class).titleFont = lana24;
		GameContainer.skin.get(CheckBoxStyle.class).font = lana24;
		GameContainer.skin.get(SelectBoxStyle.class).font = lana24;
		GameContainer.skin.get(SelectBoxStyle.class).listStyle.font = lana24;
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void show() {
		
		GameContainer.assets.loadFonts();
		GameContainer.assets.manager.finishLoading();
		GameContainer.fadeActor.addAction(Actions.fadeOut(0.5f));
		
	}
	
	SequenceAction sequenceAction = new SequenceAction();
	public void switchScreen() {

		sequenceAction.addAction(Actions.fadeIn(0.5f));

	    sequenceAction.addAction(Actions.run(new Runnable() {
	       @Override
	        public void run() {
	    	   game.play = new PlayScreen(game);
	   		   game.pause = new PauseScreen(game);
	        }
	    }));

	    sequenceAction.addAction(Actions.delay(1f));
	    
	    sequenceAction.addAction(Actions.run(new Runnable() {
	       @Override
	        public void run() {
	    	  
	            game.setScreen(game.play);
	        }
	    }));
	    
	    GameContainer.fadeActor.addAction(sequenceAction);
	}

	
	public void hide() {

	    trigger = false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
