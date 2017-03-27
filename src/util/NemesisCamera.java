package util;

import edu.wpi.first.wpilibj.CameraServer;

public class NemesisCamera {

  private CameraServer cameraServer;
  
  public NemesisCamera(boolean isAxisCamera) {
    cameraServer = CameraServer.getInstance();
    if(!isAxisCamera) {
      cameraServer.startAutomaticCapture();
    }
  }
  
  public void setCameraIP(String ip) {
    cameraServer.addAxisCamera(ip);

  }
  
}
