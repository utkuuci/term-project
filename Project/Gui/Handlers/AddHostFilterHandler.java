package Project.Gui.Handlers;

import java.awt.event.ActionEvent;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

public class AddHostFilterHandler extends HandlerRepository<AddHostFilterHandler> {

    public AddHostFilterHandler(String args) {
        super(args);
        //TODO Auto-generated constructor stub
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        super.actionPerformed(e);
        try{
            String url = JOptionPane.showInputDialog("Please add URL");
            Socket socket = new Socket("localhost", 80);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
    
            dos.writeBytes("Ban-Url: " + url + "\r\n\r\n");
        } catch(Exception ex) {ex.printStackTrace();}
    }
}
