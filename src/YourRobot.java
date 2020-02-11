//**************************************IMPORTS******************************************
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.CustomEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import standardOdometer.Odometer;
import robocode.util.Utils;
//***************************************************************************************



public class YourRobot extends AdvancedRobot {
	
	//robot's properties
	private double distanceRoundDone = 0.0;
    private double oldX,oldY,posX,posY;
    private boolean isRacing = false;
    private int overtakes = 0;
    private Odometer odometer = new Odometer("isRacing", this);



	public void run() {
		
		// Initialization of the robot with the color orange
        setColors(Color.orange, Color.orange, Color.orange);
        
        // Set the position of the robot
        this.oldX = this.getX();
        this.oldY = this.getY();
        this.posX = this.getX();
        this.posY = this.getY();

        //add CustomEvent to our Robot
        addCustomEvent(this.odometer);
        addCustomEvent(new Condition("timer") {
            public boolean test() {
                return (getTime() != 0);
            }
        });
        
     // Face robot to origin
        turnRight(225 - getHeading());
        
     // Go to origin position
        while (!isRacing)
            move(18, 18);
	}
	
	
	/**
	 * Actions taken when it sees another robot
	 * @param e -> robot that was scanned
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
        //fire(1);
    }
	
	
	/**
	 * Function to make the robot move to a given set of coordinates
	 * @param x -> is the x coordinate target
	 * @param y -> is the y coordinate target
	 */
	private void move(double x, double y) {
        x = x - this.getX();
        y = y - this.getY();
        double moveAngle = Utils.normalRelativeAngle(Math.atan2(x, y) - getHeadingRadians());
        setTurnRightRadians(Math.atan(Math.tan(moveAngle)));
        ahead(Math.cos(moveAngle) * Math.hypot(x, y));

    }
	
	//** Method for handling the condition of race finished **\\ {código dos slides do prof}
	public void onCustomEvent(CustomEvent ev) {
		Condition cd = ev.getCondition();
		if (cd.getName().equals("IsRacing"))
			this.odometer.getRaceDistance();
	}
	
	
}
