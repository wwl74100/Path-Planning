package dataHandler;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * 
 * @author X.K.T
 * @class MapCreator
 * @brief Create new map file randomly
 *
 */
public class MapCreator {
	public static void createMap() {
		try {
			int threshold = 30;
			for(int i = 5; i < 100; ++ i) {
				String path = "map/map" + String.valueOf(i) + ".txt";
				FileOutputStream fos   = new FileOutputStream(path);
				OutputStreamWriter osw = new OutputStreamWriter(fos);
				BufferedWriter bw      = new BufferedWriter(osw);
				
				bw.write("20");
				bw.newLine();
				bw.write("20");
				bw.newLine();
				
				String lineContent = "";
				Random ran = new Random();
				for(int k = 0; k < 19; ++ k) {
					int a = ran.nextInt(100);
					if(a > threshold) {
						lineContent += "0 ";
					}
					else {
						lineContent += "1 ";
					}
				}
				lineContent += "9 ";
				bw.write(lineContent);
				bw.newLine();
				
				for(int j = 1; j < 19; ++ j) {
					lineContent = "";
					for(int k = 0; k < 20; ++ k) {
						int a = ran.nextInt(100);
						if(a > threshold) {
							lineContent += "0 ";
						}
						else {
							lineContent += "1 ";
						}
					}
					bw.write(lineContent);
					bw.newLine();
				}
				
				lineContent = "8 ";
				for(int k = 1; k < 20; ++ k) {
					int a = ran.nextInt(100);
					if(a > threshold) {
						lineContent += "0 ";
					}
					else {
						lineContent += "1 ";
					}
				}
				bw.write(lineContent);
				
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MapCreator.createMap();
		System.out.println("Complete!");
	}
}
