package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.HubRegistrationException;
import ca.uvic.seng330.assn3.Mediator;

import org.json.JSONException;
import org.json.JSONObject;

public class AndroidClient extends Client {

  private final Mediator aMed;
  private JSONObject aJsonObj;

  public AndroidClient(Mediator pMed) {
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
      System.out.println("AndroidClient is displaying alert from : " + aJsonObj.getString("node_id"));
  }

  @Override
  public String toString() {
    return "AndroidClient: " + getIdentifier().toString();
  }
}
