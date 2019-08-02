package ca.uvic.seng330.assn3;

import java.util.LinkedList;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.Device;
import ca.uvic.seng330.assn3.devices.SmartPlug;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SmartPlugController {

  public void toggleStart(SmartPlug c) {
    if (c.getStatus().equals(Status.NORMAL)) {
      c.setStatus(Status.OFF);
    } else {
      c.setStatus(Status.NORMAL);
    }
  }

  public Status getStatus(SmartPlug c) {
    return c.getStatus();
  }
}


