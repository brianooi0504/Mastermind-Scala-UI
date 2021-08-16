// Parts of the code in this class is referenced from:
// [1] Code.makery. (2015). JavaFX Tutorial. Available from: https://code.makery.ch/library/javafx-tutorial/

// Code blocks referenced will be cited with its reference number.

package brian.mastermind.view

import scalafxml.core.macros.sfxml
import brian.mastermind.MainApp
import brian.mastermind.model.Player
import brian.mastermind.util.DateTimeUtil._
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.stage.Stage
import scalafx.scene.control.{TableView, TableColumn}
import scalafx.beans.property.StringProperty

@sfxml
class LeaderboardController(
    private val playerTable: TableView[Player],
    private val dateTimeColumn: TableColumn[Player, String],
    private val nameColumn: TableColumn[Player, String],
    private val scoreColumn: TableColumn[Player, String]
    ) {
        // Window reference
        var dialogStage: Stage = null
        
        // [1]
        // Initializes TableView by displaying the contents from the playerLeaderboard model
        playerTable.items = MainApp.playerLeaderboard

        // Initializes the columns's cell values
        dateTimeColumn.cellValueFactory = (y) => StringProperty(y.value.date.value.asString)
        nameColumn.cellValueFactory  = (y) => y.value.name
        scoreColumn.cellValueFactory = (y) => StringProperty(y.value.score.value.toString)

        // Handles the 'Done' button by closing the dialog
        def handleDone(ac: ActionEvent): Unit = {
            dialogStage.close()
        }
    }