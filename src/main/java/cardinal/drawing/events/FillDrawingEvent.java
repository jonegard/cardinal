package cardinal.drawing.event;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public class FillDrawingEvent extends DrawingEvent
{
  private final static long serialVersionUID = 4875634595657843534L;

  private final Color newColor;
  private final Color oldColor;
  private final Point point;

  public FillDrawingEvent(DrawingEventType type, Color newColor, Color oldColor, Point point) {
    super(type);
    if (newColor == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'newColor' was null.");
    }
    if (oldColor == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'oldColor' was null.");
    }
    if (point == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'point' was null.");
    }
    this.newColor = newColor;
    this.oldColor = oldColor;
    this.point = point;
  }

  @Override
  public void drawEvent(BufferedImage drawing)
  {
    int newColor = this.newColor.getRGB();
    int oldColor = this.oldColor.getRGB();
    if (newColor == oldColor) return;

    int maxHorizontal = drawing.getWidth() - 1;
    int maxVertical = drawing.getHeight() - 1;
    int minHorizontal = 0;
    int minVertical = 0;

    Queue<Point> pixels = new LinkedList<>();
    pixels.add(point);

    while (!pixels.isEmpty())
    {
      Point pixel = pixels.remove();

      boolean pixelsBelowLineExist = pixel.y + 1 <= maxVertical;
      boolean pixelsAboveLineExist = pixel.y - 1 >= minVertical;

      boolean pixelAddedAbove = false;
      boolean pixelAddedBelow = false;

      boolean pixelAddedAboveCopy;
      boolean pixelAddedBelowCopy;

      drawing.setRGB(pixel.x, pixel.y, newColor);

      if (pixelsBelowLineExist && drawing.getRGB(pixel.x, pixel.y + 1) == oldColor)
      {
        pixels.add(new Point(pixel.x, pixel.y + 1));
        pixelAddedBelow = true;
      }
      if (pixelsAboveLineExist && drawing.getRGB(pixel.x, pixel.y - 1) == oldColor)
      {
        pixels.add(new Point(pixel.x, pixel.y - 1));
        pixelAddedAbove = true;
      }

      pixelAddedAboveCopy = pixelAddedAbove;
      pixelAddedBelowCopy = pixelAddedBelow;
      Point eastPixel = new Point(pixel.x + 1, pixel.y);
      while (eastPixel.x <= maxHorizontal && drawing.getRGB(eastPixel.x, eastPixel.y) == oldColor)
      {
        if (pixelsAboveLineExist)
        {
          if (drawing.getRGB(eastPixel.x, eastPixel.y - 1) == oldColor)
          {
            if (!pixelAddedAboveCopy)
            {
              pixels.add(new Point(eastPixel.x, eastPixel.y - 1));
              pixelAddedAboveCopy = true;
            }
          }
          else pixelAddedAboveCopy = false;
        }
        if (pixelsBelowLineExist)
        {
          if (drawing.getRGB(eastPixel.x, eastPixel.y +  1) == oldColor)
          {
            if (!pixelAddedBelowCopy)
            {
              pixels.add(new Point(eastPixel.x, eastPixel.y + 1));
              pixelAddedBelowCopy = true;
            }
          }
          else pixelAddedBelowCopy = false;
        }
        drawing.setRGB(eastPixel.x++, eastPixel.y, newColor);
      }

      pixelAddedAboveCopy = pixelAddedAbove;
      pixelAddedBelowCopy = pixelAddedBelow;
      Point westPixel = new Point(pixel.x - 1, pixel.y);
      while (westPixel.x >= minHorizontal && drawing.getRGB(westPixel.x, westPixel.y) == oldColor)
      {
        if (pixelsAboveLineExist)
        {
          if (drawing.getRGB(westPixel.x, westPixel.y - 1) == oldColor)
          {
            if (!pixelAddedAboveCopy)
            {
              pixels.add(new Point(westPixel.x, westPixel.y - 1));
              pixelAddedAboveCopy = true;
            }
          }
          else pixelAddedAboveCopy = false;
        }
        if (pixelsBelowLineExist)
        {
          if (drawing.getRGB(westPixel.x, westPixel.y + 1) == oldColor)
          {
            if (!pixelAddedBelowCopy)
            {
              pixels.add(new Point(westPixel.x, westPixel.y + 1));
              pixelAddedBelowCopy = true;
            }
          }
          else pixelAddedBelowCopy = false;
        }
        drawing.setRGB(westPixel.x--, westPixel.y, newColor);
      }
    }
  }
}
