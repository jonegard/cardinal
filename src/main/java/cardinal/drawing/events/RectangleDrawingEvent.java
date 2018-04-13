package cardinal.drawing.event;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

public class RectangleDrawingEvent extends DrawingEvent
{
  private final static long serialVersionUID = 4390578346905345690L;

  private final Color color;
  private final float width;
  private final Point point1;
  private final Point point2;

  public RectangleDrawingEvent(DrawingEventType type, Color color, float width, Point point1, Point point2)
  {
    super(type);
    if (color == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'color' was null.");
    }
    if (point1 == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'point1' was null.");
    }
    if (point2 == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'point2' was null.");
    }
    this.color = color;
    this.width = width;
    this.point1 = point1;
    this.point2 = point2;
  }

  @Override
  public void drawEvent(BufferedImage drawing)
  {
    Graphics2D graphics = (Graphics2D) drawing.getGraphics();
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.setColor(color);
    graphics.setStroke(new BasicStroke(this.width));

    int x = Math.min(point1.x, point2.x);
    int y = Math.min(point1.y, point2.y);
    int width = Math.abs(point1.x - point2.x);
    int height = Math.abs(point1.y - point2.y);

    graphics.drawRect(x, y, width, height);
    graphics.dispose();
  }
}
