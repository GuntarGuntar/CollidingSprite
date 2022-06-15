import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ExcitableBackground implements Background {

    private Image excitedSpace;
    private Image blank;
    private Image[] danceFloor;
    private Image happySpace;

    private long elapsedTime = 0;
	boolean dance = false;
	boolean energetic = false;
	boolean happy = false;
    
    private int tileWidth = AnimationFrame.SCREEN_WIDTH;
    private int tileHeight = AnimationFrame.SCREEN_HEIGHT;

    public ExcitableBackground() {
    	try {
    		happySpace = ImageIO.read(new File("res\\excitable\\happy-space.jpg"));
    		excitedSpace = ImageIO.read(new File("res\\excitable\\excited-space.jpg"));
    		danceFloor = new Image[5];
    		for (int i = 0; i <= 3; i++) {
    			String path = String.format("res\\excitable\\dance-floor\\frame_%d.gif", i);
    			this.danceFloor[i] = ImageIO.read(new File(path));    			
    		}
    		
    		
    	}
    	catch (IOException e) {
    		System.out.println(e.toString());
    	}		
    }
	
	public Tile getTile(int col, int row) {
		//row is an index of tiles, with 0 being the at the origin
		//col is an index of tiles, with 0 being the at the origin
		int x = (col * tileWidth);
		int y = (row * tileHeight);
		Tile newTile = null;
		
		Image image = null;
				
		if (this.happy) {
			//top right
			image = this.happySpace;
		}
		else if (this.energetic) {
			//bottom left
			image = excitedSpace;
		}
		else if (this.dance) {
			//bottom right
			long slice = (this.elapsedTime) / 200;
			int frame = (int)(slice % 4);
			image = danceFloor[frame];
		}
		else {
			image = null;
		}
		
		newTile = new Tile(image, x, y, tileWidth, tileHeight, false);					
		
		return newTile;
	}
	
	public int getCol(int x) {
		return x / tileWidth - 1;
	}
	
	public int getRow(int y) {
		return y / tileHeight - 1;
	}
	
	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {		
		elapsedTime += actual_delta_time;
	}

	public boolean getIsHappy() {
		// TODO Auto-generated method stub
		return this.happy;
	}

	public void setIsHappy(boolean isHappy) {
		this.happy = isHappy;		
	}

	public boolean getIsEnergetic() {
		// TODO Auto-generated method stub
		return this.energetic;
	}

	public void setIsEnergetic(boolean isEnergetic) {
		this.energetic = isEnergetic;
	}

	public void setDance(boolean dance) {
		this.dance = dance;
	}

}


