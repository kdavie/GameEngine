package com.tinhat.android;

import android.graphics.Bitmap;

import com.tinhat.framework.Graphics.PixmapFormat;

public class Pixmap implements com.tinhat.framework.Pixmap {
	Bitmap bitmap;
	PixmapFormat format;
	
	public Pixmap(Bitmap bitmap, PixmapFormat format) {
		this.bitmap = bitmap;
		this.format = format;
	}
	
	@Override
	public void dispose() {
		bitmap.recycle();
	}

	@Override
	public PixmapFormat getFormat() {
		return format;
	}

	@Override
	public int getHeight() {
		return bitmap.getHeight();
	}

	@Override
	public int getWidth() {
		return bitmap.getWidth();
	}

}
