package com.tinhat.framework.math;

public class FramedAverage {
	float[] parts;
	int addedParts;
	int lastPart;
	float average  = 0, sum;
	
	public FramedAverage(int frames){
		parts = new float[frames];
	}
	
	public void addPart(float part){
		addedParts++;
		parts[lastPart++] = part;
		if(lastPart>parts.length-1) {
			lastPart = 0;
		}
	}
	
	public float getAverage(){
		if(addedParts>=parts.length){
			sum = 0;
			for(int i = 0;i<parts.length;i++){
				sum += parts[i];
			}
			average = sum / parts.length;
			
			return average;
		}
		return 0;
	}
}
