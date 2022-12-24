package Project.Core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadFileService {
    private File file;
    public ReadFileService(File file) {
        this.file = file;
    }

    public String getTExt() throws IOException {
        FileReader reader = new FileReader(file);
        String result = "";

        Path p = Paths.get(file.getAbsolutePath());
        byte[] buffer = new byte[(int) Files.size(p)];
        int hc = 0;
        int a;
        while((a = reader.read()) != -1) {
            buffer[hc++] = (byte) a;
        }
        result = new String(buffer, 0, hc);
        reader.close();
        return result;
    }
}
