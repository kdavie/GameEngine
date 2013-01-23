package com.tinhat.android;

import android.media.SoundPool;

public class Sound implements com.tinhat.framework.Sound {
	int soundId;
	SoundPool soundPool;
	
	public Sound(SoundPool soundPool, int soundId){
		this.soundPool = soundPool;
		this.soundId = soundId;
	}
	
	@Override
	public void dispose() {
		soundPool.unload(soundId);

	}

	@Override
	public void play(float volume) {
		soundPool.play(soundId, volume, volume, 0, 0, 1);
	}

}
