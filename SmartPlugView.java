package ca.uvic.seng330.assn3;

import java.util.LinkedList;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.Device;
import ca.uvic.seng330.assn3.devices.SmartPlug;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class SmartPlugView {
  private GridPane view;
  private Label numField;
  private WebClientView mainview;
  private Stage stage;

  private SmartPlugController ccontroller;
  private WebClientController controller;

  public SmartPlugView(SmartPlugController ccontroller, WebClientController wcontroller,
      WebClientView webClientView, Stage stage) {
    this.controller = wcontroller;
    this.ccontroller = ccontroller;
    mainview = webClientView;
    this.stage = stage;

    createAndConfigurePane();
    createAndLayoutControls();
    updateControllerFromListeners();
    observeModelAndUpdateControls();
  }

  public Parent asParent() {
    return view;
  }

  private void observeModelAndUpdateControls() {

  }

  private void updateControllerFromListeners() {
    numField.textProperty().addListener((obs, oldText, newText) -> controller.numOfDevices());
  }

  private void createAndLayoutControls() {

    numField = new Label();
    numField.setMaxWidth(100);
    numField.setMaxWidth(40);
    numField.setText(Integer.toString(controller.numOfDevices()));

    view.addRow(0, new Label("Number of Devices:" + controller.getNumber("Camera")));

    int row = 1;

    @SuppressWarnings("unchecked")
    LinkedList<Device> devices = (LinkedList<Device>) controller.GetDeviceInfo("SmartPlug");
    for (int num = 0; num < devices.size(); num++) {
      Device c = devices.get(num);
      Label l = new Label("Plug " + (num + 1) + ": ");
      Button StartButton = new Button("Switch");
      StartButton.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
      Label id = new Label("KEY ID: " + c.getIdentifier().toString());
      id.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
      Label currentStatus = new Label("Status: " + c.getStatus().toString());
      id.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");

      StartButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          ccontroller.toggleStart((SmartPlug) c);
          currentStatus.setText("Status: " + c.getStatus().toString());
        }
      });


      view.addRow(row, l, StartButton);
      view.addRow(row + 1, id, currentStatus);
      row = row + 3;
    }


    Label settingsDivider = new Label();
    settingsDivider.setText("Current User: " + controller.getUser());

    // what I did above is called event handling. we can use it to do a lot of stuff
    // also need to link the hub to this to display the user information.

    view.addRow(row, settingsDivider);

    Button BackButton = new Button("Back");
    BackButton.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");

    view.addRow(row + 3, BackButton);

    BackButton.setOnAction(actionEvent -> {
      Scene scene3 = new Scene(mainview.asParent(), 400, 400);
      stage.setScene(scene3);
      stage.show();
    });

  }

  private void createDialog(String info) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Information Dialog");
    alert.setHeaderText(null);
    alert.setContentText(info);

    alert.showAndWait();
  }

  private void createAndConfigurePane() {
    view = new GridPane();

    ColumnConstraints leftCol = new ColumnConstraints();
    leftCol.setHalignment(HPos.RIGHT);
    leftCol.setHgrow(Priority.NEVER);

    ColumnConstraints rightCol = new ColumnConstraints();
    rightCol.setHgrow(Priority.SOMETIMES);

    view.getColumnConstraints().addAll(leftCol, rightCol);

    view.setAlignment(Pos.CENTER);
    view.setHgap(5);
    view.setVgap(10);
  }


}
