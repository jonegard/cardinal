package cardinal.ui.controllers;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

import cardinal.Cardinal;
import cardinal.ui.UIBuilder;

public final class CloseWindowController extends WindowAdapter
{
  @Override
  public void windowClosing(WindowEvent event)
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
