package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import Game.abstraction.Solid;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;

import Screens.GameContainer;
import Screens.PlayScreen;
import Tiles.Coordinate;
import Tiles.Enums.GameObjectType;
import Tiles.Enums.Tile;
import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class Box extends Solid { // generic dynamic box

	Window window = new Window("", GameContainer.skin);
	Window defaultWin = new Window("",GameContainer.skin);
	Label positionX = new Label(null, GameContainer.skin);
	Label positionY = new Label(null, GameContainer.skin);
	Label widthLabel = new Label(null, GameContainer.skin);
	Label heightLabel = new Label(null, GameContainer.skin);
	Label zin = new Label("Z-Index:", GameContainer.skin);
	Slider widthSlider = new Slider(1, 25, 0.25f, false, GameContainer.skin);
	Slider heightSlider = new Slider(1, 25, 0.25f, false, GameContainer.skin);
	CheckBox gravityCheck = new CheckBox("Gravity", GameContainer.skin);
	TextButton delete = new TextButton("DELETE", GameContainer.skin);
	TextField zOrder = new TextField("0", GameContainer.skin);
	Table table = new Table();
	
	TranslateBar beginBar;
	
	Table tabs = new Table();
	
	TextButton general = new TextButton("General", GameContainer.skin);
	
	float buttonScale = 0.6f;
	float buttonPad = 5f;

	public Box() { /* A generic dynamic box */

		coor = new Coordinate(0, 0);
		rect = new Rectangle(0, 0, 6, 6);
		position = new Vector3();
		prevPosition = new Vector3();
		renderPosition = new Vector3();
		velocity = new Vector3();

		window.setRound(false);
		table.setRound(false);
		
		//window.setDebug(true);
		
		window.setMovable(false);

		defaultWin.setMovable(false);
		
		defaultWin.add(table);
		
		defaultWin.padTop(0);
		
		general.pad(buttonPad);
		
		general.getLabel().setFontScale(buttonScale);
		
		general.setDisabled(true);
		
		general.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				window.removeActorAt(2, false);
				window.getCells().removeIndex(1);
				window.row();
				window.add(defaultWin).expand();
				//window.pack();
				reset();
				general.setDisabled(true);
			}
		});
		
		tabs.add(general).padRight(2);
		
		window.add(tabs).left().top().row();
		
		window.add(defaultWin);
		
		window.padTop(0);
		
		widthSlider.setValue(rect.width);
		
		heightSlider.setValue(rect.height);

		widthString.replace(7, widthString.length(), Float.toString(rect.width));
		
		heightString.replace(8, heightString.length(), Float.toString(rect.height));
		
		table.add(new Label("Position", GameContainer.skin)).padTop(10).row();
		
		table.add(positionX).padTop(5).row();
		
		table.add(positionY).padBottom(5).row();
		
		table.add(widthSlider).row();

		table.add(widthLabel).padTop(2).row();
		
		table.add(heightSlider).row();
		
		table.add(heightLabel).padTop(2).row();

		table.add(gravityCheck).padTop(10).padBottom(10).row();
		
		table.add(zin).row();

		table.add(zOrder).width(Value.percentWidth(0.9f, window)).row();
		
		table.add(delete).padTop(10).padBottom(5);

		zOrder.setTextFieldFilter(new TextField.TextFieldFilter() {
            // Accepts all Alphanumeric Characters except
            public boolean acceptChar(TextField textField, char c) {
                if (Character.toString(c).matches("[0-9]+")) {
                    return true;
                }
                return false;
            }
        });
		
		zOrder.setMaxLength(9);
		
		window.setSize(window.getPrefWidth(), window.getPrefHeight());
		
		type = GameObjectType.Solid;
		
		beginBar = new TranslateBar(position);
		

	}
	
	public abstract void reset();
	
	StringBuilder widthString = new StringBuilder("WIDTH:  ");
	StringBuilder heightString = new StringBuilder("HEIGHT:  ");
	
	StringBuilder xString = new StringBuilder("X:  ");
	StringBuilder yString = new StringBuilder("Y:  ");

	public void init() {
		
		velocity.setZero();
		widthSlider.setValue(6);
		heightSlider.setValue(6);

		PlayScreen.LevelEditor.stage.addActor(window);
		
		window.setScale(0);

		window.addAction(Actions.scaleTo(1, 1, 0.2f, Interpolation.smooth));

		extendedInit();
		
	}
	
	public void extendedInit() {
		
	}

	public abstract void update(float dt);
	
	public abstract void render(SpriteBatch sb, ShapeDrawer sd);
	
	Vector2 screenCoor = new Vector2();
	Vector2 dragPoint = new Vector2();
	
	Vector3 screenCoor3D = new Vector3();
	
	public void editRoutine(SpriteBatch sb, ShapeDrawer sd) {
		
		screenCoor.set(Gdx.input.getX(), Gdx.input.getY());
		
		rect.setWidth(widthSlider.getValue());
		
		rect.setHeight(heightSlider.getValue());
		
		xString.replace(3, xString.length(), Float.toString(position.x));
		
		yString.replace(3, yString.length(), Float.toString(position.y));
		
		positionX.setText(xString);
		
		positionY.setText(yString);


		if(!zOrder.getText().equals(""))
		
			zIndex = Integer.parseInt(zOrder.getText());
		
		if(delete.isChecked()) {

			window.remove();

			PlayScreen.gameObjects.removeValue(this, true);

			//PlayScreen.boxPool.free(this);
			
			dispose();

			delete.setChecked(false);
			
		}

		if(((!Gdx.input.isButtonPressed(Buttons.LEFT)) )//&& (PlayScreen.stage.hit(screenCoor.x, screenCoor.y, true) == null)) 
				|| (beginBar.draggedH || beginBar.draggedV || beginBar.draggedA)) { //could be simplified

			screenCoor3D.set(renderPosition);
			screenCoor3D.y += rect.height / 4;
			PlayScreen.gameCamera.getCamera().project(screenCoor3D);
			
			window.setY(MathUtils.lerp(window.getY(), screenCoor3D.y, 0.2f));
			
			screenCoor3D.set(renderPosition);
			screenCoor3D.x += rect.width / 2;
			PlayScreen.gameCamera.getCamera().project(screenCoor3D);
			
			window.setX(MathUtils.lerp(window.getX(), screenCoor3D.x + rect.width / 2, 0.2f));


		}

		beginBar.update(0);
		checkMinimize();

		renderBox(sd, color);

		beginBar.render(sb, sd, renderPosition, rect);
		
		

		
	}
	
	public void checkMinimize() {
		
		if (rect.contains(PlayScreen.clickPos.x, PlayScreen.clickPos.y)) {
			
			if(Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
				PlayScreen.LevelEditor.getList().setSelected(Tile.Null);
				PlayScreen.level.selection = Tile.Null;
			}

			window();

		}
		
	}
	
	public void window() {
		
		if (Gdx.input.isButtonJustPressed(Buttons.LEFT) && 
				!(beginBar.draggedH || beginBar.draggedV || (beginBar.draggedA && rect.width > 2 && rect.height > 2))) 
			minimize();
		
	}
	
	public void minimize() {
		
		if(window.getScaleX() == 1) window.addAction(Actions.scaleTo(0, 0, 0.2f, Interpolation.smooth));
		
		else window.addAction(Actions.scaleTo(1, 1, 0.2f, Interpolation.smooth));
		
	}
	
}
