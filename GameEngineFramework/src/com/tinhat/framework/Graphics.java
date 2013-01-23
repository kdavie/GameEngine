package com.tinhat.framework;

public interface Graphics {
	public static enum PixmapFormat {
		ARGB8888,
		ARGB4444,
		RGB565
	}
	public Pixmap pixmap(String fileName, PixmapFormat format);
	public void clear(int color);
	public void drawPixel(int x, int y, int color);
	public void drawLine(int startX, int startY, int endX, int endY, int color);
	public void drawRect(int x, int y, int width, int height, int color);
	public void drawPixmap(Pixmap pixmap, int x, int y);
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight);
	public int getWidth();
	public int getHeight();
}
