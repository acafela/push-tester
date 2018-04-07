package push.simple.service.test;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotifications;
import push.simple.service.APNsConnector;

public class APNsTest {
	
	String _KEYSTORE_PATH;
	String _KEYSTOREPASSWORD;
	
	@Before
	public void setKeystore(){
		_KEYSTORE_PATH = "C:/ys/test/APNs/apns-test.p12";
		_KEYSTOREPASSWORD = "adt1234";
	}

	@Test
	public void test() throws JSONException {
		PushNotificationPayload payload = new PushNotificationPayload();
		payload.addAlert("Today is sunday~");
		payload.addBadge(1);
		payload.addSound("default");
		
		List<String> sendPushkeys = Arrays.asList("67df4ce50b922ce79cb71e4e7d6d3ead93cd2b184e44fe7cb13ede53b327a983");
		
		APNsConnector connector = new APNsConnector(_KEYSTORE_PATH, _KEYSTOREPASSWORD);
		try {
			PushedNotifications sendResult = connector.send(payload, sendPushkeys);
			System.out.println("Send result : " + sendResult);
		} catch (CommunicationException e) {
			System.out.println("Error occurs while trying to communicate with Apple servers!");
			e.printStackTrace();
		} catch (KeystoreException e) {
			System.out.printf("Error occurs when loading the keystore - keystore : [%s]\n", _KEYSTORE_PATH);
			e.printStackTrace();
		}
	}

}
