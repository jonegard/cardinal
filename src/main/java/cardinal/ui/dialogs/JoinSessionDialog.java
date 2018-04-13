package cardinal.ui.dialogs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;

import cardinal.ui.UIBuilder;
import cardinal.ui.UIHelper;

public final class JoinSessionDialog
{
  private static final int MAX_PORT_VALUE = 65535;
  private static final int MIN_PORT_VALUE = 0;

  private boolean dialogAborted;
  private boolean dialogFinished;

  private String remoteHost;
  private int remotePort;

  private final JDialog dialog;
  private final JTextField remoteHostField = new JTextField(10);
  private final JTextField remotePortField = new JTextField(5);

  public JoinSessionDialog()
  {
    JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
    JLabel titleLabel = new JLabel("Join a New Session");
    JLabel remoteHostLabel = new JLabel("Remote Host:");
    JLabel remotePortLabel = new JLabel("Remote Port:");
    JButton acceptButton = new JButton("Accept");

    Font titleLabelFont = titleLabel.getFont();
    String fontName = titleLabelFont.getName();
    int fontStyle = titleLabelFont.getStyle();
    int fontSize = titleLabelFont.getSize() + 4;
    titleLabel.setFont(new Font(fontName, fontStyle, fontSize));

    acceptButton.addActionListener(new JoinSessionDialog.AcceptButtonController());

    GridBagConstraints iconLabelConstraints = new GridBagConstraints();
    iconLabelConstraints.gridx = 0;
    iconLabelConstraints.gridy = 0;
    iconLabelConstraints.insets = new Insets(0, 0, 0, 12);
    iconLabelConstraints.anchor = GridBagConstraints.CENTER;

    GridBagConstraints titleLabelConstraints = new GridBagConstraints();
    titleLabelConstraints.gridx = 1;
    titleLabelConstraints.gridy = 0;
    titleLabelConstraints.gridwidth = 2;
    titleLabelConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints remoteHostLabelConstraints = new GridBagConstraints();
    remoteHostLabelConstraints.gridx = 1;
    remoteHostLabelConstraints.gridy = 1;
    remoteHostLabelConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints remoteHostFieldConstraints = new GridBagConstraints();
    remoteHostFieldConstraints.gridx = 2;
    remoteHostFieldConstraints.gridy = 1;
    remoteHostFieldConstraints.insets = new Insets(3, 0, 3, 0);
    remoteHostFieldConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints remotePortLabelConstraints = new GridBagConstraints();
    remotePortLabelConstraints.gridx = 1;
    remotePortLabelConstraints.gridy = 2;
    remotePortLabelConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints remotePortFieldConstraints = new GridBagConstraints();
    remotePortFieldConstraints.gridx = 2;
    remotePortFieldConstraints.gridy = 2;
    remotePortFieldConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints inputSeparatorConstraints = new GridBagConstraints();
    inputSeparatorConstraints.gridx = 1;
    inputSeparatorConstraints.gridy = 3;
    inputSeparatorConstraints.gridwidth = 2;
    inputSeparatorConstraints.insets = new Insets(4, 0, 4, 0);
    inputSeparatorConstraints.anchor = GridBagConstraints.CENTER;
    inputSeparatorConstraints.fill = GridBagConstraints.HORIZONTAL;

    GridBagConstraints acceptButtonConstraints = new GridBagConstraints();
    acceptButtonConstraints.gridx = 2;
    acceptButtonConstraints.gridy = 4;
    acceptButtonConstraints.anchor = GridBagConstraints.LAST_LINE_END;

    JPanel components = new JPanel(new GridBagLayout());
    components.add(iconLabel, iconLabelConstraints);
    components.add(titleLabel, titleLabelConstraints);
    components.add(remoteHostLabel, remoteHostLabelConstraints);
    components.add(remoteHostField, remoteHostFieldConstraints);
    components.add(remotePortLabel, remotePortLabelConstraints);
    components.add(remotePortField, remotePortFieldConstraints);
    components.add(new JSeparator(), inputSeparatorConstraints);
    components.add(acceptButton, acceptButtonConstraints);

    dialog = new JDialog(UIBuilder.WINDOW);
    dialog.setSize(235, 175);
    dialog.setTitle("Join Session");
    dialog.setResizable(false);
    dialog.setModal(true);
    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    dialog.addWindowListener(new JoinSessionDialog.CloseWindowController());
    dialog.add(components);
  }

  public final boolean execute()
  {
    if (dialogAborted == true || dialogFinished == true)
    {
      throw new IllegalStateException("Dialog has already been run and the state has been altered. Create a new instance and run it instead.");
    }

    UIHelper.center(dialog, dialog.getParent());
    while (true)
    {
      dialog.setVisible(true);
      if (dialogAborted)
      {
        return false;
      }
      if (validateInput())
      {
        dialogFinished = true;
        return dialogFinished;
      }
    }
  }

  private boolean validateInput()
  {
    {
      String hostErrMsgTitle = "Host validation failed";
      String hostErrMsg1 = "Please enter a host address!";

      if (remoteHostField.getText().equals(""))
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, hostErrMsg1, hostErrMsgTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
      this.remoteHost = remoteHostField.getText();
    }
    {
      String errorMessageTitle = "Port invalid";
      String errorMessage1 = "Please enter a port number!";
      String errorMessage2 = "Please enter a number!";
      String errorMessage3 = "Please enter a valid port number\n in the range of " + MIN_PORT_VALUE + " - " + MAX_PORT_VALUE;

      String portText = remotePortField.getText();
      if (portText.isEmpty())
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage1, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
      try
      {
        this.remotePort = Integer.parseInt(portText);
      }
      catch (NumberFormatException nfe)
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage2, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
      if (this.remotePort > 65535 || this.remotePort < 0)
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage3, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
    }
    return true;
  }

  public final String getRemoteHost()
  {
    if (!dialogFinished)
    {
      throw new IllegalStateException("Dialog has not finished running.");
    }
    return remoteHost;
  }

  public final int getRemotePort()
  {
    if (!dialogFinished)
    {
      throw new IllegalStateException("Dialog has not finished running.");
    }
    return remotePort;
  }

  private final class AcceptButtonController implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent event)
    {
      dialog.setVisible(false);
    }
  }

  private final class CloseWindowController extends WindowAdapter
  {
	  @Override
	  public void windowClosing(WindowEvent event)
    {
      dialogAborted = true;
      dialog.setVisible(false);
	  }
  }
}
