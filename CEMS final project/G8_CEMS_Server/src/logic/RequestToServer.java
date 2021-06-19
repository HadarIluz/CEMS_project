package logic;

import java.io.Serializable;

/**
 * The class contains information about a new request that the client
 * creates to transfer to the server. The client create a request with an ID
 * indicating which details he wants to receive. And adds an object that
 * specifies the details by which it wants the search or action to be performed.
 * 
 * @author Yuval Hayam
 *
 */
@SuppressWarnings("serial")
public class RequestToServer implements Serializable {

	private String requestType;
	private Object requestData = null; // Object that we used to set in order to pass the server.
	
	/*constructor*/
	public RequestToServer(String requestType) {
		this.requestType = requestType;
	}
	/*get RequestData object server*/
	public Object getRequestData() {
		return requestData;
	}
	/*set RequestData object by client*/
	public void setRequestData(Object requestData) {
		this.requestData = requestData;
	}
	/*get getRequestType string*/
	public String getRequestType() {
		return requestType;
	}

}
