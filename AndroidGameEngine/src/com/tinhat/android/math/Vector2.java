package com.tinhat.android.math;

import android.util.FloatMath;

public class Vector2 extends com.tinhat.framework.math.Vector2 {

	public Vector2() {
		super();
	}
	
	public Vector2(Vector2 other){
		super(other);
	}

	public Vector2(float x, float y) {
		super(x, y);
	}
	
	

	@Override
	public com.tinhat.framework.math.Vector2 copy() {
		return new Vector2(x, y);
	}

	@Override
	public float distance(com.tinhat.framework.math.Vector2 other) {
		float distanceX = this.x - other.x;
		float distanceY = this.y - other.y;
		return FloatMath.sqrt(distanceX * distanceX + distanceY * distanceY);
	}

	@Override
	public float distance(float x, float y) {
		float distanceX = this.x - x;
		float distanceY = this.y - y;
		return FloatMath.sqrt(distanceX * distanceX + distanceY * distanceY);
	}

	@Override
	public float length() {
		return FloatMath.sqrt(x*x+y*y);
	}

	@Override
	public com.tinhat.framework.math.Vector2 rotate(float angle) {
		float rad = angle * TO_RADIANS;
		float cos = FloatMath.cos(rad);
		float sin = FloatMath.sin(rad);
		
		float newX = this.x * cos - this.y * sin;
		float newY = this.x * sin + this.y * cos;
		
		this.x = newX;
		this.y = newY;
		
		return this;
	}

}
