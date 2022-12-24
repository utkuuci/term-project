package Project.Gui.Handlers;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import Project.Core.CoreFactory;
import Project.Core.ReadFileService;
import Project.Core.WriteFileService;

public class DisplayCurrentFilteredHostHandler extends HandlerRepository<DisplayCurrentFilteredHostHandler> {

    public DisplayCurrentFilteredHostHandler(String args) {
        super(args);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        try{ 
            Socket socket = new Socket("localhost", 80);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeBytes("Get-filtered-hosts\r\n\r\n");
            String urls = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = "";
            while((line = reader.readLine()) != null){
                urls += line + "\n";
            }
            WriteFileService writeFileService = CoreFactory.creaWriteFileService(new File(Paths.get("Project", "Gui", "DarkList.txt").toAbsolutePath().toString()));
            writeFileService.write(urls);
            JOptionPane.showMessageDialog(null ,urls, "Filtered URLs", JOptionPane.INFORMATION_MESSAGE);
            socket.close();
        } catch(Exception ex) { 
            ex.printStackTrace();
            try{
                ReadFileService readFileService = CoreFactory.creatReadFileService(new File(Paths.get("Project", "Gui", "DarkList.txt").toAbsolutePath().toString()));
                String urls = readFileService.getTExt();
                JOptionPane.showMessageDialog(null ,urls, "Filtered URLs", JOptionPane.INFORMATION_MESSAGE);
            }catch(Exception ex2){ ex2.printStackTrace(); }
        }
    }
}
