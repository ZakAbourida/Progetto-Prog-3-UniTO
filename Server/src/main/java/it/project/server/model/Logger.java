package it.project.server.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private ListProperty<String> logs;
    private File fd;
    private FileWriter append_writer;

    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy H:m:s");

    public Logger(File logFile) throws IOException {
        this.logs = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.fd = logFile;
        append_writer = new FileWriter(this.fd,true);
        append_writer.write("New server execution started:\n");
        append_writer.flush();
    }

    public ListProperty<String> getLogs() {
        return logs;
    }

    public void addLog(String log){
        log = this.format.format(new Date())+ " | " + log;
        logs.add(log);
        try {
            append_writer.write(log + "\n");
            append_writer.flush();
        }catch (IOException e){
            String err = "An error has occurred while logging to logfile check integrity\nError:";
            System.err.println(err + e.toString());
            logs.add(err);
        }
    }
}
