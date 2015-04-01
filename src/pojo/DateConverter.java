package pojo;

import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by gandy on 13.10.14.
 */
public class DateConverter extends StringConverter<LocalDate> {

    private String pattern = "d MMMM yyyy";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    @Override
    public String toString(LocalDate date) {

        if (date != null){
            return formatter.format(date);
        } else {
            return "";
        }
    }

    @Override
    public LocalDate fromString(String string) {
        if (string != null &&  !string.isEmpty()){
            return LocalDate.parse(string, formatter);
        } else {
            return  null;
        }
    }
}
