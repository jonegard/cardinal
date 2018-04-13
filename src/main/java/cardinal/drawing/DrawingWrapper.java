package cardinal.drawing;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class DrawingWrapper implements Serializable
{
  private transient BufferedImage drawing;

  public DrawingWrapper(BufferedImage drawing)
  {
    if (drawing == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'drawing' was null.");
    }
    this.drawing = drawing;
  }

  public BufferedImage getDrawing()
  {
    return drawing;
  }

  private void writeObject(ObjectOutputStream outputStream) throws IOException
  {
    outputStream.defaultWriteObject();
    ImageIO.write(drawing, "png", outputStream);
  }

  private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException
  {
    inputStream.defaultReadObject();
    this.drawing = ImageIO.read(inputStream);
  }
}
