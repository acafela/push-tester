package push.simple.service;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GCMConfig {
	
	/**
	 * This parameter specifies how long (in seconds) the message should be kept in GCM storage if the device is offline. 
	 * The maximum time to live supported is 4 weeks, and the default value is 4 weeks. 
	 */
	private int timeToLive = 1800;
	private int retryCount = 5;
	private String apikey;
	
	public GCMConfig(String apikey) {
		this.apikey = apikey;
	}
	
	public int getTimeToLive() {
		return timeToLive;
	}
	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	public String getApikey() {
		return apikey;
	}
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}

}
