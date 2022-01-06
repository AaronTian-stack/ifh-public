package UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class RadialSprite implements Drawable {
	
  private Texture texture;
  
  private final float[] verts = new float[60];
  
  private float x;
  
  private float y;
  
  private float angle;
  
  private float width;
  
  private float height;
  
  private float u1;
  
  private float u2;
  
  private float v1;
  
  private float v2;
  
  private float du;
  
  private float dv;
  
  private boolean dirty = true;
  
  private int draw = 0;
  
  private float angleOffset = 270.0F;
  
  private float originX;
  
  private float originY;
  
  private float scaleX = 1.0F;
  
  private float scaleY = -1.0F;
  
  int o;
  
  private float leftWidth;
  
  private float rightWidth;
  
  private float topHeight;
  
  private float bottomHeight;
  
  private float minWidth;
  
  private float minHeight;
  
  public void setColor(float packedColor) {
    for (int i = 0; i < 12; i++)
      this.verts[i * 5 + 2] = packedColor; 
  }
  
  public void setColor(Color color) {
    setColor(color.toFloatBits());
  }
  
  private final void vert(float[] verts, int offset, float x, float y) {
    float u = this.u1 + this.du * (x - this.x) / this.width;
    float v = this.v1 + this.dv * (1.0F - (y - this.y) / this.height);
    vert(verts, offset, x, y, u, v);
  }
  
  private final void vert(float[] verts, int offset, float x, float y, float u, float v) {
    verts[offset] = this.x + this.originX + (x - this.x - this.originX) * this.scaleX;
    verts[offset + 1] = this.y + this.originY + (y - this.y - this.originY) * this.scaleY;
    verts[offset + 3] = u;
    verts[offset + 4] = v;
  }
  
  private void calculate(float x, float y, float width, float height, float angle, float u0, float v0, float u1, float v1) {
    if (!this.dirty && this.x == x && this.y == y && this.angle == angle && this.width == width && this.height == height && 
      this.u1 == u0 && this.v2 == v1 && this.u2 == u1 && this.v2 == v1)
      return; 
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.angle = angle;
    this.u1 = u0;
    this.v1 = v0;
    this.u2 = u1;
    this.v2 = v1;
    float centerX = width * 0.5F;
    float centerY = height * 0.5F;
    float x2 = x + width;
    float y2 = y + height;
    float xc = x + centerX;
    float yc = y + centerY;
    float ax = MathUtils.cosDeg(angle + this.angleOffset);
    float ay = MathUtils.sinDeg(angle + this.angleOffset);
    float txa = (ax != 0.0F) ? Math.abs(centerX / ax) : 1.0E8F;
    float tya = (ay != 0.0F) ? Math.abs(centerY / ay) : 1.0E8F;
    float t = Math.min(txa, tya);
    float tx = t * ax;
    float ty = t * ay;
    vert(this.verts, 5, x + centerX, y);
    if (ax >= 0.0F) {
      vert(this.verts, 15, x, y2);
      vert(this.verts, 0, xc, y2);
      vert(this.verts, 10, x, y);
      vert(this.verts, 30, xc, yc);
      vert(this.verts, 35, xc, y2);
      if (txa < tya) {
        vert(this.verts, 20, x2, y2);
        vert(this.verts, 25, x2, yc + ty);
        this.draw = 2;
      } else if (ay > 0.0F) {
        vert(this.verts, 25, xc + tx, y2);
        vert(this.verts, 20, xc + tx * 0.5F, y2);
        this.draw = 2;
      } else {
        vert(this.verts, 20, x2, y2);
        vert(this.verts, 25, x2, y);
        vert(this.verts, 55, xc, yc);
        vert(this.verts, 40, x2, y);
        vert(this.verts, 50, xc + tx, y);
        vert(this.verts, 45, xc + tx * 0.5F, y);
        this.draw = 3;
      } 
    } else {
      vert(this.verts, 0, x + centerX, y + centerY);
      if (txa < tya) {
        vert(this.verts, 10, x, y);
        vert(this.verts, 15, x, yc + ty);
        this.draw = 1;
      } else if (ay < 0.0F) {
        vert(this.verts, 15, xc + tx, y);
        vert(this.verts, 10, xc + tx * 0.5F, y);
        this.draw = 1;
      } else {
        vert(this.verts, 15, x, y2);
        vert(this.verts, 10, x, y);
        vert(this.verts, 25, xc, yc);
        vert(this.verts, 30, x, y2);
        vert(this.verts, 35, xc + tx * 0.5F, y2);
        vert(this.verts, 20, xc + tx, y2);
        this.draw = 2;
      } 
    } 
    this.dirty = false;
  }
  
  private void draw(SpriteBatch batch, float x, float y, float width, float height, float angle) {
    if (width < 0.0F) {
      this.scaleX = -1.0F;
      width = -width;
    } 
    if (height < 0.0F) {
      this.scaleY = -1.0F;
      height = -height;
    } 
    calculate(x, y, width, height, angle, this.u1, this.v1, this.u2, this.v2);
    batch.draw(this.texture, this.verts, 0, 20 * this.draw);
  }
  
  private void draw(SpriteBatch batch, float x, float y, float angle) {
    draw(batch, x, y, this.width, this.height, angle);
  }
  
  public void setOrigin(float x, float y) {
    if (this.originX == x && this.originY == y)
      return; 
    this.originX = x;
    this.originY = y;
    this.dirty = true;
  }
  
  public void setScale(float x, float y) {
    if (this.scaleX == x && this.scaleY == y)
      return; 
    this.scaleX = x;
    this.scaleY = y;
    this.dirty = true;
  }
  
  public float getAngle() {
    return this.angle;
  }
  
  public void setAngle(float angle) {
    if (this.angle == angle)
      return; 
    this.angle = angle;
    this.dirty = true;
  }
  
  public RadialSprite(TextureRegion textureRegion) {
    this.leftWidth = 0.0F;
    this.rightWidth = 0.0F;
    this.topHeight = 0.0F;
    this.bottomHeight = 0.0F;
    this.minWidth = 0.0F;
    this.minHeight = 0.0F;
    this.texture = textureRegion.getTexture();
    this.u1 = textureRegion.getU();
    this.v1 = textureRegion.getV();
    this.u2 = textureRegion.getU2();
    this.v2 = textureRegion.getV2();
    this.du = this.u2 - this.u1;
    this.dv = this.v2 - this.v1;
    this.width = textureRegion.getRegionWidth();
    this.height = textureRegion.getRegionHeight();
    setColor(Color.WHITE);
  }
  
  public void draw(Batch batch, float x, float y, float width, float height) {
    draw((SpriteBatch)batch, x, y, this.angle);
  }
  
  public float getLeftWidth() {
    return this.leftWidth;
  }
  
  public void setLeftWidth(float leftWidth) {
    this.leftWidth = leftWidth;
  }
  
  public float getRightWidth() {
    return this.rightWidth;
  }
  
  public void setRightWidth(float rightWidth) {
    this.rightWidth = rightWidth;
  }
  
  public float getTopHeight() {
    return this.topHeight;
  }
  
  public void setTopHeight(float topHeight) {
    this.topHeight = topHeight;
  }
  
  public float getBottomHeight() {
    return this.bottomHeight;
  }
  
  public void setBottomHeight(float bottomHeight) {
    this.bottomHeight = bottomHeight;
  }
  
  public float getMinWidth() {
    return this.minWidth;
  }
  
  public void setMinWidth(float minWidth) {
    this.minWidth = minWidth;
  }
  
  public float getMinHeight() {
    return this.minHeight;
  }
  
  public void setMinHeight(float minHeight) {
    this.minHeight = minHeight;
  }
  
  public Texture getTexture() {
    return this.texture;
  }
  
  public void setTextureRegion(TextureRegion textureRegion) {
    this.texture = textureRegion.getTexture();
    this.u1 = textureRegion.getU();
    this.v1 = textureRegion.getV();
    this.u2 = textureRegion.getU2();
    this.v2 = textureRegion.getV2();
    this.dirty = true;
  }
  
  public float getWidth() {
    return this.width;
  }
  
  public void setWidth(float width) {
    this.width = width;
  }
  
  public float getHeight() {
    return this.height;
  }
  
  public void setHeight(float height) {
    this.height = height;
  }
}
