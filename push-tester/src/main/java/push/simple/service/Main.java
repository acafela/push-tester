package push.simple.service;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.google.android.gcm.server.Result;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushedNotifications;

public class Main {
	
	static final Logger logger = Logger.getLogger(Main.class);
	final static String _PROPERTIES_FILE_NAME = "push.properties";
	
	public static void main(String[] args) {
        
		if(args.length < 3) {
			printHelpMessage();
			return;
		}
		
		PushProperties pushProp;
		try {
			pushProp = new PushProperties(_PROPERTIES_FILE_NAME);
		} catch (IOException e) {
			logger.error("push.properties was not founded!");
			return;
		}
		
		String pushType = args[0].toLowerCase();
		String pushKey = args[1];
		String message = args[2];
		String keystorePath = pushProp.getProperty("apns.keystore.path");
		String keystorePassword = pushProp.getProperty("apns.keystore.password");
		String gcmApiKey = pushProp.getProperty("gcm.api.key");
		
		if(pushType.equals("apns")) {
			sendApns(pushKey, message, keystorePath, keystorePassword);
		} else if (pushType.equals("gcm")) {
			sendGcm(pushKey, message, gcmApiKey);
		} else {
			logger.error("Only APNs, GCM are supported.");
			return;
		}	
	}

	private static void sendGcm(String pushKey, String message, String apiKey) {
		List<String> targetIds = Arrays.asList(pushKey);
		GCMConfig config = new GCMConfig(apiKey);
		GCMConnector gcmConnector = new GCMConnector(config);
		try {
			List<Result> results = gcmConnector.send(message, targetIds);
			logger.info("Send result : " + results.get(0));
		} catch (IOException e) {
			logger.error("Push send fail!", e);
		}
	}

	private static void sendApns(String pushKey, String message, String keystorePath, String keystorePassword) {
		List<String> sendPushkeys = Arrays.asList(pushKey);
		APNsConnector connector = new APNsConnector(keystorePath, keystorePassword);
		try {
			PushedNotifications sendResult = connector.send(message, sendPushkeys);
			logger.info("Send result : " + sendResult);
		} catch (CommunicationException e) {
			logger.error("Error occurs while trying to communicate with Apple servers!");
		} catch (KeystoreException e) {
			logger.error("Error occurs when loading the keystore - keystore : [" + keystorePath + "]");
		} catch (JSONException e) {
			logger.error("Send message add fail! message : " + message);
		}
	}

	private static void printHelpMessage() {
		System.out.println("Enter as follow..\n"
				+ "Template - [{apns|gcm} {pushkey} {message}]\n"
				+ "Ex - [apns 67df4ce50b922ce79cb71e4e7d6d3ead93cd2b184e44fe7cb13ede53b327a983 aloha!]\n"
				+ "\t* Pushkey means registration id issued by APNs, GCM");
	}
	
}
