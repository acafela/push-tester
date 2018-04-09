package push.simple.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

/**
 * GCM Connector for Android push service
 * 
 * @author yshwang
 */
public class GCMConnector {
	
	private Sender sender;
	private GCMConfig config;
	
	/**
	 * 생성자
	 */
	public GCMConnector(GCMConfig gcmConfig){
		this.sender = new Sender(gcmConfig.getApikey());
		this.config = gcmConfig;
	}
	
	public List<Result> send(Message message, List<String> targetIds) throws IOException{
		List<Result> results = new ArrayList<Result>();
		if(!targetIds.isEmpty()){
			MulticastResult multicastResult = sender.send(message, targetIds, config.getRetryCount());
			results = multicastResult.getResults();
		}
		return results;
	}
	
	/**
	 * 분할 발송
	 * @throws IOException 
	 */
	public List<Result> sendAsSplit(Message message, List<String> targetIds, int splitCount) throws IOException{
		
		List<Result> results = new ArrayList<Result>();
		
		if(!targetIds.isEmpty()){
			
			if(targetIds.size() <= splitCount){
				results = send(message, targetIds);
			} else {
				
				List<String> splitTargetIds = new ArrayList<String>();
				
				for(int i = 0; i < targetIds.size(); i++){
					if((i+1) % splitCount == 0){
						List<Result> splitResults = send(message, targetIds);
						results.addAll(splitResults);
						splitTargetIds.clear();						
					}
					splitTargetIds.add(targetIds.get(i));
				}
				
				//분할 후 남은 것 보내기
				if(!splitTargetIds.isEmpty()){
					List<Result> splitResults = send(message, targetIds);
					results.addAll(splitResults);
				}
			}
		}
		
		return results;
	}
	
	//참고
//	Message.Builder	gcmMessage	= new Message.Builder();
//	gcmMessage.timeToLive( certificateInfo.getTimeToLive() );
//	gcmMessage.delayWhileIdle( certificateInfo.isDelayWhileIdle() );
//	gcmMessage.addData( KEY_BADGE,		String.valueOf( pushMessage.getBadge() ) );
//	gcmMessage.addData( KEY_MESSAGE_ID,	pushMessage.getMessageId() );
//	gcmMessage.addData( KEY_HAS_BIN, String.valueOf( pushMessage.hasBin() ) );
//	
//	/**
//	 * 2017.03.13 - Push 제목 필드 추가
//	 */
//	gcmMessage.addData( KEY_TITLE, pushMessage.getSubject() );
//	
//	byte[]	data = pushMessage.getMessage().getBytes();
//	if ( MESSAGE_LIMITATION_BYTES_LENGTH > data.length ) {
//		gcmMessage.addData( KEY_MESSAGE, pushMessage.getMessage() );
//	} else {
//		String	msg	= new String( data, 0, MESSAGE_LIMITATION_BYTES_LENGTH );
//		String	filteredMessage = msg.substring( 0, msg.length() - 1 );		// the reason why "msg.length() - 1" is to remove a broken last character...  
//		gcmMessage.addData( KEY_MESSAGE, filteredMessage );
//	}

}
