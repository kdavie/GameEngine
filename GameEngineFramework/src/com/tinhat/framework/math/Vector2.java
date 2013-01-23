package com.tinhat.framework.math;

public abstract class Vector2 {
	public static float TO_RADIANS = (1 / 180.0f) * (float)Math.PI;
	public static float TO_DEGREES = (1 / (float)Math.PI) * 180;
	public float x, y;
	
	public Vector2() {
	}
	
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 other){
		this.x = other.x;
		this.y = other.y;
	}
	
	public abstract Vector2 copy();
	
	public abstract float length();
	
	public abstract Vector2 rotate(float angle);

	public abstract float distance(Vector2 other);
	
	public abstract float distance(float x, float y);
	
	public float distanceSquared(Vector2 other){
		float distX = this.x - other.x;
		float distY = this.y - other.y;
		return distX*distX + distY*distY;
	}
	
	public float distanceSquared(float x, float y){
		float distX = this.x - x;
		float distY = this.y - y;
		return distX*distX + distY*distY;
	}
	
	public Vector2 set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2 set(Vector2 other) {
		this.x = other.x;
		this.y = other.y;
		return this;
	}
	
	public Vector2 add(float x, float y){
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector2 add(Vector2 other){
		this.x += other.x;
		this.y += other.y;
		return this;
	}
	
	public Vector2 subtract(float x, float y){
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vector2 subtract(Vector2 other){
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}
	
	public Vector2 multiply(float scalar){
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}
	
	public Vector2 normalize() {
		float length = length();
		if( length != 0){
			this.x /= length;
			this.y /= length;
		}
		return this;
	}
	
	public float angle() {
		float angle = (float)Math.atan2(y, x) * TO_DEGREES;
		if(angle < 0) {
			angle += 360;
		}
		return angle;
	}
	
	

	
	
	
	
}
