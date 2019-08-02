package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.SmartPlug;
import ca.uvic.seng330.assn3.devices.Thermostat;
import ca.uvic.seng330.assn3.devices.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * Code sample from
 * https://stackoverflow.com/questions/36868391/using-javafx-controller-without-fxml/36873768
 */
public class MVCExample extends Application {

  @Override
  public void start(Stage primaryStage) {
    Hub h = new Hub();
    
    WebClient webclientmodel = new WebClient(h);
    WebClientController controller = new WebClientController(webclientmodel);
    WebClientView view = new WebClientView(controller, webclientmodel, primaryStage);
    /*
     * WebClient model = new WebClient(h); WebClientController controller = new
     * WebClientController(model); WebClientView view = new WebClientView(controller, model);
     */
    Scene scene = new Scene(view.asParent(), 900, 900);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
