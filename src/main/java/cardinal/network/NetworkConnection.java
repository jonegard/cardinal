package cardinal.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.PortUnreachableException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cardinal.Session;
import cardinal.drawing.DrawingSource;

public class NetworkConnection implements DrawingSource, Runnable
{
  private static final int DEFAULT_BUFFER_SIZE = 50000;

  private final Session session;
  private final SocketChannel tcpSocket;
  private final DatagramChannel udpSocket;
  private volatile boolean closed = false;

  public NetworkConnection(Session session, SocketChannel tcpSocket, DatagramChannel udpSocket)
  {
    if (session ==  null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'session' was null.");
    }
    if (tcpSocket == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'tcpSocket' was null.");
    }
    if (udpSocket == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'udpSocket' was null.");
    }

    this.session = session;
    this.tcpSocket = tcpSocket;
    this.udpSocket = udpSocket;

    try
    {
      tcpSocket.configureBlocking(false);
    }
    catch (IOException ioe)
    {
      System.err.println("Failed to configure blocking for TCP socket.");
      ioe.printStackTrace(System.err);
    }
    try
    {
      udpSocket.configureBlocking(false);
    }
    catch (IOException ioe)
    {
      System.err.println("Failed to configure blocking for UDP socket.");
      ioe.printStackTrace(System.err);
    }
  }

  @Override
  public void run()
  {
    List<Object> objects = new ArrayList<>(500);
    while (this.session.run && !closed)
    {
      readObjects(objects, NetworkProtocol.TCP);
      readObjects(objects, NetworkProtocol.UDP);

      for (Iterator<Object> iterator = objects.iterator(); iterator.hasNext();)
      {
        Object object = iterator.next();
        session.getNetworkManager().distributeInternally(object, this);
        iterator.remove();
      }

      try
      {
        Thread.sleep(5);
      }
      catch (InterruptedException ie)
      {
        System.err.println("Sleep was aborted.");
        ie.printStackTrace(System.err);
      }
    }
    close();
  }

  public void writeObjects(List<Object> objects, NetworkProtocol protocol)
  {
    if (objects == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'objects' was null.");
    }
    if (protocol == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'protocol' was null.");
    }
    ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);

    for (Object object : objects)
    {
      if (object != null)
      {
        buffer = buffer.put(serialize(object));
      }
    }

    if (buffer.position() > 0)
    {
      buffer.flip();
      write(buffer, protocol);
    }
  }

  public void readObjects(List<Object> objects, NetworkProtocol protocol)
  {
    if (objects == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'objects' was null.");
    }
    if (protocol == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'protocol' was null.");
    }
    ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);

    if (read(buffer, protocol) > 0)
    {
      buffer.flip();
      while (buffer.hasRemaining())
      {
        objects.add(deserialize(buffer));
      }
    }
  }

  private static ByteBuffer serialize(Object object)
  {
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    try
    {
      new ObjectOutputStream(byteStream).writeObject(object);
    }
    catch (IOException ioe)
    {
      System.err.println("Could not serialize object.");
      ioe.printStackTrace(System.err);
      return null;
    }
    ByteBuffer objectBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
    byte[] objectData = byteStream.toByteArray();
    int objectSize = objectData.length;

    objectBuffer.putInt(objectSize);
    objectBuffer.put(objectData);
    objectBuffer.flip();

    return objectBuffer;
  }

  private static Object deserialize(ByteBuffer buffer)
  {
    int objectSize = buffer.getInt();
    byte[] objectData = new byte[objectSize];
    buffer.get(objectData);

    ByteArrayInputStream byteStream = new ByteArrayInputStream(objectData);
    try
    {
      return new ObjectInputStream(byteStream).readObject();
    }
    catch (IOException | ClassNotFoundException ex)
    {
      System.err.println("Could not deserialize data.");
      ex.printStackTrace(System.err);
    }
    return null;
  }

  private synchronized int read(ByteBuffer buffer, NetworkProtocol protocol)
  {
    if (buffer == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'buffer' was null.");
    }
    if (protocol == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'protocol' was null.");
    }

    try
    {
      if (protocol == NetworkProtocol.TCP && !closed)
      {
        return tcpSocket.read(buffer);
      }
      if (protocol == NetworkProtocol.UDP && !closed)
      {
        return udpSocket.read(buffer);
      }
    }
    catch (IOException ioe)
    {
      System.err.println("Failed to read data using " + protocol.toString() + ".");
      ioe.printStackTrace(System.err);
      close();
    }
    return 0;
  }

  private synchronized int write(ByteBuffer buffer, NetworkProtocol protocol)
  {
    if (buffer == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'buffer' was null.");
    }
    if (protocol == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'protocol' was null.");
    }

    try
    {
      if (protocol == NetworkProtocol.TCP && !closed)
      {
        return tcpSocket.write(buffer);
      }
      if (protocol == NetworkProtocol.UDP && !closed)
      {
        return udpSocket.write(buffer);
      }
    }
    catch (IOException ioe)
    {
      System.err.println("Failed to write data using " + protocol.toString() + ".");
      ioe.printStackTrace(System.err);
      close();
    }
    return 0;
  }

  public synchronized void close()
  {
    try
    {
      tcpSocket.close();
    }
    catch (IOException ioe)
    {
      System.err.println("Failed to close TCP socket properly.");
      ioe.printStackTrace(System.err);
    }
    try
    {
      udpSocket.close();
    }
    catch (IOException ioe)
    {
      System.err.println("Failed to close UDP socket properly.");
      ioe.printStackTrace(System.err);
    }
    this.closed = true;
  }

  public synchronized boolean isClosed()
  {
    return closed;
  }
}
