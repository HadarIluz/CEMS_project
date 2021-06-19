package client;

import java.io.IOException;

import gui_cems.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;
import logic.LoggedInUser;

/* _____  ______  __  __   _____                _  _               _   
  / ____||  ____||  \/  | / ____|              | |(_)             | |  
 | |     | |__   | \  / || (___  ______    ___ | | _   ___  _ __  | |_ 
 | |     |  __|  | |\/| | \___ \|______|  / __|| || | / _ \| '_ \ | __|
 | |____ | |____ | |  | | ____) |        | (__ | || ||  __/| | | || |_ 
  \_____||______||_|  |_||_____/          \___||_||_| \___||_| |_| \__|                                                                                                                                    
*/

public class ClientUI extends Application {
	public static ClientController cems; // only one instance
	public static LoggedInUser loggedInUser;

	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	@Override
	public void start(Stage primaryStage) throws IOException {
		cems = new ClientController("localhost", 5555);

		LoginController LogInHomeScreen = new LoginController();
		LogInHomeScreen.start(primaryStage);

	}
	
}
