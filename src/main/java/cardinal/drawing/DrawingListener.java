package cardinal.drawing;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import cardinal.Cardinal;
import cardinal.Session;
import cardinal.drawing.event.DrawingEvent;
import cardinal.drawing.event.DrawingEventType;
import cardinal.drawing.event.EllipseDrawingEvent;
import cardinal.drawing.event.FillDrawingEvent;
import cardinal.drawing.event.LineDrawingEvent;
import cardinal.drawing.event.RectangleDrawingEvent;
import cardinal.network.NetworkManager;
import cardinal.ui.UIBuilder;

public final class DrawingListener extends MouseAdapter implements DrawingSource
{
  private final Session session;
  private volatile Point origin;

  public DrawingListener(Session session)
  {
    if (session == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'session' was null.");
    }
    this.session = session;
  }

  @Override
  public final void mousePressed(MouseEvent event)
  {
    this.origin = event.getPoint();
  }

  @Override
  public final void mouseClicked(MouseEvent event)
  {
    DrawingEvent drawingEvent = null;
    DrawingEventType drawingEventType = DrawingEventType.PERMANENT_DRAWING_EVENT;

    if (UIBuilder.SELECT_FILL_BRUSH_BUTTON.isSelected())
    {
      BufferedImage drawing = session.getDrawingManager().renderDrawing();
      Point point = event.getPoint();
      Color newColor = Cardinal.SELECTED_COLOR;
      Color oldColor = new Color(drawing.getRGB(point.x, point.y));

      drawingEvent = new FillDrawingEvent(drawingEventType, newColor, oldColor, point);
    }

    if (drawingEvent != null)
    {
      drawingEvent.source = this;
      DrawingManager drawingManager = session.getDrawingManager();
      drawingManager.appendDrawingEvent(drawingEvent);

      NetworkManager netWorkManager = session.getNetworkManager();
      if (netWorkManager != null)
      {
        netWorkManager.distributeExternally(drawingEvent, null);
      }
    }
  }

  @Override
  public final void mouseReleased(MouseEvent event)
  {
    DrawingEvent drawingEvent = null;
    DrawingEventType drawingEventType = DrawingEventType.PERMANENT_DRAWING_EVENT;

    if (UIBuilder.SELECT_LINE_BRUSH_BUTTON.isSelected())
    {
      drawingEvent = new LineDrawingEvent(drawingEventType, Cardinal.SELECTED_COLOR, Cardinal.SELECTED_WIDTH, origin, event.getPoint());
    }
    if (UIBuilder.SELECT_ELLIPSE_BRUSH_BUTTON.isSelected())
    {
      drawingEvent = new EllipseDrawingEvent(drawingEventType, Cardinal.SELECTED_COLOR, Cardinal.SELECTED_WIDTH, origin, event.getPoint());
    }
    if (UIBuilder.SELECT_RETANGLE_BRUSH_BUTTON.isSelected())
    {
      drawingEvent = new RectangleDrawingEvent(drawingEventType, Cardinal.SELECTED_COLOR, Cardinal.SELECTED_WIDTH, origin, event.getPoint());
    }

    if (drawingEvent != null)
    {
      drawingEvent.source = this;
      DrawingManager drawingManager = session.getDrawingManager();
      drawingManager.appendDrawingEvent(drawingEvent);

      NetworkManager netWorkManager = session.getNetworkManager();
      if (netWorkManager != null)
      {
        netWorkManager.distributeExternally(drawingEvent, null);
      }
    }
  }

  @Override
  public final void mouseDragged(MouseEvent event)
  {
    DrawingEvent drawingEvent = null;
    DrawingEventType drawingEventType = DrawingEventType.TEMPORARY_DRAWING_EVENT;

    if (UIBuilder.SELECT_LINE_BRUSH_BUTTON.isSelected())
    {
      drawingEvent = new LineDrawingEvent(drawingEventType, Cardinal.SELECTED_COLOR, Cardinal.SELECTED_WIDTH, origin, event.getPoint());
    }
    if (UIBuilder.SELECT_ELLIPSE_BRUSH_BUTTON.isSelected())
    {
      drawingEvent = new EllipseDrawingEvent(drawingEventType, Cardinal.SELECTED_COLOR, Cardinal.SELECTED_WIDTH, origin, event.getPoint());
    }
    if (UIBuilder.SELECT_RETANGLE_BRUSH_BUTTON.isSelected())
    {
      drawingEvent = new RectangleDrawingEvent(drawingEventType, Cardinal.SELECTED_COLOR, Cardinal.SELECTED_WIDTH, origin, event.getPoint());
    }

    if (drawingEvent != null)
    {
      drawingEvent.source = this;
      DrawingManager drawingManager = session.getDrawingManager();
      drawingManager.appendDrawingEvent(drawingEvent);

      NetworkManager netWorkManager = session.getNetworkManager();
      if (netWorkManager != null)
      {
        netWorkManager.distributeExternally(drawingEvent, null);
      }
    }
  }
}
