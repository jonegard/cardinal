package cardinal.ui.dialogs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cardinal.Cardinal;
import cardinal.ui.UIBuilder;
import cardinal.ui.UIHelper;

public class SelectBrushWidthDialog
{
  private boolean dialogFinished = false;

  private float width;

  private final JDialog dialog;
  private final JSlider widthSlider;
  private final JTextField widthField;
  private final WidthDisplayComponent widthDisplayComponent = new WidthDisplayComponent(new Dimension(240, 120));

  public SelectBrushWidthDialog(float width)
  {
    this.width = width;

    JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
    JLabel titleLabel = new JLabel("Select a Brush Width");
    JLabel widthLabel = new JLabel("Width: ");
    widthSlider = new JSlider(1, 5, (int) width);
    widthField = new JTextField(Integer.toString((int) width));
    JPanel widthDisplayContainer = new JPanel();
    JButton acceptButton = new JButton("Accept");

    Font titleLabelFont = titleLabel.getFont();
    String fontName = titleLabelFont.getName();
    int fontStyle = titleLabelFont.getStyle();
    int fontSize = titleLabelFont.getSize() + 4;
    titleLabel.setFont(new Font(fontName, fontStyle, fontSize));

    widthSlider.addChangeListener(new WidthChangeController());
    widthField.setEditable(false);

    Border border = BorderFactory.createTitledBorder("Width");
    widthDisplayContainer.setBorder(border);
    widthDisplayContainer.add(widthDisplayComponent);

    acceptButton.addActionListener(new AcceptButtonController());

    GridBagConstraints iconLabelConstraints = new GridBagConstraints();
    iconLabelConstraints.gridx = 0;
    iconLabelConstraints.gridy = 0;
    iconLabelConstraints.insets = new Insets(0, 0, 0, 12);
    iconLabelConstraints.anchor = GridBagConstraints.CENTER;

    GridBagConstraints titleLabelConstraints = new GridBagConstraints();
    titleLabelConstraints.gridx = 1;
    titleLabelConstraints.gridy = 0;
    titleLabelConstraints.gridwidth = 3;
    titleLabelConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints widthLabelConstraints = new GridBagConstraints();
    widthLabelConstraints.gridx = 1;
    widthLabelConstraints.gridy = 1;
    widthLabelConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints widthSliderConstraints = new GridBagConstraints();
    widthSliderConstraints.gridx = 2;
    widthSliderConstraints.gridy = 1;

    GridBagConstraints widthFieldConstraints = new GridBagConstraints();
    widthFieldConstraints.gridx = 3;
    widthFieldConstraints.gridy = 1;

    GridBagConstraints widthDisplayContainerConstraints = new GridBagConstraints();
    widthDisplayContainerConstraints.gridx = 1;
    widthDisplayContainerConstraints.gridy = 4;
    widthDisplayContainerConstraints.fill = GridBagConstraints.HORIZONTAL;
    widthDisplayContainerConstraints.gridwidth = 3;

    GridBagConstraints inputSeparatorConstraints = new GridBagConstraints();
    inputSeparatorConstraints.gridx = 1;
    inputSeparatorConstraints.gridy = 5;
    inputSeparatorConstraints.gridwidth = 3;
    inputSeparatorConstraints.insets = new Insets(4, 0, 4, 0);
    inputSeparatorConstraints.anchor = GridBagConstraints.CENTER;
    inputSeparatorConstraints.fill = GridBagConstraints.HORIZONTAL;

    GridBagConstraints acceptButtonConstraints = new GridBagConstraints();
    acceptButtonConstraints.gridx = 1;
    acceptButtonConstraints.gridy = 6;
    acceptButtonConstraints.gridwidth = 3;
    acceptButtonConstraints.anchor = GridBagConstraints.LAST_LINE_END;

    JPanel components = new JPanel(new GridBagLayout());
    components.add(iconLabel, iconLabelConstraints);
    components.add(titleLabel, titleLabelConstraints);
    components.add(widthLabel, widthLabelConstraints);
    components.add(widthSlider, widthSliderConstraints);
    components.add(widthField, widthFieldConstraints);
    components.add(widthDisplayContainer, widthDisplayContainerConstraints);
    components.add(new JSeparator(), inputSeparatorConstraints);
    components.add(acceptButton, acceptButtonConstraints);

    dialog = new JDialog(UIBuilder.WINDOW);
    dialog.setSize(350, 300);
    dialog.setTitle("Select Brush Color");
    dialog.setResizable(false);
    dialog.setModal(true);
    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    dialog.addWindowListener(new SelectBrushWidthDialog.CloseWindowController());
    dialog.add(components);
  }

  public final boolean execute()
  {
    if (dialogFinished == true)
    {
      throw new IllegalStateException("Dialog has already been run and the state has been altered. Create a new instance and run it instead.");
    }
    UIHelper.center(dialog, dialog.getParent());
    dialog.setVisible(true);
    return dialogFinished;
  }

  public final float getWidth()
  {
    if (!dialogFinished)
    {
      throw new IllegalStateException("Dialog has not finished running.");
    }
    return width;
  }

  private final class WidthDisplayComponent extends JComponent
  {
    private Dimension dimensions;

    public WidthDisplayComponent(Dimension dimensions)
    {
      if (dimensions == null)
      {
        throw new IllegalArgumentException("Bad parameter. Parameter 'dimensions' was null.");
      }
      this.dimensions = dimensions;

      super.setMaximumSize(dimensions);
      super.setPreferredSize(dimensions);
      super.setMinimumSize(dimensions);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);

      ((Graphics2D) graphics).setColor(Color.WHITE);
      ((Graphics2D) graphics).fillRect(0, 0, dimensions.width, dimensions.height);
      ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      ((Graphics2D) graphics).setStroke(new BasicStroke(width));
      ((Graphics2D) graphics).setColor(Cardinal.SELECTED_COLOR);
      ((Graphics2D) graphics).drawLine(20, 20, 220, 20);
      ((Graphics2D) graphics).drawOval(20, 40, 60, 60);
      ((Graphics2D) graphics).drawRect(120, 40, 100, 60);
    }
  }

  private final class WidthChangeController implements ChangeListener
  {
    @Override
    public void stateChanged(ChangeEvent event)
    {
      width = (float) widthSlider.getValue();
      widthField.setText(Integer.toString((int) width));
      widthDisplayComponent.repaint();
    }
  }

  private final class AcceptButtonController implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent event)
    {
      dialogFinished = true;
      dialog.setVisible(false);
    }
  }

  private final class CloseWindowController extends WindowAdapter
  {
    @Override
    public void windowClosing(WindowEvent event)
    {
      dialog.setVisible(false);
    }
  }
}
