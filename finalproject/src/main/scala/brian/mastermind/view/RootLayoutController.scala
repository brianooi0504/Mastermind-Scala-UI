package brian.mastermind.view

import brian.mastermind.model.{InformationAlert, ConfirmationAlert, TutorialAlert}
import brian.mastermind.MainApp
import scalafxml.core.macros.sfxml
import scalafx.application.Platform
import scalafx.scene.control.ButtonType

@sfxml
class RootLayoutController() {
    // Handles the 'Leaderboard' in the menu bar by displaying the Leaderboard window
    def handleLeaderboard() {
        MainApp.showLeaderboard
    }
    
    // Handles the 'Restart Game' in the menu bar by restarting the game after displaying an alert and getting a confirmation from user
    def handleRestart() {
        val choice = new ConfirmationAlert(
          "Restart", "Are you sure you want to restart?",
          "Your current progress will not be saved."
        ).show.showAndWait
        
        choice match {
          case Some(ButtonType.OK) => MainApp.restartGame
          case _ => 
        }
    }

    // Handles the 'Quit' in the menu bar by quitting the game after displaying an alert and getting a confirmation from user
    def handleClose() {
        val choice = new ConfirmationAlert(
          "Quit", "Are you sure you want to quit?",
          "Your current progress will not be saved."
        ).show.showAndWait
        
        choice match {
          case Some(ButtonType.OK) => Platform.exit()
          case _ => 
        }
        
    }

    // Handles the 'About' in the menu bar by displaying the About alert
    def handleAbout() {
        val ia = new InformationAlert(
          "About", "Mastermind V1.0", 
          "Designed and created by Brian Ooi"
        ).show.showAndWait()
    }

    // Handles the 'Tutorial' in the menu bar by displaying the Tutorial alert
    def handleTutorial(){
        val alert = new TutorialAlert().show.showAndWait()
    }

    // Handles the 'Home Page' in the menu bar by displaying the Welcome window after displaying an alert and getting a confirmation from user
    def handleHome(){
        val choice = new ConfirmationAlert(
          "Return to Home Page", "Are you sure you want to return to the home page?",
          "Your current progress will not be saved."
        ).show.showAndWait
        
        choice match {
          case Some(ButtonType.OK) => MainApp.showWelcome()
          case _ => 
        }
    }
}