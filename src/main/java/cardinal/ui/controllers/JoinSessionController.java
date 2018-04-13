package cardinal.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import javax.swing.JOptionPane;

import cardinal.Session;
import cardinal.network.NetworkConnection;
import cardinal.network.NetworkManager;
import cardinal.ui.UIBuilder;
import cardinal.ui.dialogs.JoinSessionDialog;

public final class JoinSessionController implements ActionListener
{
  @Override
  public void actionPerformed(ActionEvent event)
  {
    JoinSessionDialog dialog = new JoinSessionDialog();
    if (!dialog.execute())
    {
      return;
    }

    InetSocketAddress address = null;
    ServerSocketChannel socket = null;
    try
    {
      socket = ServerSocketChannel.open();
      socket.bind(new InetSocketAddress(0));
      address = (InetSocketAddress) socket.getLocalAddress();
    }
    catch (IOException ioe)
    {
      System.err.println("Socket could not be properly initialized.");
      ioe.printStackTrace(System.err);
    }

    if (socket == null)
    {
      String errorMessage = "The selected session could not be joined.\nPlease try again later.";
      JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage, "Could Not Open Session", JOptionPane.WARNING_MESSAGE);
      return;
    }

    SocketChannel tcpSocket = null;
    DatagramChannel udpSocket = null;
    try
    {
      tcpSocket = SocketChannel.open();
      tcpSocket.bind(new InetSocketAddress("localhost", 0));
      tcpSocket.connect(new InetSocketAddress(dialog.getRemoteHost(), dialog.getRemotePort()));

      udpSocket = DatagramChannel.open();
      udpSocket.bind(tcpSocket.getLocalAddress());
      udpSocket.connect(tcpSocket.getRemoteAddress());
    }
    catch (IOException ioe)
    {
      System.err.println("Could not initialize NetworkConnection. I/O error occurred.");
      ioe.printStackTrace(System.err);
    }

    if (tcpSocket == null || udpSocket == null)
    {
      String errorMessage = "A connection could not be opened to the\nselected node. Please try joining a\nsession on another node.";
      JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage, "Could Not Open Session", JOptionPane.WARNING_MESSAGE);
      return;
    }

    Session session = new Session();
    NetworkConnection connection = new NetworkConnection(session, tcpSocket, udpSocket);
    NetworkManager manager = new NetworkManager(session, socket, connection);
    session.setNetworkmanager(manager);
    new Thread(manager).start();

    try
    {
      String addressText = InetAddress.getLocalHost().getHostAddress() + ":" + address.getPort();
      String messageText = "The session has been joined and is\nopen on address: " + addressText;
      JOptionPane.showMessageDialog(UIBuilder.WINDOW, messageText, "Session Has Been Joined", JOptionPane.INFORMATION_MESSAGE);
    }
    catch (IOException ioe) {}

    UIBuilder.SAVE_DRAWING_BUTTON.setEnabled(true);
    UIBuilder.OPEN_SESSION_BUTTON.setEnabled(false);
    UIBuilder.QUIT_SESSION_BUTTON.setEnabled(true);
  }
}
