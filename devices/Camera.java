package ca.uvic.seng330.assn3.devices;

import java.util.UUID;
import ca.uvic.seng330.assn3.HubRegistrationException;
import ca.uvic.seng330.assn3.Mediator;
import ca.uvic.seng330.assn3.Status;

public class Camera extends Device {

  private boolean isRecording;
  private int diskSize;

  private final Mediator aMed;

  public Camera(Mediator med) {
    super();
    aMed = med;
    diskSize = 999;

    this.setID(UUID.randomUUID());
    try {
      aMed.register(this);
      System.out.println("register");
    } catch (HubRegistrationException e) {
      // in future, log this
    }
  }
  public Camera(Mediator med, UUID id, Status s) {
    //super();
    aMed = med;
    diskSize = 999;
    this.setID(id);
    this.setStatus(s);
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
      // in future, log this
    }
    
  }

  public String startup() {
    isRecording = false;
    this.setStatus(Status.OFF);
    return "started";
  }

  public void record() throws CameraFullException {
    isRecording = true;
    aMed.alert(this, "Started recording");
    if(Math.random()*1000 > diskSize) {
      throw new CameraFullException("Camera Full");
    }
  }

  @Override
  public Status getStatus() {
    return super.getStatus();
  }

  @Override
  public String toString() {
    return "Camera id " + super.getIdentifier().toString();
  }
  
  public String getDeviceName() {
	  return this.getClass().getSimpleName();
  }

public boolean isRecording() {
	if(isRecording==true) {
		return true;
	}
	else return false;
}
}

