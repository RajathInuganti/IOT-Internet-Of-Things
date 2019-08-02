package ca.uvic.seng330.assn3.devices;

import ca.uvic.seng330.assn3.Status;

import java.util.UUID;

public abstract class Device {

  private UUID aUuid=UUID.randomUUID();
  private Status aStatus = Status.OFF; // This can't be NULL!

  public UUID getIdentifier() {
    return aUuid;
  }
  public void setID(UUID id) {
    aUuid = id;
  }
  public Status getStatus() {
    // Since the status can't be NULL, then check IF NULL and IF return dummy
    // status.
    return aStatus == null ? Status.NOT_AVAILABLE : aStatus;
  }

  public void setStatus(Status status) {
    this.aStatus = status;
  }

  @Override
  public String toString() {
    return aUuid.toString();
  }
  
  public String getDeviceName() {
	  return this.getClass().getSimpleName();
  }

}
