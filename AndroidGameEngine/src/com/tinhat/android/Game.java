package com.tinhat.android;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;
import com.tinhat.framework.Screen;


public abstract class Game extends Activity implements com.tinhat.framework.Game {
	RenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input; 
	FileIO fileIO;
	Screen screen;
	WakeLock wakeLock;


	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		int frameBufferWidth = isLandscape ? 480 : 320;
		int frameBufferHeight = isLandscape ? 320 : 480;
		
		
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);
		
		float scaleX = (float)frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
		float scaleY = (float)frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();
		
		renderView = new RenderView(this, frameBuffer);
		graphics = new Graphics(getAssets(), frameBuffer);
		fileIO = new FileIO(getAssets()); 
		audio = new Audio(this);
		input = new Input(this, renderView, scaleX, scaleY);
		screen = getStartScreen();
		
		setContentView(renderView);
		
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "TinhatGame");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}
	
	public void onPause() {
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();
		
		if(isFinishing()){
			screen.dispose();
		}
	}
	
	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public Screen getCurrentScreen() {
		return screen;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Graphics getGraphics() {
		return graphics;
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public Screen getStartScreen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setScreen(Screen screen) {
		if(screen == null) {
			throw new IllegalArgumentException("Screen cannot be null");
		}
		
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;

	}
	

}
