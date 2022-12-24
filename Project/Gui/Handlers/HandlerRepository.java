package Project.Gui.Handlers;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Paths;

import Project.Core.CoreFactory;
import Project.Core.LoggingService;

public class HandlerRepository<T> implements IHandlerRepository<T>{

    private String args;
    public HandlerRepository(String args) {
        this.args = args;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        File file = new File(Paths.get("Project", "Gui", "gui.log").toAbsolutePath().toString());
        
        try{
            LoggingService loggingService = CoreFactory.creatLoggingService(file);
            loggingService.InfoLog(this.args);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
