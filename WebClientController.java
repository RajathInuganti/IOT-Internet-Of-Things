package ca.uvic.seng330.assn3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import ca.uvic.seng330.assn3.devices.*;
import ca.uvic.seng330.assn3.devices.Device;

public class WebClientController {
  private final WebClient model ;
  String username = "Bob";
  List<String> choices = new ArrayList<>();
  boolean UserLogin = false;
  private UserStatus currentUserStatus = UserStatus.ADMIN;
  

  public WebClientController(WebClient pmodel) {
    model = pmodel;
    choices.add("Camera");
    choices.add("Thermostat");
    choices.add("SmartPlug");
    choices.add("Lightbulb");
  }
  public int getNumber(String dev) {
    int numdev = 0;
    HashMap<UUID, Device> map = new HashMap<UUID,Device>();
      map = (HashMap<UUID, Device>) model.getHubInstance().getDevices();
      for(HashMap.Entry<UUID, Device> entry : map.entrySet()) {
          //UUID key = entry.getKey();
          Device value = entry.getValue();
          if(value.getDeviceName().equals(dev)) {
              numdev++;
          }
      }
      return numdev;
  }
  public int numOfDevices() {
    HashMap<UUID, Device> map = new HashMap<UUID,Device>();
    map = (HashMap<UUID, Device>) model.getHubInstance().getDevices();
    return map.size();
  }
  public List deviceList() {
    List l = new LinkedList();
    HashMap<UUID, Device> map = new HashMap<UUID,Device>();
    map = (HashMap<UUID, Device>) model.getHubInstance().getDevices();
    for(Entry<UUID, Device> entry: map.entrySet()) {
      l.add(entry.getValue());
    }
    return l;
  }
  
  public LinkedList<Device> GetDeviceInfo(String dev) {
	    LinkedList<Device> l = new LinkedList<Device>();
	    HashMap<UUID, Device> map = new HashMap<UUID,Device>();
	      map = (HashMap<UUID, Device>) model.getHubInstance().getDevices();
	      for(HashMap.Entry<UUID, Device> entry : map.entrySet()) {
	          //UUID key = entry.getKey();
	          Device value = entry.getValue();
	          if(value.getDeviceName().equals(dev)) {
	              l.add(value);
	          }
	      }
	      return l;
	  }
  public String getLog() {
    String s = model.getHubInstance().readJSONFile("log.txt");
    return s;
  }
  public String getUser() {
    return username + currentUserStatus;
  }
  public void setUser(String s) {
    username = s;
  }
  public UserStatus getUserStatus() {
    return currentUserStatus;
  }
  public List getUserList() {
    return model.getHubInstance().userList();
  }
  public void setUserStatus(UserStatus currentUserStatus) {
    this.currentUserStatus = currentUserStatus;
  }
  public void addDevice(String letter) {
    if (letter.equals("Camera")) {new Camera(model.getHubInstance());};
    if (letter.equals("Lightbulb")) {new Lightbulb(model.getHubInstance());};
    if (letter.equals("SmartPlug")) {new SmartPlug(model.getHubInstance());};
    if (letter.equals("Thermostat")) {new Thermostat(model.getHubInstance());};}
  
  public void removeUser(String letter) {
    model.getHubInstance().removeUser(letter);;
  }
  public void removeDevice(Device d) {
    try {
      model.getHubInstance().unregister(d);
    }catch(HubRegistrationException e){
      e.printStackTrace();
    }
  }
}
