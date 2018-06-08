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
	
	public static final String _DEFAULT_MESSAGE_KEY = "message";	
	
	private Sender sender;
	private GCMConfig config;
	
	/**
	 * 생성자
	 */
	public GCMConnector(GCMConfig gcmConfig){
		this.sender = new Sender(gcmConfig.getApikey());
		this.config = gcmConfig;
	}
	
	public List<Result> send(String message, List<String> targetIds) throws IOException{
		Message gcmMessage = buildSimpleMessage(message);
		return send(gcmMessage, targetIds);
	}
	
	private Message buildSimpleMessage(String message) {
		return new Message.Builder().addData(_DEFAULT_MESSAGE_KEY, message).build();
	}

	/**
	 * 푸시 발송 요청
	 * 
	 * @param message 푸시 발송 메세지
	 * @param targetIds 푸시 발송 대상(GCM에서 발급 받은 푸시키)
	 * @return 발송결과 리스트
	 * @throws IOException
	 */
	public List<Result> send(Message message, List<String> targetIds) throws IOException{
		List<Result> results = new ArrayList<Result>();
		if(!targetIds.isEmpty()){
			MulticastResult multicastResult = sender.send(message, targetIds, config.getRetryCount());
			results = multicastResult.getResults();
		}
		return results;
	}
	
	/**
	 * {@code splitCount} 단위로 분할해서 푸시 발송 요청
	 * 
	 * @param message 푸시 발송 메세지
	 * @param targetIds 푸시 발송 대상(GCM에서 발급 받은 푸시키)
	 * @param splitCount 분할 발송할 카운트
	 * @return 발송결과 리스트
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
}
