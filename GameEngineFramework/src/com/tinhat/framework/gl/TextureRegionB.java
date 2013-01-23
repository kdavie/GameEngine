package com.tinhat.framework.gl;

public class TextureRegionB {
	public final float u1, v1,
					   u2, v2;
	public final Texture texture;
	public final int textureWidth, textureHeight;
	
	public TextureRegionB(Texture texture, float x, float y, float width, float height) {
		this.textureWidth = texture.width();
		this.textureHeight = texture.height();
		this.u1 = x / this.textureWidth;
		this.v1 = y / this.textureHeight;
		this.u2 = this.u1 + width / this.textureWidth;
		this.v2 = this.v1 + height / this.textureHeight;
		this.texture = texture;
		
	}
	
	
}
