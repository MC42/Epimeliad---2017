package util;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class NemesisCamera {

  private boolean isNight;
  private AxisCamera axis;
  private UsbCamera camera;
  private CameraServer cameraServer;
  
  public NemesisCamera() {
    isNight = false;
    cameraServer = CameraServer.getInstance();
  }
  
  public void addUsbCamera() {
    camera = cameraServer.startAutomaticCapture();
  }
  
  public void changeToDayMode() {
    isNight = false;
    System.out.println("day mode");
    //axis.setBrightness(50);
    //axis.setExposureManual(50);
  }
  
  public void changeToNightMode() {
    isNight = true;
    System.out.println("night mode");
    //axis.setBrightness(25);
    //axis.setExposureManual(0);
  }
  
  public boolean isNightMode() {
    return isNight;
  }
  
  public void setCameraIP(String ip) {
    axis = cameraServer.addAxisCamera(ip);
   
  }
  
}
