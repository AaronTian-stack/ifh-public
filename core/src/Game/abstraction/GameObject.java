package Game.abstraction;

import Screens.GameContainer;
import Screens.PlayScreen;
import Tiles.Coordinate;
import Tiles.Enums.GameObjectType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.OrderedSet;

import Game.level.Chunk;
import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class GameObject implements Comparable<GameObject>{
	
	protected GameObjectType type;

	protected Vector3 renderPosition;

	protected Vector3 position;

	protected Vector3 prevPosition;
	
	protected int zIndex;
	
	protected float bounceHorizontal;
	
	protected float bounceVertical;

	protected Vector3 velocity;
	
	protected Sprite frame;

	protected Rectangle rect;
	
	protected Coordinate coor;

	protected Color color;

	protected boolean ground;

	protected boolean air;

	protected boolean climb;

	protected boolean ramp;

	protected boolean dead;
	
	protected boolean orientation;
	
	protected GameObject parent;
	
	protected OrderedSet<Chunk> insideChunks = new OrderedSet<Chunk>();

	public abstract void update(float dt);

	public abstract void render(SpriteBatch sb, ShapeDrawer sd);

	public abstract void dispose();

	public void interpolate() {
		renderPosition.x = position.x * GameContainer.alpha + prevPosition.x * (1 - GameContainer.alpha);
		renderPosition.y = position.y * GameContainer.alpha + prevPosition.y * (1 - GameContainer.alpha);
	}

	Chunk chunk;

	public void addPoints() {
		
		insideChunks.clear();

		for (int i=-1;i<=1;i+=2) {
			
			for (int j=-1;j<=1;j+=2) {

				coor.set((int)((position.x + i * rect.width / 2) / PlayScreen.level.getTilesize() / PlayScreen.level.getChunkWidth()), 
						(int)((position.y + j * rect.height / 2) / PlayScreen.level.getTilesize() / PlayScreen.level.getChunkHeight())); //could do this for all four corners so no early clipping
				
	
				chunk = PlayScreen.level.getLevel().get(coor);
				
				if (chunk != null) {

					chunk.levelObjects.add(this);

					insideChunks.add(chunk);
			
				}


			}
			
		}

	}

	public void vZero() {
		if (MathUtils.isZero(velocity.x, 0.001f))
			velocity.x = 0;
		if (MathUtils.isZero(velocity.y, 0.001f))
			velocity.y = 0;
	}
	
	public void renderBox(ShapeDrawer sd, Color color) {
		sd.rectangle(renderPosition.x - rect.width / 2, renderPosition.y - rect.height / 2, rect.width,
				rect.height, color, PlayScreen.imageScale);
	}
	
	public void editReset() { // called when editmode is toggled
		
	}

	public int compareTo(GameObject lo) { 
		return Integer.compare(lo.zIndex, this.zIndex);
	}

	public Vector3 getPosition() {
		return this.position;
	}

	public void setPosition(float posX, float posY) {
		this.position.x = posX;
		this.position.y = posY;
	}
	
	public void setPosition(Vector3 position) {
		this.position.set(position);
	}
	
	public void setRenderPosition(Vector3 position) {
		this.renderPosition.x = position.x;
		this.renderPosition.y = position.y;
	}

	public void setRenderPosition(float posX, float posY) {
		this.renderPosition.x = posX;
		this.renderPosition.y = posY;
	}

	public void setPositionAll(Vector3 position) {
		this.position.set(position);
		this.prevPosition.set(position);
		this.renderPosition.set(position);
	}
	
	public void setPositionAll(float x, float y) {
		this.position.set(x, y, 0);
		this.prevPosition.set(x, y, 0);
		this.renderPosition.set(x, y, 0);
	}


	public void addPosition(float posX, float posY) {
		this.position.x += posX;
		this.position.y += posY;
	}

	public void addRenderPos(float posX, float posY) {
		this.renderPosition.x += posX;
		this.renderPosition.y += posY;
	}

	public Vector3 getVelocity() {
		return this.velocity;
	}

	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
	}

	public void setVelocity(float x, float y) {
		this.velocity.x = x;
		this.velocity.y = y;
	}

	public void addVelocity(float x, float y) {
		this.velocity.x += x;
		this.velocity.y += y;
	}

	public Rectangle getRect() {
		return this.rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}
	
	public void setSize(float x, float y) {
		this.rect.width = x;
		this.rect.height = y;
	}
	
	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isGround() {
		return this.ground;
	}

	public void setGround(boolean ground) {
		this.ground = ground;
	}

	public boolean isAir() {
		return this.air;
	}

	public void setAir(boolean air) {
		this.air = air;
	}

	public Vector3 getPrevPosition() {
		return this.prevPosition;
	}

	public void setPrevPosition(Vector3 prevPosition) {
		this.prevPosition = prevPosition;
	}

	public Vector3 getRenderPosition() {
		return this.renderPosition;
	}

	public GameObjectType getType() {
		return this.type;
	}

	public void setType(GameObjectType type) {
		this.type = type;
	}

	public boolean isClimb() {
		return this.climb;
	}

	public void setClimb(boolean climb) {
		this.climb = climb;
	}

	public boolean isRamp() {
		return this.ramp;
	}

	public void setRamp(boolean ramp) {
		this.ramp = ramp;
	}

	public OrderedSet<Chunk> getInsideChunks() {
		return insideChunks;
	}

	public void setInsideChunks(OrderedSet<Chunk> insideChunks) {
		this.insideChunks = insideChunks;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public float getBounceHorizontal() {
		return bounceHorizontal;
	}

	public void setBounceHorizontal(float bounceHorizontal) {
		this.bounceHorizontal = bounceHorizontal;
	}

	public float getBounceVertical() {
		return bounceVertical;
	}

	public void setBounceVertical(float bounceVertical) {
		this.bounceVertical = bounceVertical;
	}

	public Sprite getFrame() {
		return frame;
	}

	public void setFrame(Sprite frame) {
		this.frame = frame;
	}

	public GameObject getParent() {
		return parent;
	}

	public void setParent(GameObject parent) {
		this.parent = parent;
	}

}
