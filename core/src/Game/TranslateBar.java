package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import Game.abstraction.GameObject;
import Screens.GameContainer;
import Screens.PlayScreen;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class TranslateBar extends GameObject { /* An abstraction for UI to move level objects */
	
	Sprite upArrow = new Sprite(GameContainer.atlas.findRegion("arrowBlue"));
	Sprite rightArrow = new Sprite(GameContainer.atlas.findRegion("arrowRed"));
	Sprite allArrow = new Sprite(GameContainer.atlas.findRegion("transform"));
	boolean draggedV, draggedH, draggedA;
	
	public TranslateBar(Vector3 parent) {
		
		upArrow.setOrigin(upArrow.getWidth()/2, 0);
		rightArrow.setOrigin(rightArrow.getWidth()/2, 0);
		upArrow.setScale(PlayScreen.imageScale);
		rightArrow.setScale(PlayScreen.imageScale);
		rightArrow.setRotation(-90);
		allArrow.setScale(PlayScreen.imageScale);
		allArrow.setOrigin(allArrow.getWidth()/2, allArrow.getHeight()/2);
		
		position = parent;

	}
	
	@Override
	public void update(float dt) {

		dragVertical();
		dragHorizontal();
		dragAll();
		
	}
	
	public void render(SpriteBatch sb, ShapeDrawer sd, Vector3 position, Rectangle rect) {
		
		upArrow.setOriginBasedPosition(position.x, position.y + rect.height / 2);
		
		upArrow.draw(sb);
		
		rightArrow.setOriginBasedPosition(position.x + rect.width / 2, position.y);
		
		rightArrow.draw(sb);
		
		allArrow.setOriginBasedPosition(position.x, position.y);
		
		allArrow.draw(sb);
		
		
	}

	public void dragVertical() {

		if (PlayScreen.dragging != this && !(PlayScreen.dragging == null))
			return;
		
		if (upArrow.getBoundingRectangle().contains(PlayScreen.clickPos.x, PlayScreen.clickPos.y)) {
			draggedV = (Gdx.input.isButtonPressed(Buttons.LEFT));
		}				

		if (draggedV && Gdx.input.isButtonPressed(Buttons.LEFT) && !(draggedH || draggedA)) {
			upArrow.setColor(Color.GRAY);
			position.y += (PlayScreen.clickPos.y - PlayScreen.oldClickPos.y);
			PlayScreen.dragging = this;
		}
		else {
			upArrow.setColor(Color.WHITE);
			draggedV = false;
		}

	}
	
	public void dragHorizontal() {

		if (PlayScreen.dragging != this && !(PlayScreen.dragging == null))
			return;
		
		if (rightArrow.getBoundingRectangle().contains(PlayScreen.clickPos.x, PlayScreen.clickPos.y)) {
			draggedH = (Gdx.input.isButtonPressed(Buttons.LEFT));
		}				

		if (draggedH && Gdx.input.isButtonPressed(Buttons.LEFT) && !(draggedV || draggedA)) {
			rightArrow.setColor(Color.GRAY);
			position.x += (PlayScreen.clickPos.x - PlayScreen.oldClickPos.x);
			PlayScreen.dragging = this;
		}
		else {
			rightArrow.setColor(Color.WHITE);
			draggedH = false;
		}

	}
	
	public void dragAll() {

		if (PlayScreen.dragging != this && !(PlayScreen.dragging == null))
			return;
		
		if (allArrow.getBoundingRectangle().contains(PlayScreen.clickPos.x, PlayScreen.clickPos.y)) {
			draggedA = (Gdx.input.isButtonPressed(Buttons.LEFT));
		}				

		if (draggedA && Gdx.input.isButtonPressed(Buttons.LEFT) && !(draggedH || draggedV)) {
			allArrow.setColor(Color.GRAY);
			position.x += (PlayScreen.clickPos.x - PlayScreen.oldClickPos.x);
			position.y += (PlayScreen.clickPos.y - PlayScreen.oldClickPos.y);
			PlayScreen.dragging = this;
		}
		else {
			allArrow.setColor(Color.WHITE);
			draggedA = false;
		}
	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(SpriteBatch sb, ShapeDrawer sd) {
		// TODO Auto-generated method stub
		
	}
	

}
