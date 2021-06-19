// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.CemsIF;
import common.MyFile;
import gui_cems.GuiCommon;
import logic.ResponseFromServer;
import logic.StatusMsg;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author CEMS_TEAM
 */
public class CEMSClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	CemsIF clientUI;

	// define transfer of data of client-server.
	public static ResponseFromServer responseFromServer = new ResponseFromServer(null);
	public static StatusMsg statusMsg = new StatusMsg();
	public static boolean awaitResponse = false;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the cems client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 * @throws IOException when port or host not good
	 */

	public CEMSClient(String host, int port, CemsIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		// openConnection();
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */

	public void handleMessageFromServer(Object msg) {
		System.out.println("--> handleMessageFromServer");

		if (msg instanceof StatusMsg) {
			statusMsg = (StatusMsg) msg;
			clientUI.display(statusMsg.toString());
			awaitResponse = false;
		}

		if (msg instanceof ResponseFromServer) {
			responseFromServer = (ResponseFromServer) msg;
			awaitResponse = false;
			// If the data is an NOTIFICATION type we will send it to the appropriate client
			// we want to receive the notification
			if (responseFromServer.getResponseType().startsWith("NOTIFICATION"))
				GuiCommon.handleNotifications(responseFromServer);
			responseFromServer.getStatusMsg().setStatus(responseFromServer.getResponseType());
			clientUI.display(responseFromServer.toString());
		}

		if (msg instanceof MyFile) {
			MyFile downloadExam = (MyFile) msg;
			int fileSize = ((MyFile) msg).getSize();
			System.out.println("Message received: " + msg + " from server");
			System.out.println("length " + fileSize);
			String home = System.getProperty("user.home");
			awaitResponse = false;
			try {
				FileOutputStream fos = new FileOutputStream(home + "/Downloads/" + downloadExam.getFileName());
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				bos.write(downloadExam.getMybytearray(), 0, fileSize);
				bos.flush();
				fos.flush();
				fos.close();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	public void handleMessageFromClientUI(String message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;

			sendToServer(message);
			// wait for response from server
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// return.this.response; //Response
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
		// return null;
	}

	public void handleMessageFromClientUI(Object obj) {
		try {
			openConnection();
			awaitResponse = true;

			sendToServer(obj);
			// wait for response from server
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// return.this.response; //Response
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
		// return null;
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of cemsClient class
