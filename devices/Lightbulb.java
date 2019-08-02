package ca.uvic.seng330.assn3.devices;

import java.util.UUID;
import ca.uvic.seng330.assn3.HubRegistrationException;
import ca.uvic.seng330.assn3.Mediator;
import ca.uvic.seng330.assn3.Status;

public class Lightbulb extends Device implements SwitchableDevice {

  private boolean isOn = false;
  private final Mediator aMed;

  public Lightbulb(Mediator pMed) {
    super();
    aMed = pMed;
    isOn = false;

    this.setID(UUID.randomUUID());
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
      e.printStackTrace();
    }
  }
  public Lightbulb(Mediator pMed, UUID id, Status s) {
    //super();
    aMed = pMed;
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
    String status = "lightbulb is now ";
    aMed.alert(this, status + isOn);
  }

  public boolean getCondition() {
    return isOn;
  }

  @Override
  public String toString() {
    return "Lightbulb id " + super.getIdentifier().toString();
  }
}
