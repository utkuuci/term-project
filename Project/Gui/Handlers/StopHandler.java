package Project.Gui.Handlers;

import java.awt.event.ActionEvent;

public class StopHandler extends HandlerRepository<StopHandler>{

    public StopHandler(String args) {
        super(args);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        try{
           ProcessBuilder builder = new ProcessBuilder("docker", "container", "stop", "proxy");
           builder.start();
        } catch(Exception ex) { ex.printStackTrace(); }
    }
}
