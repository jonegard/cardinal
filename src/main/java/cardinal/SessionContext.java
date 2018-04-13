package cardinal;

import java.awt.Dimension;
import java.io.Serializable;

public class SessionContext implements Serializable
{
  private final String sessionName;
  private final Dimension drawingSize;

  public SessionContext(String sessionName, Dimension drawingSize)
  {
    if (sessionName == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'sessionName' was null.");
    }
    if (drawingSize == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'drawingSize' was null.");
    }
    this.sessionName = sessionName;
    this.drawingSize = drawingSize;
  }

  public String getSessionName()
  {
    return sessionName;
  }

  public Dimension getDrawingSize()
  {
    return drawingSize;
  }
}
