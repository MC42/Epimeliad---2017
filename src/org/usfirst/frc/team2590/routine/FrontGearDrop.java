package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

public class FrontGearDrop extends AutoRoutine {

  //points
  private Point start;
  private Point front;
  private Point middle;
  
  //drive straight
  private DriveAtAngle driveToDropGear;
 
  //path
  private RunPath pathToBoil;
  
  public FrontGearDrop(boolean side) {
    
    //drive straight
    driveToDropGear = new DriveAtAngle(-6, 0);
    
    //points
    start = new Point(0, 0); 
    middle = new Point(4 , -4.8 * (side?1:-1)); 
    front = new Point(4.6 , -8.2 * (side?1:-1)); 
    
    //path
    pathToBoil = new RunPath(new PathSegment(start , middle) , new PathSegment(middle, front));
  }

  @Override
  public void run() {

    //make sure were in high gear
    Robot.driveT.resetSensors();
    Robot.driveT.shiftHigh();
    
    //drive to the gear
    driveToDropGear.run();    
    waitUntilDone(3, driveToDropGear.done());
    
    //drop the gear on the peg
    Robot.gearHold.openWings();
    
    //revv the shooter
    Robot.shooter.setSetpoint(6600);
    Robot.shooter.revShooter();
    
    //get to the boiler
    Robot.driveT.reset();
    pathToBoil.startChange();
    pathToBoil.run();
    waitUntilDone(3 , pathToBoil.done());
    
    //shoot
    Robot.shooter.shootWhenReady();
  }

  @Override
  public void end() {
    Robot.driveT.setStop();
  }

  
}
