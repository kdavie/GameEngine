package com.tinhat.android;

import java.util.List;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;

public class Input implements com.tinhat.framework.Input {
	AccelerometerHandler accelHandler;
	KeyboardHandler keyHandler; 
	TouchHandler touchHandler;
	
	public Input(Context context, View view, float scaleX, float scaleY	) {
		accelHandler = new AccelerometerHandler(context);
		keyHandler = new KeyboardHandler(view);
		if(Integer.parseInt(VERSION.SDK) < 5) {
			touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
		} else {
			touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
		}
	}
	
	@Override
	public float getAccelX() {
		return accelHandler.getAccelX();
	}

	@Override
	public float getAccelY() {
		return accelHandler.getAccelY();
	}

	@Override
	public float getAccelZ() {
		return accelHandler.getAccelZ();
	}

	@Override
	public List<KeyEvent> getKeyEvents() {
		return keyHandler.getKeyEvents();
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		return touchHandler.getTouchEvents();
	}

	@Override
	public int getTouchX(int pointer) {
		return touchHandler.getTouchX(pointer);
	}

	@Override
	public int getTouchY(int pointer) {
		return touchHandler.getTouchY(pointer);
	}

	@Override
	public boolean isKeyPressed(int keyCode) {
		return keyHandler.isKeyPressed(keyCode);
	}

	@Override
	public boolean isTouchDown(int pointer) {
		return touchHandler.isTouchDown(pointer);
	}
 
	public void setGestureDetector(Context context, OnGestureListener listener){
		touchHandler.setGestureDetector(context, listener);
	}

}
