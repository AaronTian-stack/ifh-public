package UI.Pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import Screens.GameContainer;

public class Settings extends Menu {
	
	NinePatchDrawable patchD;
	Table settings;
	
	TextButton videoButton, audioButton, exitButton;
	
	public Settings(Viewport viewport, SpriteBatch sb, final PauseMenu pauseMenu, NinePatchDrawable patchD) {
		stage = new Stage(viewport, sb);
		this.patchD = patchD;
		this.pauseMenu = pauseMenu;
		 
		settings = new Table();
		settings.setFillParent(true);
		settings.center();
	
		videoButton = new TextButton("Video", GameContainer.skin);
	
		audioButton = new TextButton("Audio", GameContainer.skin);
		
		exitButton = new TextButton("QUIT", GameContainer.skin);
		 
		videoButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					pauseMenu.setMenu(pauseMenu.video);
					pauseMenu.transition();
					show();
				}
			});
			
		audioButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pauseMenu.setMenu(pauseMenu.audio);
				pauseMenu.transition();
				show();
			}
		});
		
		exitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pauseMenu.setMenu(pauseMenu.exitWarn);
				pauseMenu.transition();
				show();
			}
		});
			 
		float padTop = 130;
		float pad = 5;
		 
		settings.add(videoButton).size(videoButton.getPrefWidth() * 1.1f, videoButton.getPrefHeight() * 1.1f).pad(pad).padTop(padTop);
		settings.add(audioButton).size(audioButton.getPrefWidth() * 1.1f, audioButton.getPrefHeight() * 1.1f).pad(pad).padTop(padTop).row();;
		settings.add(exitButton).pad(pad).colspan(3).fillX();
		 
		stage.addActor(settings);
		 
		escape(patchD, stage);
		
		escape.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pauseMenu.setMenu(pauseMenu.getSettings());
				pauseMenu.game.setScreen(pauseMenu.game.play);
				escape.setChecked(false);
			}
		});
		 
	}

	public void apply() {
		
	}

	@Override
	public void show() {

		videoButton.setChecked(false);
		audioButton.setChecked(false);
		exitButton.setChecked(false);
		
	}

	@Override
	public void update() {
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			escape.setChecked(true);
		}
		
	}
	
	
	

}
