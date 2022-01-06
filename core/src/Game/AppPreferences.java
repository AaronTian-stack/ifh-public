package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
 
public class AppPreferences {
	
	private static final String PREF_MUSIC_VOLUME = "volume";
	private static final String PREF_MUSIC_ENABLED = "music.enabled";
	private static final String PREF_SOUND_ENABLED = "sound.enabled";
	private static final String PREF_SOUND_VOL = "sound";
	private static final String PREF_RESOLUTION = "resolution";
	private static final String PREF_DISPLAY = "display";
	private static final String PREF_VSYNC_ENABLED = "vsync";
	private static final String PREFS_NAME = "ifhPref";
 
	protected Preferences getPrefs() {
		return Gdx.app.getPreferences(PREFS_NAME);
	}
 
	public boolean isSoundEffectsEnabled() {
		return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
	}
 
	public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
		getPrefs().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
		getPrefs().flush();
	}
 
	public boolean isMusicEnabled() {
		return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
	}
 
	public void setMusicEnabled(boolean musicEnabled) {
		getPrefs().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
		getPrefs().flush();
	}
 
	public float getMusicVolume() {
		return getPrefs().getFloat(PREF_MUSIC_VOLUME, 1f);
	}
 
	public void setMusicVolume(float volume) {
		getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
		getPrefs().flush();
	}
	
	public float getSoundVolume() {
		return getPrefs().getFloat(PREF_SOUND_VOL, 1f);
	}
 
	public void setSoundVolume(float volume) {
		getPrefs().putFloat(PREF_SOUND_VOL, volume);
		getPrefs().flush();
	}
	
	public void setResolution(int index) {
		getPrefs().putInteger(PREF_RESOLUTION, index);
		getPrefs().flush();
	}
	
	public int getResolution() {
		return getPrefs().getInteger(PREF_RESOLUTION, 2);
	}
	
	public void setDisplayMode(int index) {
		getPrefs().putInteger(PREF_DISPLAY, index);
		getPrefs().flush();
	}
	
	public int getDisplayMode() {
		return getPrefs().getInteger(PREF_DISPLAY, 0);
	}
	
	public int isVSyncEnabled() {
		return getPrefs().getInteger(PREF_VSYNC_ENABLED, 0);
	}
 
	public void setVSyncEnabled(int enable) {
		getPrefs().putInteger(PREF_VSYNC_ENABLED, enable);
		getPrefs().flush();
	}
}