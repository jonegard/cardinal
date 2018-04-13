package cardinal;

import cardinal.drawing.DrawingManager;
import cardinal.network.NetworkManager;

public class Session
{
  public volatile boolean run = true;
  private SessionContext sessionContext;
  private DrawingManager drawingManager;
  private NetworkManager networkManager;

  public void setSessionContext(SessionContext sessionContext)
  {
    if (sessionContext == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'sessionContext' was null.");
    }
    if (this.sessionContext == null)
    {
      this.sessionContext = sessionContext;
    }
    else throw new IllegalStateException("Illegal state. Instance variable 'sessionContext' has already been set.");
  }

  public SessionContext getSessionContext()
  {
    return sessionContext;
  }

  public void setDrawingmanager(DrawingManager drawingManager)
  {
    if (drawingManager == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'drawingManager' was null.");
    }
    if (this.drawingManager == null)
    {
      this.drawingManager = drawingManager;
    }
    else throw new IllegalStateException("Illegal state. Instance variable 'drawingManager' has already been set.");
  }

  public DrawingManager getDrawingManager()
  {
    return drawingManager;
  }

  public void setNetworkmanager(NetworkManager networkManager)
  {
    if (drawingManager == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'networkManager' was null.");
    }
    if (this.networkManager == null)
    {
      this.networkManager = networkManager;
    }
    else throw new IllegalStateException("Illegal state. Instance variable 'networkManager' has already been set.");
  }

  public NetworkManager getNetworkManager()
  {
    return networkManager;
  }
}
