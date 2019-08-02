package ca.uvic.seng330.assn3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import ca.uvic.seng330.assn3.devices.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.uvic.seng330.assn3.Client;

public class Hub extends Device implements Mediator {

  private HashMap<UUID, Device> aDevices = new HashMap<UUID, Device>();
  private HashMap<UUID, Client> aClients = new HashMap<UUID, Client>();
  private Logger logger = LoggerFactory.getLogger(Hub.class);
  
  Hub(){
    System.out.println("strating");
    this.startup();
    
  }
  public String readJSONFile(String filename) {
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(filename));
      StringBuilder stringBuilder = new StringBuilder();
      String line = null;
      String ls = System.getProperty("line.separator");
      while ((line = reader.readLine()) != null) {
          stringBuilder.append(line);
          stringBuilder.append(ls);
      }
      // delete the last new line separator
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      reader.close();
      
      String content = stringBuilder.toString();
      return content;
  }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }
    
  public UserStatus login(String pUserName, String pPassword) {
    //"./login.json"
      String content = readJSONFile("./login.json");
      JSONObject mainJSONObject = new JSONObject(content);  
      JSONArray loginInfoArray = mainJSONObject.getJSONArray("login");
      for (int i=0; i < loginInfoArray.length(); i++) {
        JSONObject loginInfo = loginInfoArray.getJSONObject(i);
        String aUserName = (String) loginInfo.get("userName");
        if (pUserName.equals(aUserName)) {
          String aPassword = (String) loginInfo.get("password");
          if (pPassword.equals(aPassword)) {
            String aStatus = (String) loginInfo.get("status");
            if (aStatus.equals("A")) {
              log("logging in "+ aUserName + " as ADMIN");
              return UserStatus.ADMIN;
            }else {
              log("logging in "+ aUserName + " as STANDARD");
              return UserStatus.STANDARD;
            }
          }
          
        }
        
      }
    log("Failed login attempt, please restart application");
    return UserStatus.NA;
  }
  public void startup() {
    String content = readJSONFile("./devices.json");
    JSONArray deviceArray = new JSONArray(content);
    //JSONObject mainJSONObject = new JSONObject(content);  
    //JSONArray deviceArray = mainJSONObject.getJSONArray("");
    
    for (int i=0; i < deviceArray.length(); i++) {
      JSONObject deviceInfo = deviceArray.getJSONObject(i);
      JSONObject d = deviceInfo.getJSONObject("device");
      String key = (String) d.get("identifier");
      String dev = (String) d.get("deviceName");
      Status stat = Status.valueOf((String) d.get("status"));
      Device temp = null;
      if(dev.equals("Camera")) {
        temp = new Camera(this, UUID.fromString(key), stat);
      }
      else if(dev.equals("Thermostat")) {
        temp = new Thermostat(this, UUID.fromString(key), stat);}
      else if(dev.equals("SmartPlug")) {
        temp = new SmartPlug(this, UUID.fromString(key), stat);}
      else if(dev.equals("Lightbulb")) {
        temp = new Lightbulb(this, UUID.fromString(key), stat);
        }
      //aDevices.put(UUID.fromString(key), temp); 
    }
  }
  public void shutdown() {
    
    JSONObject j;
    JSONArray ja = new JSONArray();
    HashMap<String, Device> jsonMap = new HashMap<String, Device>();
    for(Entry<UUID, Device> entry: aDevices.entrySet()) {
      jsonMap.put("device",entry.getValue());
      j = new JSONObject(jsonMap);
      ja.put(j);
    }
    
    /*for(int i=0; i<aDevices.size();i++) {
      j.put(new JSONObject(aDevices.get(i)));
    }*/
    
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter("./devices.json"));
      String s = "{devices";
      writer.write(ja.toString());
      writer.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

  @Override
  public void register(Device pDevice) throws HubRegistrationException {
    if (!aDevices.containsKey(pDevice.getIdentifier())) {
      aDevices.put(pDevice.getIdentifier(), pDevice);
    } else {
      throw new HubRegistrationException(pDevice + " was already registered");
    }
     
  }

  @Override
  public void register(Client pClient) throws HubRegistrationException {
    if (!aClients.containsKey(pClient.getIdentifier())) {
      aClients.put(pClient.getIdentifier(), pClient);
    } else {
      throw new HubRegistrationException(pClient + " was already registered");
    }
  }

  @Override
  public void unregister(Device device) throws HubRegistrationException {
    if (!aDevices.containsKey(device.getIdentifier())) {
      log("Unknown Device unregister");
      throw new HubRegistrationException("Device does not exists!");
    }
    aDevices.remove(device.getIdentifier());
    log("Device removed " + device);
  }

  @Override
  public void unregister(Client client) throws HubRegistrationException {
    if (!aClients.containsKey(client.getIdentifier())) {
      throw new HubRegistrationException("Client does not exists!");
    }
    aClients.remove(client.getIdentifier());
  }

  /**
   * Logging. Use SLF4J to write all message traffic to the log file.
   *
   * @param logMsg
   */
  public void log(String logMsg) {
    logger.info(logMsg);
  }

  /**
   * Alerts are events that happen at the Device level. They send the alert to the hUb, which
   * redistributes to the clients
   *
   * @param pMessage
   */
  @Override
  public void alert(Device pDevice, String pMessage) {

    // initialize the map
    JSONObject jsonMessage = new JSONMessaging(pDevice, pMessage).invoke();

    // ordinarily, we would have logic for which clients to notify
    notifyClients(jsonMessage);
    log("ALERT msg: " + pMessage + " - from Device " + pDevice.toString());
  }

  private void notifyClients(JSONObject pMsg) {
    for (Client c : aClients.values()) {
      c.notify(pMsg);
      log("Notified: " + c.toString());
    }
  }

  public Map<UUID, Device> getDevices() {
    return new HashMap<UUID, Device>(aDevices);
  }
  public void addUser(List l) {
    String content = readJSONFile("./login.json");
    JSONObject mainJSONObject = new JSONObject(content);  
    JSONArray loginInfoArray = mainJSONObject.getJSONArray("login");
    JSONObject temp = new JSONObject();
    
    temp.put("userName", l.get(0).toString());
    temp.put("password", l.get(1).toString());
    temp.put("status", l.get(2).toString().substring(0,1));
    loginInfoArray.put(temp);
    
    try {

      BufferedWriter writer = new BufferedWriter(new FileWriter("./login.json"));
      writer.write(mainJSONObject.toString());
      writer.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  public void removeUser(String s) {
    String content = readJSONFile("./login.json");
    JSONObject mainJSONObject = new JSONObject(content);  
    JSONArray loginInfoArray = mainJSONObject.getJSONArray("login");
    
    for (int i=0; i < loginInfoArray.length(); i++) {
      JSONObject loginInfo = loginInfoArray.getJSONObject(i);
      String aUserName = (String) loginInfo.get("userName");
      if (s.equals(aUserName)) {
        loginInfoArray.remove(i);
        break;
      }
    }
    
    try {

      BufferedWriter writer = new BufferedWriter(new FileWriter("./login.json"));
      writer.write(mainJSONObject.toString());
      writer.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  public List userList() {
    List<String> l = new LinkedList();
    String content = readJSONFile("./login.json");
    JSONObject mainJSONObject = new JSONObject(content);  
    JSONArray loginInfoArray = mainJSONObject.getJSONArray("login");
    for (int i=0; i < loginInfoArray.length(); i++) {
      JSONObject loginInfo = loginInfoArray.getJSONObject(i);
      String aUserName = (String) loginInfo.get("userName");
      l.add(aUserName);
    }
    return l;
  }
}