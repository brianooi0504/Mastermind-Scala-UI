// Parts of the code in this class is referenced from:
// [1] Code.makery. (2015). JavaFX Tutorial. Available from: https://code.makery.ch/library/javafx-tutorial/

// Code blocks referenced will be cited with its reference number.

package brian.mastermind.model

import brian.mastermind.util.Database
import brian.mastermind.util.DateTimeUtil._
import java.time.LocalDateTime
import scalikejdbc._
import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import scala.util.{Try, Success, Failure}

class Player (_name : String, _score : Int) extends Database {
    val id    = ObjectProperty[Long](-1)
    var name  = new StringProperty(_name)
	var score = IntegerProperty(_score)
	// The current date and time would be used as the time the player has won the game
	var date  = ObjectProperty[LocalDateTime](LocalDateTime.now())

	// [1]
	// Saves the player into the database
	def save() : Try[Long] = {
        Try(DB autoCommit { implicit session => 
            id.value = sql"""
                insert into player (name, score, date) values 
                    (${name.value}, ${score.value}, ${date.value.asString})
            """.updateAndReturnGeneratedKey().apply()
            id.value
        })
	}
}

object Player extends Database{
	// [1]
	def apply (
		_id : Long,
		_name : String, 
		_score : Int,
		_date : String
		) : Player = {

		new Player(_name, _score) {
			id.value         = _id
			date.value       = _date.parseLocalDateTime
		}
		
	}

	// [1]
	// Creates the table in the database
	def initializeTable() = {
		DB autoCommit { implicit session => 
			sql"""
			create table player (
			  id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
              name varchar(64),  
			  score int,
			  date varchar(64)
			)
			""".execute.apply()
		}
	}

	// Deletes the table from the database during testing
	def deleteTable() = {
		DB autoCommit { implicit session=>
			sql"""
			drop table player
			""".execute.apply()
		}
	}
	
	// Returns all players from the sorted according to their score in an ascending order
	def allPlayers : List[Player] = {
		DB readOnly { implicit session =>
			sql"select * from player ORDER BY score ".map(rs => Player(rs.long("id"), rs.string("name"),
				rs.int("score"), rs.string("date"))).list.apply()
		}
	}
}
