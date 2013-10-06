package com.example.cameraexample;

public class WatchDog {
	
	public enum State {
		UNSET, SETTING, COMPARING; 
	}
	// Current state
	public static State currentState = State.UNSET; 
	public static int[][] templateImg;
	public static boolean shutDown = false; // Shut down the program if it has detected change. 
	
	public static boolean processImage(int[][] newImage) {
		
		switch(currentState) {
			case UNSET: 
				// Do nothing. 
				break; 
			case SETTING: 
				// Set current image as template
				templateImg = newImage; 
				break;
			case COMPARING: 
				// Compare against template
				if (Motion.Detect(templateImg, newImage, 30, 2)) {
					// Change Detected!
					notifyPebble();
					shutDown = true;
				}
				break;
		}
		
		return shutDown;
	}
	
	public static void notifyPebble() {
		// TODO: Notify Pebble LOL. 
	}
	
}

