package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.Device;
import ca.uvic.seng330.assn3.Client;

public interface Mediator {

  public void unregister(Device device) throws HubRegistrationException;

  public void unregister(Client client) throws HubRegistrationException;

  //not in spec, do not test
  public void register(Device pDevice) throws HubRegistrationException;

  public void register(Client pClient) throws HubRegistrationException;

  public void alert(Device pDevice, String pMessage);
}
