package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import Game.shared.SharedObjects;
import Screens.GameContainer;
import Screens.PlayScreen;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class MovingPlatform extends Box {
	
	TranslateBar endBar;
	
	Vector3 startPosition = new Vector3();

	Vector3 endPosition = new Vector3();
	
	TextButton interpolation = new TextButton("Easing", GameContainer.skin);
	
	Window moveInterpolation = new Window("", GameContainer.skin);
	
	TextField durationField = new TextField("0", GameContainer.skin);
	
	TextField holdDuration = new TextField("0", GameContainer.skin);
	
	SelectBox<String> interpolationF = new SelectBox<>(GameContainer.skin);
	
	SelectBox<String> interpolationB = new SelectBox<>(GameContainer.skin);
	
	Interpolation inF, inB;
	
	float pad = 2.5f;

	public MovingPlatform() {
		
		frame = new Sprite();
		boxBack.scale(PlayScreen.imageScale, PlayScreen.imageScale);
		widthSlider.setRange(4, 25);
		heightSlider.setRange(4, 25);
		endBar = new TranslateBar(endPosition);
		interpolation.pad(buttonPad);
		interpolation.getLabel().setFontScale(buttonScale);
		tabs.add(interpolation);
		
		Table table = new Table();
		
		Label forward = new Label("Foward Curve", GameContainer.skin);
		
		table.add(forward).pad(pad).row();
		
		TextTooltip textTooltip = new TextTooltip("Foward Interpolation", GameContainer.skin);
		forward.addListener(textTooltip);
		textTooltip.setInstant(true);
		
		interpolationF.setItems(SharedObjects.boxInterpolations);

		table.add(interpolationF).pad(pad).row();

		Label backward = new Label("Backward Curve", GameContainer.skin);
		
		table.add(backward).pad(pad).row();

		textTooltip = new TextTooltip("Backward interpolation", GameContainer.skin);
		backward.addListener(textTooltip);
		textTooltip.setInstant(true);
		
		interpolationB.setItems(SharedObjects.boxInterpolations);
		
		table.add(interpolationB).pad(pad).row();
		
		Label duration = new Label("Duration", GameContainer.skin);
		
		table.add(duration).pad(pad).row();
		
		table.add(durationField).pad(pad).row();
		
		textTooltip = new TextTooltip("Duration in seconds", GameContainer.skin);
		duration.addListener(textTooltip);
		textTooltip.setInstant(true);
		
		duration = new Label("holdTime Duration End", GameContainer.skin);
		
		table.add(duration).pad(pad).row();
		
		table.add(holdDuration).pad(pad).row();
		
		textTooltip = new TextTooltip("holdTime duration at end of path in seconds", GameContainer.skin);
		duration.addListener(textTooltip);
		textTooltip.setInstant(true);

		moveInterpolation.add(table);
		
		moveInterpolation.padTop(0);
		
		interpolation.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				window.removeActorAt(2, false);
				window.getCells().removeIndex(1);
				window.row();
				window.add(moveInterpolation).growY();
				reset();
				interpolation.setDisabled(true);
			}
		});
		
		durationField.setTextFieldFilter(new TextField.TextFieldFilter() {
            // Accepts all Alphanumeric Characters except
            public boolean acceptChar(TextField textField, char c) {
                if (Character.toString(c).matches("[0-9]+") || c == '.') {
                    return true;
                }
                return false;
            }
        });
		
		holdDuration.setTextFieldFilter(new TextField.TextFieldFilter() {
            // Accepts all Alphanumeric Characters except
            public boolean acceptChar(TextField textField, char c) {
                if (Character.toString(c).matches("[0-9]+") || c == '.') {
                    return true;
                }
                return false;
            }
        });

		
	}
	
	public void editReset() {
		position.set(startPosition);
		start = false;
		hold = false;
		timer = 0;
		startTimer = false;
	}
	
	public void extendedInit() {
		endPosition.set(position);
	}
	
	public void reset() {
		interpolation.setDisabled(false);
		general.setDisabled(false);
	}

	public void update(float dt) {
		
		prevPosition.set(position);
		
		widthString.replace(7, widthString.length(), Float.toString(rect.width));
		
		widthLabel.setText(widthString);
		
		heightString.replace(8, heightString.length(), Float.toString(rect.height));
		
		heightLabel.setText(heightString);
		
		if(!PlayScreen.editMode)
			move(dt);
		else {
			timer = 0;
			velocity.setZero();
			direction = false;
		}

		vZero();
		
		if(canMove)
			physicsStep();
		
	}
	
	float duration, holdTime;
	float timer;
	boolean direction, start, hold, startTimer, canMove;
	Interpolation foward, backward;
	Vector3 leftover = new Vector3(); // leftover position for the player
	
	public void move(float dt) {
		
		if(parent != null) {
			start = true;
		}
		
		if(start && !hold) {
			
			startTimer = true;
			
			canMove = true;
			
			if(!direction) {
				
				velocity.x = foward.apply(startPosition.x, endPosition.x, timer / duration) - position.x;
				
				velocity.y = foward.apply(startPosition.y, endPosition.y, timer / duration) - position.y;
				
				if(timer > duration) {
					stop();
					hold = true;
					
					PlayScreen.gameCamera.shake(velocity.x * 0.05f, 0, 1);
					leftover.set(endPosition).sub(position);
					position.set(endPosition); // need to move player too.
					if(parent != null)
						parent.getPosition().add(leftover);
					
				}
				
				
			}
			else {

				velocity.x = backward.apply(endPosition.x, startPosition.x, timer / duration) - position.x;
				
				velocity.y = backward.apply(endPosition.y, startPosition.y, timer / duration) - position.y;
				
				if(timer > duration) {
					stop();
					
					leftover.set(startPosition).sub(position);	
					position.set(startPosition); // need to move player too.
					if(parent != null)
						parent.getPosition().add(leftover);
					
					parent = null; // extra reset
				}

			}
			
		}
		else if (hold) {
			
			canMove = false;
			
			start = false;
			
			startTimer = true;
			
			if(timer > holdTime) {
				start = true;
				timer = 0;
				hold = false;
				startTimer = false;
			}

		}
		else
			startTimer = false;
		

	}
	
	public void stop() {
		direction = !direction;
		timer = 0;
		velocity.setZero();
		start = false;
		startTimer = false;
	}

	private TextureRegion back = new TextureRegion(GameContainer.atlas.findRegion("movingPlatformBack"));
	
	Sprite light;
	
	private Sprite redlight = new Sprite(new TextureRegion(GameContainer.atlas.findRegion("redlight")));
	
	private Sprite greenlight = new Sprite(new TextureRegion(GameContainer.atlas.findRegion("greenlight")));

	private NinePatch boxBack = new NinePatch(back, 1, 1, 1, 1);
	private Animation<TextureRegion> gear = new Animation<TextureRegion>(0.15f,
			GameContainer.atlas.findRegions("movingPlatform"), Animation.PlayMode.LOOP);
	
	float stateTime;
	TextureRegion currentFrame;
	
	boolean last;
	
	public void render(SpriteBatch sb, ShapeDrawer sd) {
		
		last = PlayScreen.editMode;
		
		interpolate();
		
		window.setWidth(window.getPrefWidth());
		
		sd.line(startPosition.x, startPosition.y, endPosition.x, endPosition.y, Color.BLUE, PlayScreen.imageScale * 2);

		boxBack.draw(sb, renderPosition.x - rect.width / 2, renderPosition.y - rect.height / 2, rect.width, rect.height);
		
		stateTime += Gdx.graphics.getDeltaTime();
		
		currentFrame = gear.getKeyFrame(Math.max(0, stateTime));
		
		frame.setRegion(currentFrame);
		
		frame.setBounds(0, 0, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

		frame.setOrigin(frame.getWidth() / 2, frame.getHeight() / 2);
		
		frame.setScale(PlayScreen.imageScale);
		
		frame.setCenter(renderPosition.x, renderPosition.y);
		
		frame.setAlpha(1);
		
		frame.draw(sb);
		
		if(start && !direction) 
			light = greenlight;
		else 
			light = redlight;

		light.setOrigin(redlight.getWidth() / 2, redlight.getHeight() / 2);
		light.setScale(PlayScreen.imageScale);
		light.setCenter(renderPosition.x, renderPosition.y);
		light.draw(sb);

		if(PlayScreen.editMode) {
			
			if(!last)
				renderPosition.set(startPosition);
			
			startPosition.set(renderPosition);
			
			endBar.update(0);
			
			editRoutine(sb, sd);
			
			try{
				duration = Float.parseFloat(durationField.getText());
				holdTime = Float.parseFloat(holdDuration.getText());
		    }catch(NumberFormatException e){
		        //not int
		    }

			if(duration <= 0) {
				duration = 1f;
				durationField.setText("1");
			}

			if(holdTime <= 0) {
				holdTime = 1f;
				holdDuration.setText("1");
			}
			
			foward = SharedObjects.interpolations[interpolationF.getSelectedIndex()];
			
			backward = SharedObjects.interpolations[interpolationB.getSelectedIndex()];
			
			boxBack.getColor().a = 0.5f;
			
			boxBack.draw(sb, endPosition.x - rect.width / 2, endPosition.y - rect.height / 2, rect.width, rect.height);
			
			frame.setCenter(endPosition.x, endPosition.y);
			
			frame.setAlpha(0.5f);
			
			frame.draw(sb);
			
			boxBack.getColor().a = 1;
			
			sd.rectangle(endPosition.x - rect.width / 2, endPosition.y - rect.height / 2, rect.width, rect.height, Color.GREEN, PlayScreen.imageScale);
			
			endBar.render(sb, sd, endPosition, rect);

		}
		
		if(startTimer && !PlayScreen.editMode) {
			timer += Gdx.graphics.getDeltaTime();
		}
		
	}
	
	public void window() {

		if (Gdx.input.isButtonJustPressed(Buttons.LEFT) && !(beginBar.draggedH || beginBar.draggedV || (beginBar.draggedA && rect.width > 2 && rect.height > 2))
				&& !(endBar.draggedH || endBar.draggedV || (endBar.draggedA && rect.width > 2 && rect.height > 2))) 
			
			minimize();

		
	}
	
	public void dispose() {
		PlayScreen.boxPool.free(this);
	}

}
