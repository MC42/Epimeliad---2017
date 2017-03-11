package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class FrontGearDrop extends AutoRoutine implements RobotMap{

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
    driveToDropGear = new DriveAtAngle(-((125-36)/12), 0);

    //points
    start = new Point(0, 0 );
    front = new Point(4.6 , -8.2 * (side?1:-1));
    middle = new Point(2 , -1 * (side?1:-1) , Robot.gearHold::closeWings);

    //path
    pathToBoil = new RunPath(new PathSegment(start , middle) , new PathSegment(middle, front));
  }

  @Override
  public void run() {
    
    //drive to the gear
    driveToDropGear.run();
    Timer.delay(1.25);
    Robot.gearHold.openWings();
    waitUntilDone(1.35, driveToDropGear::done);

    //get to the boiler
    Robot.driveT.reset();
    pathToBoil.run();
    waitUntilDone(3 , pathToBoil::done);

    //shoot
    Robot.shooter.setSetpoint(6400);
    Robot.shooter.shootWhenReady();
  }

  @Override
  public void end() {
    Robot.driveT.setStop();
    Robot.feeder.stopFeeder();
  }


}
