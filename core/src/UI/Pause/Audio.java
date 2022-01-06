package UI.Pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.Viewport;

import Screens.GameContainer;

public class Audio extends Menu {

	Table audio;
	NinePatchDrawable patchD;
	PauseMenu pauseMenu;
	
	public Slider musicVolume, effectVolume;
	
	public Label musicN, effectN;

	public Audio(Viewport viewport, SpriteBatch sb, final PauseMenu pauseMenu, NinePatchDrawable patchD) {
		 stage = new Stage(viewport, sb);
		 this.patchD = patchD;
		 this.pauseMenu = pauseMenu;
		 
		 audio = new Table();
		 audio.setFillParent(true);
		 audio.center();
		 
		 musicVolume = new Slider(0, 1, 0.1f, false, GameContainer.skin);
		 
		 musicVolume.setValue(GameContainer.pref.getMusicVolume());

		 musicN = new Label(Integer.toString((int) (GameContainer.pref.getMusicVolume() * 10)), GameContainer.skin);
		 
		 musicVolume.addListener(new ChangeListener() {
	            @Override
	            public void changed(ChangeEvent event, Actor actor) {
	            	musicN.setText((int) (musicVolume.getValue() * 10));
	            	GameContainer.pref.setMusicVolume(musicVolume.getValue());
	            }
	        });
		 
		 effectVolume = new Slider(0, 1, 0.1f, false, GameContainer.skin);
		 
		 effectVolume.setValue(GameContainer.pref.getSoundVolume());
		 
		 effectN = new Label(Integer.toString((int) (GameContainer.pref.getSoundVolume() * 10)), GameContainer.skin);
		 
		 effectVolume.addListener(new ChangeListener() {
	            @Override
	            public void changed(ChangeEvent event, Actor actor) {
	            	effectN.setText((int) (effectVolume.getValue() * 10));
	            	GameContainer.pref.setSoundVolume(effectVolume.getValue());
	            }
	        });
		 
		 music();
		 effect();
		 //apply(patchD, audio);
		 escape(patchD, stage);
		 
		 show();
		 
		 escape.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					
					pauseMenu.hide();
					pauseMenu.transition();

					
					escape.setChecked(false);
				}
			});
		 
		 stage.addActor(audio);

		 
	}
	
	public void show() {
		musicVolume.setValue(GameContainer.pref.getMusicVolume());
		effectVolume.setValue(GameContainer.pref.getSoundVolume());
	}
	
	public void music() {
		Table table = new Table();
		Label music = new Label("MUSIC VOLUME", GameContainer.skin);
		table.add(music).pad(5);
		table.add(musicVolume).pad(5).fillX();
		table.add(musicN).width(20).center();
		table.setBackground(patchD);
		audio.add(table).fillX().row();
	}
	
	public void effect() {
		Table table = new Table();
		Label effect = new Label("EFFECT VOLUME", GameContainer.skin);
		table.add(effect).pad(5);
		table.add(effectVolume).pad(5).fillX();
		table.add(effectN).width(20).center();
		table.setBackground(patchD);
		audio.add(table).padTop(10).fillX().row();
	}
	
	public void apply() {
		GameContainer.pref.setMusicVolume(musicVolume.getValue());
		GameContainer.pref.setSoundVolume(effectVolume.getValue());
	}
	
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void update() {
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			escape.setChecked(true);
		}
		
	}

}
