package util;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * The Robots cameras
 * @author Connor_Hofenbitzer
 *
 */
public class NemesisCamera {

  private boolean isNight;
  private AxisCamera axis;
  private UsbCamera camera;
  private CameraServer cameraServer;
  
  public NemesisCamera() {
    isNight = false;
    cameraServer = CameraServer.getInstance();
  }
  
  /**
   * Registers the usb lifecam
   */
  public void addUsbCamera() {
    camera = cameraServer.startAutomaticCapture();
  }
  
  /**
   * Day mode for the vision camera, use this if dahany wants to see through the shooter camera
   */
  public void changeToDayMode() {
    isNight = false;
    System.out.println("day mode");
    axis.setBrightness(50);
    axis.setExposureManual(50);
  }
  
  /**
   * Night mode for the vision camera, use this if your turning to a target
   */
  public void changeToNightMode() {
    isNight = true;
    System.out.println("night mode");
    axis.setBrightness(25);
    axis.setExposureManual(0);
  }
  

  /**
   * Checks what mode the camera is in
   * @return : night mode or not
   */
  public boolean isNightMode() {
    return isNight;
  }
  
  /**
   * Registers a new IP camera 
   * @param ip : ip of the camera
   */
  public void setCameraIP(String ip) {
    axis = cameraServer.addAxisCamera(ip);
  }
  
}
