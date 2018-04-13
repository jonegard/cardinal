package cardinal.drawing.event;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

import cardinal.drawing.DrawingSource;

public abstract class DrawingEvent implements Comparable<DrawingEvent>, Serializable
{
  public transient DrawingSource source;
  private final DrawingEventType type;
  private final OffsetDateTime time = OffsetDateTime.now();

  public DrawingEvent(DrawingEventType type)
  {
    if (type == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'type' was null.");
    }
    this.type = type;
  }

  public DrawingEventType getType()
  {
    return type;
  }

  public OffsetDateTime getTime()
  {
    return time;
  }

  public abstract void drawEvent(BufferedImage drawing);

  @Override
  public final int compareTo(DrawingEvent that)
  {
    return this.time.compareTo(that.time);
  }

  @Override
  public final boolean equals(Object other)
  {
    if (other == null)
    {
      return false;
    }
    if (!(other instanceof DrawingEventType))
    {
      return false;
    }
    DrawingEvent that = (DrawingEvent) other;
    if (!that.type.equals(this.type))
    {
      return false;
    }
    if (!that.time.equals(this.time))
    {
      return false;
    }
    return true;
  }

  @Override
  public final int hashCode()
  {
    int hash = 7;
    hash = 23 * hash + Objects.hashCode(this.type);
    hash = 23 * hash + Objects.hashCode(this.time);
    return hash;
  }
}
