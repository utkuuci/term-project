package Project.Gui.Components;

import java.net.Socket;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Project.Gui.Handlers.AboutDeveloperHandler;
import Project.Gui.Handlers.AddHostFilterHandler;
import Project.Gui.Handlers.DisplayCurrentFilteredHostHandler;
import Project.Gui.Handlers.ReportHandler;
import Project.Gui.Handlers.StartHandler;
import Project.Gui.Handlers.StopHandler;

public class Frame extends JFrame{
    Socket proxyServer;
    
    public Frame(String name) {
        super(name);
        try{
        } catch(Exception e) {  
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
        this.setTitle(name);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(360, 360);

        MenuBar menuBar = new MenuBar();
        this.getContentPane().add(BorderLayout.NORTH, menuBar);
        
        Menu file = new Menu("File");
        Menu help = new Menu("Help");

        menuBar.add(file);
        menuBar.add(help);

        MenuItem aboutDeveloper = new MenuItem("About Developer");
        help.add(aboutDeveloper);
        aboutDeveloper.addActionListener(new AboutDeveloperHandler("About developer clicked."));
        
        MenuItem start = new MenuItem("Start");
        MenuItem stop = new MenuItem("Stop");
        MenuItem report = new MenuItem("Report");
        MenuItem addHostToFilter = new MenuItem("Add host to filter");
        MenuItem displayCurrentFilteredHost = new MenuItem("Display current filtered host");
        MenuItem exit = new MenuItem("Exit");

        start.addActionListener(new StartHandler("Proxy Server has been started."));
        stop.addActionListener(new StopHandler("Proxy has been closed."));
        report.addActionListener(new ReportHandler("Report has been clicked."));
        addHostToFilter.addActionListener(new AddHostFilterHandler("Add Host filter clicked."));
        displayCurrentFilteredHost.addActionListener(new DisplayCurrentFilteredHostHandler("Display filtered host clicked"));
        
        exit.addActionListener(v -> {
			this.setVisible(false);
            this.dispose();
		});


        file.add(start);
        file.add(stop);
        file.add(report);
        file.add(addHostToFilter);
        file.add(displayCurrentFilteredHost);
        file.add(exit);

        this.setVisible(true);
        
    }
}
