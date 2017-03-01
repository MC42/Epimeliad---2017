package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

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
    driveToDropGear = new DriveAtAngle(-5.3, 0);
    
    //points
    start = new Point(0, 0 ); 
    middle = new Point(2 , -1 * (side?1:-1) , Robot.gearHold::closeWings); 
    front = new Point(4.6 , -8.2 * (side?1:-1)); 
    
    //path
    pathToBoil = new RunPath(new PathSegment(start , middle) , new PathSegment(middle, front));
  }

  @Override
  public void run() {
    //get ready for auto
    Robot.driveT.resetSensors();
    Robot.driveT.shiftHigh();    
    Robot.gearHold.closeWings();
    
    //drive to the gear
    driveToDropGear.run(); 
    waitUntilDone(3, driveToDropGear::done);
    
    //drop the gear on the peg
    Robot.gearHold.openWings();
    Timer.delay(.5);
       
    //get to the boiler
    Robot.driveT.reset();
    pathToBoil.startChange();
    pathToBoil.run();
    //waitUntilDone(3 , pathToBoil.done());
    
    //shoot
    //Robot.shooter.shootWhenReady();
  }

  @Override
  public void end() {
    Robot.driveT.setStop();
  }

  
}
