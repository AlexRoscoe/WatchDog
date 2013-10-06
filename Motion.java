import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Motion {
	
	public final static int Background = 0;
	public final static int Foreground = 255;
	
	public static int[][] loadImage(String path) {
		BufferedImage hugeImage = null;
		try {
			hugeImage = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int width = hugeImage.getWidth();
		int height = hugeImage.getHeight();
		int[][] result = new int[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				int color = hugeImage.getRGB(col, row);
				int red = (color & 0x00ff0000) >> 16;
				int green = (color & 0x0000ff00) >> 8;
				int blue = (color & 0x000000ff);
				result[row][col] = (red + green + blue)/3;
			}
		}
		
		return result;
	}
/*	
	public static void main(String[] args) {
		
		int[][] map1 = loadImage("yolohashswag.jpg");
		int[][] map2 = loadImage("yolohashswag.jpg");
		
		System.out.println("Map 1: ");
		print(map1);
		System.out.println("Map 2: ");
		print(map2);
		
		int threshold = 25;
		int noise = 3;
		
		boolean result = Detect(map1, map2, threshold, noise);
		System.out.println(result);
	}
*/	
	public static void print(int[][] frame) {
		for (int i = 0; i < frame.length; i++) {
			for (int j = 0; j < frame[0].length; j++) {
				System.out.print(frame[i][j] + "   ");
			}
			System.out.println();
		}
	}
	
	public static boolean Detect(int[][] f1, int[][] f2, int differenceThreshold, int noiseFilterSize)	
	{
		
		PGM frame1 = new PGM(f1);
		PGM frame2 = new PGM(f2);
		
	    int width = (int)frame1.getWidth();
	    int height = (int)frame1.getHeight();

	    //first-pass: difference and threshold filter (mark the pixels that are changed between two frames)
	    PGM step1Image = new PGM(new int[width][height]);
	    for (int x = 0; x < width; x++)
	    {
	        for (int y = 0; y < height; y++)
	        {
	            int diff = Math.abs(frame1.getPixel(x, y) - frame2.getPixel(x, y));
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
	            for (int i = x - n; i < x + n; i++){
	                for (int j = y - n; j < y + n; j++) {
	                    marked += step1Image.getPixel(i,j) == Foreground ? 1 : 0;
	                }
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
	    
		
		System.out.println("Step 1: ");
		print(imgSteps[0].getMap());
		
		System.out.println("Step 2: ");
		print(imgSteps[1].getMap());
	    
		for(int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (step2Image.getPixel(i, j) == Foreground) {
					return true;
				}
			}
		}
		return false;
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
		if (x < 0 || y < 0 || x >= sizeX || y >= sizeY) {
			return 0;
		}
		return Map[x][y];
	}
	
	public void setPixel(int x, int y, int v) {
		Map[x][y] = v;
	}
	
	public int[][] getMap() {
		return Map;
	}
}
