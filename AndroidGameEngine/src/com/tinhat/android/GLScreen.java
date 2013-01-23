package com.tinhat.android;

import com.tinhat.framework.Screen;

public abstract class GLScreen extends Screen {
	protected final GLGraphics glGraphics;
	protected final GLGame glGame;
	
	public GLScreen(com.tinhat.framework.Game game){
		super(game);
		glGame = (GLGame)game;
		glGraphics = glGame.getGLGraphics();
	}
}
