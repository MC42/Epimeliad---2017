package subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;

public class Intake implements RobotMap{
	
  private enum intakeState {
    STOP , INTAKE , EXAUST
  };
  private intakeState intake = intakeState.STOP;
	
  private Victor intakeMotor;
  private static final double INTAKESPEED = 1;
	
  public Intake() {
    intakeMotor = new Victor(intakePWM);
  }
	
  private Loop loop_ = new Loop() {

    @Override
	public void onStart() {
			
	}

	@Override
	public void loop() {
	  switch(intake) {
	    case STOP : //completely stop the motor
		  intakeMotor.set(0);
		  break;
	    case INTAKE : //suck in the balls
	      intakeMotor.set(INTAKESPEED);
		  break;
	    case EXAUST : //spit out the balls
	      intakeMotor.set(-INTAKESPEED);
		  break;
		}
	 }

	@Override
	public void onEnd() {
			
		}
	};
	
  public Loop getIntakeLoop() {
    return loop_;
  }
	
  public void suckBalls() {
    intake = intakeState.INTAKE;
  }
	
  public void stopSuck() {
    intake = intakeState.STOP;
  }
	
  public void unSuck() {
    intake = intakeState.EXAUST;
  }
}
