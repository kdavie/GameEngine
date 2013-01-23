package com.tinhat.android;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;
 

import com.tinhat.android.Pool.PoolObjectFactory;
import com.tinhat.android.math.Vector2;

public class ParticleSystem {
	private final Random random = new Random();
	private final Pool<Particle> particlePool;	
	private final List<Particle> particles;
	private final List<TextureRegion> regions;
	private final Vector2 location;
	private final float minParticleWidth;
	private final float maxParticleWidth;
	private final float minParticleHeight;
	private final float maxParticleHeight;
	private final Vector2 maxVelocity;
	private final int maxLifespan;
	private final Texture texture;
	private final int maxParticles; 
	
	private TextureRegion region;
	private int lifespan;
	private float width, height;
	private Vector2 velocity;
	private Iterator<Particle> iterator;
	private Particle particle;
	
	public ParticleSystem(Texture texture, List<TextureRegion> regions, Vector2 location, float minParticleWidth, float maxParticleWidth, 
			float minParticleHeight, float maxParticleHeight, Vector2 maxVelocity, int maxLifespan, int maxParticles ){
		
		PoolObjectFactory<Particle> factory = new PoolObjectFactory<Particle>(){
			@Override
			public Particle createObject(){
				return createParticle();
			}
		};
		
		this.texture = texture;
		this.location = location;
		this.regions = regions;
		this.particles = new ArrayList<Particle>();
		this.particlePool = new Pool<Particle>(factory, maxParticles);	
		this.minParticleWidth = minParticleWidth;
		this.maxParticleWidth = maxParticleWidth;
		this.minParticleHeight = minParticleHeight;
		this.maxParticleHeight = maxParticleHeight;
		this.maxVelocity = maxVelocity;
		this.maxLifespan = maxLifespan;
		this.maxParticles = maxParticles;
        this.velocity = new Vector2();
	}
	
	public void updateLocation(Vector2 location){
		this.location.set(location);	
	}
	
	public void update(float deltaTime){
		 
		if(particles.size() < maxParticles){
			this.setValues();
			particle = particlePool.newObject();
		    particle.reset(location.x, location.y, width, height, region, lifespan, velocity);
		    
			particles.add(particle);
		}
		
		iterator = particles.iterator();
		
		while(iterator.hasNext()){
			particle = iterator.next();
			particle.update(deltaTime);
			if(particle.lifespan <= 0){
				particle.lifespan = maxLifespan;
				particlePool.free(particle);
				iterator.remove();
			}
		}
		
		 
	}
	
	private void setValues(){
		region = regions.get(random.nextInt(regions.size()));
		lifespan = random.nextInt(maxLifespan);
		width = random.nextFloat() * (maxParticleWidth - minParticleWidth) + minParticleWidth;
		height = maxParticleHeight / maxParticleWidth * width;
		
		if(height < minParticleHeight){
			height = minParticleHeight;
			width = maxParticleWidth / maxParticleHeight * height;
		}
		
		velocity.x = maxVelocity.x == 0 ? 0 : maxVelocity.x * random.nextFloat() * 2 - 1;
		velocity.y = maxVelocity.y == 0 ? 0 : maxVelocity.y * random.nextFloat() * 2 - 1;
	}
	
	private Particle createParticle(){
		 
		this.setValues();
		
		return new Particle(location.x, location.y, width, height, region, lifespan, velocity);
	}
	
	
	public void render(SpriteBatcher batcher, GL10 gl){
		if(particles.size() > 0){
			batcher.beginBatch(texture);
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			 
			iterator = particles.iterator();
			
			while(iterator.hasNext()){
				particle = iterator.next();
				particle.render(batcher);
			}
			
			batcher.endBatch();
		}
	}
	
	
}
