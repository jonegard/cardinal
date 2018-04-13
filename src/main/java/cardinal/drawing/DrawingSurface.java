package cardinal.drawing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import cardinal.Session;

public class DrawingSurface extends JComponent implements Runnable
{
  private final Session session;

  public DrawingSurface(Dimension dimensions, Session session)
  {
    if (dimensions == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'dimensions' was null.");
    }
    if (session == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'session' was null.");
    }
    this.session = session;

    super.setMaximumSize(dimensions);
    super.setPreferredSize(dimensions);
    super.setMinimumSize(dimensions);
    super.setBounds(0, 0, dimensions.width, dimensions.height);
  }

  @Override
  protected void paintComponent(Graphics graphics)
  {
    super.paintComponent(graphics);

    DrawingManager drawingManager = session.getDrawingManager();
    BufferedImage drawing = drawingManager.renderDrawing();
    ((Graphics2D) graphics).drawImage(drawing, 0, 0, null);
  }

  @Override
  public void run()
  {
    while (this.session.run)
    {
      super.repaint();
      try
      {
        Thread.sleep(1000 / 45);
      }
      catch (InterruptedException ie)
      {
        System.err.println("Sleep was aborted.");
        ie.printStackTrace(System.err);
      }
    }
  }
}
