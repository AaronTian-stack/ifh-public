package Game.level;

import Screens.PlayScreen;
import Tiles.Coordinate;
import Tiles.Enums.Tile;
import Tiles.Enums.TileType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import com.badlogic.gdx.utils.Pool.Poolable;

import Collision.CollideManager;
import Game.abstraction.GameObject;
import Game.shared.GameSprites;

import java.util.Arrays;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Chunk extends GameObject implements Poolable{
	
	private Tile[][][] chunk;

	private TileType[][][] chunkType;

	private Level level;

	private int width, height;

	private int blockCount = 0;

	private Coordinate p;

	private Color debugColor;

	int cellX, cellY;

	float x, y;

	public Chunk() {
		this.debugColor = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
		this.p = new Coordinate(0, 0);
	}
	
	public void setDimension(Level level, int width, int height) {
		this.width = width;
		this.height = height;
		this.level = level;
		
		this.chunk = new Tile[4][height][width];
		this.chunkType = new TileType[4][height][width];

		
		reset();
	}

	public OrderedSet<GameObject> levelObjects = new OrderedSet<GameObject>();
	public Array<GameObject> levelObjectsArray = new Array<GameObject>();

	public void update(float dt) {
		
		levelObjects.clear();
		
	}

	public void render(SpriteBatch sb, ShapeDrawer sd) {
		//update(Gdx.graphics.getDeltaTime());
		for(int k = chunk.length - 1; k>=0; k--) {
			
			setColor(k);
			
			for (int i = 0; i < height; i++) {
				y = (i * level.getTilesize() + p.getY() * level.getTilesize() * height) + level.getTilesize() / 2;
				
				for (int j = 0; j < width; j++) {
					x = (j * level.getTilesize() + p.getX() * level.getTilesize() * width) + level.getTilesize() / 2;

					
					if (chunkType[k][i][j] != TileType.Empty) {
						if (PlayScreen.debugMode)
							switch(k) {
							case 0:
								sd.rectangle(x - level.getTilesize() / 2, y - level.getTilesize() / 2, level.getTilesize(), level.getTilesize(), Color.RED, PlayScreen.imageScale);
								break;
							case 1:
								sd.rectangle(x - level.getTilesize() / 2, y - level.getTilesize() / 2, level.getTilesize(), level.getTilesize(), Color.BLUE, PlayScreen.imageScale);
								break;
							case 2:
								sd.rectangle(x - level.getTilesize() / 2, y - level.getTilesize() / 2, level.getTilesize(), level.getTilesize(), Color.YELLOW, PlayScreen.imageScale);
								break;
							case 3:
								sd.rectangle(x - level.getTilesize() / 2, y - level.getTilesize() / 2, level.getTilesize(), level.getTilesize(), Color.GREEN, PlayScreen.imageScale);
								break;
							}
							
								
						drawSwitch(sb, i, j, k);
		
						
					} else if (PlayScreen.editMode & k == chunk.length - 1) {
						sd.rectangle(x - level.getTilesize() / 2, y - level.getTilesize() / 2, level.getTilesize(), level.getTilesize(), debugColor, PlayScreen.imageScale);
					} 
					
					
					
				} 
			}
		}
	}
	
	Color l0 = Color.WHITE;
	
	Color l1 = Color.LIGHT_GRAY;
	
	Color l2 = Color.GRAY;
	
	Color l3 = Color.DARK_GRAY;
	
	float in = 0.8f;
	
	Color tran = new Color(in,in,in,0.5f);
	
	public void setColor(int layer) {
		
		if(PlayScreen.editMode) {
			if(layer != level.layer)
				switchColor(layer);
			else
				GameSprites.setColor(Color.WHITE);
			return;
		}
		
		switch(layer){
		case 0:
			GameSprites.setColor(l0);
			break;
		case 1:
			GameSprites.setColor(l1);
			break;
			
		case 2:
			GameSprites.setColor(l2);
			break;
			
		case 3:
			GameSprites.setColor(l3);
			break;
		}
	}
	
	Color redT = new Color(1,0,0,0.5f);
	
	Color blueT = new Color(0,0,1,0.5f);
	
	Color yellowT = new Color(1,1,0,0.5f);
	
	Color greenT = new Color(0,1,0,0.5f);
	
	public void switchColor(int layer) {
		switch(layer){
		case 0:
			GameSprites.setColor(redT);
			break;
		case 1:
			GameSprites.setColor(blueT);
			break;
			
		case 2:
			GameSprites.setColor(yellowT);
			break;
			
		case 3:
			GameSprites.setColor(greenT);
			break;
		}
	}
	
	public void drawSwitch(SpriteBatch sb, int i, int j, int layer) {
		
		drawSwitch(sb, i, j, chunk[layer][i][j], layer);
		
	}

	@SuppressWarnings("incomplete-switch")
	public void drawSwitch(SpriteBatch sb, int i, int j, Tile selection, int layer) {
		
		x = (j * level.getTilesize() + p.getX() * level.getTilesize() * width) + level.getTilesize() / 2;
		y = (i * level.getTilesize() + p.getY() * level.getTilesize() * height) + level.getTilesize() / 2;

		switch (selection) {
		case Debug:
			GameSprites.debug.setOriginBasedPosition(x, y);
			GameSprites.debug.draw(sb);
			break;
		case Dirt:
			drawWithCorners(layer, sb, i, j, GameSprites.dirt, GameSprites.dirtIn1, GameSprites.dirtIn2, GameSprites.dirtIn3, GameSprites.dirtIn4, 
					
					GameSprites.dirtIn5, GameSprites.dirtIn6, GameSprites.dirtIn7, GameSprites.dirtIn8, GameSprites.dirt1, 
					
					GameSprites.dirtC, GameSprites.dirtP, GameSprites.dirt3, 
					
					GameSprites.dirtA, GameSprites.corner, GameSprites.grassC, GameSprites.grassf, GameSprites.grassf3);
			break;
		case DirtSlopeRight:
			GameSprites.dirtslope.setOriginBasedPosition(x, y);
			GameSprites.dirtslope.setFlip(false, false);
			GameSprites.dirtslope.draw(sb);
			break;
		case DirtSlopeLeft:
			GameSprites.dirtslope.setOriginBasedPosition(x, y);
			GameSprites.dirtslope.setFlip(true, false);
			GameSprites.dirtslope.draw(sb);
			break;
		case Cave:
			genericDraw(layer, sb, i, j, GameSprites.cave, GameSprites.cave1, GameSprites.caveA, GameSprites.caveC, GameSprites.caveS, GameSprites.cave3, GameSprites.caveP);
			break;
		case CaveSlopeRight:
			GameSprites.caveS.setOriginBasedPosition(x, y);
			GameSprites.caveS.setFlip(false, false);
			GameSprites.caveS.draw(sb);
			break;
		case CaveSlopeLeft:
			GameSprites.caveS.setOriginBasedPosition(x, y);
			GameSprites.caveS.setFlip(true, false);
			GameSprites.caveS.draw(sb);
			break;
		} 
	}
	
	public void renderEntity(SpriteBatch sb, ShapeDrawer sd) {
		
		levelObjectsArray.addAll(levelObjects.orderedItems());
		
		//levelObjectsArray.sort(); // from greatest to least z index
		
		//System.out.println(levelObjectsArray);

		if(levelObjectsArray.size > 0 && levelObjectsArray.get(levelObjectsArray.size-1) == PlayScreen.player) 
			levelObjectsArray.removeIndex(levelObjectsArray.size-1);
		
		for (GameObject levOb : levelObjectsArray) {
				if(!PlayScreen.rendered.contains(levOb, true)) {
					//levOb.render(sb, sd);
					PlayScreen.rendered.add(levOb);
				}
		}
		
		levelObjectsArray.clear();
	}
	
	int orient;
	
	public void genericDraw(int layer, SpriteBatch sb, int i, int j, Sprite cave, Sprite cave1, Sprite caveA, Sprite caveC, Sprite caveS, Sprite cave3, Sprite caveP) {
		getSurround(layer, j, i, 1);
		switch (orient) {
		case 0:
			caveA.setOriginBasedPosition(x, y);
			caveA.draw(sb);
			break;
		case 1:
			cave3.setOriginBasedPosition(x, y);
			cave3.setRotation(0);
			cave3.setFlip(false, true);
			cave3.draw(sb);
			break;
		case 2:
			cave3.setOriginBasedPosition(x, y);
			cave3.setRotation(90);
			cave3.setFlip(false, false);
			cave3.draw(sb);
			break;
		case 3:
			caveC.setOriginBasedPosition(x, y);
			caveC.setRotation(0);
			caveC.setFlip(true, true);
			caveC.draw(sb);
			break;
		case 4:
			cave3.setOriginBasedPosition(x, y);
			cave3.setRotation(0);
			cave3.setFlip(false, false);
			cave3.draw(sb);
			break;
		case 5:
			caveP.setOriginBasedPosition(x, y);
			caveP.setRotation(90);
			caveP.setFlip(false, false);
			caveP.draw(sb);
			break;
		case 6:
			caveC.setOriginBasedPosition(x, y);
			caveC.setRotation(0);
			caveC.setFlip(true, false);
			caveC.draw(sb);
			break;
		case 7:
			cave1.setOriginBasedPosition(x, y);
			cave1.setRotation(90);
			cave1.draw(sb);
			break;
		case 8:
			cave3.setOriginBasedPosition(x, y);
			cave3.setRotation(-90);
			cave3.setFlip(false, false);
			cave3.draw(sb);
			break;
		case 9:
			caveC.setOriginBasedPosition(x, y);
			caveC.setRotation(0);
			caveC.setFlip(false, true);
			caveC.draw(sb);
			break;
		case 10:
			caveP.setOriginBasedPosition(x, y);
			caveP.setRotation(0);
			caveP.setFlip(false, false);
			caveP.draw(sb);
			break;
		case 11:
			cave1.setOriginBasedPosition(x, y);
			cave1.setRotation(180);
			cave1.draw(sb);
			break;
		case 12:
			caveC.setOriginBasedPosition(x, y);
			caveC.setRotation(0);
			caveC.setFlip(false, false);
			caveC.draw(sb);
			break;
		case 13:
			cave1.setOriginBasedPosition(x, y);
			cave1.setRotation(-90);
			cave1.draw(sb);
			break;
		case 14:
			cave1.setOriginBasedPosition(x, y);
			cave1.setRotation(0);
			cave1.draw(sb);
			break;
		case 15:
			cave.setOriginBasedPosition(x, y);
			cave.draw(sb);
			break;
		} 
	}

	public void drawWithCorners(int layer, SpriteBatch sb, int i, int j, Sprite dirt, Sprite dirtIn1, Sprite dirtIn2, Sprite dirtIn3, Sprite dirtIn4, Sprite dirtIn5, Sprite dirtIn6, Sprite dirtIn7, Sprite dirtIn8,
			Sprite dirt1, Sprite dirtC, Sprite dirtP, Sprite dirt3, Sprite dirtA, Sprite corner, Sprite grassC, Sprite grassf, Sprite grassf3) {
		getSurround(layer, j, i, 1);
		switch (orient) {
		case 0:
			dirtA.setOriginBasedPosition(x, y);
			dirtA.draw(sb);
			grassf3.setOriginBasedPosition(x, y);
			grassf3.draw(sb);
			break;
		case 1:
			dirt3.setOriginBasedPosition(x, y);
			dirt3.setRotation(0);
			dirt3.setFlip(false, true);
			dirt3.draw(sb);
			break;
		case 2:
			dirt3.setOriginBasedPosition(x, y);
			dirt3.setRotation(90);
			dirt3.setFlip(false, false);
			dirt3.draw(sb);
			grassC.setOriginBasedPosition(x, y);
			grassC.setFlip(true, false);
			grassC.draw(sb);
			break;
		case 3:
			dirtC.setOriginBasedPosition(x, y);
			dirtC.setRotation(0);
			dirtC.setFlip(true, true);
			dirtC.draw(sb);
			corner3(corner, sb, i, j, x, y, layer);
			break;
		case 4:
			dirt3.setOriginBasedPosition(x, y);
			dirt3.setRotation(0);
			dirt3.setFlip(false, false);
			dirt3.draw(sb);
			grassf3.setOriginBasedPosition(x, y);
			grassf3.setFlip(true, false);
			grassf3.draw(sb);
			break;
		case 5:
			dirtP.setOriginBasedPosition(x, y);
			dirtP.setRotation(90);
			dirtP.setFlip(false, false);
			dirtP.draw(sb);
			break;
		case 6:
			dirtC.setOriginBasedPosition(x, y);
			dirtC.setRotation(0);
			dirtC.setFlip(true, false);
			dirtC.draw(sb);
			corner6(corner, sb, i, j, x, y, layer);
			grassC.setOriginBasedPosition(x, y);
			grassC.setFlip(true, false);
			grassC.draw(sb);
			break;
		case 7:
			dirt1.setOriginBasedPosition(x, y);
			dirt1.setRotation(90);
			dirt1.draw(sb);
			corner3(corner, sb, i, j, x, y, layer);
			corner6(corner, sb, i, j, x, y, layer);
			break;
		case 8:
			dirt3.setOriginBasedPosition(x, y);
			dirt3.setRotation(-90);
			dirt3.setFlip(false, false);
			dirt3.draw(sb);
			grassC.setOriginBasedPosition(x, y);
			grassC.setFlip(false, false);
			grassC.draw(sb);
			break;
		case 9:
			dirtC.setOriginBasedPosition(x, y);
			dirtC.setRotation(0);
			dirtC.setFlip(false, true);
			dirtC.draw(sb);
			corner9(corner, sb, i, j, x, y, layer);
			break;
		case 10:
			dirtP.setOriginBasedPosition(x, y);
			dirtP.setRotation(0);
			dirtP.setFlip(false, false);
			dirtP.draw(sb);
			grassf.setOriginBasedPosition(x, y);
			grassf.draw(sb);
			break;
		case 11:
			dirt1.setOriginBasedPosition(x, y);
			dirt1.setRotation(180);
			dirt1.draw(sb);
			corner3(corner, sb, i, j, x, y, layer);
			corner9(corner, sb, i, j, x, y, layer);
			break;
		case 12:
			dirtC.setOriginBasedPosition(x, y);
			dirtC.setRotation(0);
			dirtC.setFlip(false, false);
			dirtC.draw(sb);
			corner12(corner, sb, i, j, x, y, layer);
			grassC.setOriginBasedPosition(x, y);
			grassC.setFlip(false, false);
			grassC.draw(sb);
			break;
		case 13:
			dirt1.setOriginBasedPosition(x, y);
			dirt1.setRotation(-90);
			dirt1.draw(sb);
			corner12(corner, sb, i, j, x, y, layer);
			corner9(corner, sb, i, j, x, y, layer);
			break;
		case 14:
			dirt1.setOriginBasedPosition(x, y);
			dirt1.setRotation(0);
			dirt1.draw(sb);
			corner6(corner, sb, i, j, x, y, layer);
			corner12(corner, sb, i, j, x, y, layer);
			grassf.setOriginBasedPosition(x, y);
			grassf.draw(sb);
			break;
		case 15:
			
			getSurround(layer, j, i, 2);

				
			switch(orient) {
			case 0:
				dirtIn1.setOriginBasedPosition(x, y);
				dirtIn1.draw(sb);
				break;
			case 1:
				dirtIn2.setOriginBasedPosition(x, y);
				dirtIn2.draw(sb);
				break;
			case 2:
				dirtIn5.setOriginBasedPosition(x, y);
				dirtIn5.draw(sb);
				break;
			case 3:
				dirtIn6.setOriginBasedPosition(x, y);
				dirtIn6.draw(sb);
				break;
			case 4:
				dirtIn7.setOriginBasedPosition(x, y);
				dirtIn7.draw(sb);
				break;
			case 5:
				dirtIn6.setOriginBasedPosition(x, y);
				dirtIn6.draw(sb);
				break;
			case 6:
				dirtIn1.setOriginBasedPosition(x, y);
				dirtIn1.draw(sb);
				break;
			case 7:
				dirtIn4.setOriginBasedPosition(x, y);
				dirtIn4.draw(sb);
				break;
			case 8:
				dirtIn4.setOriginBasedPosition(x, y);
				dirtIn4.draw(sb);
				break;
			case 9:
				dirtIn8.setOriginBasedPosition(x, y);
				dirtIn8.draw(sb);
				break;
			case 10:
				dirtIn8.setOriginBasedPosition(x, y);
				dirtIn8.draw(sb);
				break;
			case 11:
				dirtIn7.setOriginBasedPosition(x, y);
				dirtIn7.draw(sb);
				break;
			case 12:
				dirtIn3.setOriginBasedPosition(x, y);
				dirtIn3.draw(sb);
				break;
			case 13:
				dirtIn2.setOriginBasedPosition(x, y);
				dirtIn2.draw(sb);
				break;
			case 14:
				dirtIn2.setOriginBasedPosition(x, y);
				dirtIn2.draw(sb);
				break;
			case 15:
				dirt.setOriginBasedPosition(x, y);
				dirt.draw(sb);
				break;
				
			}
			
			
			corner3(corner, sb, i, j, x, y, layer);
			corner6(corner, sb, i, j, x, y, layer);
			corner9(corner, sb, i, j, x, y, layer);
			corner12(corner, sb, i, j, x, y, layer);
			break;
		} 
	}

	public void corner3(Sprite corner, SpriteBatch sb, int i, int j, float x, float y, int layer) {

		j += p.getX() * level.getChunkWidth();
		i += p.getY() * level.getChunkHeight();
		
		if (CollideManager.getCell(j+1, i+1, layer) == Tile.Empty) {
			corner.setOriginBasedPosition(x, y);
			corner.setFlip(true, true);
			corner.draw(sb);
			
		} 

	}

	public void corner6(Sprite corner, SpriteBatch sb, int i, int j, float x, float y, int layer) {
		
		j += p.getX() * level.getChunkWidth();
		i += p.getY() * level.getChunkHeight();
		
		if (CollideManager.getCell(j+1, i-1, layer) == Tile.Empty) {
			corner.setOriginBasedPosition(x, y);
			corner.setFlip(true, false);
			corner.draw(sb);
		} 
	}

	public void corner12(Sprite corner, SpriteBatch sb, int i, int j, float x, float y, int layer) {
		
		j += p.getX() * level.getChunkWidth();
		i += p.getY() * level.getChunkHeight();
		
		if (CollideManager.getCell(j-1, i-1, layer) == Tile.Empty) {
			corner.setOriginBasedPosition(x, y);
			corner.setFlip(false, false);
			corner.draw(sb);
		} 
	}

	public void corner9(Sprite corner, SpriteBatch sb, int i, int j, float x, float y, int layer) {
		
		j += p.getX() * level.getChunkWidth();
		i += p.getY() * level.getChunkHeight();
		
		if (CollideManager.getCell(j-1, i+1, layer) == Tile.Empty) {
			corner.setOriginBasedPosition(x, y);
			corner.setFlip(false, true);
			corner.draw(sb);
		} 
	}

	public void getSurround(int layer, int x, int y, int rad) {
		
		x += p.getX() * level.getChunkWidth();
		y += p.getY() * level.getChunkHeight();

		orient = 0;
		
		if (CollideManager.getCellType(x - rad, y, layer) != TileType.Empty)
			orient += 8; 
		if (CollideManager.getCellType(x + rad, y, layer) != TileType.Empty)
			orient += 2; 
		if (CollideManager.getCellType(x, y - rad, layer) != TileType.Empty)
			orient += 4; 
		if (CollideManager.getCellType(x, y + rad, layer) != TileType.Empty)
			orient++; 
		

	}



	public Tile[][][] getChunk() {
		return this.chunk;
	}

	public TileType[][][] getChunkType() {
		return this.chunkType;
	}

	public void setChunk(int x, int y, Tile value, int layer) {
		if (chunk[layer][y][x] == Tile.Empty && value != Tile.Empty)
			blockCount++; 
		if (chunk[layer][y][x] != Tile.Empty && value == Tile.Empty)
			blockCount--; 
		chunk[layer][y][x] = value;
	}

	public void setChunkType(int x, int y, TileType value, int layer) {
		chunkType[layer][y][x] = value;
	}

	public Coordinate getP() {
		return this.p;
	}

	public void setP(int x, int y) {
		p.set(x, y);
	}

	public int getBlockCount() {
		return blockCount;
	}

	public void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		for(int i=0;i<chunk.length;i++) {
			
			for(int j=0;j<chunk[0].length;j++) {
				Arrays.fill(chunk[i][j], Tile.Empty);
				Arrays.fill(chunkType[i][j], TileType.Empty);
			}

			
		}
		
	}


}
