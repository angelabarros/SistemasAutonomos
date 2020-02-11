import java.awt.Graphics2D;
import java.awt.Rectangle;

import robocode.AdvancedRobot;
import robocode.Robot;
import robocode.ScannedRobotEvent;


public class YourRobot extends AdvancedRobot {

	public void run() {
		while(true) {
			ahead(100);
			turnGunRight(360);
            back(100);
            turnGunRight(360);
            
            
            
            setAdjustGunForRobotTurn(true);
            setAdjustRadarForGunTurn(true);
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
        //fire(1);
    }
	
	
}
