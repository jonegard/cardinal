package cardinal.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import cardinal.Cardinal;
import cardinal.Session;
import cardinal.SessionHelper;
import cardinal.ui.UIBuilder;

public final class QuitSessionController implements ActionListener
{
  @Override
  public void actionPerformed(ActionEvent event)
  {
    String errorMessageTitle = "Quit Session?";
    String errorMessage = "Are you sure you want to quit this session? Remember\nto save your drawing if you wish to keep it.";
    int response = JOptionPane.showConfirmDialog(UIBuilder.WINDOW, errorMessage, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
    if (response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION)
    {
      return;
    }

    Session session = SessionHelper.getActiveSession();
    JComponent sessionUI = Cardinal.sessions.get(session);

    session.run = false;
    Cardinal.sessions.remove(session);
    UIBuilder.SESSION_BAR.remove(sessionUI);

    if (Cardinal.sessions.isEmpty())
    {
      UIBuilder.SAVE_DRAWING_BUTTON.setEnabled(false);
      UIBuilder.OPEN_SESSION_BUTTON.setEnabled(false);
      UIBuilder.QUIT_SESSION_BUTTON.setEnabled(false);
    }
  }
}
