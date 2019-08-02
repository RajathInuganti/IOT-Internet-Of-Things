
package ca.uvic.seng330.assn3;

import java.util.LinkedList;
import java.util.Optional;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.Device;
import ca.uvic.seng330.assn3.devices.Temperature;
import ca.uvic.seng330.assn3.devices.Thermostat;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Pair;

public class ThermostatView {
    private GridPane view;
    private Label numField;
    private WebClientView mainview;
    private Stage stage;

    private ThermostatController ccontroller;
    private WebClientController controller;

    public ThermostatView(ThermostatController ccontroller, WebClientController wcontroller,
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

      view.addRow(0, new Label("Number of Devices:" + controller.getNumber("Thermostat")));

      int row = 1;

      @SuppressWarnings("unchecked")
      LinkedList<Device> devices = (LinkedList<Device>) controller.GetDeviceInfo("Thermostat");
      for (int num = 0; num < devices.size(); num++) {
        Device c = devices.get(num);
        Label l = new Label("Thermostat " + (num + 1) + ": ");
        TextField recorder = new TextField();
        recorder.setText("Temperature");
        
        ChoiceBox cb = new ChoiceBox();
        cb.getItems().add("CELSIUS");
        cb.getItems().add("FAHRENHEIT");
        Button SetButton = new Button("Set");
        SetButton.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        Button StartButton = new Button("Switch");
        StartButton.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        Label id = new Label("KEY ID: " + c.getIdentifier().toString());
        Label currentTemp = new Label("Current Temp: " + ccontroller.getTemperature((Thermostat)c));
        id.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
        currentTemp.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
        Label currentStatus = new Label("Status: " + c.getStatus().toString());
        id.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");

        SetButton.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            if (c.getStatus().equals(Status.NORMAL)) {
              ccontroller.setTemp((Thermostat) c, recorder.getText(), cb.getValue().toString());
              currentStatus.setText("Status: " + c.getStatus().toString());
              currentTemp.setText("Current Temp: " + ccontroller.getTemperature((Thermostat)c));
            }
          }
        });
        StartButton.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            ccontroller.toggleStart((Thermostat) c);
            createDialog("ON");
            currentStatus.setText("Status: " + c.getStatus().toString());
          }
        });


        view.addRow(row, l, recorder,cb, SetButton,StartButton);
        view.addRow(row + 1, id, currentStatus, currentTemp);
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
