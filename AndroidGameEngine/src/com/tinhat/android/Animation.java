package com.tinhat.android;



 

public class Animation {
	public static final int ANIMATION_LOOPING = 0;
	public static final int ANIMATION_NONLOOPING = 1;
	
	final TextureRegion[] keyFrames;
	final float frameDuration;
	public final int frameCount;
	public int frameNumber;
	public Animation(float frameDuration, TextureRegion ... keyFrames) {
		this.frameDuration = frameDuration;
		this.keyFrames = keyFrames;
		this.frameCount = keyFrames.length;
	}
	
	public TextureRegion getKeyFrame(float stateTime, int mode){
		frameNumber = (int)(stateTime / frameDuration);
		// Log.d("ANIMATION", Integer.toString(frameNumber ));
		if(mode == ANIMATION_NONLOOPING){
			frameNumber = Math.min(keyFrames.length-1, frameNumber);
		} else {
			frameNumber = frameNumber % keyFrames.length;
		}
		
		return keyFrames[frameNumber];
		
	}
}
