package ca.uvic.seng330.assn3;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import ca.uvic.seng330.assn3.devices.Device;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class WebClientView {
  private GridPane view ;
  private Label numField;

  private WebClientController controller ;
  private WebClient model ;
  private Stage stage;

  public WebClientView(WebClientController controller2, WebClient model2, Stage primaryStage) {

    this.controller = controller2 ;
    this.model = model2 ;
    stage = primaryStage;

    createAndConfigurePane();
    createAndLayoutControls();
    updateControllerFromListeners();
    observeModelAndUpdateControls();
  }

  public Parent asParent() {
    return view ;
  }

  private void observeModelAndUpdateControls() {
    
  }

  private void updateIfNeeded(Number value, TextField field) {
    String s = value.toString() ;
    if (! field.getText().equals(s)) {
      field.setText(s);
    }
  }

  private void updateControllerFromListeners() {
    numField.textProperty().addListener((obs, oldText, newText) -> controller.numOfDevices());
  }

  private void createAndLayoutControls() {
    if(controller.UserLogin == false) {
      Dialog<List> dialog = new Dialog<>();
    
    dialog = loginBox(dialog,"login");
    Optional<List> result = dialog.showAndWait();

    List petName = null;
    UserStatus loginUser;
    if(result.isPresent()){
        petName = result.get();
        loginUser = model.getHubInstance().login(petName.get(0).toString(),petName.get(1).toString());
        controller.setUser(petName.get(0).toString());
        controller.setUserStatus(loginUser);
        controller.UserLogin = true;
    }else {
      controller.setUserStatus(UserStatus.NA);
    }
    }
  

    //NB: this code has VERY limited UI design. You should do more.
    numField = new Label();
    numField.setMaxWidth(100);
    numField.setMaxWidth(40);
    numField.setText(Integer.toString(controller.numOfDevices()));
    //Setting "ID" allows us to lookup the control by ID in DesktopPaneTest
    
    TextArea logArea = new TextArea();
    logArea.setPrefHeight(50);  //sets height of the TextArea to 400 pixels 
    logArea.setPrefWidth(300);
    logArea.setText(controller.getLog());
    
    Button cameraButton = new Button("Cameras: "+ controller.getNumber("Camera"));
    cameraButton.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
    
    Button bulbButton = new Button("SmartBulbs: "+ controller.getNumber("SmartBulb"));
    bulbButton.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
    
    Button thermButton = new Button("Thermostats: "+ controller.getNumber("Thermostat"));
    thermButton.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
    
    Button plugButton = new Button("SmartPlugs: "+ controller.getNumber("SmartPlug"));
    plugButton.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
    
    Button shutdownButton = new Button("Shutdown");
    Button addUser = new Button("addUser");
    Button removeUser = new Button("removeUser");
    
    
    Button addDevice = new Button("addDevice: ");
    Button removeDevice = new Button("removeDevice:");
    
    Label l = new Label();
    
    cameraButton.setOnAction(actionEvent -> {
    	CameraController ccontroller = new CameraController();
        CameraView cview = new CameraView(ccontroller, controller,new WebClientView(controller,model,stage),stage);
    	Scene scene2 = new Scene(cview.asParent(), 900, 900);
    	stage.setScene(scene2);
        stage.show();
    });
    bulbButton.setOnAction(actionEvent -> {
      LightbulbController ccontroller = new LightbulbController();
      LightbulbView cview = new LightbulbView(ccontroller, controller,new WebClientView(controller,model,stage),stage);
      Scene scene2 = new Scene(cview.asParent(), 900, 900);
      stage.setScene(scene2);
      stage.show();
  });
    plugButton.setOnAction(actionEvent -> {
      SmartPlugController ccontroller = new SmartPlugController();
      SmartPlugView cview = new SmartPlugView(ccontroller, controller,new WebClientView(controller,model,stage),stage);
      Scene scene2 = new Scene(cview.asParent(), 900, 900);
      stage.setScene(scene2);
      stage.show();
  });
    thermButton.setOnAction(actionEvent -> {
      ThermostatController ccontroller = new ThermostatController();
      ThermostatView cview = new ThermostatView(ccontroller, controller,new WebClientView(controller,model,stage),stage);
      Scene scene2 = new Scene(cview.asParent(), 900, 900);
      stage.setScene(scene2);
      stage.show();
  });
    shutdownButton.setOnAction(actionEvent -> {
      model.getHubInstance().shutdown();
  });
    l.setTextFill(Color.web("#0076a3"));
    l.setFont(Font.font("Cambria", 12));
    
    Label settingsDivider = new Label();
    settingsDivider.setText("Current User: " + controller.getUser());
    
    // what I did above is called event handling. we can use it to do a lot of stuff 
    //also need to link the hub to this to display the user information.
    if(!(controller.getUserStatus().equals(UserStatus.NA))) {
    view.addRow(0, new Label("Number of Devices:"), numField, logArea);
    view.addRow(3, cameraButton, bulbButton, thermButton, plugButton);
    view.addRow(4,l);
    view.addRow(6,settingsDivider,shutdownButton);
    
    if(controller.getUserStatus().equals(UserStatus.ADMIN)) {
      view.addRow(7, addDevice, removeDevice, addUser, removeUser);
    }
    }
    //addDevice.setOnAction(value);
    
    addUser.setOnAction(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent t){
      Dialog<List> dialog = new Dialog<>();
      dialog = loginBox(dialog, "add");
      Optional<List> result = dialog.showAndWait();
      if (result.isPresent()){
        model.getHubInstance().addUser(result.get());
        System.out.println("Your choice: " + result.get());
    }
    }
    });
    removeUser.setOnAction(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent t){
      ChoiceDialog<String> dialog = new ChoiceDialog<>("Select One", controller.getUserList());
      dialog.setTitle("Choice Dialog");
      dialog.setHeaderText("Look, a Choice Dialog");
      dialog.setContentText("Choose your letter:");
      Optional<String> result=dialog.showAndWait();
    
    result.ifPresent(letter -> controller.removeUser(letter));
    }
    });
    addDevice.setOnAction(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent t){
      ChoiceDialog<String> dialog = new ChoiceDialog<>("Select One", controller.choices);
      dialog.setTitle("Choice Dialog");
      dialog.setHeaderText("Look, a Choice Dialog");
    dialog.setContentText("Choose your letter:");
    Optional<String> result=dialog.showAndWait();
    
    result.ifPresent(letter -> controller.addDevice(letter));
    }});
    removeDevice.setOnAction(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent t){
      ChoiceDialog<Device> dialog = new ChoiceDialog<>("Select One", controller.deviceList());
      dialog.setTitle("Choice Dialog");
      dialog.setHeaderText("Look, a Choice Dialog");
    dialog.setContentText("Choose your letter:");
    Optional<Device> result=dialog.showAndWait();
    
    result.ifPresent(d -> controller.removeDevice(d));
    }});
    // The Java 8 way to get the response value (with lambda expression).
    class statuses{
    	
    	WebClientController controller;
    	GridPane view;
    	
    	statuses(WebClientController controller2, GridPane view2){
    		controller = controller2;
    		view = view2;
    	}
    	
    	public void setupstatuses(){
    		LinkedList<Device> devices = (LinkedList<Device>) controller.GetDeviceInfo("Camera");
    		for(int i = 0; i < devices.size(); i++) {
    			Device c = devices.get(i);
    			Label l = new Label("Camera " + (i + 1) + ": "+c.getStatus().toString());
    			l.setTextFill(Color.web("#0076a3"));
    		    l.setFont(Font.font("Cambria", 12));
    		    view.addRow(8, l);
    		}
    		LinkedList<Device> devices2 = (LinkedList<Device>) controller.GetDeviceInfo("Lightbulb");
    		for(int i = 0; i < devices2.size(); i++) {
    			Device c = devices2.get(i);
    			Label l = new Label("SmartBulb " + (i + 1) + ": "+c.getStatus().toString());
    			l.setTextFill(Color.web("#0076a3"));
    		    l.setFont(Font.font("Cambria", 12));
    		    view.addRow(9, l);
    		}
    		LinkedList<Device> devices3 = (LinkedList<Device>) controller.GetDeviceInfo("Thermostat");
    		for(int i = 0; i < devices3.size(); i++) {
    			Device c = devices3.get(i);
    			Label l = new Label("Thermostat " + (i + 1) + ": "+c.getStatus().toString());
    			l.setTextFill(Color.web("#0076a3"));
    		    l.setFont(Font.font("Cambria", 12));
    		    view.addRow(10, l);
    		}
    		LinkedList<Device> devices4 = (LinkedList<Device>) controller.GetDeviceInfo("SmartPlug");
    		for(int i = 0; i < devices4.size(); i++) {
    			Device c = devices4.get(i);
    			Label l = new Label("SmartPlug " + (i + 1) + ": "+c.getStatus().toString());
    			l.setTextFill(Color.web("#0076a3"));
    		    l.setFont(Font.font("Cambria", 12));
    		    view.addRow(11, l);
    		}
    		//add list of devices into linked list and the label instances too to update them constantly until 
    		//interrupted by no change routine.
    	}
    	
    	public void const_update() {
    		
    	}
    	
    }
    
	statuses stat = new statuses(controller,view);
	stat.setupstatuses();
	//new Thread(new Runnable() 
	//{
		//public void run()
		//{
			//while(true)
			//{
			//}
		//}
		//}).start();
	//}
        
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

  private void configTextFieldForInts(TextField field) {
    field.setTextFormatter(new TextFormatter<Integer>((Change c) -> {
      if (c.getControlNewText().matches("-?\\d*")) {
        return c ;
      }
      return null ;
    }));
  }
  private Dialog<List> loginBox(Dialog<List> dialog, String t) {
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField username = new TextField();
    username.setPromptText("Username");
    PasswordField password = new PasswordField();
    password.setPromptText("Password");
    ComboBox userTypeBox = new ComboBox();
    
    ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
    ButtonType signUpButtonType = new ButtonType("Sign Up",ButtonData.OK_DONE);
   
    dialog.setTitle("hi");
    dialog.setHeaderText("This is a custom dialog. Enter info and \n" +
        "press Okay (or click title bar 'X' for cancel).");
    
    if(t.equals("login")) {
    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
    Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
    loginButton.setDisable(true);
    username.textProperty().addListener((observable, oldValue, newValue) -> {
     loginButton.setDisable(newValue.trim().isEmpty());
    });
    }else {
    dialog.getDialogPane().getButtonTypes().addAll(signUpButtonType, ButtonType.CANCEL);
    
    Node signUpButton = dialog.getDialogPane().lookupButton(signUpButtonType);
    signUpButton.setDisable(true);
    username.textProperty().addListener((observable, oldValue, newValue) -> {
      signUpButton.setDisable(newValue.trim().isEmpty());
    });
    }
    if (t.equals("add")) {
      Label userType = new Label("Type of User: ");
      
      userTypeBox.getItems().addAll("ADMIN", "STANDARD");
      userTypeBox.getSelectionModel().select(1);
      grid.add(new Label("Type of User: "), 0, 2);
      grid.add(userTypeBox, 1, 2);
    }
    
    grid.add(new Label("Username:"), 0, 0);
    grid.add(username, 1, 0);
    grid.add(new Label("Password:"), 0, 1);
    grid.add(password, 1, 1);

    // Do some validation (using the Java 8 lambda syntax).
    

    dialog.getDialogPane().setContent(grid);

    // Request focus on the username field by default.
    Platform.runLater(() -> username.requestFocus());

    // Convert the result to a username-password-pair when the login button is clicked.
    dialog.setResultConverter(dialogButton -> {
        List l = new LinkedList();
        l.add(username.getText());
        l.add(password.getText());
        if (dialogButton.equals(loginButtonType)) {
          return l;
        }
        if (dialogButton.equals(signUpButtonType)) {
            l.add(userTypeBox.getValue());
            return l;
        }
        return null;
    });
    return dialog;
  }
}

