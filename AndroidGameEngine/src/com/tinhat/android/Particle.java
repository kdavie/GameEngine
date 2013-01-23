package com.tinhat.android;

import com.tinhat.android.math.Vector2;

public class Particle extends DynamicGameObject {

	private TextureRegion region;
	public int lifespan;

	public Particle(float x, float y, float width, float height, TextureRegion region, int lifespan, Vector2 velocity) {
		super(x, y, width, height);
		this.region = region;
		this.lifespan = lifespan;
		this.velocity.set(velocity);		
	}
	
	public void render(SpriteBatcher batcher) {
		batcher.drawSprite(position.x, position.y, bounds.width, bounds.height, region);
	}
	
	public void reset(float x, float y, float width, float height, TextureRegion region, int lifespan, Vector2 velocity){
		 this.position.set(x, y);
		 this.bounds.lowerLeft.set(x,y);
		 this.bounds.width = width;
		 this.bounds.height = height;
		 this.region = region;
		 this.lifespan = lifespan;
		 this.velocity.set(velocity);	
	}
	
	public void update(float deltaTime) {
		lifespan--;
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
	}

}
