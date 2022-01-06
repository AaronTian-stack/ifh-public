package Game.level;

import Screens.GameContainer;
import Screens.PlayScreen;
import Tiles.Coordinate;
import Tiles.Enums;
import Tiles.Enums.DrawMode;
import Tiles.Enums.Tile;
import Tiles.Enums.TileType;
import Game.Box;
import Game.MovingPlatform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import Collision.CollideManager;
import Game.abstraction.GameObject;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Level extends GameObject {
	
	private int tilesize = 2;

	private ObjectMap<Coordinate, Chunk> level = new ObjectMap<Coordinate, Chunk>();

	private int chunkHeight = 14;

	private int chunkWidth = 24;

	Pool<Chunk> chunkPool = Pools.get(Chunk.class);

	int mX, mY;

	Coordinate p;

	Chunk poolC;

	public Tile selection = Tile.Null;

	public DrawMode drawMode;

	boolean notInside;

	public Level() {
		PlayScreen.clickPos = new Vector3();
		p = new Coordinate(0, 0);
		drawMode = DrawMode.Brush;

		poolC = chunkPool.obtain();
		
		poolC.setP(0, 0);
		poolC.setDimension(this, chunkWidth, chunkHeight);
		
		level.put(poolC.getP(), poolC);
	}

	Tile floodStart;
	
	public void update(float dt) {

		for (Chunk c : chunker) {
			c.update(dt); //pretty sure this is fine
		}

		
	}
	
	float timer;
	float fadeLength = 0.8f;
	int direction = 1;
	float alpha = 0;
	float maxAlpha = 0.8f;
	
	public void colorFade() {
		timer += Gdx.graphics.getDeltaTime() * direction;
		alpha = Interpolation.linear.apply(0, maxAlpha, timer / fadeLength);
		alpha = MathUtils.clamp(alpha, 0, maxAlpha);
		if(timer > fadeLength || timer < 0)
			direction *= -1;
		if(timer > fadeLength)
			timer = fadeLength;
		if(timer < 0)
			timer = 0;
	}

	
	Vector2 screenCoor = new Vector2();
	Color c = new Color();
	
	public void renderUpdate(SpriteBatch sb) {
		
		colorFade();
		
		if(!PlayScreen.editMode)
			return;

		notInside = true;

		screenCoor.set(Gdx.input.getX(), Gdx.input.getY());
		
		PlayScreen.LevelEditor.stage.screenToStageCoordinates(screenCoor);

		if (notInside && PlayScreen.LevelEditor.stage.hit(screenCoor.x, screenCoor.y, true) != null) 
			notInside = false;

		screenCoor.set(Gdx.input.getX(), Gdx.input.getY());
		
		if(notInside) {
			for (GameObject lo : PlayScreen.gameObjects) {
				
				if(lo.getColor() == Color.BLUE) {
					notInside = false;
					break;
				}
				
			}
		}
		
		x = (int)(PlayScreen.clickPos.x / tilesize);
		y = (int)(PlayScreen.clickPos.y / tilesize);
		
		p.setX(x / chunkWidth);
		p.setY(y / chunkHeight);

		if (notInside) {
			switch (drawMode) {
			case Brush:
				if(selection == Tile.Empty || Gdx.input.isButtonPressed(Buttons.RIGHT)) 
					c.set(Color.RED);
				else	
					c.set(Color.GREEN);

				if (selection != Tile.Null) {
					
					c.a = alpha;
					
					if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
						c.a = 0.8f;
						setTile(selection);
					} 

	
				}
				break;
			case Fill:
				if (Gdx.input.isButtonJustPressed(Buttons.LEFT))
					floodfill(); 
				break;
			} 
			if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
				c.set(Color.RED);
				c.a = 0.8f;
				setTile(Tile.Empty);
				removeChunk();
			} 

			if (selection != Tile.Null) {
				
				if(level.get(p) != null)
					level.get(p).drawSwitch(sb, y % chunkHeight, x % chunkWidth, selection, PlayScreen.LevelEditor.getList3().getSelectedIndex()); // Chunk takes in local coordinates

				GameContainer.shape.filledRectangle(x * tilesize, y * tilesize, tilesize, tilesize, c);
			}
			
			if(Gdx.input.isKeyJustPressed(Keys.P)) { // TEST. Should probably move this to another class.
				
				MovingPlatform box = PlayScreen.boxPool.obtain();
				box.setPositionAll(PlayScreen.clickPos);
				box.init();
				box.setColor(Color.RED);
				PlayScreen.gameObjects.add(box);

			}
			
		} 
		
		
	}

	public void removeChunk() {
		poolC = level.get(p);
		if (poolC != null && poolC.getBlockCount() == 0) {
			chunkPool.free(poolC);
			level.remove(p);
			System.out.println("Deleted Chunk!");
		} 
	}

	IntArray queue = new IntArray();
	int x, y, count;
	public void floodfill() {
		count = 0;
		
		floodStart = CollideManager.getCell(x, y, layer);
		if (floodStart == selection || selection == Tile.Chunk)
			return; 
		queue.add(x);
		queue.add(y);
		while (!queue.isEmpty()) {
			y = queue.pop();
			x = queue.pop();
			count++;
			setTile(x, y, selection);
			if (CollideManager.getCellBounded(x + 1, y, layer) == floodStart) {
				queue.add(x + 1);
				queue.add(y);
			} 
			if (CollideManager.getCellBounded(x - 1, y, layer) == floodStart) {
				queue.add(x - 1);
				queue.add(y);
			} 
			if (CollideManager.getCellBounded(x, y + 1, layer) == floodStart) {
				queue.add(x);
				queue.add(y + 1);
			} 
			if (CollideManager.getCellBounded(x, y - 1, layer) == floodStart) {
				queue.add(x);
				queue.add(y - 1);
			} 
		} 
		System.out.println("finished, filled " + count + " tiles");
	}

	public void setTile(int x, int y, Tile t) { // setting tiles is bounded to chunk your mouse in in
		
		p.set(x / chunkWidth, y / chunkHeight);
		
		poolC = level.get(p);
		if (poolC != null) {
			
			set(poolC, x % chunkWidth, y % chunkHeight, t, layer);
			
		} 
	}

	public void setTile(Tile t) {

		if (level.get(p) == null && t != Tile.Empty) {
			
			poolC = chunkPool.obtain();
			
			poolC.setP(p.getX(), p.getY());

			poolC.setDimension(this, chunkWidth, chunkHeight);

			level.put(poolC.getP(), poolC);
			
		} 

		if(level.get(p) != null)
			set(level.get(p), Math.max(x % chunkWidth, 0), Math.max(y % chunkHeight, 0), t, layer);
		
		
	}
	
	public void set(Chunk c, int x, int y, Tile t, int layer) {
		c.setChunk(x, y, t, layer);
		setType(c, x, y, t, layer);
	}

	public void setType(Chunk c, int x, int y, Tile t, int layer) {
		
		switch (t) {
		default:
			return;
		case Empty:
			c.setChunkType(x, y, TileType.Empty, layer);
			break;
		case Debug:
		case Dirt:
		case Cave:
			c.setChunkType(x, y, TileType.Solid, layer);
			break;
		case DirtSlopeRight:
		case CaveSlopeRight:
			c.setChunkType(x, y, TileType.SlopeRight, layer);
			break;
		case DirtSlopeLeft:
		case CaveSlopeLeft:
			c.setChunkType(x, y, TileType.SlopeLeft, layer);
			break;
		} 
		
	}
	
	Array<Chunk> chunker = new Array<Chunk>();

	BoundingBox box =  new BoundingBox();
	Vector3 boxmin = new Vector3();
	Vector3 boxmax = new Vector3();

	public int layer;
	
	public void render(SpriteBatch sb, ShapeDrawer sd) {
		
		sb.setColor(Color.WHITE);
		count = 0;
		
		chunker.clear();

		
		for (ObjectMap.Entry<Coordinate, Chunk> entry : (Iterable<ObjectMap.Entry<Coordinate, Chunk>>)level.entries()) {

			boxmin.set(entry.value.getP().getX() * PlayScreen.level.getTilesize() * PlayScreen.level.getChunkWidth(), 
					entry.value.getP().getY() * PlayScreen.level.getTilesize() * PlayScreen.level.getChunkHeight(), 0);

			
			boxmax.set((entry.value.getP().getX() + 1) * PlayScreen.level.getTilesize() * PlayScreen.level.getChunkWidth(), 
					(entry.value.getP().getY() + 1) * PlayScreen.level.getTilesize() * PlayScreen.level.getChunkHeight(), 0);

			box.set(boxmin, boxmax);
			
			if (PlayScreen.gameCamera.getCamera().frustum.boundsInFrustum(box)) {
				entry.value.render(sb, sd);
				chunker.add(entry.value);
				count++;
			} 
		} 

		for (Chunk c : chunker) {
			c.renderEntity(sb, sd);
		}
		
		renderUpdate(sb);

		
	}

	public int getTilesize() {
		return tilesize;
	}

	public void setTilesize(int tilesize) {
		this.tilesize = tilesize;
	}

	public ObjectMap<Coordinate, Chunk> getLevel() {
		return level;
	}

	public void setLevel(ObjectMap<Coordinate, Chunk> level) {
		this.level = level;
	}

	public int getChunkHeight() {
		return chunkHeight;
	}

	public void setChunkHeight(int chunkHeight) {
		this.chunkHeight = chunkHeight;
	}

	public int getChunkWidth() {
		return chunkWidth;
	}

	public void setChunkWidth(int chunkWidth) {
		this.chunkWidth = chunkWidth;
	}

	public int getCount() {
		return count;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
