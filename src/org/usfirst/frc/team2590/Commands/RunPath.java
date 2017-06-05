package org.usfirst.frc.team2590.Commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.usfirst.frc.team2590.Controllers.Path;
import org.usfirst.frc.team2590.Controllers.PathSegment;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

/**
 * Runs a given path
 * @author Connor_Hofenbitzer
 *
 */
public class RunPath extends NemesisCommand {

  private Path paths;

  //... means array
  public RunPath(PathSegment... path) {
    paths = new Path(new ArrayList<PathSegment>(Arrays.asList(path)));
  }

  /**
   * Flips the path so that the robot can drive backwards
   */
  public void flip() {
    Robot.driveT.flipPath();
  }

  /**
   * Flips the path again so that it drives forwards
   */
  public void unFlip() {
    Robot.driveT.unFlipPath();
  }
  
  @Override
  public void run() {
    Robot.driveT.resetPath();
    Robot.driveT.resetSensors();
    Robot.driveT.changePath(paths);
    Timer.delay(.1);
    Robot.driveT.followPath();
  }

  @Override
  public boolean done() {
    return Robot.driveT.pathIsDone();
  }

}
