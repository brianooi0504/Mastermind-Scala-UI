// Parts of the code in this class is referenced from:
// [1] Code.makery. (2015). JavaFX Tutorial. Available from: https://code.makery.ch/library/javafx-tutorial/

// Code blocks referenced will be cited with its reference number.

package brian.mastermind.util

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// [1]
// Prepares the date and time format to be stored in the database
// Prepares the date and time format to be displayed in the Leaderboard
object DateTimeUtil {
  val DATE_PATTERN = "dd/MM/yy HH:mm"
  val DATE_FORMATTER =  DateTimeFormatter.ofPattern(DATE_PATTERN)
  
  implicit class DateFormater (val date : LocalDateTime){
    // Returns the given date as a well formatted String. The above defined 
    // {@link DateUtil#DATE_PATTERN} is used.
    /** 
    * @param date the date to be returned as a string
    * @return formatted string
    */
      def asString : String = {
          if (date == null) {
              return null;
          }
          return DATE_FORMATTER.format(date);
      }
  }
  implicit class StringFormater (val data : String) {
    // Converts a String in the format of the defined {@link DateUtil#DATE_PATTERN} 
    // to a {@link LocalDateTime} object.
    // Returns null if the String could not be converted.
    /** 
    * @param dateString the date as String
    * @return the date object or null if it could not be converted
    */
    def parseLocalDateTime : LocalDateTime = {
        try {
          LocalDateTime.parse(data, DATE_FORMATTER)
        } catch {
          case  e: DateTimeParseException => null
        }
    }
    
    def isValid : Boolean = {
      data.parseLocalDateTime != null
    }
  }
}
