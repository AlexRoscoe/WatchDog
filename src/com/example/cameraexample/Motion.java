package com.example.cameraexample;

public class Motion {
	
	public final static int Background = 0;
	public final static int Foreground = 255;

	/*
	
	public static void main(String[] args) {
		
		int[][] map1 = {{147,177,142,42,62},{54,155,12,87,24},{5,92,42,155,166,24},{216,47,221,226,212},{82,172,65,159,98}};
		PGM frame1 = new PGM(map1);
		
		System.out.println("Map 1: ");
		
		print(frame1);
		
		int[][] map2 = {{5,5,5,42,23},{2,3,76,52,47},{5,92,45,155,45,99},{145,47,76,25,212},{82,172,65,25,98}};
		PGM frame2 = new PGM(map2);
		
		System.out.println("Map 2: ");
		
		print(frame2);
		
		int threshold = 55;
		int noise = 2;
		
		PGM[] result = Detect(frame1, frame2, threshold, noise);
		
		System.out.println("Step 1: ");
		print(result[0]);
		
		System.out.println("Step 2: ");
		print(result[1]);
	}
	
	*/
	
	public static void print(PGM frame) {
		for (int i = 0; i < frame.getWidth(); i++) {
			for (int j = 0; j < frame.getHeight(); j++) {
				System.out.print(frame.getPixel(i, j) + "   ");
			}
			System.out.println();
		}
	}
	
	public static PGM[] Detect(PGM frame1, PGM frame2, int differenceThreshold, int noiseFilterSize)
	{
	    int width = (int)frame1.getWidth();
	    int height = (int)frame1.getHeight();

	    //first-pass: difference and threshold filter (mark the pixels that are changed between two frames)
	    PGM step1Image = new PGM(new int[width][height]);
	    for (int x = 0; x < width; x++)
	    {
	        for (int y = 0; y < height; y++)
	        {
	            int diff = Math.abs(frame1.getPixel(x, y) - frame2.getPixel(x, y));
	            System.out.println(diff);
	            step1Image.setPixel(x, y, diff >= differenceThreshold ? Foreground : Background);
	        }
	    }
	    //second-pass: erosion filter (remove noise i.e. ignore minor differences between two frames)
	    PGM step2Image = new PGM(new int[width][height]);
	    int m = noiseFilterSize, n = (m - 1) / 2; //'m' will be an odd number always
	    for (int x = 0; x < width; x++)
	    {
	        for (int y = 0; y < height; y++)
	        {
	            //count the number of marked pixels in current window
	            int marked = 0;
	            for (int i = x - n; i < x + n; i++)
	                for (int j = y - n; j < y + n; j++) {
	                    marked += step1Image.getPixel(i, j) == Foreground ? 1 : 0;
	                }

	            if (marked >= m) //if atleast half the number of pixels are marked, then mark the full window
	            {
	                for (int i = x - n; i < x + n; i++)
	                    for (int j = y - n; j < y + n; j++)
	                        step2Image.setPixel(i, j, Foreground);
	            }
	        }
	    }
	    
	    PGM[] imgSteps = new PGM[2];
	    imgSteps[0] = step1Image;
	    imgSteps[1] = step2Image;
	    
	    return imgSteps;
	}
	
}
/* Portable grey map (PGM) */
class PGM {
	
	private int sizeX;
	private int sizeY;
	private int[][] Map;
	
	public PGM(int[][] Map) {
		this.Map = Map;
		sizeX = Map.length;
		sizeY = Map[0].length;
		
	}
	
	public int getWidth() {
		return sizeX;
	}
	
	public int getHeight() {
		return sizeY;
	}
	
	public int getPixel(int x, int y) {
		return Map[x][y];
	}
	
	public void setPixel(int x, int y, int v) {
		Map[x][y] = v;
	}
	
	
	
}