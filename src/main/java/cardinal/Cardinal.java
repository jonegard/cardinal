package cardinal;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.UIManager;

import cardinal.ui.UIBuilder;
import cardinal.ui.UIHelper;

public final class Cardinal
{
  public static final Map<Session, JComponent> sessions = new HashMap<>();
  public static volatile Color SELECTED_COLOR = Color.BLACK;
  public static volatile float SELECTED_WIDTH = 1.0F;

  public static final void main(String[] arguments)
  {
    UIHelper.style(UIManager.getSystemLookAndFeelClassName());
    UIBuilder.buildWindow();
    UIHelper.center(UIBuilder.WINDOW, null);
    UIBuilder.WINDOW.setVisible(true);
  }

  public static final void exit()
  {
    for (Session session : sessions.keySet())
    {
      session.run = false;
    }
    UIBuilder.WINDOW.dispose();
  }
}
