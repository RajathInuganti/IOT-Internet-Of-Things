package ca.uvic.seng330.assn3;

import java.net.URL;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.CameraFullException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class CameraController {
    public Media loadMedia() {
      //URL mediaUrl = getClass().getResource("http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv");
      String mediaStringUrl = "http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv";
      // Create a Media
      Media media = new Media(mediaStringUrl);
      return media;
      // Create a Media Player
    }

	public void toggleRecord(Camera c) {
		try {
			c.record();
		} catch (CameraFullException e) {
			// TODO Auto-generated catch block
			c.setStatus(Status.ERROR);
		}
	}
	
	public void toggleStart(Camera c) {
	    if(c.getStatus().equals(Status.NORMAL)) {
	      c.setStatus(Status.OFF);
	    }else {
	      c.setStatus(Status.NORMAL);
	    }
	}
	
	public Status getStatus(Camera c) {
		return c.getStatus();
	}
	
	

}
