package push.simple.service.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;

import push.simple.service.GCMConfig;
import push.simple.service.GCMConnector;

public class GCMConnectorTest {
	
	public static final String KEY_MESSAGE_ID	= "messageId";	
	public static final String KEY_HAS_BIN		= "hasBin";	
	public static final String KEY_MESSAGE		= "message";	
	public static final String KEY_TITLE		= "title";	
	public static final String KEY_BODY			= "body";	
	public static final String KEY_BADGE		= "badge";
	public static final String API_KEY			= "AIzaSyD_UZT9PhaQbxpPyliZx1XkXPpwANXg-ec";
	Message.Builder builder						= new Message.Builder();

	@Before
	public void setUp() throws Exception {
		builder.timeToLive( 1800 );
		builder.addData( KEY_BADGE, "1" );
		builder.addData( KEY_MESSAGE_ID, "MESSAGEID7897" );
		builder.addData( KEY_HAS_BIN, "0" );
		builder.addData( KEY_TITLE, "WELCOME!");
		builder.addData( KEY_MESSAGE, "Notification Service");
	}

	@Test
	public void test() {
		List<String> targetIds = Arrays.asList("APA91bFZwxsnz5FjHKH7aHjGg8u9H63KsCIgOPIvg88HPMPiuT2I64RzAYP-A4AFIkiop9h9NZT829mzma_2Zh5CYyTUwuKjYI2PJFzWNzI9WVKoz1fvpxfk3ecnMveFPu1JhReJR5g4");
		GCMConfig config = new GCMConfig(API_KEY);
		GCMConnector gcmConnector = new GCMConnector(config);
		try {
			List<Result> results = gcmConnector.send(builder.build(), targetIds);
			System.out.println("Send result : " + results);
		} catch (IOException e) {
			System.out.println("Push send fail!");
			e.printStackTrace();
		}
	}

}
