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

public final class StartSessionDialog
{
  private static final int SESSION_NAME_MAX_LENGTH = 25;
  private static final int IMAGE_WIDTH_MAX_SIZE = 1200;
  private static final int IMAGE_WIDTH_MIN_SIZE = 300;
  private static final int IMAGE_HEIGHT_MAX_SIZE = 900;
  private static final int IMAGE_HEIGHT_MIN_SIZE = 300;

  private boolean dialogAborted = false;
  private boolean dialogFinished = false;

  private String sessionName;
  private int imageWidth;
  private int imageHeight;

  private final JDialog dialog;
  private final JTextField sessionNameField = new JTextField(10);
  private final JTextField imageWidthField = new JTextField(4);
  private final JTextField imageHeightField = new JTextField(4);

  public StartSessionDialog()
  {
    JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
    JLabel titleLabel = new JLabel("Start a New Session");
    JLabel sessionNameLabel = new JLabel("Session Name: ");
    JLabel imageWidthLabel = new JLabel("Image Width: ");
    JLabel imageHeightLabel = new JLabel("Image Height: ");
    JButton acceptButton = new JButton("Accept");

    Font titleLabelFont = titleLabel.getFont();
    String fontName = titleLabelFont.getName();
    int fontStyle = titleLabelFont.getStyle();
    int fontSize = titleLabelFont.getSize() + 4;
    titleLabel.setFont(new Font(fontName, fontStyle, fontSize));

    acceptButton.addActionListener(new StartSessionDialog.AcceptButtonController());

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

    GridBagConstraints sessionNameLabelConstraints = new GridBagConstraints();
    sessionNameLabelConstraints.gridx = 1;
    sessionNameLabelConstraints.gridy = 1;
    sessionNameLabelConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints sessionNameFieldConstraints = new GridBagConstraints();
    sessionNameFieldConstraints.gridx = 2;
    sessionNameFieldConstraints.gridy = 1;
    sessionNameLabelConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints imageWidthLabelConstraints = new GridBagConstraints();
    imageWidthLabelConstraints.gridx = 1;
    imageWidthLabelConstraints.gridy = 2;
    imageWidthLabelConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints imageWidthFieldConstraints = new GridBagConstraints();
    imageWidthFieldConstraints.gridx = 2;
    imageWidthFieldConstraints.gridy = 2;
    imageWidthFieldConstraints.insets = new Insets(3, 0, 3, 0);
    imageWidthFieldConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints imageHeightLabelConstraints = new GridBagConstraints();
    imageHeightLabelConstraints.gridx = 1;
    imageHeightLabelConstraints.gridy = 3;
    imageHeightLabelConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints imageHeightFieldConstraints = new GridBagConstraints();
    imageHeightFieldConstraints.gridx = 2;
    imageHeightFieldConstraints.gridy = 3;
    imageHeightFieldConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints inputSeparatorConstraints = new GridBagConstraints();
    inputSeparatorConstraints.gridx = 1;
    inputSeparatorConstraints.gridy = 4;
    inputSeparatorConstraints.gridwidth = 2;
    inputSeparatorConstraints.insets = new Insets(4, 0, 4, 0);
    inputSeparatorConstraints.anchor = GridBagConstraints.CENTER;
    inputSeparatorConstraints.fill = GridBagConstraints.HORIZONTAL;

    GridBagConstraints acceptButtonConstraints = new GridBagConstraints();
    acceptButtonConstraints.gridx = 2;
    acceptButtonConstraints.gridy = 5;
    acceptButtonConstraints.anchor = GridBagConstraints.LAST_LINE_END;

    JPanel components = new JPanel(new GridBagLayout());
    components.add(iconLabel, iconLabelConstraints);
    components.add(titleLabel, titleLabelConstraints);
    components.add(sessionNameLabel, sessionNameLabelConstraints);
    components.add(sessionNameField, sessionNameFieldConstraints);
    components.add(imageWidthLabel, imageWidthLabelConstraints);
    components.add(imageWidthField, imageWidthFieldConstraints);
    components.add(imageHeightLabel, imageHeightLabelConstraints);
    components.add(imageHeightField, imageHeightFieldConstraints);
    components.add(new JSeparator(), inputSeparatorConstraints);
    components.add(acceptButton, acceptButtonConstraints);

    dialog = new JDialog(UIBuilder.WINDOW);
    dialog.setSize(250, 190);
    dialog.setTitle("Start Session");
    dialog.setResizable(false);
    dialog.setModal(true);
    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    dialog.addWindowListener(new StartSessionDialog.CloseWindowController());
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
      String errorMessageTitle = "Session Name Invalid";
      String errorMessage1 = "Please enter a session name!";
      String errorMessage2 = "Session name was too long, please\n select a shorter session name.";

      String sessionNameText = sessionNameField.getText();
      if (sessionNameText.isEmpty())
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage1, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
      if (sessionNameText.length() > SESSION_NAME_MAX_LENGTH)
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage2, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
      this.sessionName = sessionNameText.trim();
    }
    {
      String errorMessageTitle = "Image Width Invalid";
      String errorMessage1 = "Please enter an image width!";
      String errorMessage2 = "Please enter a number for the image width!";
      String errorMessage3 = "Image width was too small, the minimum image width is \n" + IMAGE_WIDTH_MIN_SIZE + " pixels. Please select a new, larger, image width.";
      String errorMessage4 = "Image width was too large, the maximum image width is \n" + IMAGE_WIDTH_MAX_SIZE + " pixels. Please select a new, smaller, image width.";

      String imageWidthText = imageWidthField.getText();
      if (imageWidthText.isEmpty())
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage1, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
      try
      {
        this.imageWidth = Integer.parseInt(imageWidthText);
      }
      catch (NumberFormatException nfe)
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage2, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
      if (this.imageWidth < IMAGE_WIDTH_MIN_SIZE)
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage3, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
      if (this.imageWidth > IMAGE_WIDTH_MAX_SIZE)
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage4, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
    }
    {
      String errorMessageTitle = "Image Height Invalid";
      String errorMessage1 = "Please enter an image height!";
      String errorMessage2 = "Please enter a number for the image height!";
      String errorMessage3 = "Image height was too small, the minimum image height is \n" + IMAGE_HEIGHT_MIN_SIZE + " pixels. Please select a new, larger, image height.";
      String errorMessage4 = "Image height was too large, the maximum image height is \n" + IMAGE_HEIGHT_MAX_SIZE + " pixels. Please select a new, smaller, image height.";

      String imageHeightText = imageHeightField.getText();
      if (imageHeightText.isEmpty())
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage1, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
      try
      {
        this.imageHeight = Integer.parseInt(imageHeightText);
      }
      catch (NumberFormatException nfe)
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage2, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
      if (this.imageHeight < IMAGE_HEIGHT_MIN_SIZE)
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage3, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
      if (this.imageHeight > IMAGE_HEIGHT_MAX_SIZE)
      {
        JOptionPane.showMessageDialog(UIBuilder.WINDOW, errorMessage4, errorMessageTitle, JOptionPane.WARNING_MESSAGE);
        return false;
      }
    }
    return true;
  }

  public final String getSessionName()
  {
    if (!dialogFinished)
    {
      throw new IllegalStateException("Illegal state. Dialog has not finished running.");
    }
    return sessionName;
  }

  public final int getDrawingWidth()
  {
    if (!dialogFinished)
    {
      throw new IllegalStateException("Illegal state. Dialog has not finished running.");
    }
    return imageWidth;
  }

  public final int getDrawingHeight()
  {
    if (!dialogFinished)
    {
      throw new IllegalStateException("Illegal state. Dialog has not finished running.");
    }
    return imageHeight;
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
