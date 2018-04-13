package cardinal.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import cardinal.Cardinal;
import cardinal.ui.UIBuilder;

public final class ExitSystemController implements ActionListener
{
  @Override
  public void actionPerformed(ActionEvent event)
  {
    if (!Cardinal.sessions.isEmpty())
    {
      String errorMessageTitle = "Quit Cardinal?";
      String errorMessage = "Are you sure you want to quit Cardinal? Remember\nto save your drawing if you wish to keep it.";
      int response = JOptionPane.showConfirmDialog(UIBuilder.WINDOW, errorMessage, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
      if (response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION)
      {
        return;
      }
    }
    Cardinal.exit();
  }
}
