package brian.mastermind.model

import brian.mastermind.MainApp
import scalafxml.core.macros.sfxml
import scalafx.application.Platform
import scalafx.scene.control.{Alert, ButtonType, TextInputDialog}
import scalafx.scene.control.Alert.AlertType

abstract class PopUpAlert() {
    var _title: String
    var _headerText: String
    var _contentText: String

    // Returns an alert with the specific type
    def show(a: AlertType): Alert = {
        val alert: Alert = new Alert(a){
            initOwner(MainApp.stage)
            title       = _title
            headerText  = _headerText
            contentText = _contentText
        }
        alert
    }
}

// Returns an alert with the AlertType Information
class InformationAlert(var _title: String, var _headerText: String, var _contentText: String) extends PopUpAlert {
    def show(): Alert = show(AlertType.Information)
}

// Returns an alert with the AlertType Confirmation
class ConfirmationAlert(var _title: String, var _headerText: String, var _contentText: String) extends PopUpAlert {
    def show(): Alert = show(AlertType.Confirmation)
}

// Returns an alert with the AlertType Error
class ErrorAlert(var _title: String, var _headerText: String, var _contentText: String) extends PopUpAlert {
    def show(): Alert = show(AlertType.Error)
}

// Returns a tutorial alert with the AlertType Information
class TutorialAlert() extends InformationAlert(
    "Tutorial", "MASTERMIND - RULES OF THE GAME", 
    "-	The computer picks a sequence of 4 colors.\n-	The objective of the game is to guess the exact positions of the colors in the computer's sequence.\n-	Under the ‘correct position’ tab, the game will display the number of colors in your guess that is in the CORRECT color and CORRECT position in the code sequence.\n-	Under the ‘wrong position’ tab, the game will display the number of colors in your guess that is in the CORRECT color but WRONG position in the code sequence.\n-	You win the game when you manage to guess all the colors in all their right positions in the code sequence within 10 tries.\n-	Good Luck!!"
){}

// Displays a Confirmation type alert to ask players for confirmation before restarting the game
class PlayAgainAlert(var _title: String, var _headerText: String, var _contentText: String) extends PopUpAlert {
    def showPlayAgain(): Unit = {
        val pa = show(AlertType.Confirmation)
        pa.buttonTypes = Seq(ButtonType.No, ButtonType.Yes)
        val choice = pa.showAndWait
        
        choice match {
            case Some(ButtonType.Yes) => MainApp.restartGame()
            case _ => MainApp.showWelcome()
        }
    }
}

// Returns a name after displaying a text input dialog to get the player's name
class NameAlert(var _title: String, var _headerText: String, var _contentText: String) extends PopUpAlert{
    // Overrides the inherited function to return the name entered by the player
    def show(): String = {
        val dialog = new TextInputDialog(defaultValue = f"Player${MainApp.playerLeaderboard.length + 1}") {
            initOwner(MainApp.stage)
            title = _title
            headerText = _headerText
            contentText = _contentText
        }
        var reply = dialog.showAndWait()
        
        reply match{
            // Verifies if the text input field is empty
            case Some(name) => if (name != ""){
                name
            }else{
                // If empty, prompts the user again
                show()
            }
            case _ => "Player"
        }
        
    }
}