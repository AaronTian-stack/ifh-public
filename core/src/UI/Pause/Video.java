package UI.Pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import Screens.GameContainer;

public class Video extends Menu { // does all video settings stuff

	ProgressBar bar;
	
	public SelectBox<String> resolutionSelect = new SelectBox<>(GameContainer.skin);
	
	public SelectBox<String> displaySelect = new SelectBox<>(GameContainer.skin);
	
	public SelectBox<String> vSync = new SelectBox<>(GameContainer.skin);
	
	Table video;
	
	TooltipManager tm;

	public Video(Viewport viewport, SpriteBatch sb, final PauseMenu pauseMenu, NinePatchDrawable patchD) {
		 stage = new Stage(viewport, sb);
		 this.patchD = patchD;
		 this.pauseMenu = pauseMenu;
		 
		 video = new Table();
		 video.setFillParent(true);
		 video.center();
		 
		 display();
		 res();
		 vSync();
		 apply(patchD, video);
		 escape(patchD, stage);
		 
		 show();
		 
		 apply.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					apply();
				}
			});
		 
		 escape.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(resolutionSelect.getSelectedIndex() != GameContainer.pref.getResolution() || 
							displaySelect.getSelectedIndex() != GameContainer.pref.getDisplayMode() || 
							vSync.getSelectedIndex() != GameContainer.pref.isVSyncEnabled()) {
						
						pauseMenu.transition();
						pauseMenu.setMenu(pauseMenu.videoWarn);
						
					}
					else {
						pauseMenu.hide();
						pauseMenu.transition();
					}
					
					escape.setChecked(false);
				}
			});
		 
		 stage.addActor(video);
		 
	}
	
	public void update() {
		bar.setValue(resolutionSelect.getSelectedIndex());
		if(displaySelect.getSelectedIndex() == 1) {
			resolutionSelect.setDisabled(true);
			resolutionSelect.setSelectedIndex(resolutionSelect.getItems().size - 1);
		}
		else
			resolutionSelect.setDisabled(false);
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			escape.setChecked(true);
		}
	}
	
	public void show() {
		resolutionSelect.setSelectedIndex(GameContainer.pref.getResolution());
		displaySelect.setSelectedIndex(GameContainer.pref.getDisplayMode());
		vSync.setSelectedIndex(GameContainer.pref.isVSyncEnabled());
		bar.setValue(resolutionSelect.getSelectedIndex());
		bar.updateVisualValue();
	}

	public void res() {
		
		
		Label resolution = new Label("Screen Resolution:", GameContainer.skin);
		
		TextTooltip textTooltip = new TextTooltip("Size of game screen", GameContainer.skin);
		resolution.addListener(textTooltip);
		textTooltip.setInstant(true);

		Table table = new Table();

		// 1024×576, 1152×648, 1280×720, 1366×768, 1600×900, 1920×1080, 2560×1440 and 3840×2160
		
		int[][] reso = {{1024,576}, {1152, 648}, {1280,720}, {1366,768}, {1600,900}, {1920,1080}, {2560,1440}, {3840, 2160}};
		
		Array<String> test = new Array<String>();
		
		String[] res = new String[] {"1024 x 576", "1152 x 648", "1280 x 720", "1366 x 768", "1600 x 900", "1920 x 1080", "2560 x 1440", "3840 x 2160"};

		test.addAll(res);
		
		for(int i = test.size - 1; i > 0; i--) { // remove resolution too large
			if (reso[i][0] > Gdx.graphics.getDisplayMode().width || reso[i][1] > Gdx.graphics.getDisplayMode().height)
				test.removeIndex(test.size - 1);
			else
				break;
		}
		
		bar = new ProgressBar(0, test.size - 1, 1, false, GameContainer.skin);
		
		resolutionSelect.setItems(test);
		
		resolutionSelect.setAlignment(Align.center);
		resolutionSelect.getList().setAlignment(Align.center);

		Container<Label> c1 = new Container<Label>(resolution);
		Container<SelectBox<String>> c2 = new Container<SelectBox<String>>(resolutionSelect);
		Container<ProgressBar> c3 = new Container<ProgressBar>(bar);
		
		c3.align(Align.center);
		
		table.add(c1).pad(10).align(Align.left).expandX();
		
		table.add(c2).pad(10).align(Align.right).expandX().row();

		table.add(c3).padBottom(5).colspan(2);
		
		table.setBackground(patchD);

		video.add(table).padTop(10).fillX().row();
		
		bar.setAnimateDuration(0.1f);
		
		bar.setVisualInterpolation(Interpolation.smooth2);

	}
	
	public void display() {

		displaySelect.setItems(new String[] {"Windowed", "Fullscreen"});
		displaySelect.setAlignment(Align.center);
		displaySelect.getList().setAlignment(Align.center);

		Label display = new Label("Display Mode:", GameContainer.skin);
		
		TextTooltip textTooltip = new TextTooltip("Sets the game to run fullscreen or in a window", GameContainer.skin);
		display.addListener(textTooltip);
		textTooltip.setInstant(true);
		
		Container<Label> c1 = new Container<Label>(display);
		Container<SelectBox<String>> c2 = new Container<SelectBox<String>>(displaySelect);
		
		Table table = new Table();
		
		table.add(c1).pad(10).align(Align.left).expandX();
		table.add(c2).pad(10).align(Align.right).expandX();
		
		video.add(table).fillX().row();

		table.setBackground(patchD);

	}
	
	public void vSync() {
		
		vSync.setItems(new String[] {"Enabled", "Disabled"});
		vSync.setAlignment(Align.center);
		vSync.getList().setAlignment(Align.center);

		Label vSync = new Label("V-Sync:", GameContainer.skin);
		Table table = new Table();
		
		TextTooltip textTooltip = new TextTooltip("Enable/disable vertical sync. If disabled framerate will unlock", GameContainer.skin);
		vSync.addListener(textTooltip);
		textTooltip.setInstant(true);

		Container<Label> c1 = new Container<Label>(vSync);
		
		Container<SelectBox<String>> c2 = new Container<SelectBox<String>>(this.vSync);

		table.add(c1).align(Align.left).expandX().pad(5).padLeft(10);
		
		table.add(c2).align(Align.right).expandX().pad(5).padRight(10);
		
		table.setBackground(patchD);

		video.add(table).padTop(10).fillX().row();
	}

	@Override
	public void apply() {
		
		/* Apply video settings */
		
		// 1024×576, 1152×648, 1280×720, 1366×768, 1600×900, 1920×1080, 2560×1440 and 3840×2160
		
		switch(displaySelect.getSelectedIndex()) {
		case 0:
			switch(resolutionSelect.getSelectedIndex()) {
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
			resolutionSelect.setSelectedIndex(resolutionSelect.getItems().size - 1);
			break;
		}
		
		Gdx.graphics.setVSync(vSync.getSelectedIndex() == 0);
		
		GameContainer.pref.setVSyncEnabled(vSync.getSelectedIndex());
		
		GameContainer.pref.setResolution(resolutionSelect.getSelectedIndex());
		
		GameContainer.pref.setDisplayMode(displaySelect.getSelectedIndex());

		
	}

}
