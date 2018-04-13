package cardinal;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import cardinal.ui.UIBuilder;

public final class SessionHelper
{
  public static final Session getActiveSession()
  {
    JTabbedPane sessionBar = UIBuilder.SESSION_BAR;
    for (Session session : Cardinal.sessions.keySet())
    {
      JComponent sessionUI = Cardinal.sessions.get(session);
      if (sessionBar.getSelectedComponent() == sessionUI)
      {
        return session;
      }
    }
    throw new IllegalStateException("Illegal state. No sessions currently exist.");
  }

  public static final void setActiveSession(Session session)
  {
    if (session == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'session' was null.");
    }
    if (!Cardinal.sessions.containsKey(session))
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'session' does not exist.");
    }
    UIBuilder.SESSION_BAR.setSelectedComponent(Cardinal.sessions.get(session));
  }
}
