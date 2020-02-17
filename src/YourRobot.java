//**************************************IMPORTS******************************************
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.CustomEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
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
    private ScannedRobotEvent lastEnemyScanned;



    public void run() {
		// Initialization of the robot with the color orange
        setColors(Color.orange, Color.orange, Color.orange);
        this.oldX = getX();
        this.oldY = getY();
        this.posX = getX();
        this.posY = getY();
        addCustomEvent(odometer);
        addCustomEvent(new Condition("timer") {
            public boolean test() {
                return (getTime() != 0);
            }
        });
        
        
        // Face robot to origin
        turnRight(225 - getHeading());
        
        // Go to origin position
        while (!isRacing)
        	move(18.0, 18.0);
        
        // Face robot to North
        turnRight(360 - getHeading());
        
        while (true) {
            if (lastEnemyScanned == null) {
                turnRight(1);
            }
            
            if(lastEnemyScanned != null) {
                turnLeft(lastEnemyScanned.getBearing() * 1.6);
                ahead(lastEnemyScanned.getDistance() + 18);
                setTurnRight(60);
                ahead(30);
                this.overtakes++;
                lastEnemyScanned = null;
                
                // Return to origin
                while (this.overtakes == 3) {
                    move(18.0, 18.0);
                    if (this.posX == 18.0 && this.posY == 18.0) {
                        break;
                    }
                }
                // Final prints
                if (this.posX == 18.0 && this.posY == 18.0 && isRacing && this.overtakes == 3) {
                    System.out.println("Distância Percorrida: " + this.distanceRoundDone + "pixels");
                    System.out.println("Distância Odometer Professor: " + this.odometer.getRaceDistance());
                    isRacing = false;
                    System.out.println("Acabou a corrida");
                    // Face robot to North
                    turnRight(360 - getHeading());
                    break;
                }
            }
        }
    }
    
    
    /**
     * onScannedRobot: What to do when you see another robot
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        if (isRacing) {
            lastEnemyScanned = e;
            System.out.println("Viu um robot");
        }
    }
    
    /**
     * onHitRobot: What to do when you see another robot
     */
    public void onHitRobot(HitRobotEvent e) {
        setTurnLeft(10);
        back(20);
    }
    
    
    /**
     * onHitWall: What to do when you hit a wall
     */
    public void onHitWall(HitWallEvent e) {
        if (isRacing) {
            turnRight(e.getBearing());
            turnRight(90);
            ahead(20);
        }
    }
    
    
    /**
     * onCustomEvent handler
     */
    public void onCustomEvent(CustomEvent e) {
        // If our custom event "timer" went off,
        if (e.getCondition().getName().equals("timer")) {
            this.posX = getX();
            this.posY = getY();
            if (this.posX == 18.0 && this.posY == 18.0 && this.overtakes == 0 && !isRacing) {
                System.out.println("Começou a corrida");
                isRacing = true;
            }
            if (isRacing) {
                double aux = Math.sqrt(Math.pow((this.posX - this.oldX), 2) + Math.pow((this.posY - this.oldY), 2));
                this.distanceRoundDone += aux;
            }
            this.oldX = getX();
            this.oldY = getY();
        } else if (e.getCondition().getName().equals("IsRacing")) {
            this.odometer.getRaceDistance();
        }
    }
    
    
    private void move(double x, double y) {
        x = x - getX();
        y = y - getY();
        double goAngle = Utils.normalRelativeAngle(Math.atan2(x, y) - getHeadingRadians());
        setTurnRightRadians(Math.atan(Math.tan(goAngle)));
        ahead(Math.cos(goAngle) * Math.hypot(x, y));
    }
}