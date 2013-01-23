package com.tinhat.android;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Style;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.tinhat.framework.Pixmap;

public class Graphics implements com.tinhat.framework.Graphics {
	AssetManager assets;
	Bitmap frameBuffer;
	Canvas canvas;
	Paint paint;
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();
	
	public Graphics(AssetManager assets, Bitmap frameBuffer){
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}
	@Override
	public void clear(int color) {
		canvas.drawRGB((color & 0xff0000) >> 16,(color & 0xff00) >> 8, (color & 0xff));

	}

	@Override
	public void drawLine(int startX, int startY, int endX, int endY, int color) {
		paint.setColor(color);
		canvas.drawLine(startX, startY, endX, endY, paint);

	}

	@Override
	public void drawPixel(int x, int y, int color) {
		paint.setColor(color);
		canvas.drawPoint(x,y,paint);

	}

	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y) {
		canvas.drawBitmap(((com.tinhat.android.Pixmap)pixmap).bitmap, x, y, null);
	}

	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth-1;
		srcRect.bottom = srcY + srcHeight-1;
		
		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + srcWidth-1;
		dstRect.bottom = y + srcHeight-1;
		
		canvas.drawBitmap(((com.tinhat.android.Pixmap)pixmap).bitmap, srcRect, dstRect, null);
	}

	@Override
	public void drawRect(int x, int y, int width, int height, int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x+width-1, y+height-1, paint);
	}

	@Override
	public int getHeight() {
		return frameBuffer.getHeight();
	}

	@Override
	public int getWidth() {
		return frameBuffer.getWidth();
	}

	@Override
	public Pixmap pixmap(String fileName, PixmapFormat format) {
		Config config = null;
		if(format == PixmapFormat.RGB565){
			config = Config.RGB_565;
		} else if (format == PixmapFormat.ARGB4444) {
			config = Config.ARGB_4444;
		} else {
			config = Config.ARGB_8888;
		}
		
		Options options = new Options();
		options.inPreferredConfig = config;
		
		InputStream stream = null;
		Bitmap bitmap = null;
		try {
			stream = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(stream);
			if(bitmap == null){
				throw new RuntimeException("Could not load bitmap from asset '" + fileName + "'");
			}
		} catch(IOException e){
			throw new RuntimeException("Could not load bitmap from asset '" + fileName + "'");
		} finally {
			if(stream != null){
				try {
					stream.close();
				} catch (IOException e) {
					//ignore
				}
				
			}
		}
		
		if(bitmap.getConfig() == Config.RGB_565) {
			format = PixmapFormat.RGB565;
		} else if (bitmap.getConfig() == Config.ARGB_4444) {
			format = PixmapFormat.ARGB4444;
		}
		else {
			format = PixmapFormat.ARGB8888;
		}
		
		return new com.tinhat.android.Pixmap(bitmap, format);
	}

}
