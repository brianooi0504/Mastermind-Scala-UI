// export JAVA_HOME=`/usr/libexec/java_home -v 1.8` // Set JDK version to JDK 8 on mac

// Parts of the code in this class is referenced from:
// [1] Code.makery. (2015). JavaFX Tutorial. Available from: https://code.makery.ch/library/javafx-tutorial/

// Code blocks referenced will be cited with its reference number.

package brian.mastermind

import brian.mastermind.util.Database
import brian.mastermind.model.Player
import brian.mastermind.view.LeaderboardController
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}
import scala.util.Random
import scala.collection.immutable.{List, NumericRange}
import scalafx.collections.ObservableBuffer
import scalafx.scene.paint.Color
import scalafx.scene.image.Image
import scalafx.stage.{Stage, Modality}

object MainApp extends JFXApp{
    // Database.deleteDB() // Clears out old database during testing

    // Initializing DB
    Database.setupDB() 
    
    // Assign all players into playerLeaderboard array
    var playerLeaderboard = new ObservableBuffer[Player]()
    playerLeaderboard ++= Player.allPlayers

    // Maximum chances given to players
    val maxChance = 10

    // Total colors available: red, yellow, green, blue, purple, white
    val allColors: List[Color] = List(Color.Red, Color.Yellow, Color.Green, Color.Blue, Color.MediumPurple, Color.White)
    val allColorList = List("Red", "Yellow", "Green", "Blue", "Purple", "White")
    
    // Total number of colors to be guessed
    val numberOfColors = 4
    var randomColorList: List[String] = List.empty

    // Initializes the game by randomizing the colors into an array
    def initializeGame() = {
        // Randomly shuffles colors to be added to randomColorList (answers)
        randomColorList = List.empty
        for (x <- NumericRange(0, numberOfColors, 1)){
            randomColorList = randomColorList :+ allColorList(Random.nextInt(allColorList.length))
        }
        // println(randomColorList) // Prints out all randomized colors (answers) during testing
    }

    // [1]
    // Transforms path of RootLayout.fxml to URI for resource location
    val rootResource = getClass.getResourceAsStream("view/RootLayout.fxml")
    // Initializes the loader object
    val loader = new FXMLLoader(null, NoDependencyResolver)
    // Loads root layout from fxml file
    loader.load(rootResource); 
    // Retrieves the root component BorderPane from the FXML
    val roots = loader.getRoot[jfxs.layout.BorderPane]
    
    // [1]
    // Initializes the stage
    stage = new PrimaryStage {
        title = "Mastermind"
        scene = new Scene {
        root = roots
        stylesheets += getClass.getResource("view/MyCSS.css").toString()
        }
        icons += new Image(getClass.getResourceAsStream("/images/mastermind.png"))
    }

    // [1]
    // Displays the Game window and initializes the game
    def showGamePage() = {
        val resource = getClass.getResourceAsStream("view/GamePage.fxml")
        val loader = new FXMLLoader(null, NoDependencyResolver)
        loader.load(resource);
        val roots = loader.getRoot[jfxs.layout.AnchorPane]
        this.roots.setCenter(roots)
        initializeGame()
    }

    // Restarts the game by displaying the Game window and re-initializing the game
    def restartGame() = {
        showGamePage()
    }

    // Updates the leaderboard everytime there is a new winner
    def updateLeaderboard() = {
        playerLeaderboard = new ObservableBuffer[Player]()
        playerLeaderboard ++= Player.allPlayers
    }

    // [1]
    // Displays the Leaderboard window 
    def showLeaderboard() = {
        val resource = getClass.getResourceAsStream("view/Leaderboard.fxml")
        val loader = new FXMLLoader(null, NoDependencyResolver)
        loader.load(resource);
        val roots2 = loader.getRoot[jfxs.Parent]
        val control = loader.getController[LeaderboardController#Controller]
        
        // Create the window
        val dialog = new Stage(){
            initModality(Modality.ApplicationModal)
            initOwner(stage)
            scene = new Scene{
                root = roots2
                stylesheets += getClass.getResource("view/MyCSS.css").toString()
            }
        }

        control.dialogStage = dialog
        dialog.showAndWait()
    }
    
    // [1]
    // Displays the Welcome window
    def showWelcome() = {
        val resource = getClass.getResourceAsStream("view/Welcome.fxml")
        val loader = new FXMLLoader(null, NoDependencyResolver)
        loader.load(resource);
        val roots = loader.getRoot[jfxs.layout.AnchorPane]
        this.roots.setCenter(roots)
    }

    // Starts the game with the Welcome window
    showWelcome()
}