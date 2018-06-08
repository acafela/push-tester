package push.simple.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.json.JSONException;

import com.google.android.gcm.server.Result;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushedNotifications;

public class Main {
	
	public static void main(String[] args) {
		printHelpMessage();
        System.out.print("Enter : ");
        
        String cmd = "";
        Scanner scanner = new Scanner(System.in);
        try {
        	cmd = scanner.nextLine();			
		} finally {
			scanner.close();
		}
        
        String[] cmds = cmd.split(" ");
		
		if(cmds.length < 1) {
			printIllegalArgumentMessage();
			return;
		}
		
		String pushType = cmds[0].toLowerCase();
		if(pushType.equals("apns")) {
			if(validateApnsArgs(cmds)) {
				String pushKey = cmds[1];
				String message = cmds[2];
				String keystorePath = cmds[3];
				String keystorePassword = cmds[4];
				sendApns(pushKey, message, keystorePath, keystorePassword);
			}
		} else if (pushType.equals("gcm")) {
			if(validateGcmArgs(cmds)) {
				String pushKey = cmds[1];
				String message = cmds[2];
				String apiKey = cmds[3];
				sendGcm(pushKey, message, apiKey);
			}
		} else {
			System.out.println("Only APNs, GCM are supported.");
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
			printIllegalArgumentMessage();
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
			printIllegalArgumentMessage();
			return false;
		}
		if(!Files.exists(Paths.get(args[3]))) {
			System.out.println("Keystore file does not exists!");
			return false;
		}
		return true;
	}

	private static void printHelpMessage() {
		System.out.println("Enter as follow..\n"
				+ "APNs - [apns {pushkey} {message} {keystore path} {keystore password}]\n"
				+ "GCM - [gcm {pushkey} {message}] {API KEY}\n"
				+ "\t* Pushkey means registration id issued by APNs, GCM");
	}
	
	private static void printIllegalArgumentMessage() {
		System.out.println("Invaild Values..!");
	}
	
	
}
