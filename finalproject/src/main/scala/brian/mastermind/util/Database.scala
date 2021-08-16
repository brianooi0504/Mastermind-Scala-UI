// Parts of the code in this class is referenced from:
// [1] Code.makery. (2015). JavaFX Tutorial. Available from: https://code.makery.ch/library/javafx-tutorial/

// Code blocks referenced will be cited with its reference number.

package brian.mastermind.util

import brian.mastermind.model.Player
import scalikejdbc._

// [1]
trait Database {
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"

  val dbURL = "jdbc:derby:myDB;create=true;";
  // initialize JDBC driver & connection pool
  Class.forName(derbyDriverClassname)
  
  ConnectionPool.singleton(dbURL, "me", "mine")

  // ad-hoc session provider on the REPL
  implicit val session = AutoSession
}

object Database extends Database{
  // Deletes the database if it already exists during testing 
  def deleteDB() = {
    if (hasDBInitialize) {
      Player.deleteTable()
    }
  }

  // [1]
  // Initializes the database if it does not exist.
  def setupDB() = {
  	if (!hasDBInitialize)
  		Player.initializeTable()
  }
  
  // [1]
  // Checking if the database exists.
  def hasDBInitialize : Boolean = {
    DB.getTable("Player") match {
      case Some(_) => true
      case None => false
    }
  }
}
