package com.example.cameraexample;

public class ImageTranslator {

	 //Method from Ketai project! Not mine! See below...  
    public static int[][] decodeYUV420SP(byte[] yuv420sp, int width, int height) {  
            
    	int[][] grayScaledImg = new int[width][height]; 
    		
        final int frameSize = width * height;  
  
        for (int j = 0, yp = 0; j < height; j++) {       
        	int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;  
        	for (int i = 0; i < width; i++, yp++) {  
        		int y = (0xff & ((int) yuv420sp[yp])) - 16;  
                if (y < 0)  
                  y = 0;  
                if ((i & 1) == 0) {  
                  v = (0xff & yuv420sp[uvp++]) - 128;  
                  u = (0xff & yuv420sp[uvp++]) - 128;  
                }  
  
                int y1192 = 1192 * y;  
                int r = (y1192 + 1634 * v);  
                int g = (y1192 - 833 * v - 400 * u);  
                int b = (y1192 + 2066 * u);  
  
                if (r < 0)                 
                	r = 0;               
                else if (r > 262143)  
                   r = 262143;  
                if (g < 0)                  
                	g = 0;
                else if (g > 262143)  
                   g = 262143;  
                if (b < 0)
                	b = 0;
                else if (b > 262143)  
                   b = 262143;  
  
                grayScaledImg[i][j] = (r + g + b) / 3;
        	}  
        }
        return grayScaledImg;
    }  
}
