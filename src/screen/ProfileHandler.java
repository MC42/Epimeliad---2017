package screen;

import edu.wpi.first.wpilibj.DriverStation;

public class ProfileHandler {
	private Profile[] profiles;
	
	public ProfileHandler(Profile... profiles) {
		this.profiles = profiles;
		profiles[0].setState(true);
	}
	
	public void setProfile(String name) {
		int index = -1;
		
		for(int i = 0; i < profiles.length; i++) {
			profiles[i].setState(false);
			System.out.println("my nam is " + profiles[i].myNames());
			if(profiles[i].getName().equalsIgnoreCase(name)) {
				i = index;
			}
		}
		
		if(index == -1) {
			profiles[0].setState(true);
			DriverStation.reportWarning("no profile with this name", false);
		} else {
			profiles[index].setState(true);
		}
	}
}
