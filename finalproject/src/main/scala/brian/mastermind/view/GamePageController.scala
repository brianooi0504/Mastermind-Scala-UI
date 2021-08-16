package brian.mastermind.view

import scalafxml.core.macros.sfxml
import brian.mastermind.MainApp
import brian.mastermind.model.{Guesses, Player, ErrorAlert, PlayAgainAlert, NameAlert}
import scala.util.control.Breaks
import scala.collection.immutable.List
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.shape.Circle
import scalafx.scene.paint.Color
import scalafx.scene.control.Label
import scalafx.geometry.Pos
import scalafx.beans.property.StringProperty

@sfxml
class GamePageController(
        private val guessed1: Circle,
        private val guessed2: Circle,
        private val guessed3: Circle,
        private val guessed4: Circle,
        private val rightCol: VBox,
        private val chancesLabel: Label
    ){
        // A list with the 4 displayed circles 
        var guessedList = List(guessed1, guessed2, guessed3, guessed4)

        // Number of colors guessed and the initialized list for the colors guessed
        var guessed = 0
        var guessedAnswers = List.empty[String]

        // The default color of the 4 displayed circles
        val defaultColor:Color = Color.Gray
        val defaultColorString:String = guessed1.fill.value.toString //"0x808080ff"

        // A new Guesses object to evaluate the results
        var guesses1 = new Guesses

        // Number of tries the player has used
        var tries = 0

        // Fills the displayed circles with the colors guessed by the player
        def handleGuess(_color: Color, _colorname: String): Unit ={
            val loop = new Breaks
            loop.breakable{
                for (x <- guessedList){
                    // Fills in the colors only if the circles are in their default color
                    if (x.fill.value.toString.equals(defaultColorString)){
                        x.fill = _color
                        guessed += 1
                        guessedAnswers = guessedAnswers :+ _colorname
                        loop.break
                    }
                }
            }
        }
        
        // Fills the displayed circle red
        def handleRed(): Unit = {
            handleGuess(Color.Red, "Red")
        }

        // Fills the displayed circle yellow
        def handleYellow(): Unit = {
            handleGuess(Color.Yellow, "Yellow")
        }

        // Fills the displayed circle green
        def handleGreen(): Unit = {
            handleGuess(Color.Green, "Green")
        }

        // Fills the displayed circle blue
        def handleBlue(): Unit = {
            handleGuess(Color.Blue, "Blue")
        }

        // Fills the displayed circle purple
        def handlePurple(): Unit = {
            handleGuess(Color.MediumPurple, "Purple")
        }

        // Fills the displayed circle white
        def handleWhite(): Unit = {
            handleGuess(Color.White, "White")
        }

        // Clears out all guesses made by the player and returns all the displayed circles back to their default colors
        def clear(): Unit = {
            for (x <- guessedList){
                x.fill = defaultColor
            }
            guessed = 0
            guessedAnswers = List.empty[String]
        }

        // Returns a center-aligned label to display the results
        def resultLabel(_result: String): Label = {
            var newlabel = new Label(_result){
                styleClass = List(".resultLabel")
                textFill = Color.SaddleBrown
                prefHeight = 40
                prefWidth = 50
                alignment = Pos.Center
            }
            newlabel
        }

        // Adds 4 circles filled with the player's guesses and the results to the right column as their guesses history
        def addGuesses(_guessedAnswers: List[String], _correctPos: Int, _wrongPos: Int): Unit = {
            var circles: List[Circle] = List.empty

            for (x <- _guessedAnswers) {
                circles = circles :+ new Circle {
                    radius = 20
                    fill = MainApp.allColors(MainApp.allColorList.indexOf(x))
                    stroke = Color.Black
                    strokeWidth = 1.0
                }
            }

            // Returns a HBox containing the 4 colored circles
            var circleBox: HBox = new HBox(10.0){
                children = circles
            }
            
            // Returns a HBox containing the number of tries taken, the 4 circles, and the results
            var guessBox: HBox = new HBox(10.0){
                padding = Insets(10.0)
                children = new Label("#" + tries.toString){
                    prefWidth = 20
                }
                children += circleBox
                children += resultLabel(_correctPos.toString)
                children += resultLabel(_wrongPos.toString)
            }

            // Inserts the HBox into the right column
            rightCol.children += guessBox
            
            // Resets the 4 displayed circles to their default color
            clear()
        }

        // Updates the label that displays the number of chances the player still has
        def updateChances(): Unit = {
            chancesLabel.text = (MainApp.maxChance - tries).toString
        }

        // Handles the 'Clear' button by resetting the displayed circles to their default colors and resetting the player's guesses
        def handleClear(action: ActionEvent): Unit = {
            clear()
        }

        // Handles the condition where if the player wins
        def handleWin(): Unit = {
            // An text input dialog is displayed to get the player's name to be inserted into the leaderboard together with their score
            val playerName = new NameAlert(
                "Congratulations!", f"Your score is ${tries}!\nEnter your name to be entered into the leaderboard.",
                "Enter your name here:"
            ).show
            var player = new Player(playerName, tries)
            
            // The player is inserted into the database
            player.save()

            // The playerLeaderboard array is updated so that it can be displayed in the Leaderboard window
            MainApp.updateLeaderboard()

            // A confirmation alert is displayed to ask whether the player wants to play again or return to the home screen
            val alert = new PlayAgainAlert(
                "Congratulations!", f"Your score is ${tries}!\nYour name has been entered into the leaderboard.",
                "Do you want to play again?\nPress 'Yes' to play again.\nPress 'No' to return to the home page."
            ).showPlayAgain
        }

        // Handles the 'Submit' button and evaluates the results
        def handleSubmit(action: ActionEvent): Unit = {
            // Ensures all of the positions are guessed
            if (guessedAnswers.length == MainApp.numberOfColors){
                tries += 1
                updateChances()
                
                // Returns the result after checking the player's guesses with the answers
                var results: List[Int] = guesses1.checkGuesses(MainApp.randomColorList, guessedAnswers)
                
                // Checks if the player has used up all of their chances
                if (tries == MainApp.maxChance){
                    // If the users gets all colors and their positions correct, calls the handleWin() function
                    if (results(0) == MainApp.numberOfColors){
                        addGuesses(guessedAnswers, results(0), results(1))

                        handleWin()
                    }
                    else{
                        // If the user fails to guess correctly within their chances
                        addGuesses(guessedAnswers, results(0), results(1))
                        
                        // An alert will be displayed asking if the user wants to play again or return to the home screen
                        val pa = new PlayAgainAlert(
                            "Game over!", f"Oops, you lost! :(\nBetter luck next time! ¯\\_(ツ)_/¯\n\nThe correct answer was ${MainApp.randomColorList.mkString(", ")}.", 
                            "Do you want to play again?\nPress 'Yes' to play again.\nPress 'No' to return to the home page."
                        ).showPlayAgain
                        
                    }
                }
                // If users still has not used up all of their chances
                else{
                    // If the users gets all colors and their positions correct, calls the handleWin() function
                    if (results(0) == MainApp.numberOfColors){
                        addGuesses(guessedAnswers, results(0), results(1))

                        handleWin()
                    }
                    // If the users did not guess it correctly, just update the guesses history
                    else{
                        addGuesses(guessedAnswers, results(0), results(1))
                    }
                }
            }
            else{
                // If not all positions are guessed, an error alert is displayed, notifying the player
                val ea = new ErrorAlert(
                    "Warning", "Your guesses are not complete!", 
                    "Try and finish your guesses before submitting again!"
                ).show.showAndWait()
            }
        }
}

