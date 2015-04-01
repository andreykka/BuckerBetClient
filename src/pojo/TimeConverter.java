package pojo;

import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by gandy on 13.10.14.
 *
 */
public class TimeConverter extends StringConverter<LocalTime> {

    private String pattern = "HH:mm";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    @Override
    public String toString(LocalTime time) {

        if (time != null){
            return formatter.format(time);
        } else {
            return "";
        }
    }

    @Override
    public LocalTime fromString(String string) {
        if (string != null &&  !string.isEmpty()){
            return LocalTime.parse(string, formatter);
        } else {
            return  null;
        }
    }
}
