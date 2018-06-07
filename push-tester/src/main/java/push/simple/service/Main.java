package push.simple.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;

import com.google.android.gcm.server.Result;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotifications;

public class Main {
	public static void main(String[] args) {
		//apns pushkey message keystore_path keystore_password
		//gcm pushkey message API_KEY
		if(args.length < 1) {
			printHelpMessage();
			return;
		}
		
		String pushType = args[0].toLowerCase();
		if(pushType.equals("apns")) {
			if(validateApnsArgs(args)) {
				String pushKey = args[1];
				String message = args[2];
				String keystorePath = args[3];
				String keystorePassword = args[4];
				sendApns(pushKey, message, keystorePath, keystorePassword);
			}
		} else if (pushType.equals("gcm")) {
			if(validateGcmArgs(args)) {
				String pushKey = args[1];
				String message = args[2];
				String apiKey = args[3];
				sendGcm(pushKey, message, apiKey);
			}
		} else {
			printHelpMessage();
			return;
		}
		
		
	}

	private static void sendGcm(String pushKey, String message, String apiKey) {
		List<String> targetIds = Arrays.asList(pushKey);
		GCMConfig config = new GCMConfig(apiKey);
		GCMConnector gcmConnector = new GCMConnector(config);
		try {
			List<Result> results = gcmConnector.send(message, targetIds);
			System.out.println("Send result : " + results.get(0));
		} catch (IOException e) {
			System.out.println("Push send fail!");
			e.printStackTrace();
		}
	}

	private static boolean validateGcmArgs(String[] args) {
		if(args.length < 4) {
			return false;
		}
		return true;
	}

	private static void sendApns(String pushKey, String message, String keystorePath, String keystorePassword) {
		List<String> sendPushkeys = Arrays.asList(pushKey);
		APNsConnector connector = new APNsConnector(keystorePath, keystorePassword);
		try {
			PushedNotifications sendResult = connector.send(message, sendPushkeys);
			System.out.println("Send result : " + sendResult);
		} catch (CommunicationException e) {
			System.out.println("Error occurs while trying to communicate with Apple servers!");
		} catch (KeystoreException e) {
			System.out.printf("Error occurs when loading the keystore - keystore : [%s]\n", keystorePath);
		} catch (JSONException e) {
			System.out.println("Send message add fail! message : " + message);
		}
	}

	private static boolean validateApnsArgs(String[] args) {
		if(args.length < 5) {
			return false;
		}
		if(Files.exists(Paths.get(args[3]))) {
			System.out.println("Keystore file does not exists!");
			return false;
		}
		return true;
	}

	private static void printHelpMessage() {
		System.out.println("APNs - [apns {message} {keystore path} {keystore password}]");
	}
	
	
}
