package brian.mastermind.model

import scala.collection.immutable.{List, NumericRange}
import scala.collection.mutable.ListBuffer

class Guesses(){
    
    // Checks the answers by comparing the lists of randomly-generated answers and the user input colors
    // Returns a list containing two ints
    // First int of the list is the number of correct colors in the correct positions
    // Second int of the list is the number of correct colors in the wrong positions
    def checkGuesses(_answers: List[String], _guesses: List[String]): List[Int] = {
        var answers = _answers
        var guesses = _guesses
        var correctPos = 0
        var wrongPos = 0
        var wrongIndex: List[Int] = List.empty
        var wrongAnswers: ListBuffer[String] = ListBuffer.empty
        var wrongGuesses: ListBuffer[String] = ListBuffer.empty
        
        // Finds number of correct colors in the correct positions
        for (x <- NumericRange(0, answers.length, 1)){
            if (answers(x).equals(guesses(x))){
                // Adds up the number of correct colors in the correct positions
                correctPos += 1
            }
            else {
                // Inserts the indexes of all colors not in the correct positions
                wrongIndex = wrongIndex :+ x
            }
        }
        
        // Generates new lists containing elements from the indexes of all colors not in the correct positions
        for (x <- wrongIndex){
            wrongAnswers = wrongAnswers :+ answers(x)
            wrongGuesses = wrongGuesses :+ guesses(x)
        }

        // Finds the number of correct colors but in the wrong positions
        for (x <- NumericRange(0, wrongIndex.length, 1)){
            // If a user input color is found in the randomly generated list,
            // Adds up the number of correct colors in the wrong positions
            // Removes it from the new list to prevent duplication.
            if (wrongAnswers.contains(wrongGuesses(x))){
                wrongPos += 1
                wrongAnswers -= wrongGuesses(x)
            }
        }

        // Returns the list
        List(correctPos, wrongPos)
    }
}

