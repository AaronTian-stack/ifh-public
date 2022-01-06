package Collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import Game.abstraction.GameObject;
import Game.level.Chunk;
import Screens.PlayScreen;
import Tiles.Coordinate;
import Tiles.Enums.Direction;
import Tiles.Enums.Tile;
import Tiles.Enums.TileType;
import Game.CollideInfo;

public class CollideManager {
	static Coordinate loop = new Coordinate(0, 0);

	static int x1 = 0;

	static int x2 = 0;

	static int y1 = 0;

	static int y2 = 0;

	static int cellX, cellY;

	static int yUT, yDT, xLT, xRT;

	static int bx, by;

	static float rX, rY;

	static float playerL, playerR, playerU, playerD;

	static Array<Direction> collideDirectionsStatic = new Array<Direction>();
	
	static Array<Direction> collideDirectionsDynamic = new Array<Direction>();

	public static Array<Direction> collideStatic(GameObject go, boolean resolve) { // return list of all collide resolutions

		collideDirectionsStatic.clear();
		
		if(resolve) {
			go.setGround(false);
			//go.setRamp(false);
		}
		x1 = (int)(((go.getPosition()).x - (go.getRect()).width / 2.0f) / PlayScreen.level.getTilesize());
		x2 = (int)(((go.getPosition()).x + (go.getRect()).width / 2.0f) / PlayScreen.level.getTilesize());
		y1 = (int)(((go.getPosition()).y - (go.getRect()).height / 2.0f) / PlayScreen.level.getTilesize());
		y2 = (int)(((go.getPosition()).y + (go.getRect()).height / 2.0f) / PlayScreen.level.getTilesize());
		if ((go.getPosition()).x - (go.getRect()).width / 2.0f < 0.0f) {
			(go.getPosition()).x = (go.getRect()).width / 2.0f; 
			collideDirectionsStatic.add(Direction.Right);
		}
		if ((go.getPosition()).y - (go.getRect()).height / 2.0f < 0.0f) {
			(go.getPosition()).y = (go.getRect()).height / 2.0f;
			go.setVelocity((go.getVelocity()).x, 0.0f);
			go.setGround(true);
			collideDirectionsStatic.add(Direction.Top);
		} 
		playerL = (go.getPosition()).x - (go.getRect()).width / 2f;
		playerR = (go.getPosition()).x + (go.getRect()).width / 2f;
		playerU = (go.getPosition()).y + (go.getRect()).height / 2f;
		playerD = (go.getPosition()).y - (go.getRect()).height / 2f;
		for (int i = y1; i <= y2; i++) {
			yUT = (i + 1) * PlayScreen.level.getTilesize();
			yDT = i * PlayScreen.level.getTilesize();
			by = i * PlayScreen.level.getTilesize() + PlayScreen.level.getTilesize() / 2;
			for (int j = x1; j <= x2; j++) {
				xLT = j * PlayScreen.level.getTilesize();
				xRT = (j + 1) * PlayScreen.level.getTilesize();
				bx = j * PlayScreen.level.getTilesize() + PlayScreen.level.getTilesize() / 2;
				
				switch (getCellType(j, i)) {
				case Solid:
					
					if(resolve) {
						switch (intersection((go.getPosition()).x, (go.getPosition()).y, j, i, loop)) {
						case Top:			
							collideDirectionsStatic.add(Direction.Top);
							go.setGround(true);
							
							if(go.getBounceVertical() != 0)
								go.setVelocity((go.getVelocity()).x, go.getVelocity().y * go.getBounceVertical());
							else
								go.setVelocity((go.getVelocity()).x, 0);
							
							go.setPosition((go.getPosition()).x, yUT + (go.getRect()).height / 2f);
							
							
							break;
						case Bottom:
							collideDirectionsStatic.add(Direction.Bottom);
							
							go.setPosition((go.getPosition()).x, yDT - (go.getRect()).height / 2f);
							go.setVelocity((go.getVelocity()).x, (go.getVelocity()).y * 0.2f);
							
							break;
						case Left:
							collideDirectionsStatic.add(Direction.Left);
							
							go.setPosition(xLT - (go.getRect()).width / 2f, (go.getPosition()).y);
							// TEST
							go.setVelocity(0, go.getVelocity().y);
							if(go.getBounceHorizontal() != 0)
								go.setVelocity(go.getVelocity().x * go.getBounceHorizontal(), go.getVelocity().y);
							
							break;
						case Right:
	
							collideDirectionsStatic.add(Direction.Right);
							
							go.setPosition(xRT + (go.getRect()).width / 2f, (go.getPosition()).y);
							// TEST
							go.setVelocity(0, go.getVelocity().y);
							if(go.getBounceHorizontal() != 0)
								go.setVelocity(go.getVelocity().x * go.getBounceHorizontal(), go.getVelocity().y);
							
							break;
						case NONE:
							collideDirectionsStatic.add(Direction.NONE);
							break;
						
						} 
					}
					else {
						collideDirectionsStatic.add(intersection((go.getPosition()).x, (go.getPosition()).y, j, i, loop));
					}
					
					break;
				case SlopeLeft:
					
					rX = playerR - xLT;
					rY = yUT - playerD;
					
					if (rX <= PlayScreen.level.getTilesize() && rX + rY > PlayScreen.level.getTilesize()) {
						
						go.setGround(true);
						go.setVelocity((go.getVelocity()).x, 0);
						
						go.addPosition(0, Math.abs(rX+rY-PlayScreen.level.getTilesize()));
						go.setRamp(true);
						//go.setRampdirection(true);
					} 
					break;
					
				case SlopeRight:
					
					rX = xRT - playerL;
					rY = yUT - playerD;
					
					if (rX <= PlayScreen.level.getTilesize() && rX + rY - PlayScreen.level.getTilesize() > 0) {
						
						go.setGround(true);
						go.setVelocity((go.getVelocity()).x, 0);
						go.addPosition(0, Math.abs(rX+rY-PlayScreen.level.getTilesize()));
						go.setRamp(true);
						//go.setRampdirection(false);
					} 
					break;
				case Empty:
					break;
				case OUTOFBOUNDS:
					break;
				default:
					break;
				
					
				}
				
			} 
		} 
		
		//System.out.println(collideDirectionsStatic);
		
		return collideDirectionsStatic;
	}
	
	static float leftD, rightD, downD, upD;
	
	static CollideInfo pojo = new CollideInfo();
	
	static Direction direct;
	
	static Array<GameObject> resolved = new Array<GameObject>();

	public static CollideInfo collideDynamic(GameObject go, boolean resolve, boolean order) {
		
		pojo.collide = null;

		collideDirectionsDynamic.clear();
		
		resolved.clear();

		for (Chunk c : go.getInsideChunks()) {

			for (GameObject lo : c.levelObjects) {
				
				if(go.getType() == lo.getType() || resolved.contains(lo, true) || go == lo || lo.isDead() ) { 	
					continue;
				}
				
				if(order)
					direct = dynamicResolve(go, lo, resolve);
				else
					direct = dynamicResolve(lo, go, resolve);
			
				
				if(direct!=Direction.NONE) {
					pojo.collide = lo;
					resolved.add(pojo.collide);
					
				}
				
				collideDirectionsDynamic.add(direct);
					

			}
			
			
		}
		
		pojo.directions = collideDirectionsDynamic;
		
		return pojo;
		
	}
	
	static Rectangle gor = new Rectangle();
	static Rectangle lor = new Rectangle();
	//static float tolerance = 0.95f;
	
	public static boolean overlap(GameObject go, float tolerance) {
		
		for (Chunk c : go.getInsideChunks()) {

			for (GameObject lo : c.levelObjects) {

				if(go.getType() == lo.getType() || go == lo || lo.isDead())
					continue;
				
				gor.set(go.getRect());
				gor.width *= tolerance;
				gor.height *= tolerance;
				gor.setCenter(go.getPosition().x, go.getPosition().y);
				
				lor.set(lo.getRect());
				lor.width *= tolerance;
				lor.height *= tolerance;
				lor.setCenter(lo.getPosition().x, lo.getPosition().y);

				if(gor.overlaps(lor))
					return true;

			}
			
			
		}
		return false;
	}
	
	public static boolean overlapSingle(GameObject go, GameObject lo, float scale) {
		gor.set(go.getRect());
		gor.width *= scale;
		gor.height *= scale;
		gor.setCenter(go.getPosition().x, go.getPosition().y);
		
		lor.set(lo.getRect());
		lor.width *= scale;
		lor.height *= scale;
		lor.setCenter(lo.getPosition().x, lo.getPosition().y);

		return gor.overlaps(lor);
	}
	
	public static Direction dynamicResolve(GameObject go, GameObject lo, boolean resolve) { // go is moved against lo

		leftD = (lo.getPosition()).x - (lo.getRect()).width / 2f;
		rightD = (lo.getPosition()).x + (lo.getRect()).width / 2f;
		upD = (lo.getPosition()).y + (lo.getRect()).height / 2f;
		downD = (lo.getPosition()).y - (lo.getRect()).height / 2f;
		
		if(resolve) {
			switch(intersectionDynamic(go, lo)) {
			case Top:
				go.setGround(true);
				if(go.getBounceVertical() != 0)
					go.setVelocity((go.getVelocity()).x, go.getVelocity().y * go.getBounceVertical());
				else
					go.setVelocity((go.getVelocity()).x, 0);
				go.vZero(); //otherwise the velocity will ping pong and never end
				go.setPosition((go.getPosition()).x, upD + (go.getRect()).height / 2f); 
				return Direction.Top;
			case Bottom:

				go.setPosition((go.getPosition()).x, downD - (go.getRect()).height / 2f); 
				go.setVelocity((go.getVelocity()).x, (go.getVelocity()).y * 0.2f);
				return Direction.Bottom;
			case Left:
				if(go.getBounceHorizontal() != 0)
					go.setVelocity(go.getVelocity().x * go.getBounceHorizontal(), go.getVelocity().y);
				else
					go.setVelocity(0, go.getVelocity().y);
				go.setPosition(leftD - (go.getRect()).width / 2f, (go.getPosition()).y); 
				return Direction.Left;
			case Right:
				if(go.getBounceHorizontal() != 0)
					go.setVelocity(go.getVelocity().x * go.getBounceHorizontal(), go.getVelocity().y);
				else
					go.setVelocity(0, go.getVelocity().y);
				go.setPosition(rightD + (go.getRect()).width / 2f, (go.getPosition()).y); 
				return Direction.Right;
			case NONE:
				return Direction.NONE;
				
			}
		}
		else 
			return intersectionDynamic(go, lo);
		
		return Direction.NONE;

	}

	public static Tile getCell(float x, float y) {
		return getCell((int)(x/PlayScreen.level.getTilesize()),(int)(y/PlayScreen.level.getTilesize()));
	}

	public static Tile getCell(int x, int y, int layer) {
		loop.setX(x / PlayScreen.level.getChunkWidth());
		loop.setY(y / PlayScreen.level.getChunkHeight());
		if (PlayScreen.level.getLevel().get(loop) != null) {
			x = Math.max(x % PlayScreen.level.getChunkWidth(), 0);
			y = Math.max(y % PlayScreen.level.getChunkHeight(), 0);
			return (PlayScreen.level.getLevel().get(loop)).getChunk()[layer][y][x];
		} 
		return Tile.Empty;
	}
	
	public static Tile getCell(int x, int y) {
		return getCell(x, y, 0);
	}

	public static Tile getCellBounded(int x, int y, int layer) {
		if (x < 0 || y < 0)
			return Tile.OUTOFBOUNDS; 
		loop.setX(x / PlayScreen.level.getChunkWidth());
		loop.setY(y / PlayScreen.level.getChunkHeight());
		if (PlayScreen.level.getLevel().get(loop) != null) {
			x = Math.max(x % PlayScreen.level.getChunkWidth(), 0);
			y = Math.max(y % PlayScreen.level.getChunkHeight(), 0);
			return (PlayScreen.level.getLevel().get(loop)).getChunk()[layer][y][x];
		} 
		return Tile.OUTOFBOUNDS;
	}
	
	public static TileType getCellType(float x, float y) {
		return getCellType((int)(x/PlayScreen.level.getTilesize()),(int)(y/PlayScreen.level.getTilesize()));
	}

	public static TileType getCellType(int x, int y) {
		return getCellType(x, y, 0);
	}
	
	public static TileType getCellType(int x, int y, int layer) {
		loop.setX(x / PlayScreen.level.getChunkWidth());
		loop.setY(y / PlayScreen.level.getChunkHeight());
		if (PlayScreen.level.getLevel().get(loop) != null) {
			x = Math.max(x % PlayScreen.level.getChunkWidth(), 0);
			y = Math.max(y % PlayScreen.level.getChunkHeight(), 0);
			return (PlayScreen.level.getLevel().get(loop)).getChunkType()[layer][y][x];
		} 
		return TileType.Empty;
	}
	
	static int direction = 0;

	public static Direction intersection(float x, float y, int cellX, int cellY, Coordinate loop) {
		direction = 0;
		if (y > by && getCell(cellX, cellY + 1) == Tile.Empty) //top
			direction++; 
		if (y < by && getCell(cellX, cellY - 1) == Tile.Empty) //bottom
			direction += 4; 
		if (x < bx && getCell(cellX - 1, cellY) == Tile.Empty) //left
			direction += 8; 
		if (x > bx && getCell(cellX + 1, cellY) == Tile.Empty) //right
			direction += 2; 
		switch (direction) {
		case 1:
			return Direction.Top;
		case 2:
			return Direction.Right;
		case 4:
			return Direction.Bottom;
		case 8:
			return Direction.Left;
		case 5:
			if (Math.abs(playerD - yUT) < Math.abs(playerU - yDT))
				return Direction.Top; 
			return Direction.Bottom;
		case 3:
			if (Math.abs(playerD - yUT) < Math.abs(playerL - xRT))
				return Direction.Top; 
			return Direction.Right;
		case 6:
			if (Math.abs(playerU - yDT) < Math.abs(playerL - xRT))
				return Direction.Bottom; 
			return Direction.Right;
		case 12:
			if (Math.abs(playerU - yDT) < Math.abs(playerR - xLT))
				return Direction.Bottom; 
			return Direction.Left;
		case 9:
			if (Math.abs(playerD - yUT) < Math.abs(playerR - xLT))
				return Direction.Top; 
			return Direction.Left;
		} 
		return Direction.NONE;
	}
	

	public static Direction intersectionDynamic(GameObject go, GameObject lo) { // checks go against lo
		
		go.getRect().setCenter(go.getPosition().x, go.getPosition().y);
		lo.getRect().setCenter(lo.getPosition().x, lo.getPosition().y);

		if(!go.getRect().overlaps(lo.getRect()))
			return Direction.NONE;

		playerL = (go.getPosition()).x - (go.getRect()).width / 2f;
		playerR = (go.getPosition()).x + (go.getRect()).width / 2f;
		playerU = (go.getPosition()).y + (go.getRect()).height / 2f;
		playerD = (go.getPosition()).y - (go.getRect()).height / 2f;
		
		direction = 0;
		
		if (playerD < upD && go.getPosition().y > lo.getPosition().y) //top
			direction++;
		if (playerU > downD && go.getPosition().y < lo.getPosition().y) //bottom
			direction += 4;
		if (playerR > leftD && go.getPosition().x < lo.getPosition().x) //left
			direction += 8;
		if (playerL < rightD && go.getPosition().x > lo.getPosition().x) //right
			direction += 2;

		switch (direction) {
		case 1:
			return Direction.Top;
		case 2:
			return Direction.Right;
		case 4:
			return Direction.Bottom;
		case 8:
			return Direction.Left;
		case 5:
			if (Math.abs(playerD - upD) < Math.abs(playerU - downD) && go.getVelocity().y <= 0)
				return Direction.Top; 
			return Direction.Bottom;
		case 3:
			if (Math.abs(playerD - upD) < Math.abs(playerL - rightD))
				return Direction.Top; 
			return Direction.Right;
		case 6:
			if (Math.abs(playerU - downD) < Math.abs(playerL - rightD)) //KEEP THIS  && Math.abs(go.getVelocity().y) >= Math.abs(go.getVelocity().x)
				return Direction.Bottom; 
			return Direction.Right;
		case 12:
			if (Math.abs(playerU - downD) < Math.abs(playerR - leftD)) //KEEP THIS  && Math.abs(go.getVelocity().y) >= Math.abs(go.getVelocity().x)
				return Direction.Bottom; 
			return Direction.Left;
		case 9:
			if (Math.abs(playerD - upD) < Math.abs(playerR - leftD))
				return Direction.Top;
			return Direction.Left;
		} 

		return Direction.NONE;
		
	}
	
	public static float areaOverlap(GameObject go, GameObject lo) { /* Calculates overlap area between two gameobjects */
		
		go.getRect().setCenter(go.getPosition().x, go.getPosition().y);
		lo.getRect().setCenter(lo.getPosition().x, lo.getPosition().y);
		
		return (Math.min(go.getRect().x + go.getRect().width, lo.getRect().x + lo.getRect().width) 
				- Math.max(go.getRect().x, lo.getRect().x)) * //x
				(Math.min(go.getRect().y + go.getRect().height, lo.getRect().y + lo.getRect().height) 
						- Math.max(go.getRect().y, lo.getRect().y)); //y

	}
	
	public static float yOverlap(GameObject go, GameObject lo) { /* Exit the climb of dynamic earlier */
		
		go.getRect().setCenter(go.getPosition().x, go.getPosition().y);
		lo.getRect().setCenter(lo.getPosition().x, lo.getPosition().y);
		
		return (Math.min(go.getRect().y + go.getRect().height, lo.getRect().y + lo.getRect().height) 
				- Math.max(go.getRect().y, lo.getRect().y));
		
	}
	
}
