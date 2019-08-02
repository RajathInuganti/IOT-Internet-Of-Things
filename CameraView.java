package ca.uvic.seng330.assn3;

import java.util.LinkedList;
import java.util.Optional;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.Device;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Pair;

public class CameraView {
  private GridPane view;
  private Label numField;
  private WebClientView mainview;
  private Stage stage;

  private CameraController ccontroller;
  private WebClientController controller;
  
  String cameraNum;
  Device c;
  Label id;
  Label currentStatus;
  

  public CameraView(CameraController ccontroller, WebClientController wcontroller, WebClientView webClientView, Stage stage) {
    this.controller = wcontroller;
    this.ccontroller = ccontroller;
    this.mainview = webClientView;
    this.stage = stage;

    createAndConfigurePane();
    createAndLayoutControls();
    updateControllerFromListeners();
    observeModelAndUpdateControls();
  }

  public Parent asParent() {
    return view;
  }

  private void observeModelAndUpdateControls() {}

  private void updateControllerFromListeners() {
  }

  private void createAndLayoutControls() {
    LinkedList<Device> devices = (LinkedList<Device>) controller.GetDeviceInfo("Camera");
    ChoiceBox cb = new ChoiceBox();
    for (int i=0; i<devices.size(); i++) {  
      cb.getItems().add("Camera "+ Integer.toString(i));
    }
    cb.getSelectionModel().select(0);
    Button RecordButton = new Button("Record");
    Button StartButton = new Button("Start");
    Button BackButton = new Button("Back");
    
    cameraNum = (cb.getValue().toString());
    cameraNum = cameraNum.substring(cameraNum.length()-1);
    c = devices.get(Integer.parseInt(cameraNum));
    
    //Player player = new Player("file:///file-examples.com/wp-content/uploads/2017/04/file_example_MP4_480_1_5MG.mp4");
    id = new Label("KEY ID: " + c.getIdentifier().toString());
    currentStatus = new Label("Status: " + c.getStatus().toString());
    //Player player = new Player("file:///C://srk.mp4");
    Player player = new Player("http://file-examples.com/wp-content/uploads/2017/04/file_example_MP4_480_1_5MG.mp4");
    player.setVisible(false);
    cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
      public void changed(ObservableValue ov, Number value, Number new_value) 
      { 
        cameraNum = (cb.getValue().toString());
        cameraNum = cameraNum.substring(cameraNum.length()-1);
        c = devices.get(Integer.parseInt(cameraNum));
        currentStatus.setText("Status: " + c.getStatus().toString());
        id.setText("KEY ID: " + c.getIdentifier().toString());
        if (c.getStatus().equals(Status.NORMAL)) {
          player.setVisible(true);
        }else {
          player.setVisible(false);
        }
      }
    });
    RecordButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if(c.getStatus().equals(Status.NORMAL)) {
          ccontroller.toggleRecord((Camera) c);
        if(RecordButton.getText().equals("Record")) {
          RecordButton.setText("Recording");
          player.player.play();
        }else {
          RecordButton.setText("Record");
          player.player.pause();
        }
      }
      }
    });
    StartButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if(StartButton.getText().equals("OFF")) {
          RecordButton.setText("Record");
          StartButton.setText("Start");
          player.setVisible(false);
          ccontroller.toggleStart((Camera) c);
          currentStatus.setText("Status: " + c.getStatus().toString());
        }else {
          ccontroller.toggleStart((Camera) c);
          createDialog("ON");
          player.setVisible(true);
          currentStatus.setText("Status: " + c.getStatus().toString());
          StartButton.setText("OFF");
        }
      }
    });
    BackButton.setOnAction(actionEvent -> {
      Scene scene3 = new Scene(mainview.asParent(), 400, 400);
      stage.setScene(scene3);
      stage.show();
    });
    
    view.add(cb, 2,0);
    view.add(BackButton, 0, 2);
    view.add(player,  0, 1);
    view.addRow(3, RecordButton, StartButton);
    view.addRow(4, id, currentStatus);
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
