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
	public char[] mMapVector;
	
	/**
	 *  Store the map info as a matrix
	 */
	public char[][] mMapMatrix;
	
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
	public void readMapFile(String fileName) {
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
	}
	
	public static void main(String[] args) {
		DataHandler dataHandler = new DataHandler();
		dataHandler.readMapFile("map/map0.txt");
	}
}
