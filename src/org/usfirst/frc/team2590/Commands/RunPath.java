package org.usfirst.frc.team2590.Commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.usfirst.frc.team2590.navigation.Path;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.robot.Robot;

public class RunPath extends NemesisCommand {

  Path paths;
  boolean pathFlip;
  
  //paths
  //... means array
  //2D array that's fed into the drivetrain
  public RunPath(PathSegment... path) {
    paths = new Path(new ArrayList<PathSegment>(Arrays.asList(path)));
  }

  public void startChange() {
    Robot.driveT.changePath(paths);
  }

  public void flip() {
    Robot.driveT.flipPath();
  }
  
  @Override
  public void run() {
    Robot.driveT.followPath();
  }

  @Override
  public boolean done() {
    return Robot.driveT.pathIsDone();
  }

}
