package com.tinhat.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.tinhat.android.Pool.PoolObjectFactory;
import com.tinhat.framework.Input.TouchEvent;

public class MultiTouchHandler implements TouchHandler {
	boolean[] isTouched = new boolean[20];
	int[] touchX = new int[20];
	int[] touchY = new int[20];
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEventBuffer = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;
	GestureDetector gestureDetector;
	
	public MultiTouchHandler(View view, float scaleX, float scaleY){
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			@Override
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		//gestureDetector = new GestureDetector(view.getContext(),(OnGestureListener) this);
		
		view.setOnTouchListener(this);
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
	    if(gestureDetector != null){
	    	gestureDetector.onTouchEvent(event);
	    }
		synchronized(this){
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			int pointerId = event.getPointerId(pointerIndex);
			TouchEvent touchEvent;
			
			switch(action){
			case MotionEvent.ACTION_DOWN: 
			case MotionEvent.ACTION_POINTER_DOWN:
				 touchEvent = touchEventPool.newObject();
				 touchEvent.type = TouchEvent.TOUCH_DOWN;
				 touchEvent.pointer = pointerId;
				 touchEvent.x = touchX[pointerId] = (int)(event.getX(pointerIndex) * scaleX);
				 touchEvent.y = touchY[pointerId] = (int)(event.getY(pointerIndex) * scaleY);
				 isTouched[pointerId] = true;
				 touchEventBuffer.add(touchEvent);
				break;
			case MotionEvent.ACTION_UP: 
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_CANCEL:
				touchEvent = touchEventPool.newObject();
				 touchEvent.type = TouchEvent.TOUCH_UP;
				 touchEvent.pointer = pointerId;
				 touchEvent.x = touchX[pointerId] = (int)(event.getX(pointerIndex) * scaleX);
				 touchEvent.y = touchY[pointerId] = (int)(event.getY(pointerIndex) * scaleY);
				 isTouched[pointerId] = false;
				 touchEventBuffer.add(touchEvent);
				break;
			case MotionEvent.ACTION_MOVE:
				 int pointerCount = event.getPointerCount();
				 for(int i = 0; i < pointerCount; i++){
					 pointerIndex = i;
					 pointerId = event.getPointerId(pointerIndex);
					 touchEvent = touchEventPool.newObject();
					 touchEvent.type = TouchEvent.TOUCH_DRAGGED;
					 touchEvent.pointer = pointerId;
					 touchEvent.x = touchX[pointerId] = (int)(event.getX(pointerIndex) * scaleX);
					 touchEvent.y = touchY[pointerId] = (int)(event.getY(pointerIndex) * scaleY);
					 touchEventBuffer.add(touchEvent);
				 }
				break;
			 
			}
		}
		return true;
	}

	@Override
	public boolean isTouchDown(int pointer) {
		synchronized(this){
			if(pointer < 0 || pointer >= 20) return false;
			
			return isTouched[pointer];
		}
	}

	@Override
	public int getTouchX(int pointer) {
		synchronized(this){
			if(pointer < 0 || pointer >= 20) return 0;
			
			return touchX[pointer];
		}
	}

	@Override
	public int getTouchY(int pointer) {
		synchronized(this){
			if(pointer < 0 || pointer >= 20) return 0;
			
			return touchY[pointer];
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
		gestureDetector = new GestureDetector(context, listener);
		
	}

	

}
