package Project.Core;

import java.io.File;
import java.io.FileWriter;

public class WriteFileService {
    private File file;
    public WriteFileService(File file) {
        this.file = file;
    }

    public void write(String text) {
        try{
            FileWriter writer = new FileWriter(this.file);
            writer.write(text);
            writer.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public void append(String text) {
        try{
            FileWriter writer = new FileWriter(this.file, true);
            writer.append(text + "\n");
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
