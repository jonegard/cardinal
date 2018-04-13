package cardinal.network;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import cardinal.Session;
import cardinal.SessionContext;
import cardinal.drawing.DrawingManager;
import cardinal.drawing.DrawingWrapper;
import cardinal.drawing.event.DrawingEvent;
import cardinal.drawing.event.DrawingEventType;
import cardinal.ui.UIBuilder;

public class NetworkManager implements Runnable
{
  private Session session;
  private ServerSocketChannel socket;
  private final Set<NetworkConnection> connections = new HashSet<>();

  public NetworkManager(Session session, ServerSocketChannel socket, NetworkConnection connection)
  {
    if (session == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'session' was null.");
    }
    if (socket == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'socket' was null.");
    }

    this.session = session;
    this.socket = socket;

    try
    {
      socket.configureBlocking(false);
    }
    catch (IOException ioe)
    {
      System.err.println("Failed to configure blocking for 'tcpSocket'.");
      ioe.printStackTrace(System.err);
    }

    if (connection != null) connections.add(connection);
  }

  @Override
  public void run()
  {
    if (!connections.isEmpty())
    {
      initializeInternalSession(connections.iterator().next());
    }

    while (this.session.run)
    {
      try
      {
        Thread.sleep(500);
      }
      catch (InterruptedException ie)
      {
        System.err.println("Sleep was aborted.");
        ie.printStackTrace(System.err);
      }

      SocketChannel tcpSocket = null;
      DatagramChannel udpSocket = null;
      try
      {
        tcpSocket = socket.accept();
        if (tcpSocket != null)
        {
          udpSocket = DatagramChannel.open();
          udpSocket.bind(tcpSocket.getLocalAddress());
          udpSocket.connect(tcpSocket.getRemoteAddress());
        } else continue;
      }
      catch (IOException ioe)
      {
        System.err.println("Failed to initialize new NetworkConnection properly.");
        ioe.printStackTrace(System.err);
        try
        {
          if (tcpSocket != null) tcpSocket.close();
        }
        catch (IOException ioex)
        {
          System.err.println("Failed to close 'tcpSocket' properly.");
          ioex.printStackTrace(System.err);
        }
        try
        {
          if (udpSocket != null) udpSocket.close();
        }
        catch (IOException ioex)
        {
          System.err.println("Failed to close 'udpSocket' properly.");
          ioex.printStackTrace(System.err);
        }
        continue;
      }
      initializeExternalSession(new NetworkConnection(session, tcpSocket, udpSocket));

      for (Iterator<NetworkConnection> iterator = connections.iterator(); iterator.hasNext();)
      {
        NetworkConnection connection = iterator.next();
        if (connection.isClosed()) iterator.remove();
        session.getDrawingManager().removeDrawingEvent(connection);
      }
    }
  }

  public synchronized void initializeInternalSession(NetworkConnection connection)
  {
    List<Object> responseObjects = new ArrayList<>();
    List<Object> sessionDemand = Collections.singletonList(NetworkEvent.SESSION_DEMAND);
    List<Object> sessionAccept = Collections.singletonList(NetworkEvent.SESSION_ACCEPT);

    connection.writeObjects(sessionDemand, NetworkProtocol.TCP);
    while (responseObjects.size() < 2)
    {
      try
      {
        Thread.sleep(200);
      }
      catch (InterruptedException ie)
      {
        System.err.println("Sleep was aborted.");
        ie.printStackTrace(System.err);
      }
      connection.readObjects(responseObjects, NetworkProtocol.TCP);
    }

    SessionContext sessionContext = null;
    DrawingWrapper drawingWrapper = null;
    try
    {
      sessionContext = (SessionContext) responseObjects.get(0);
      drawingWrapper = (DrawingWrapper) responseObjects.get(1);
    }
    catch (ClassCastException cce)
    {
      System.err.println("Session initialization failed.");
      cce.printStackTrace(System.err);
    }

    if (sessionContext == null || drawingWrapper == null)
    {
      String errorMessageTitle = "Session Not Established";
      String errorMessage = "A session could not be established with the chosen\nhost. Please try again later or with another host.";
      JOptionPane.showConfirmDialog(UIBuilder.WINDOW, errorMessage, errorMessageTitle, JOptionPane.ERROR_MESSAGE);
      return;
    }
    connection.writeObjects(sessionAccept, NetworkProtocol.TCP);

    BufferedImage cachedDrawing = drawingWrapper.getDrawing();
    DrawingManager drawingManager = new DrawingManager(cachedDrawing);
    this.session.setSessionContext(sessionContext);
    this.session.setDrawingmanager(drawingManager);
    UIBuilder.buildTab(session);
    new Thread(connection).start();
  }

  public synchronized void initializeExternalSession(NetworkConnection connection)
  {
    List<Object> sessionDemand = new ArrayList<>();
    while (sessionDemand.isEmpty() && !connection.isClosed())
    {
      try
      {
        Thread.sleep(200);
      }
      catch (InterruptedException ie)
      {
        System.err.println("Sleep was aborted.");
        ie.printStackTrace(System.err);
      }
      connection.readObjects(sessionDemand, NetworkProtocol.TCP);
    }
    if (sessionDemand.isEmpty() || connection.isClosed()) return;

    NetworkEvent sessionDemandEvent = null;
    try
    {
      sessionDemandEvent = (NetworkEvent) sessionDemand.get(0);
    }
    catch (ClassCastException cce)
    {
      System.err.println("Session initialization failed.");
      cce.printStackTrace(System.err);
    }
    if (sessionDemandEvent == null || sessionDemandEvent != NetworkEvent.SESSION_DEMAND)
    {
      connection.close();
    }

    SessionContext context = session.getSessionContext();
    BufferedImage drawing = session.getDrawingManager().getCachedDrawing();
    DrawingWrapper drawingWrapper = new DrawingWrapper(drawing);

    List<Object> sessionData = new ArrayList<>();
    sessionData.add(context);
    sessionData.add(drawingWrapper);
    connection.writeObjects(sessionData, NetworkProtocol.TCP);

    List<Object> sessionAccept = new ArrayList<>();
    while (sessionAccept.isEmpty() && !connection.isClosed())
    {
      try
      {
        Thread.sleep(200);
      }
      catch (InterruptedException ie)
      {
        System.err.println("Sleep was aborted.");
        ie.printStackTrace(System.err);
      }
      connection.readObjects(sessionAccept, NetworkProtocol.TCP);
    }
    if (sessionAccept.isEmpty() || connection.isClosed()) return;

    NetworkEvent sessionAcceptEvent = null;
    try
    {
      sessionAcceptEvent = (NetworkEvent) sessionAccept.get(0);
    }
    catch (ClassCastException cce)
    {
      System.err.println("Session initialization failed.");
      cce.printStackTrace(System.err);
    }
    if (sessionAcceptEvent == null || sessionAcceptEvent != NetworkEvent.SESSION_ACCEPT)
    {
      connection.close();
    }

    List<Object> drawingData = new ArrayList<>();
    drawingData.addAll(session.getDrawingManager().getDrawingEvents());
    connection.writeObjects(drawingData, NetworkProtocol.TCP);

    connections.add(connection);
    new Thread(connection).start();
  }

  public synchronized void distributeInternally(Object object, NetworkConnection caller)
  {
    if (object instanceof DrawingEvent)
    {
      DrawingEvent event = (DrawingEvent) object;
      event.source = caller;
      session.getDrawingManager().appendDrawingEvent(event);
    }
  }

  public synchronized void distributeExternally(Object object, NetworkConnection caller)
  {
    for (NetworkConnection connection : connections)
    {
      if (caller != null && connection == caller) continue;

      if (object instanceof DrawingEvent)
      {
        if (((DrawingEvent) object).getType() == DrawingEventType.TEMPORARY_DRAWING_EVENT)
        {
          connection.writeObjects(Collections.singletonList(object), NetworkProtocol.UDP);
        }
        if (((DrawingEvent) object).getType() == DrawingEventType.PERMANENT_DRAWING_EVENT)
        {
          connection.writeObjects(Collections.singletonList(object), NetworkProtocol.TCP);
        }
      }
      if (object instanceof NetworkEvent)
      {
        connection.writeObjects(Collections.singletonList(object), NetworkProtocol.TCP);
      }
    }
  }
}
