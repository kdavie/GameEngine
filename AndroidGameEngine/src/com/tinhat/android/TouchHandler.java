package com.tinhat.android;

import java.util.List;


import android.content.Context;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import com.tinhat.framework.Input.TouchEvent;

public interface TouchHandler extends OnTouchListener {
	public boolean isTouchDown(int pointer);
	public int getTouchX(int pointer);
	public int getTouchY(int pointer);
	public List<TouchEvent> getTouchEvents();
    public void setGestureDetector(Context context, OnGestureListener listener);
}
