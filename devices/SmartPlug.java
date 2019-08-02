package ca.uvic.seng330.assn3.devices;

import java.util.UUID;
import ca.uvic.seng330.assn3.Hub;
import ca.uvic.seng330.assn3.HubRegistrationException;
import ca.uvic.seng330.assn3.Mediator;
import ca.uvic.seng330.assn3.Status;

public class SmartPlug extends Device implements SwitchableDevice {

  private final Mediator aMed;
  private boolean isOn = false;

  public SmartPlug(Mediator med) {
    super();
    aMed = med;
    isOn = false;
    this.setID(UUID.randomUUID());
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
      e.printStackTrace();
    }
  }

  public SmartPlug(Mediator med, UUID id, Status s) {
    //super();
    aMed = med;
    isOn = false;
    this.setID(id);
    //this.setStatus(s);
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
      e.printStackTrace();
    }
    
  }

  @Override
  public void toggle() {
    isOn = !isOn;
    String status = "plug is now ";
    aMed.alert(this, status + isOn);
  }

  @Override
  public String toString() {
    return "Smartplug id " + super.getIdentifier().toString();
  }
}
