package com.tinhat.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;

import com.tinhat.framework.Audio;
import com.tinhat.framework.FileIO;
import com.tinhat.framework.Game;
import com.tinhat.framework.Graphics;
import com.tinhat.framework.Input;
import com.tinhat.framework.Screen;
import com.tinhat.framework.math.FramedAverage;

public abstract class GLGame extends Activity implements Game, Renderer {
 
	
	enum GLGameState {
		Initialized,
		Running,
		Paused,
		Finished,
		Idle
	}
	
	GLSurfaceView glView;
	GLGraphics glGraphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	GLGameState state = GLGameState.Initialized;
	Object stateChanged = new Object();
	long startTime = System.nanoTime();
	WakeLock wakeLock;
	float deltaTime;
	FramedAverage average = new FramedAverage(5);
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		glView = new GLSurfaceView(this);
		glView.setRenderer(this);
		setContentView(glView);
		
		glGraphics = new GLGraphics(glView);
		fileIO = new com.tinhat.android.FileIO(getAssets());
		audio = new com.tinhat.android.Audio(this);
		input = new com.tinhat.android.Input(this, glView, 1, 1);
		
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "TinhatGame");
	}
	
	@Override
	public void onResume(){
		super.onResume();
		glView.onResume();
		wakeLock.acquire();
		
		 
	}
	
	@Override
	public void onPause(){
		synchronized(stateChanged){
			if(isFinishing()){
				state = GLGameState.Finished;
			} else {
				state = GLGameState.Paused;
			}
			
			while(true){
				try {
					stateChanged.wait();
					break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
		wakeLock.release();
		glView.onPause();
		super.onPause();
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		GLGameState state = null;
		
		synchronized(stateChanged){
			state = this.state;
		}
		
		if(state == GLGameState.Running){
			/*float deltaTime = (System.nanoTime()-startTime)/1000000000.0f;
			startTime = System.nanoTime();
			
			screen.update(deltaTime);
			screen.render(deltaTime);*/
			
			long time = System.nanoTime();
			deltaTime = (time-startTime)/1000000000.0f;
			startTime = time;
			average.addPart(deltaTime);
			deltaTime = average.getAverage() == 0 ? deltaTime : average.getAverage();
			
			screen.update(deltaTime);
			screen.render(deltaTime);
			
		}
		
		if(state == GLGameState.Paused){
			screen.pause();
			synchronized(stateChanged){
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}
		
		if(state == GLGameState.Finished){
			screen.pause();
			screen.dispose();
			synchronized(stateChanged){
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glGraphics.setGL(gl);
		
		synchronized(stateChanged){
			if(state == GLGameState.Initialized){
				screen = getStartScreen();
			}
			state = GLGameState.Running;
			screen.resume();
			startTime = System.nanoTime();
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

	public GLGraphics getGLGraphics(){
		return glGraphics;
	}
	
	@Override
	public Graphics getGraphics() {
		throw new IllegalStateException("Cannot retrieve Graphics object. Use getGLGraphics instead.");
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public Screen getStartScreen() {
		return null;
	}

	@Override
	public void setScreen(Screen screen) {
		if(screen == null){
			throw new IllegalArgumentException("Screen must not be null");
		}
		
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}
	
	public void setGestureDetector(final OnGestureListener listener){
		GLGame.this.runOnUiThread(new Runnable() {
            public void run() {
            	((com.tinhat.android.Input)getInput()).setGestureDetector(GLGame.this.getApplicationContext(), listener);	 
        	    
            }
          });
	}

}