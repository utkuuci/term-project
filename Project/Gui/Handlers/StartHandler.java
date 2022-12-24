package Project.Gui.Handlers;

import java.awt.event.ActionEvent;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.logging.Handler;

public class StartHandler extends HandlerRepository<StartHandler>{
    public StartHandler(String args) {
        super(args);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        super.actionPerformed(e);
        try{

        } catch(Exception ex) {ex.printStackTrace();}
    }
}