package logic;

import java.io.Serializable;

/**
 * The class contains information about messages transmitted between the client
 * and the server and vice versa
 * 
 * @author Hadar Iluz
 *
 */
@SuppressWarnings("serial")
public class StatusMsg implements Serializable {

	private String status;
	private String description = null;

	/*get Status field*/
	public String getStatus() {
		return status;
	}
	/*set Status field*/
	public void setStatus(String status) {
		this.status = status;
	}
	/*get Description field*/
	public String getDescription() {
		return description;
	}
	/*set Description field*/
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {

		return "Status:" + getStatus() + " : " + getDescription();
	}

}
