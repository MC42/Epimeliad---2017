package subsystem;

import org.usfirst.frc.team2590.controllers.VelocityPositionController;
import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

public class LinearShooter implements RobotMap {
	
	private enum shooterState {
		STOP , ACCELERATING , SHOOT_NOW , WAIT
	};
	shooterState linearShooter = shooterState.STOP;
	
	private ControlledManipulator backRoller;
	private ControlledManipulator frontRoller;
		
	private Encoder backEncoder; 
	private Encoder frontEncoder;
	
	public LinearShooter() {
		 backEncoder = new Encoder(6,7);
		 frontEncoder = new Encoder(8,9);
		 backEncoder.setDistancePerPulse(1./360.);
		 frontEncoder.setDistancePerPulse(1./360.);

		 
		 backRoller = new ControlledManipulator(new VelocityPositionController(ShooterkP, ShooterkD, ShooterkF), 
				 								backEncoder, 
				 								new Victor(5), 
				 								true );
		 
		 frontRoller = new ControlledManipulator(new VelocityPositionController(ShooterkP, ShooterkD, ShooterkF), 
				 								frontEncoder, 
				 								new Victor(6), 
				 								true );
		 
		 

	}
	
	private Loop loop_ = new Loop() {
		
		@Override
		public void onStart() {
			
		}
		
		@Override
		public void loop() {
			switch(linearShooter) {
			  case STOP :
				  setSpeedManual(0, 0);
			    break;
			  case ACCELERATING :
				 backRoller.calculateOut();
				 frontRoller.calculateOut();
				break;
			  case SHOOT_NOW :
				  linearShooter = shooterState.ACCELERATING;
				break;
			  case WAIT :
				  linearShooter = shooterState.ACCELERATING;
				break;
			}
		}
		
		@Override
		public void onEnd() {
			
		}
		
		
	};
	
	public Loop getLinearShooterLoop() {
		return loop_;
	}
	
	public void setPoint(double frontSpeed , double backSpeed) {
		frontRoller.setSetpoint(frontSpeed);
		backRoller.setSetpoint(backSpeed);

	}
	
	private void setSpeedManual(double frontSpeed , double backSpeed) {
		backRoller.setMotorManual(backSpeed);
		frontRoller.setMotorManual(frontSpeed);
	}
	
	
	
}
