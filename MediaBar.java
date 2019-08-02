package ca.uvic.seng330.assn3;

import javafx.application.Platform; 
import javafx.beans.InvalidationListener; 
import javafx.beans.Observable; 
import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 
import javafx.geometry.Insets; 
import javafx.geometry.Pos; 
import javafx.scene.control.Button; 
import javafx.scene.control.Label; 
import javafx.scene.control.Slider; 
import javafx.scene.layout.HBox; 
import javafx.scene.layout.Priority; 
import javafx.scene.media.MediaPlayer; 
import javafx.scene.media.MediaPlayer.Status; 

public class MediaBar extends HBox { // MediaBar extends Horizontal Box 

    // introducing Sliders 
    Button PlayButton = new Button("||"); // For pausing the player
    MediaPlayer player; 

    public MediaBar(MediaPlayer play) 
    { // Default constructor taking 
        // the MediaPlayer object 
        player = play; 

        setAlignment(Pos.CENTER); // setting the HBox to center 
        setPadding(new Insets(5, 10, 5, 10)); 
        // Settih the preference for volume bar 
         
        PlayButton.setPrefWidth(30); 

        // Adding the components to the bottom 

        getChildren().add(PlayButton); // Playbutton 
                // Adding Functionality 
        // to play the media player 
        PlayButton.setOnAction(new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
                Status status = player.getStatus(); // To get the status of Player 
                if (status == status.PLAYING) { 

                    // If the status is Video playing 
                    if (player.getCurrentTime().greaterThanOrEqualTo(player.getTotalDuration())) { 

                        // If the player is at the end of video 
                        player.seek(player.getStartTime()); // Restart the video 
                        player.play(); 
                    } 
                    else { 
                        // Pausing the player 
                        player.pause(); 

                        PlayButton.setText(">"); 
                    } 
                } // If the video is stopped, halted or paused 
                if (status == Status.HALTED || status == Status.STOPPED || status == Status.PAUSED) { 
                    player.play(); // Start the video 
                    PlayButton.setText("||"); 
                } 
            } 
        }); 

        // Providing functionality to time slider 
        player.currentTimeProperty().addListener(new InvalidationListener() { 
            public void invalidated(Observable ov) 
            { 
                updatesValues(); 
            } 
        }); 

         } 

    // Outside the constructor 
    protected void updatesValues() 
    { 
        Platform.runLater(new Runnable() { 
            public void run() 
            { 
            } 
        }); 
    } 
} 