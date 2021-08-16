// Parts of the code in this class is referenced from:
// [1] Code.makery. (2015). JavaFX Tutorial. Available from: https://code.makery.ch/library/javafx-tutorial/

// Code blocks referenced will be cited with its reference number.

package brian.mastermind.view

import scalafxml.core.macros.sfxml
import brian.mastermind.MainApp
import brian.mastermind.model.TutorialAlert
import scalafx.application.Platform

@sfxml
class WelcomeController() {
    // Handles the 'HOW TO PLAY?' button by displaying the Tutorial alert
    def handleRules(): Unit = {
        val alert = new TutorialAlert().show.showAndWait()
    }

    // Handles the 'Leaderboard' button by displaying the Leaderboard window
    def handleLeaderboard(): Unit = {
        MainApp.showLeaderboard()
    }

    // Handles the 'Quit' button by quitting the whole game
    def handleQuit(): Unit = {
        Platform.exit()
    }

    // [1]
    // Handles the 'Start' button by displaying the Game window
    def handleClick(): Unit = {
        MainApp.showGamePage()
    }
}