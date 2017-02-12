package org.usfirst.frc.team2590.subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;
import util.NemesisSolenoid;

public class Intake implements RobotMap {

  private enum intakeStates {
    STOP , INTAKE , EXAUST , INTAKE_NO_PULL , JUST_PULL
  };
  private intakeStates intake = intakeStates.STOP;

  private Victor pullyMotor;
  private Victor intakeMotor;
  private NemesisSolenoid intakeSolenoid;

  public Intake() {
    pullyMotor = new Victor(PULLYMOTORPWM);
    intakeMotor = new Victor(INTAKEMOTORPWM);
    intakeSolenoid = new NemesisSolenoid(INTAKE_SOLENOID);
  }

  private Loop loop_ = new Loop() {

    @Override
    public void onStart() {

    }

    @Override
    public void loop() {
      switch (intake) {
        case STOP :
          //set motor speeds
          pullyMotor.set(0);
          intakeMotor.set(0);
          intakeSolenoid.set(false);
          break;
        case EXAUST :
          //spit out balls
          pullyMotor.set(-1);
          intakeMotor.set(-1);
          intakeSolenoid.set(true);
          break;
        case INTAKE :
          //suck balls
          pullyMotor.set(1);
          intakeMotor.set(1);
          intakeSolenoid.set(true);
          break;
        case INTAKE_NO_PULL :
          //intake balls without the pully
          pullyMotor.set(0);
          intakeMotor.set(1);
          intakeSolenoid.set(true);
          break;
        case JUST_PULL :
          //only use the pully
          pullyMotor.set(1);
          intakeMotor.set(0);
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

  /**
   * Stop the intake
   */
  public void stopIntake() {
    intake = intakeStates.STOP;
  }

  /**
   * Intake balls auto pully
   */
  public void intakeBalls() {
    intake = intakeStates.INTAKE;
  }

  /**
   * Spit out balls auto pully
   */
  public void outtakeBalls() {
    intake = intakeStates.EXAUST;
  }

  /**
   * Intake balls no pully
   */
  public void onlyIntake() {
    intake = intakeStates.INTAKE_NO_PULL;
  }

  /**
   * Only run the pully
   */
  public void onlyPullBalls() {
    intake = intakeStates.JUST_PULL;
  }


}
