import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class CMMSprite implements DisplayableSprite, MovableSprite, CollidingSprite {

	static Image image;	
	private double centerX = 0;
	private double centerY = 0;
	private double width = 50;
	private double height = 50;
	double velocityX = 0;
	double velocityY = 0;
	int score = 0;
	
	private boolean dispose = false;	
	private final double VELOCITY = 200;
	boolean isAtExit = false;
	boolean isClose = false;
	
	public CMMSprite(double centerX, double centerY) {

		this.centerX = centerX;
		this.centerY = centerY;
		
		if (image == null) {
			try {
				image = ImageIO.read(new File("res/CMM/avatar.jpg"));
				//set the height and width based on the actual size of the image
				this.height = CMMSprite.image.getHeight(null);
				this.width = CMMSprite.image.getWidth(null);
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}		
		}		
	}

	public CMMSprite(double centerX, double centerY, double height, double width) {
		this(centerX, centerY);
		
		this.height = height;
		this.width = width;
	}
	
	public Image getImage() {
		return image;
	}
	
	//DISPLAYABLE
	
	public boolean getVisible() {
		return true;
	}
	
	public double getMinX() {
		return centerX - (width / 2);
	}

	public double getMaxX() {
		return centerX + (width / 2);
	}

	public double getMinY() {
		return centerY - (height / 2);
	}

	public double getMaxY() {
		return centerY + (height / 2);
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public double getCenterX() {
		return centerX;
	};

	public double getCenterY() {
		return centerY;
	};
	
	
	public boolean getDispose() {
		return dispose;
	}

	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}


	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
		
		double velocityX = 0;
		double velocityY = 0;
		
		//LEFT ARROW
		if (keyboard.keyDown(37)) {
			velocityX = -VELOCITY;
			
		}
		//UP ARROW
		if (keyboard.keyDown(38)) {
			velocityY = -VELOCITY;			
		}
		//RIGHT ARROW
		if (keyboard.keyDown(39)) {
			velocityX += VELOCITY;
		}
		// DOWN ARROW
		if (keyboard.keyDown(40)) {
			velocityY += VELOCITY;			
		}
		
		double deltaX = actual_delta_time * 0.001 * velocityX;
		double deltaY = actual_delta_time * 0.001 * velocityY;

		boolean collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0);
		boolean collidingBarrierY = checkCollisionWithBarrier(universe.getSprites(), 0, deltaY);

		System.out.println(collidingBarrierX);



		if (collidingBarrierX == false) {
			this.centerX += deltaX;
	}

		if (collidingBarrierY == false) {
			this.centerY += deltaY;
		}
		this.checkOverlapWithCoin(universe, 0, 0);
		this.checkIsAtExit(universe, 0, 0);
		this.checkProximityMessage(universe);
	}
	private boolean checkCollisionWithBarrier(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {

		boolean colliding = false;

		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof BarrierSprite) {
				if (CollisionDetection.overlaps(this.getMinX() + deltaX, this.getMinY() + deltaY, 
						this.getMaxX()  + deltaX, this.getMaxY() + deltaY, 
						sprite.getMinX(),sprite.getMinY(), 
						sprite.getMaxX(), sprite.getMaxY())) {
					colliding = true;
					break;					
				}
			}
		}		
		return colliding;		
	}

	
	@Override
	public void setCenterX(double centerX) {
		this.centerX = centerX;
		
	}

	@Override
	public void setCenterY(double centerY) {
		this.centerY = centerY;
		
	}

	public void setVelocityX(double pixelsPerSecond) {
		this.velocityX =pixelsPerSecond;
	}

	public void setVelocityY(double pixelsPerSecond) {
		this.velocityY =pixelsPerSecond;
	}

	@Override
	public void moveX(double pixelsPerSecond) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveY(double pixelsPerSecond) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getProximityMessage() {
		String proximityMessage = "1 in 10 coins kill!";
		if (isClose == true) {
			proximityMessage = "lucky you made it so far!";
		}
//		else if(sprite.getClass().toString().contains("SMSRSprite") || sprite.getClass().toString().contains("DWESprite")) {
//			
//	}	
		else if (this.score == 1100) {
			proximityMessage = "you beat the odds!";
		}
		else if (this.score == 2400) {
			proximityMessage = "you win! super lucky!";
		}
		else {
		}
		return proximityMessage;

	}

	public boolean getIsAtExit() {

		return isAtExit;
	}
	
	@Override
	public long getScore() {
		return score;
	}

	public void checkOverlapWithCoin(Universe sprites, double deltaX, double deltaY) {

			for (DisplayableSprite sprite : sprites.getSprites()) {
				if (sprite instanceof CoinSprite) {
					if (CollisionDetection.overlaps(this.getMinX() + deltaX, this.getMinY() + deltaY, 
							this.getMaxX()  + deltaX, this.getMaxY() + deltaY, 
							sprite.getMinX(),sprite.getMinY(), 
							sprite.getMaxX(), sprite.getMaxY())) {
						((CoinSprite) sprite).setDispose(true);
						this.score += 100;
						int temp = (Math. random() <= 0.1) ? 1 : 10;
						if (temp < 6) {
							isAtExit = true;
						}
				}
			}
		}
			return;
	}

	private boolean checkIsAtExit(Universe sprites, double deltaX, double deltaY) {
		for (DisplayableSprite sprite : sprites.getSprites()) {
			if (sprite instanceof ExitSprite) {
				if (CollisionDetection.covers(this.getMinX() + deltaX, this.getMinY() + deltaY, 
						this.getMaxX()  + deltaX, this.getMaxY() + deltaY, 
						sprite.getMinX(),sprite.getMinY(), 
						sprite.getMaxX(), sprite.getMaxY())) {
					isAtExit = true;
					break;

				}
			}
		}
		return isAtExit;
	}



	private boolean checkProximityMessage(Universe sprites) {
		for (DisplayableSprite sprite : sprites.getSprites()) {
			if (!(sprite instanceof ExitSprite || sprite instanceof BarrierSprite || sprite instanceof CMMSprite || sprite instanceof CoinSprite))  {
				double x = sprite.getCenterX() - this.getCenterX();
				double y = sprite.getCenterY() - this.getCenterY();
				double distance = Math.sqrt((x * x) + (y * y));
				if (distance <= 100) {
					isClose = true;
				}
				else {
					isClose = false;
				}
			}
		}
		return isClose;
	}
}
