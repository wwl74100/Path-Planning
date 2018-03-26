//  DataHandler.java

package dataHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader; 

/**
 *  Class to read and store map info
 *  
 *  map file format:
 *  	line 1: map row number
 *  	line 2: map column number
 *  	line 3 ~ row + 2: map info
 *  
 *  map info is saved an matrix:
 *  	0 represents this is a free space
 *  	1 represents this is a obstacle
 *  	8 represents this is the start point
 *  	9 represents this is the target point
 */

public class DataHandler {
	/**
	 *  Store the total size of the map
	 */
	private int mMapSize;
	
	/**
	 *  Store the row number of the map
	 */
	private int mMapRow;
	
	/**
	 *  Store the column number of the map
	 */
	private int mMapColumn;
	
	/**
	 *  Store the map info as a vector
	 */
	private char[] mMapVector;
	
	/**
	 *  Store the map info as a matrix
	 */
	private char[][] mMapMatrix;
	
	/**
	 * Store the start point of the agent
	 */
	private int mStartPoint;
	
	/**
	 * Store the target point of the agent
	 */
	private int mTargetPoint;
	
	/**
	 * Constructor
	 * @param fileName: Name of the map file
	 */
	public DataHandler(String fileName) {
		readMapFile(fileName);
	}
	
	/**
	 *  print the map vector and the map matrix
	 *  used to print debug information
	 */
	private void printMap() {
		for(int i = mMapRow - 1; i > -1; -- i) {
			for(int j = 0; j < mMapColumn; ++ j) {
				System.out.print(mMapMatrix[i][j] + " ");
			}
			System.out.println();
		}
		
		for(int i = 0; i < mMapSize; ++ i) {
			System.out.print(mMapVector[i] + " ");
		}
		System.out.println();
	}
	
	/**
	 *  read map info from a file
	 *  @param fileName: the name of the map file
	 */
	private void readMapFile(String fileName) {
		File file = new File(fileName);  
        BufferedReader reader = null;
        
        try {
        	reader = new BufferedReader(new FileReader(file));  
            String tempString = null;
            
            tempString = reader.readLine();
            mMapRow = Integer.parseInt(tempString);
            //System.out.println(mMapRow);
            
            tempString = reader.readLine();
            mMapColumn = Integer.parseInt(tempString);
            //System.out.println(mMapColumn);
            
            mMapSize = mMapRow * mMapColumn;
            mMapMatrix = new char[mMapRow][mMapColumn];
            mMapVector = new char[mMapSize];
            //System.out.println(mMapSize);
            
            int row = mMapRow - 1;
            while ((tempString = reader.readLine()) != null) {  
                //System.out.println("line " + tempString + ": " + tempString);  
                int len = tempString.length() - 1, column = mMapColumn - 1;
                while(len > -1) {
                	if((tempString.charAt(len) >= '0') && (tempString.charAt(len) <= '9')) {
                		mMapMatrix[row][column] = tempString.charAt(len);
                		mMapVector[row * mMapColumn + column] = tempString.charAt(len);
                		if(tempString.charAt(len) == '8') {
                			mStartPoint = row * mMapColumn + column;
                			//System.out.println(mStartPoint);
                		}
                		if(tempString.charAt(len) == '9') {
                			mTargetPoint = row * mMapColumn + column;
                			//System.out.println(mTargetPoint);
                		}               			
                		-- column;
                	}
                	-- len;
                }
                -- row;                
            }  
            reader.close();
        } catch(Exception e) {
        	e.printStackTrace();
        } 
        //printMap();
	}

	/**
	 * get map size
	 * @return: size of map
	 */
	public int getMapSize() {
		return mMapSize;
	}

	/**
	 * get map row number
	 * @return: map row number
	 */
	public int getMapRow() {
		return mMapRow;
	}

	/**
	 * get map column number
	 * @return: map column number
	 */
	public int getMapColumn() {
		return mMapColumn;
	}

	/**
	 * get map info as a vector
	 * @return: map info vector
	 */
	public char[] getMapVector() {
		return mMapVector;
	}

	/**
	 * get map info as a matrix
	 * @return: map info matrix
	 */
	public char[][] getMapMatrix() {
		return mMapMatrix;
	}

	/** 
	 * get start point of the agent
	 * @return: start point
	 */
	public int getStartPoint() {
		return mStartPoint;
	}

	/**
	 * get target point of the agent 
	 * @return: target point
	 */
	public int getTargetPoint() {
		return mTargetPoint;
	}

}
