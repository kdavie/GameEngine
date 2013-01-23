package com.tinhat.framework.gl;

public interface Texture {

	public abstract int width();
	
	public abstract int height();
	
	public abstract void bind();

	public abstract void setFilters(int minFilter, int magFilter);

	public abstract void dispose();

}
