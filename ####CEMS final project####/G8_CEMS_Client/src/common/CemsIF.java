// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package common;

/**
 * This interface implements the abstract method used to display objects onto
 * the client or server UIs.
 *
 * @author CEMS_TEAM
 */
public interface CemsIF {
	/**
	 * @param message to display
	 * Method that when overridden is used to display objects onto a UI.
	 */
	public abstract void display(String message);
}
