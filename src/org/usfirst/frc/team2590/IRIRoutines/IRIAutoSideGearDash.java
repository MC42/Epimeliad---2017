package org.usfirst.frc.team2590.IRIRoutines;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.Commands.TurnToAngle;
import org.usfirst.frc.team2590.Controllers.PathSegment;
import org.usfirst.frc.team2590.Controllers.Point;
import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.routine.AutoRoutine;
import org.usfirst.frc.team2590.routine.SideGearSimple;

import edu.wpi.first.wpilibj.Timer;

public class IRIAutoSideGearDash extends AutoRoutine implements IRIAutoSettings {

  //basic driving
  private TurnToAngle turnToHPS;
  private SideGearSimple dropTheGear;
  
  private Point startPoint;
  private Point middlePoint;
  private Point finishPoint;
  
  //path configuration (Points are under AutoSettings)
  private PathSegment startToMiddle;
  private PathSegment middleToHumanS;
  
  //creates a runnable path
  private RunPath getToHPS;
  
  /**
   * Drops the gear on the side peg and guns it down the field
   * @param side : which side to place the gear
   * @param needsCurve : if you need to curveAround the airship
   */
  public IRIAutoSideGearDash(boolean side, boolean needsCurve) {
    
    //basic driving stuff
    turnToHPS = new TurnToAngle(turnToHP * (side?-1:1));
    dropTheGear = new SideGearSimple(side);
    startPoint = new Point(0,0,0);
    middlePoint = new Point( straightDistToHP/2 , 0 ,0);
    finishPoint = new Point( straightDistToHP , 0.5*(side?-1:1) , 0);
    startToMiddle = new PathSegment(startPoint, middlePoint);
    middleToHumanS = new PathSegment(middlePoint , finishPoint);
    getToHPS = new RunPath(startToMiddle , middleToHumanS);

    if(needsCurve) {
      middlePoint = new Point( curveXPoint/2 , 0 ,0);
      finishPoint = new Point( curveXPoint , curveYPoint*(side?-1:1) , 0);
      Point constra = new Point((curveXPoint/2+5), curveYPoint/2*(side?-1:1) , 0);
      PathSegment constrainSeg = new PathSegment(middlePoint, constra);
      startToMiddle = new PathSegment(startPoint, middlePoint);
      middleToHumanS = new PathSegment(constra , finishPoint);
      getToHPS = new RunPath(startToMiddle , constrainSeg, middleToHumanS);

    }
    //path config
    
    
  }
  
  @Override
  public void run() {
    
    dropTheGear.run();
    
    // reset all
    Robot.driveT.resetPath();
    Robot.driveT.shiftHigh();
    Robot.driveT.resetSensors();
    Robot.shooter.setInterpolation(true);
    Robot.shooter.setSetpoint(3400);
    //Robot.shooter.revShooter();

    if(!Robot.gearHold.hasGear()) {
      
      //turns towards the human player station
      turnToHPS.run();
      waitUntilDone(2, turnToHPS::done);
      Robot.driveT.setStop();
      
      //drives to the human player station
      Robot.driveT.shiftHigh();
      Robot.driveT.resetPath();
      Robot.driveT.resetSensors();
      Robot.driveT.resetDriveController();      
      Timer.delay(.5);
      getToHPS.flip();
      getToHPS.run();
      waitUntilDone(4, getToHPS::done);
      Robot.driveT.setStop();
    }
    
  }

  @Override
  public void end() {
  }

}
