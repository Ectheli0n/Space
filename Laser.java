package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Laser extends Rectangle{
	
	private Rectangle createRectangle;
	
	
	public Laser(double x, double y, double width, double height) {
		Rectangle newRec = new Rectangle();
		newRec.setFill(Color.RED);
		newRec.setX(x);
		newRec.setY(y);
		newRec.setWidth(width);
		newRec.setHeight(height);
	}
	
	public void moveLaser(){
		
	}
}
