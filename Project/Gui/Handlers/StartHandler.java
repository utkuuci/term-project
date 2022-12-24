package Project.Gui.Handlers;

import java.awt.event.ActionEvent;

public class StartHandler extends HandlerRepository<StartHandler>{
    public StartHandler(String args) {
        super(args);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        super.actionPerformed(e);
        try{
            ProcessBuilder builder = new ProcessBuilder("docker", "container", "start", "proxy");
            builder.start();
        } catch(Exception ex) {ex.printStackTrace();}
    }
}