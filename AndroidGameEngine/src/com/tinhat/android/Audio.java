package com.tinhat.android;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class Audio implements com.tinhat.framework.Audio {
	private final int CONCURRENT_SOUNDS = 20;
	
	AssetManager assets;
	SoundPool soundPool;
	
	public Audio(Activity activity){
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool= new SoundPool(CONCURRENT_SOUNDS, AudioManager.STREAM_MUSIC, 0);
	}
	
	@Override
	public Music music(String fileName) {
		try {
			AssetFileDescriptor descriptor = assets.openFd(fileName);
			return new Music(descriptor);  
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load music '" + fileName + ";");
		}
	}

	@Override
	public Sound sound(String fileName) {
		try {
			AssetFileDescriptor descriptor = assets.openFd(fileName);
			int soundId = soundPool.load(descriptor, 0);
			return new Sound(soundPool, soundId);  
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load sound '" + fileName + ";");
		}
		 
	}

}
