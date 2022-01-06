package UI;

import Screens.GameContainer;
import Screens.PlayScreen;
import Tiles.Enums;
import Tiles.Enums.DrawMode;
import Tiles.Enums.Tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LevelEditorUI {
	public Stage stage;

	public Table table = new Table();

	private Viewport viewport;

	private List<Tile> list;

	private List<DrawMode> list2;
	
	private List<String> list3;

	private ScrollPane scrollPane, scrollPane2, scrollPane3;

	private Rectangle cull;

	public LevelEditorUI(SpriteBatch sb) {

		viewport = new ScreenViewport();

		stage = new Stage(viewport, sb);
		
		cull = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		list = new List<Tile>(GameContainer.skin);
		list2 = new List<DrawMode>(GameContainer.skin);
		list3 = new List<String>(GameContainer.skin);
		
		list.setItems(new Tile[] { Tile.Null, Tile.Empty, Tile.Chunk, Tile.Debug, Tile.Dirt, Tile.Cave });
		list2.setItems(new DrawMode[] { DrawMode.Brush, DrawMode.Fill, DrawMode.Line, DrawMode.Select });
		list3.setItems(new String[] { "0 (Red)", "1 (Blue)", "2 (Yellow)", "3 (Green)" });

		scrollPane = new ScrollPane(list, GameContainer.skin);
		
		scrollPane2 = new ScrollPane(list2, GameContainer.skin);
		
		scrollPane3 = new ScrollPane(list3, GameContainer.skin);
		
		
		Window win1 = new Window("Selection", GameContainer.skin);
		Window win2 = new Window("Draw", GameContainer.skin);
		Window win3 = new Window("Layer", GameContainer.skin);

		win1.add(scrollPane).padTop(5).right();
		win2.add(scrollPane2).padTop(5).right();
		win3.add(scrollPane3).padTop(5).right();
		
		win1.getTitleLabel().setFontScale(0.5f);
		win2.getTitleLabel().setFontScale(0.5f);
		win3.getTitleLabel().setFontScale(0.5f);
		
		win1.pack();
		win2.pack();
		win3.pack();

		table.align(Align.right);

		table.setFillParent(true);
		table.setTransform(true);
		
		table.add(win3).align(Align.right).row();
		table.add(win2).align(Align.right).row();
		table.add(win1).align(Align.right).row();
		//table.align(Align.right);
		
		//stage.setDebugAll(true);
		
		stage.addActor(table);

		table.setCullingArea(cull);

	}

	public void update() {

		PlayScreen.level.selection = (Tile) list.getSelected();
		PlayScreen.level.drawMode = (DrawMode) list2.getSelected();
		PlayScreen.level.layer = list3.getSelectedIndex();
		if (PlayScreen.level.drawMode != DrawMode.Brush && PlayScreen.level.selection == Tile.Chunk)
			list2.setSelected(DrawMode.Brush); 
		
	}
	
	Interpolation inter;
	
	float scale;
	public void move() {
		if (PlayScreen.editMode) {
			scale = 1;
			inter = Interpolation.swingOut;
		}
		else {
			scale = 0;
			inter = Interpolation.swingIn;
		}
		
		for (Cell c : table.getCells()) {
			c.getActor().addAction(Actions.scaleTo(scale, scale, 0.4f, inter));
		}
		
		for(int i=1;i<stage.getActors().size;i++) {
			stage.getActors().get(i).addAction(Actions.scaleTo(scale, scale, 0.4f, inter));
		}
		
		
	}

	public void render() {

		stage.act();
		
		for (Cell c : table.getCells()) {
			if (c.getActor().getY() < 0) {
				c.getActor().setY(0);
			}
			if (c.getActor().getX() < 0) {
				c.getActor().setX(0);
			}
			if (c.getActor().getX() > Gdx.graphics.getWidth()) {
				c.getActor().setX(Gdx.graphics.getWidth());
			}
		}


		stage.draw();

	}

	public void dispose() {
		this.stage.dispose();
	}

	public void resize(int width, int height) {
		this.viewport.update(width, height, true);
		this.cull.set(0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public ScrollPane getScrollPane() {
		return this.scrollPane;
	}

	public void setScrollPane(ScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public ScrollPane getScrollPane2() {
		return this.scrollPane2;
	}

	public void setScrollPane2(ScrollPane scrollPane2) {
		this.scrollPane2 = scrollPane2;
	}

	public List<Tile> getList() {
		return list;
	}

	public List<DrawMode> getList2() {
		return list2;
	}
	
	public List<String> getList3() {
		return list3;
	}

}
