package subsystem;

import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import controllers.PIDF;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter implements RobotMap{
	
	//system states
	private enum shooterState {
		STOP , SHOOT_READY , SHOOT_NOW
	};
	private shooterState shooter = shooterState.STOP;
	
	//sensors motors and controllers
	private PIDF pidfLoop;
	private double stp = 0;
	private double output;
	private Victor feederVictor;
	private Victor shooterVictor;
	private Encoder shooterEncoder;

	public Shooter() {
		output = 0;
		//0.085 0.35 0.000083
		//0.001 0.01 1.0E-4 //seems to work
		//0.004 0.45 1.0E-4
		pidfLoop = new PIDF(0.0042 ,0.45, 1E-4 );
		feederVictor = new Victor(feederPWM);
		shooterVictor = new Victor(shooterPWM);
		shooterEncoder = new Encoder(shooterEncA , shooterEncB , false , EncodingType.k1X);
		shooterEncoder.setPIDSourceType(PIDSourceType.kRate);
		
		shooterEncoder.setDistancePerPulse(1./360.);

	}
	
	private Loop loop_ = new Loop() {

		@Override
		public void onStart() {
			
		}

		@Override
		public void loop() {
			switch(shooter) {
			case STOP : 
				shooterVictor.set(0);
				break;
			case SHOOT_READY : //fall through
				output = pidfLoop.calculate(shooterEncoder.getRate()*60);
				shooterVictor.set(output );
				if(Math.abs(shooterEncoder.getRate() - stp) < 100) {
					feederVictor.set(1);
				} else {
					feederVictor.set(0);
				}
				break;
			case SHOOT_NOW :
				output = pidfLoop.calculate(shooterEncoder.getRate()*60);
				shooterVictor.set(output );
				break;
			}

		}

		@Override
		public void onEnd() {
			
		}
		
	};
	
	public Loop getShooterLoop() {
		return loop_;
	}
	
	public void setSetpoint(double setpoint) {
		stp = setpoint;
		pidfLoop.setSetpoint(setpoint);
	}
	
	public void takeShot() {
		shooter = shooterState.SHOOT_NOW;
	}
	
	public void stopShot() {
		shooter = shooterState.STOP;
	}

	public void patienceIsKey() {
		shooter = shooterState.SHOOT_READY;
	}
	
}
