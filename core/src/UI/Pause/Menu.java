package UI.Pause;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import Screens.GameContainer;

public abstract class Menu {
	
	protected Stage stage;
	
	protected TextButton apply;
	
	protected TextButton escape;
	
	protected NinePatchDrawable patchD;
	
	protected PauseMenu pauseMenu;
	
	public void escape(NinePatchDrawable patchD, Stage stage) {
		
		Table tableB = new Table();
		
		Table table = new Table();
		
		table.setFillParent(true);

		escape = new TextButton("ESC", GameContainer.skin);

		escape.getLabel().setFontScale(0.5f);

		tableB.add(escape);
		
		tableB.setBackground(patchD);
		
		table.add(tableB);
		
		Label lab = new Label("Back", GameContainer.skin);
		
		lab.setFontScale(0.5f);
		
		tableB.add(lab).pad(5).padLeft(5);
		
		table.left().bottom().pad(5);
		
		stage.addActor(table);

	}
	
	public void apply(NinePatchDrawable patchD, Table t) {
		apply = new TextButton("APPLY", GameContainer.skin);
		Table table = new Table();
		table.add(apply).pad(5).colspan(3);
		
		apply.getLabel().setFontScale(0.8f);

		table.setBackground(patchD);

		t.add(table).padTop(10).fillX().row();
		
	}
	
	public abstract void update();

	public abstract void apply();
	
	public abstract void show();
	
	public void dispose() {
		stage.dispose();
	}

}
