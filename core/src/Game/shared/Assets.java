package Game.shared;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	
	public AssetManager manager = new AssetManager();

    public final AssetDescriptor<TextureAtlas> atlas =
        new AssetDescriptor<TextureAtlas>("Sprites/Sprites.pack", TextureAtlas.class);

    public final AssetDescriptor<Skin> skin =
        new AssetDescriptor<Skin>("UI/skin.json", Skin.class,
               new SkinLoader.SkinParameter("Sprites/Sprites.pack"));
    
    public AssetDescriptor<BitmapFont> lana24;
    
    public AssetDescriptor<BitmapFont> lana12;
    
	public void load() {
	    manager.load(atlas);
	    manager.load(skin);
	    
	}
	
	public void loadFonts() {

		System.out.println("LOAD FONT");
		
	    FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		FreeTypeFontLoaderParameter lana24 = new FreeTypeFontLoaderParameter();
		lana24.fontFileName = "UI/LanaPixel.ttf";
		lana24.fontParameters.size = 24;
		
		manager.load("lana24.ttf", BitmapFont.class, lana24);
		
		this.lana24 = new AssetDescriptor<BitmapFont>(lana24.fontFileName, BitmapFont.class, lana24);
		
		manager.load(this.lana24);
		
		

		
	}
	
	public void dispose() {
    	manager.dispose();
	}

	public AssetDescriptor<TextureAtlas> getAtlas() {
		return atlas;
	}

	public AssetDescriptor<Skin> getSkin() {
		return skin;
	}

	public AssetDescriptor<BitmapFont> getLana24() {
		return lana24;
	}


}