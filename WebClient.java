package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.HubRegistrationException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONException;
import org.json.JSONObject;

public class WebClient extends Client {

  private final Hub aMed;
  private JSONObject aJsonObj;
  //private final WebClientModel model;

  public WebClient(Hub pMed) {
    aMed = pMed;
    try {
      aMed.register(this);
    } catch (HubRegistrationException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void notify(JSONObject json) {
    super.notify(json);
    this.aJsonObj = json;
    try {
		display();
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  private void display() throws JSONException {
    System.out.println("WebClient is displaying content from : " + aJsonObj.getString("node_id"));
    //TODO  should be on web page
  }
  
  public Hub getHubInstance() {
	  return aMed;
  }

  @Override
  public String toString() {
    return "WebClient: " + getIdentifier().toString();
  }

  
}
