package Project.Core;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;

public class LoggingService {
    private File file;
    public LoggingService(File file) {
        this.file = file;
    }

    public void ErrorLog(String log) {
        try{ 
            FileWriter writer = new FileWriter(this.file, true);
            writer.append("ERROR: \t" + LocalDate.now() + " " + log + "\n");
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void InfoLog(String log) {
        try{ 
            FileWriter writer = new FileWriter(this.file, true);
            writer.append("INFO: \t" + LocalDate.now() + " " + log + "\n");
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
