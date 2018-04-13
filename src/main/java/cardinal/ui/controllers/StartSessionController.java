package cardinal.ui.controllers;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cardinal.Cardinal;
import cardinal.Session;
import cardinal.SessionContext;
import cardinal.drawing.DrawingManager;
import cardinal.ui.UIBuilder;
import cardinal.ui.dialogs.StartSessionDialog;

public final class StartSessionController implements ActionListener
{
  @Override
  public void actionPerformed(ActionEvent event)
  {
    StartSessionDialog dialog = new StartSessionDialog();
    if (!dialog.execute())
    {
      return;
    }
    String sessionName = dialog.getSessionName();
    int imageWidth = dialog.getDrawingWidth();
    int imageHeight = dialog.getDrawingHeight();
    Dimension imageDimensions = new Dimension(imageWidth, imageHeight);

    Session session = new Session();
    session.setDrawingmanager(new DrawingManager(imageDimensions));
    session.setSessionContext(new SessionContext(sessionName, imageDimensions));
    UIBuilder.buildTab(session);

    UIBuilder.SAVE_DRAWING_BUTTON.setEnabled(true);
    UIBuilder.OPEN_SESSION_BUTTON.setEnabled(true);
    UIBuilder.QUIT_SESSION_BUTTON.setEnabled(true);
  }
}
