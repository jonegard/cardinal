package cardinal.drawing;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import cardinal.drawing.event.DrawingEvent;
import cardinal.drawing.event.DrawingEventType;

public final class DrawingManager
{
  private BufferedImage cachedDrawing;
  private final Set<DrawingEvent> permanentDrawingEvents = new TreeSet<>();
  private final Set<DrawingEvent> temporaryDrawingEvents = new HashSet<>();

  public DrawingManager(Dimension dimensions)
  {
    if (dimensions == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'dimensions' was null.");
    }
    this.cachedDrawing = DrawingHelper.createDrawing(dimensions);
  }

  public DrawingManager(BufferedImage drawing)
  {
    if (drawing == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'drawing' was null.");
    }
    this.cachedDrawing = drawing;
  }

  public final synchronized BufferedImage getCachedDrawing()
  {
    return cachedDrawing;
  }

  public final synchronized Set<DrawingEvent> getDrawingEvents()
  {
    return permanentDrawingEvents;
  }

  public final synchronized void appendDrawingEvent(DrawingEvent drawingEvent)
  {
    if (drawingEvent == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'drawingEvent' was null.");
    }

    removeDrawingEvent(drawingEvent.source);

    if (drawingEvent.getType() == DrawingEventType.PERMANENT_DRAWING_EVENT)
    {
      permanentDrawingEvents.add(drawingEvent);
    }
    if (drawingEvent.getType() == DrawingEventType.TEMPORARY_DRAWING_EVENT)
    {
      temporaryDrawingEvents.add(drawingEvent);
    }
    renderDrawing();
  }

  public final synchronized void removeDrawingEvent(DrawingSource drawingSource)
  {
    for (Iterator<DrawingEvent> iterator = temporaryDrawingEvents.iterator(); iterator.hasNext();)
    {
      DrawingEvent temporaryDrawingEvent = iterator.next();
      if (temporaryDrawingEvent.source == drawingSource)
      {
        iterator.remove();
        break;
      }
    }
  }

  public synchronized BufferedImage renderDrawing()
  {
    OffsetDateTime maximumAge = OffsetDateTime.now().minusMinutes(1);
    for (Iterator<DrawingEvent> iterator = permanentDrawingEvents.iterator(); iterator.hasNext();)
    {
      DrawingEvent event = iterator.next();
      if (event.getTime().isBefore(maximumAge))
      {
        event.drawEvent(cachedDrawing);
        iterator.remove();
      }
    }

    BufferedImage drawing = DrawingHelper.copyDrawing(this.cachedDrawing);
    for (DrawingEvent event : permanentDrawingEvents)
    {
      event.drawEvent(drawing);
    }
    for (DrawingEvent event : temporaryDrawingEvents)
    {
      event.drawEvent(drawing);
    }
    return drawing;
  }
}
