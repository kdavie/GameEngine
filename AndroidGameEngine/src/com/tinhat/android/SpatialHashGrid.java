package com.tinhat.android;

import java.util.ArrayList;
import java.util.List;

import android.util.FloatMath;

public class SpatialHashGrid {
	List<GameObject>[] dynamicCells;
	List<GameObject>[] staticCells;
	List<GameObject> foundObjects;
	int cellsPerRow;
	int cellsPerColumn;
	float cellSize;
	int[] cellIds = new int[4];
	
	public SpatialHashGrid(float worldWidth, float worldHeight, float cellSize) {
		this(worldWidth, worldHeight, cellSize, 10);
	}
	
	@SuppressWarnings("unchecked")
	public SpatialHashGrid(float worldWidth, float worldHeight, float cellSize, int maxObjectsPerCell) {
		this.cellSize = cellSize;
		this.cellsPerRow = (int)FloatMath.ceil(worldWidth/cellSize);
		this.cellsPerColumn = (int)FloatMath.ceil(worldHeight/cellSize);
		int cellCount = cellsPerRow * cellsPerColumn;
		dynamicCells = new List[cellCount];
		staticCells = new List[cellCount];
		for(int i = 0; i < cellCount; i++){
			dynamicCells[i] = new ArrayList<GameObject>(maxObjectsPerCell);
			staticCells[i] = new ArrayList<GameObject>(maxObjectsPerCell);
		}
		foundObjects = new ArrayList<GameObject>(maxObjectsPerCell);
	}
	
	public void insertStaticObject(GameObject obj){
		int[] cellIds = getCellIds(obj);
		int i = 0;
		int cellId = -1;
		while(i <= 3 && (cellId = cellIds[i++]) != -1) {
			staticCells[cellId].add(obj);
		}
	}
	
	public void insertDynamicObject(GameObject obj){
		int[] cellIds = getCellIds(obj);
		int i = 0;
		int cellId = -1;
		while(i <= 3 && (cellId = cellIds[i++]) != -1) {
			dynamicCells[cellId].add(obj);
		}
	}
	
	public void removeObject(GameObject obj){
		int[] cellIds = getCellIds(obj);
		int i = 0;
		int cellId = -1;
		while(i <= 3 && (cellId = cellIds[i++]) != -1) {
			dynamicCells[cellId].remove(obj);
			staticCells[cellId].remove(obj);
		}
	}
	
	public void clearDynamicCells(GameObject obj){
		int length = dynamicCells.length;
		for(int i = 0; i < length; i++){
			dynamicCells[i].clear();
		}
	}
	
	public List<GameObject> getPotentialColliders(GameObject obj){
		foundObjects.clear();
		int[] cellIds = getCellIds(obj);
		int i = 0;
		int cellId = -1;
		GameObject collider;
		int length;
		while(i <= 3 && (cellId = cellIds[i++]) != -1){
			length = dynamicCells[cellId].size();
			for(int j = 0; j < length; j++){
				collider = dynamicCells[cellId].get(j);
				if(!foundObjects.contains(collider)){
					foundObjects.add(collider);
				}
			}
			length = staticCells[cellId].size();
			for(int j = 0; j < length; j++){
				collider = staticCells[cellId].get(j);
				if(!foundObjects.contains(collider)){
					foundObjects.add(collider);
				}
			}
		}
		return foundObjects;
	}
	

	public int[] getCellIds(GameObject obj) {
		int x1 = (int)FloatMath.floor(obj.bounds.lowerLeft.x / cellSize);
		int y1 = (int)FloatMath.floor(obj.bounds.lowerLeft.y / cellSize);		
		int x2 = (int)FloatMath.floor((obj.bounds.lowerLeft.x + obj.bounds.width) / cellSize);
		int y2 = (int)FloatMath.floor((obj.bounds.lowerLeft.y + obj.bounds.height) / cellSize);
		
		if(x1 == x2 && y1 == y2){
			if(x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerColumn){
				cellIds[0] = x1 + y1 * cellsPerRow;
			} else {
				cellIds[0] = -1;
			}
		} else if (x1 == x2) {
			int i = 0;
			if(x1 >= 0 && x1 < cellsPerRow){
				if(y1 >= 0 && y1 < cellsPerColumn){
					cellIds[i++] = x1 + y1 * cellsPerRow;
				}
				if(y2 >= 0 && y2 < cellsPerColumn){
					cellIds[i++] = x1 + y2 * cellsPerRow;
				}
				while(i<=3) {
					cellIds[i++] = -1;
				}
			}
		} else if(y1 == y2){
			int i = 0;
			if(y1 >= 0 && y1 < cellsPerColumn){
				if(x1 >= 0 && x1 < cellsPerRow) {
					cellIds[i++] = x1 + y1 * cellsPerRow;
				}
				if(x1 >= 0 && x2 < cellsPerRow) {
					cellIds[i++] = x2 + y1 * cellsPerRow;
				}
				while(i<=3) {
					cellIds[i++] = -1;
				}
			}
		} else {
			int i = 0;
			int cellsPerRowY1 = y1 * cellsPerRow;
			int cellsPerRowY2 = y2 * cellsPerRow;
			if(x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerColumn) {
				 cellIds[i++] = x1 + cellsPerRowY1;
			}
            if(x2 >= 0 && x2 < cellsPerRow && y1 >= 0 && y1 < cellsPerColumn) {
            	cellIds[i++] = x2 + cellsPerRowY1;
            }
            if(x2 >= 0 && x2 < cellsPerRow && y2 >= 0 && y2 < cellsPerColumn) {
            	 cellIds[i++] = x2 + cellsPerRowY2;
            }
            if(x1 >= 0 && x1 < cellsPerRow && y2 >= 0 && y2 < cellsPerColumn) {
            	cellIds[i++] = x1 + cellsPerRowY2;
            }
            while(i <= 3) {
            	cellIds[i++] = -1;
            }
		}
		return cellIds;
	}
	
	
}
