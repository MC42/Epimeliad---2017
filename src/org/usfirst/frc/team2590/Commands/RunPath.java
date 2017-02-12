package org.usfirst.frc.team2590.Commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.usfirst.frc.team2590.navigation.Path;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.robot.Robot;

public class RunPath extends Command {

  Path paths;

  //paths
  public RunPath(PathSegment... path) {
    paths = new Path(new ArrayList<PathSegment>(Arrays.asList(path)));
  }

  public void startChange() {
    Robot.dt.changePath(paths);
  }

  @Override
  public void run() {
    Robot.dt.followPath();
  }


}
