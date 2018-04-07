package push.simple.service;

import java.util.List;

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
	
	public PushedNotifications send(PushNotificationPayload payload, List<String> pushKeys) throws CommunicationException, KeystoreException{
		return send(payload, pushKeys, true);
	}
	
	public PushedNotifications send(PushNotificationPayload payload, List<String> pushKeys, boolean productionServer) throws CommunicationException, KeystoreException{
		return Push.payload(payload, keystorePath, keystorePassword, productionServer, pushKeys);
	}
}
