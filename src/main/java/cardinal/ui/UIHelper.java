package cardinal.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UIHelper
{
  public static void style(String style)
  {
    if (style == null || style.isEmpty())
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'style' was null or empty.");
    }
    try
    {
      UIManager.setLookAndFeel(style);
    }
    catch (ClassNotFoundException cnfe)
    {
      System.err.println("Could not set look & feel. Classloader could not load look & feel.");
      cnfe.printStackTrace(System.err);
    }
    catch (InstantiationException ie)
    {
      System.err.println("Could not set look & feel. Classloader could not instantiate look & feel.");
      ie.printStackTrace(System.err);
    }
    catch (IllegalAccessException iae)
    {
      System.err.println("Could not set look & feel. Look & feel was not accessible.");
      iae.printStackTrace(System.err);
    }
    catch (UnsupportedLookAndFeelException ulafe)
    {
      System.err.println("Could not set system look & feel. Look & feel was unsupported.");
      ulafe.printStackTrace(System.err);
    }
  }

  public static void center(Component component, Component parent)
  {
    if (component == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'component' was null.");
    }
    int x;
    int y;

    if (parent == null)
    {
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      x = (screen.width / 2) - (component.getWidth() / 2);
      y = (screen.height / 2) - (component.getHeight() / 2);
    }
    else
    {
      x = parent.getX() + (parent.getWidth() / 2) - (component.getWidth() / 2);
      y = parent.getY() + (parent.getHeight() / 2) - (component.getHeight() / 2);
    }
    component.setLocation(x, y);
  }
}
