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

public class ReportHandler extends HandlerRepository<ReportHandler>{

    public ReportHandler(String args) {
        super(args);
        //TODO Auto-generated constructor stub
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        super.actionPerformed(e);
        try{
            Socket socket = new Socket("localhost", 80);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            dos.writeBytes("Get-log\r\n\r\n");

            String line = "";
            String result = "";
            while((line = reader.readLine()) != null) {
                result += line + "\n";
            }

            ReadFileService readFileService = CoreFactory.creatReadFileService(new File(Paths.get("Project", "Gui", "gui.log").toAbsolutePath().toString()));
            String guiLogs = readFileService.getTExt();
            JOptionPane.showMessageDialog(null ,result, "Reports(Proxy Logs)", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null ,guiLogs, "Reports(Gui Logs)", JOptionPane.INFORMATION_MESSAGE);
            socket.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
