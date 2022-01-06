package UI.Pause;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.Viewport;

import Screens.GameContainer;

public class Warning extends Menu {

	Table warning;
	
	String message;
	
	ChangeListener yes, no;
	
	public Warning(String message, ChangeListener yes, ChangeListener no, Viewport viewport, SpriteBatch sb, PauseMenu pauseMenu, NinePatchDrawable patchD) {
		this.message = message;
		stage = new Stage(viewport, sb);
		this.patchD = patchD;
		this.pauseMenu = pauseMenu;
		 
		warning = new Table();
		warning.setFillParent(true);
		warning.center();
		
		this.yes = yes;
		this.no = no;

		//escape(patchD, videoStage);
		 
		label();
		button();
		show();
		 
		stage.addActor(warning);
	}
	
	public void label() {

		Label warn = new Label("WARNING!", GameContainer.skin);
		
		Container<Label> c1 = new Container<Label>(warn);

		Table table = new Table();
		
		table.add(c1).pad(10).center().expandX();
		
		warning.add(table).fillX().row();

		table.setBackground(patchD);

	}
	
	public void button() {

		Label warn = new Label(message, GameContainer.skin);
		
		Container<Label> c1 = new Container<Label>(warn);

		Table table = new Table();
		
		table.add(c1).pad(10).center().expandX();

		TextButton yes = new TextButton("YES", GameContainer.skin);
		
		TextButton no = new TextButton("NO", GameContainer.skin);
		
		yes.addListener(this.yes);
		
		no.addListener(this.no);
		
		table.add(yes).fillX().pad(5);
		table.add(no).fillX().pad(5).row();

		table.setBackground(patchD);
		
		warning.add(table).fillX().padTop(10).row();

	}


	@Override
	public void apply() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
