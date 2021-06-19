package logic;

import java.io.Serializable;

/**
 * The class contains information about an answer that the server returns to the
 * client. The server creates an answer with all the details the client
 * requested. In addition can add a message in favor of identification. For
 * example, whether the search was successful or not, whether the operation was
 * successful or not, etc.
 * 
 * @author Yuval Hayam
 *
 */
@SuppressWarnings("serial")
public class ResponseFromServer implements Serializable {
	private String responseType;
	private Object responseData = null;
	private StatusMsg statusMsg = new StatusMsg();
	
	/*constructor*/
	public ResponseFromServer(String responseType) {
		this.responseType = responseType;
	}
	/*get ResponseData object by client*/
	public Object getResponseData() {
		return responseData;
	}
	/*set ResponseData object by server*/
	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}
	/*get StatusMsg object by client*/
	public StatusMsg getStatusMsg() {
		return statusMsg;
	}
	/*set StatusMsg object by server*/
	public void setStatusMsg(StatusMsg statusMsg) {
		this.statusMsg = statusMsg;
	}
	/*get ResponseType by client*/
	public String getResponseType() {
		return responseType;
	}

	/**
	 * The method is used to print a line indicating a request or forwarding of an
	 * answer displayed in the server log.
	 */
	@Override
	public String toString() {
		return "ResponseFromServer [responseType=" + responseType + "]";
	}
}
