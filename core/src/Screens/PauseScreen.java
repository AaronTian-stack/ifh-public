package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import Game.ropes.VerletRope;
import UI.Pause.PauseMenu;

public class PauseScreen implements Screen{
	
	GameContainer game;
	
	Color white = new Color(0,0,0,0f);
	Color black = new Color(0,0,0,0.5f);
	
	Image fadeActor = new Image(new TextureRegion(GameContainer.atlas.findRegion("white")));
	
	public PauseScreen(GameContainer game) {
		this.game = game;
		fadeActor.setColor(white);
		pauseMenu.setGame(game);
	}
	
	PauseMenu pauseMenu = new PauseMenu(GameContainer.batch);

	public void update(float dt) {
		pauseMenu.update(dt);
	}

	public void render(float delta) {

		Gdx.gl.glDisable(GL20.GL_BLEND);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    game.play.renderFrame(false); // LIGHTING MUST BE ON. Also have to do twice for some reason?
		
	    game.play.renderFrame(false);

	    GameContainer.batch.begin();

	    fadeActor.act(Gdx.graphics.getDeltaTime());
	    
	    fadeActor.draw(GameContainer.batch, 1);

	    GameContainer.batch.end();

	    pauseMenu.render(GameContainer.batch, GameContainer.shape);
	    
	    
	}

	
	public void resize(int width, int height) {
		//game.play.resize(width, height);
		fadeActor.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		pauseMenu.resize(width, height);
	}

	
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	public void show() {
		
		fadeActor.addAction(Actions.color(black, 0.2f, Interpolation.smooth2));
		pauseMenu.setMenu(pauseMenu.getSettings());
		pauseMenu.inputMultiplexer.clear();
		pauseMenu.show();
		

	}
	
	SequenceAction sequenceAction = new SequenceAction();
	public void switchScreen() {
		//sequenceAction.addAction(Actions.fadeIn(0.5f));
	    //sequenceAction.addAction(Actions.delay(1f));
	    sequenceAction.addAction(Actions.run(new Runnable() {
	       @Override
	        public void run() {
	            game.setScreen(game.play);
	        }
	    }));
	    GameContainer.fadeActor.addAction(sequenceAction);
	    
	}

	public void hide() {
		
		fadeActor.setColor(white);
		pauseMenu.hide();
		
	}

	
	public void dispose() {
		pauseMenu.dispose();
		
	}

	public GameContainer getGame() {
		return game;
	}

	public void setGame(GameContainer game) {
		this.game = game;
	}

}
