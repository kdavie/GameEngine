package com.tinhat.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;


import com.tinhat.android.Pool.PoolObjectFactory;
import com.tinhat.framework.Input.TouchEvent;


public class SingleTouchHandler implements TouchHandler {
	boolean isTouched;
	int touchX;
	int touchY;
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEventBuffer = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;
			
		
	public SingleTouchHandler(View view, float scaleX, float scaleY){
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			@Override
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		synchronized(this){
			TouchEvent touchEvent = touchEventPool.newObject();
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				touchEvent.type = TouchEvent.TOUCH_DOWN; 
				isTouched = true;
				break;
			case MotionEvent.ACTION_MOVE:
				touchEvent.type = TouchEvent.TOUCH_DRAGGED;
				isTouched = true;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				touchEvent.type = TouchEvent.TOUCH_UP;
				isTouched = false;
				break;
			}
			touchEvent.x = touchX = (int)(event.getX() * scaleX);
			touchEvent.y = touchY = (int)(event.getY() * scaleY);
			touchEventBuffer.add(touchEvent);
			
			return true;
		}
	}

	@Override
	public boolean isTouchDown(int pointer) {
		synchronized(this) {
			if(pointer == 0){
				return isTouched;
			}
			else {
				return false;
			}
		}
	}

	@Override
	public int getTouchX(int pointer) {
		synchronized(this) {
			return touchX;
		}
	}

	@Override
	public int getTouchY(int pointer) {
		synchronized(this) {
			return touchY;
		}
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		synchronized(this) {
			int length = touchEvents.size();
			for(int i = 0; i < length; i++ ){
				touchEventPool.free(touchEvents.get(i));
			}
			touchEvents.clear();
			touchEvents.addAll(touchEventBuffer);
			touchEventBuffer.clear();
			return touchEvents;
		}
	}

	@Override
	public void setGestureDetector(Context context, OnGestureListener listener) {
		// TODO Auto-generated method stub
		
	}

}
