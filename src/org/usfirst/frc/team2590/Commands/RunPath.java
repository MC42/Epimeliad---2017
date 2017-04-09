package org.usfirst.frc.team2590.Commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.usfirst.frc.team2590.Controllers.Path;
import org.usfirst.frc.team2590.Controllers.PathSegment;
import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class RunPath extends NemesisCommand {

  private Path paths;

  //... means array
  public RunPath(PathSegment... path) {
    paths = new Path(new ArrayList<PathSegment>(Arrays.asList(path)));
  }

  public void flip() {
    Robot.driveT.flipPath();
  }

  public void unFlip() {
    Robot.driveT.unFlipPath();
  }
  
  @Override
  public void run() {
    System.out.println("running");
    Robot.driveT.changePath(paths);
    Timer.delay(.1);
    Robot.driveT.followPath();
  }

  @Override
  public boolean done() {
    return Robot.driveT.pathIsDone();
  }

}
