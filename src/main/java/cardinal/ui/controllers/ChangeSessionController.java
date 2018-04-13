package cardinal.ui.controllers;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cardinal.Cardinal;
import cardinal.Session;
import cardinal.SessionHelper;
import cardinal.ui.UIBuilder;

public final class ChangeSessionController implements ChangeListener
{
  @Override
  public void stateChanged(ChangeEvent event)
  {
    if (!Cardinal.sessions.isEmpty())
    {
      Session session = SessionHelper.getActiveSession();
      if (session.getNetworkManager() == null)
      {
        UIBuilder.OPEN_SESSION_BUTTON.setEnabled(true);
      }
    }
    else
    {
      UIBuilder.OPEN_SESSION_BUTTON.setEnabled(false);
    }
  }
}
