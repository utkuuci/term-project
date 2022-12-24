package Project.Core;

import java.io.File;
import java.io.IOException;

public class CoreFactory {
    public static LoggingService creatLoggingService(File file) throws IOException {
        return new LoggingService(file);
    }
    public static WriteFileService creaWriteFileService(File file) throws IOException {
        return new WriteFileService(file);
    }
    public static ReadFileService creatReadFileService (File file) throws IOException {
        return new ReadFileService(file);
    }
}
