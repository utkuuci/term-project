package Project.Gui.Handlers;

import java.awt.event.ActionEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class AddHostToFilter extends HandlerRepository<AddHostToFilter>{
    Socket socket;
    public AddHostToFilter(String args, Socket proxySocket) {
        super(args);
        try{
            this.socket = new Socket("localhost", 8000);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        try{
            String input = JOptionPane.showInputDialog("Enter the URL");
            DataOutputStream dos = new DataOutputStream(this.socket.getOutputStream());
            dos.writeBytes("Ban-Url: " + input + "\r\n");
            this.socket.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
