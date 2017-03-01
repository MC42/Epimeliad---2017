package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.AutomatedShootingSequence;
import org.usfirst.frc.team2590.Commands.DriveAtAngle;
import org.usfirst.frc.team2590.Commands.StopShootingSequence;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class CoolDahanyAuto extends AutoRoutine implements RobotMap{
  
  private DriveAtAngle driveBack;
  private TurnToAngle turnInPlace;
  
  private AutomatedShootingSequence shoot;
  private StopShootingSequence stopShooting;
  
  private DriveAtAngle driveToGear;
  private DriveAtAngle driveAwayGear;
  private DriveAtAngle driveBeforeTurn;
  
  private final static double AngleToGear = 60;
  private final static double DistanceToPeg = ((100-ROBOTLENGTH)/12);
  private final static double distanceToFirst = ((79-ROBOTLENGTH)/12);
  
  public CoolDahanyAuto() {
    
    //shooting
    stopShooting = new StopShootingSequence();
    shoot = new AutomatedShootingSequence(6400 , 4);
    
    //driving
    turnInPlace = new TurnToAngle(-66);
    driveAwayGear = new DriveAtAngle(30/12, 0);
    driveBack = new DriveAtAngle(-(5.0/12.0), 0);
    driveBeforeTurn = new DriveAtAngle(-distanceToFirst, 0);
    driveToGear = new DriveAtAngle(-DistanceToPeg, AngleToGear);
  }
  
  @Override
  public void run() {
    
    Robot.driveT.resetSensors();
    Robot.driveT.shiftHigh();    
    Robot.gearHold.closeWings();

    //shoot
    shoot.run();
    stopShooting.run();
    
    //drive to start position
    driveBack.run();
    waitUntilDone(3, driveBack::done);
    Robot.driveT.resetSensors();
    Timer.delay(.1);
    turnInPlace.run();
    Timer.delay(1);
    
    //drive to first point
    Robot.driveT.shiftHigh();
    Robot.driveT.resetSensors();
    driveBeforeTurn.run();
    waitUntilDone(3, driveBeforeTurn::done);
    
    //drive to drop the gear
    driveToGear.run();
    waitUntilDone(3, driveToGear::done);
    Robot.gearHold.openWings();
    Timer.delay(0.5);
    
    //drive away
    driveAwayGear.run();
    
  }

  @Override
  public void end() {
    Robot.driveT.setOpenLoop();
  }

}
