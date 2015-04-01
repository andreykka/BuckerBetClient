package pojo;

import javafx.beans.property.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by gandy on 24.09.14.
 *
 */

public class OutputData implements Serializable {

    private IntegerProperty id;

    private StringProperty  event;

    private ObjectProperty<LocalDate>    date;

    private ObjectProperty<LocalTime> time;

    private StringProperty result;

    public OutputData(){
        this.id = new SimpleIntegerProperty();
        this.event = new SimpleStringProperty();
        this.date = new SimpleObjectProperty<LocalDate>();
        this.time = new SimpleObjectProperty<LocalTime>();
        this.result = new SimpleStringProperty();
    }

    public OutputData(Integer id, String event, LocalDate date, LocalTime time, String result) {
        this.id = new SimpleIntegerProperty(id);
        this.event = new SimpleStringProperty(event);
        this.date = new SimpleObjectProperty<>(date);
        this.time = new SimpleObjectProperty<>(time);
        this.result = new SimpleStringProperty(result);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getEvent() {
        return event.get();
    }

    public StringProperty eventProperty() {
        return event;
    }

    public void setEvent(String event) {
        this.event.set(event);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public LocalTime getTime() {
        return time.get();
    }

    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(LocalTime.parse(time));
    }

    public String getResult() {
        return result.get();
    }

    public StringProperty resultProperty() {
        return result;
    }

    public void setResult(String result) {
        this.result.set(result);
    }
}
