package Game.shared;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Screens.GameContainer;
import Screens.PlayScreen;

public class GameSprites { //shared class for reused sprites
	
	public static Sprite dirt = new Sprite(new TextureRegion(GameContainer.atlas.findRegion("dirt")));
	
	public static Sprite dirtIn1 = new Sprite(new TextureRegion(GameContainer.atlas.findRegion("dirtIn1")));
	
	public static Sprite dirtIn2 = new Sprite(new TextureRegion(GameContainer.atlas.findRegion("dirtIn2")));
	
	public static Sprite dirtIn3 = new Sprite(new TextureRegion(GameContainer.atlas.findRegion("dirtIn3")));
	
	public static Sprite dirtIn4 = new Sprite(new TextureRegion(GameContainer.atlas.findRegion("dirtIn4")));
	
	public static Sprite dirtIn5 = new Sprite(new TextureRegion(GameContainer.atlas.findRegion("dirtIn5")));
	
	public static Sprite dirtIn6 = new Sprite(new TextureRegion(GameContainer.atlas.findRegion("dirtIn6")));
	
	public static Sprite dirtIn7 = new Sprite(new TextureRegion(GameContainer.atlas.findRegion("dirtIn7")));
	
	public static Sprite dirtIn8 = new Sprite(new TextureRegion(GameContainer.atlas.findRegion("dirtIn8")));

	public static Sprite dirt1 = new Sprite(GameContainer.atlas.findRegion("dirt1"));

	public static Sprite dirtC = new Sprite(GameContainer.atlas.findRegion("dirt2C"));

	public static Sprite dirtP = new Sprite(GameContainer.atlas.findRegion("dirt2P"));

	public static Sprite dirt3 = new Sprite(GameContainer.atlas.findRegion("dirt3"));

	public static Sprite dirtA = new Sprite(GameContainer.atlas.findRegion("dirtA"));

	public static Sprite corner = new Sprite(GameContainer.atlas.findRegion("corner"));

	public static Sprite dirtslope = new Sprite(GameContainer.atlas.findRegion("grassSlope"));

	public static Sprite debug = new Sprite((GameContainer.atlas.findRegion("debug1")));

	public static Sprite grassC = new Sprite(GameContainer.atlas.findRegion("grassC"));

	public static Sprite grassf = new Sprite(GameContainer.atlas.findRegion("grassF"));

	public static Sprite grassf3 = new Sprite(GameContainer.atlas.findRegion("grassF3"));

	public static Sprite cave = new Sprite(GameContainer.atlas.findRegion("cave"));

	public static Sprite cave1 = new Sprite(GameContainer.atlas.findRegion("cave1"));

	public static Sprite caveC = new Sprite(GameContainer.atlas.findRegion("caveC"));

	public static Sprite caveS = new Sprite(GameContainer.atlas.findRegion("caveS"));

	public static Sprite cave3 = new Sprite(GameContainer.atlas.findRegion("cave3"));

	public static Sprite caveA = new Sprite(GameContainer.atlas.findRegion("caveA"));

	public static Sprite caveP = new Sprite(GameContainer.atlas.findRegion("caveP"));
	
	public static Color color0 = new Color(46/255f, 34/255f, 47/255f, 1);
	
	public static Color color1 = new Color(62/255f, 53/255f, 70/255f, 1);
	
	public static Color color7 = new Color(155/255f, 171/255f, 178/255f, 1);
	
	public static Color color8 = new Color(199/255f, 220/255f, 208/255f, 1);
	
	public static void init() {
		dirt.setScale(PlayScreen.imageScale);
		dirtIn1.setScale(PlayScreen.imageScale);
		dirtIn2.setScale(PlayScreen.imageScale);
		dirtIn3.setScale(PlayScreen.imageScale);
		dirtIn4.setScale(PlayScreen.imageScale);
		dirtIn5.setScale(PlayScreen.imageScale);
		dirtIn6.setScale(PlayScreen.imageScale);
		dirtIn7.setScale(PlayScreen.imageScale);
		dirtIn8.setScale(PlayScreen.imageScale);
		dirt1.setScale(PlayScreen.imageScale);
		dirtC.setScale(PlayScreen.imageScale);
		dirtP.setScale(PlayScreen.imageScale);
		dirt3.setScale(PlayScreen.imageScale);
		dirtA.setScale(PlayScreen.imageScale);
		corner.setScale(PlayScreen.imageScale);
		grassC.setScale(PlayScreen.imageScale);
		grassf.setScale(PlayScreen.imageScale);
		grassf3.setScale(PlayScreen.imageScale);
		dirtslope.setScale(PlayScreen.imageScale);
		cave.setScale(PlayScreen.imageScale);
		cave1.setScale(PlayScreen.imageScale);
		caveC.setScale(PlayScreen.imageScale);
		caveS.setScale(PlayScreen.imageScale);
		cave3.setScale(PlayScreen.imageScale);
		caveA.setScale(PlayScreen.imageScale);
		caveP.setScale(PlayScreen.imageScale);
		debug.setScale(PlayScreen.imageScale);
	}
	
	public static void setColor(Color color) {
		dirt.setColor(color);
		dirtIn1.setColor(color);
		dirtIn2.setColor(color);
		dirtIn3.setColor(color);
		dirtIn4.setColor(color);
		dirtIn5.setColor(color);
		dirtIn6.setColor(color);
		dirtIn7.setColor(color);
		dirtIn8.setColor(color);
		dirt1.setColor(color);
		dirtC.setColor(color);
		dirtP.setColor(color);
		dirt3.setColor(color);
		dirtA.setColor(color);
		corner.setColor(color);
		grassC.setColor(color);
		grassf.setColor(color);
		grassf3.setColor(color);
		dirtslope.setColor(color);
		cave.setColor(color);
		cave1.setColor(color);
		caveC.setColor(color);
		caveS.setColor(color);
		cave3.setColor(color);
		caveA.setColor(color);
		caveP.setColor(color);
		debug.setColor(color);
	}

}
