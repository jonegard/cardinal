package cardinal.drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public final class DrawingHelper
{
  public static final BufferedImage createDrawing(Dimension dimensions)
  {
    if (dimensions == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'dimensions' was null.");
    }
    BufferedImage drawing = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_RGB);

    Graphics2D graphics = (Graphics2D) drawing.getGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, drawing.getWidth(), drawing.getHeight());
    graphics.dispose();

    return drawing;
  }

  public static BufferedImage copyDrawing(BufferedImage drawing)
  {
    if (drawing == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'drawing' was null.");
    }
    BufferedImage copy = new BufferedImage(drawing.getWidth(), drawing.getHeight(), BufferedImage.TYPE_INT_RGB);

    Graphics2D graphics = (Graphics2D) copy.getGraphics();
    graphics.drawImage(drawing, 0, 0, null);
    graphics.dispose();

    return copy;
  }
}
