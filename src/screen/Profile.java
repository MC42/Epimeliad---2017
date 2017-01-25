package screen;

import org.usfirst.frc.team2590.robot.Robot;

public class Profile extends Thread{
	private String[] lines;
	private boolean isOn;
	private String myname;
	
	public Profile(String name , String... lines ) {
		this.lines = lines;
		myname = name;
		System.out.println("nam " + myname);
		isOn = false;
		this.start();
	}
	
	//updater method
	public void run() {
		while(true) {
			//this should only run when it needs
			if(isOn) {
				//clear the screen
				Robot.display.LCDwriteCMD(Robot.display.LCD_CLEARDISPLAY);
				
				//write the lines
				for(int i = 0; i < lines.length; i++) {
					Robot.display.LCDwriteString(lines[i], i);
				}
				
				//give the poor thing rest
				try {
					Thread.sleep(500);
				} catch(Exception e) {}
			}
			
		}
	}
	
	public void setState(boolean on) {
		isOn = on;
	}
	
	public boolean onState() {
		return isOn;
	}
	
	public String myNames() {
		return myname;
	}
}
