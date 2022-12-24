package Project.Gui.Handlers;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

public class AboutDeveloperHandler extends HandlerRepository<AboutDeveloperHandler> {

    public AboutDeveloperHandler(String args) {
        super(args);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        super.actionPerformed(e);
        JOptionPane.showMessageDialog(null, "Developer: Muzaffer Tolga Yakar <muzaffertolgayakar@gmail.com>\nGithub: @utkuuci", "About Developer", JOptionPane.INFORMATION_MESSAGE);
    }
}
