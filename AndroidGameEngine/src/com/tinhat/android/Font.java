package com.tinhat.android;



public class Font {
	public final Texture texture;
	public final int glyphWidth;
	public final int glyphHeight;
	public final TextureRegion[] glyphs = new TextureRegion[96];
	public final char startCharacter;
 
	public Font(Texture texture, int offsetX, int offsetY, int glyphsPerRow, int glyphWidth, int glyphHeight, char startCharacter, int count){
		this.texture = texture;
		this.glyphWidth = glyphWidth;
		this.glyphHeight = glyphHeight;
		this.startCharacter = startCharacter;
	 
		int x = offsetX;
		int y = offsetY;
	 
		
		for(int i = 0; i < count; i++){
			 
			glyphs[i] = new TextureRegion(texture, x, y, glyphWidth, glyphHeight);
			 
			x += glyphWidth;
			if(x == offsetX + glyphsPerRow * glyphWidth){
				x = offsetX;
				y += glyphHeight;
			}
		}
	}
	
	
	
	public void drawText(SpriteBatcher batcher, String text, float x, float y)	{
		int length = text.length();
		 
		for(int i = 0; i < length; i++){
			int c = text.charAt(i) - startCharacter;
			 
			if(c < 0 || c > glyphs.length -1){
				continue;
			}
			TextureRegion glyph = glyphs[c];
			batcher.drawSprite(x, y, glyphWidth, glyphHeight, glyph);
			x += glyphWidth;
		}
	}
	
	public void foo(){
		
	}
	
	public void drawText(SpriteBatcher batcher, String text, float x, float y, float width, float height)	{
		int length = text.length();
		 
		for(int i = 0; i < length; i++){
			int c = text.charAt(i) - startCharacter;
			 
			if(c < 0 || c > glyphs.length -1){
				continue;
			}
			TextureRegion glyph = glyphs[c];
			batcher.drawSprite(x, y, width, height, glyph);
			x += glyphWidth;
		}
	}
}
