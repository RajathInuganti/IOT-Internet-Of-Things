package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.CameraFullException;
import ca.uvic.seng330.assn3.devices.Temperature;
import ca.uvic.seng330.assn3.devices.Temperature.TemperatureOutofBoundsException;
import ca.uvic.seng330.assn3.devices.Thermostat;

public class ThermostatController {
  public void setTemp(Thermostat c, String temp, String unit) {
    try {
    Temperature t = new Temperature(20, Temperature.Unit.CELSIUS);
    
    if (unit.equals("FAHRENHEIT")) {
      
        t = new Temperature(Integer.parseInt(temp), Temperature.Unit.FAHRENHEIT);
      
    } else {
      t = new Temperature(Integer.parseInt(temp), Temperature.Unit.CELSIUS);
    }
    c.setTemp(t);
    c.setStatus(Status.NORMAL);
    } catch (NumberFormatException | TemperatureOutofBoundsException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      c.setStatus(Status.ERROR);
    }

  }

  public void toggleStart(Thermostat c) {
    if (c.getStatus().equals(Status.NORMAL)) {
      c.setStatus(Status.OFF);
    } else {
      c.setStatus(Status.NORMAL);
    }
  }

  public Status getStatus(Thermostat c) {
    return c.getStatus();
  }

  public String getTemperature(Thermostat c) {
    return c.getTemp();
  }



}
