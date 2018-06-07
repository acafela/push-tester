package push.simple.service;

import java.util.List;

import org.json.JSONException;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotifications;

public class APNsConnector {
	
	private String keystorePath;
	private String keystorePassword;
	
	public APNsConnector(String keystorePath, String keystorePassword) {
		this.keystorePath = keystorePath;
		this.keystorePassword = keystorePassword;
	}
	
	public PushedNotifications send(String message, List<String> pushKeys) throws CommunicationException, KeystoreException, JSONException{
		PushNotificationPayload payload = buildDefaultPayload(message);
		return send(payload, pushKeys, true);
	}
	
	public PushedNotifications send(PushNotificationPayload payload, List<String> pushKeys) throws CommunicationException, KeystoreException{
		return send(payload, pushKeys, true);
	}
	
	public PushedNotifications send(PushNotificationPayload payload, List<String> pushKeys, boolean productionServer) throws CommunicationException, KeystoreException{
		return Push.payload(payload, keystorePath, keystorePassword, productionServer, pushKeys);
	}
	
	private PushNotificationPayload buildDefaultPayload(String message) throws JSONException {
		PushNotificationPayload payload = new PushNotificationPayload();
		payload.addAlert(message);
		payload.addBadge(1);
		payload.addSound("default");
		return payload;
	}

}
