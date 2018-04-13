package cardinal.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

import javax.swing.JOptionPane;

import cardinal.Session;
import cardinal.SessionHelper;
import cardinal.network.NetworkManager;
import cardinal.ui.UIBuilder;

public final class OpenSessionController implements ActionListener
{
  @Override
  public void actionPerformed(ActionEvent event)
  {
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
      String errorMessage = "A session could not be opened.\nPlease try again later.";
      JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage, "Could Not Open Session", JOptionPane.WARNING_MESSAGE);
      return;
    }

    Session session = SessionHelper.getActiveSession();
    NetworkManager networkManager = new NetworkManager(session, socket, null);
    session.setNetworkmanager(networkManager);
    new Thread(networkManager).start();

    try
    {
      String addressText = InetAddress.getLocalHost().getHostAddress() + ":" + address.getPort();
      String messageText = "The session has been opened\non address: " + addressText;
      JOptionPane.showMessageDialog(UIBuilder.WINDOW, messageText, "Session Has Been Opened", JOptionPane.INFORMATION_MESSAGE);
    }
    catch (IOException ioe) {}

    UIBuilder.OPEN_SESSION_BUTTON.setEnabled(false);
  }
}
