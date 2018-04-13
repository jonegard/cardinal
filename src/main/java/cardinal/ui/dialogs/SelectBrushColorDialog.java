package cardinal.ui.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

import cardinal.ui.UIBuilder;
import cardinal.ui.UIHelper;

public final class SelectBrushColorDialog
{
  private boolean dialogFinished = false;

  private Color color;

  private final JDialog dialog;
  private final JSlider redColorSlider;
  private final JSlider greenColorSlider;
  private final JSlider blueColorSlider;
  private final JTextField redColorField;
  private final JTextField greenColorField;
  private final JTextField blueColorField;
  private final ColorDisplayComponent colorDisplayComponent = new ColorDisplayComponent(new Dimension(240, 30));

  public SelectBrushColorDialog(Color color)
  {
    if (color == null)
    {
      throw new IllegalArgumentException("Bad parameter. Parameter 'color' was null.");
    }
    this.color = color;

    JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
    JLabel titleLabel = new JLabel("Select a Brush Color");
    JLabel redColorLabel = new JLabel("Red: ");
    JLabel greenColorLabel = new JLabel("Green: ");
    JLabel blueColorLabel = new JLabel("Blue: ");
    redColorSlider = new JSlider(0, 255, color.getRed());
    greenColorSlider = new JSlider(0, 255, color.getGreen());
    blueColorSlider = new JSlider(0, 255, color.getBlue());
    redColorField = new JTextField(Integer.toString(color.getRed()), 3);
    greenColorField  = new JTextField(Integer.toString(color.getGreen()), 3);
    blueColorField = new JTextField(Integer.toString(color.getBlue()), 3);
    JPanel colorDisplayContainer = new JPanel();
    JButton acceptButton = new JButton("Accept");

    Font titleLabelFont = titleLabel.getFont();
    String fontName = titleLabelFont.getName();
    int fontStyle = titleLabelFont.getStyle();
    int fontSize = titleLabelFont.getSize() + 4;
    titleLabel.setFont(new Font(fontName, fontStyle, fontSize));

    ColorChangeController colorChangeController = new ColorChangeController();
    redColorSlider.addChangeListener(colorChangeController);
    greenColorSlider.addChangeListener(colorChangeController);
    blueColorSlider.addChangeListener(colorChangeController);

    redColorField.setEditable(false);
    greenColorField.setEditable(false);
    blueColorField.setEditable(false);

    Border border = BorderFactory.createTitledBorder("Color");
    colorDisplayContainer.setBorder(border);
    colorDisplayContainer.add(colorDisplayComponent);

    acceptButton.addActionListener(new SelectBrushColorDialog.AcceptButtonController());

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

    GridBagConstraints redColorLabelConstraints = new GridBagConstraints();
    redColorLabelConstraints.gridx = 1;
    redColorLabelConstraints.gridy = 1;
    redColorLabelConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints greenColorLabelConstraints = new GridBagConstraints();
    greenColorLabelConstraints.gridx = 1;
    greenColorLabelConstraints.gridy = 2;
    greenColorLabelConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints blueColorLabelConstraints = new GridBagConstraints();
    blueColorLabelConstraints.gridx = 1;
    blueColorLabelConstraints.gridy = 3;
    blueColorLabelConstraints.anchor = GridBagConstraints.LINE_START;

    GridBagConstraints redColorSliderConstraints = new GridBagConstraints();
    redColorSliderConstraints.gridx = 2;
    redColorSliderConstraints.gridy = 1;

    GridBagConstraints greenColorSliderConstraints = new GridBagConstraints();
    greenColorSliderConstraints.gridx = 2;
    greenColorSliderConstraints.gridy = 2;

    GridBagConstraints blueColorSliderConstraints = new GridBagConstraints();
    blueColorSliderConstraints.gridx = 2;
    blueColorSliderConstraints.gridy = 3;

    GridBagConstraints redColorFieldConstraints = new GridBagConstraints();
    redColorFieldConstraints.gridx = 3;
    redColorFieldConstraints.gridy = 1;

    GridBagConstraints greenColorFieldConstraints = new GridBagConstraints();
    greenColorFieldConstraints.gridx = 3;
    greenColorFieldConstraints.gridy = 2;

    GridBagConstraints blueColorFieldConstraints = new GridBagConstraints();
    blueColorFieldConstraints.gridx = 3;
    blueColorFieldConstraints.gridy = 3;

    GridBagConstraints colorDisplayContainerConstraints = new GridBagConstraints();
    colorDisplayContainerConstraints.gridx = 1;
    colorDisplayContainerConstraints.gridy = 4;
    colorDisplayContainerConstraints.fill = GridBagConstraints.HORIZONTAL;
    colorDisplayContainerConstraints.gridwidth = 3;

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
    components.add(redColorLabel, redColorLabelConstraints);
    components.add(greenColorLabel, greenColorLabelConstraints);
    components.add(blueColorLabel, blueColorLabelConstraints);
    components.add(redColorSlider, redColorSliderConstraints);
    components.add(greenColorSlider, greenColorSliderConstraints);
    components.add(blueColorSlider, blueColorSliderConstraints);
    components.add(redColorField, redColorFieldConstraints);
    components.add(greenColorField, greenColorFieldConstraints);
    components.add(blueColorField, blueColorFieldConstraints);
    components.add(colorDisplayContainer, colorDisplayContainerConstraints);
    components.add(new JSeparator(), inputSeparatorConstraints);
    components.add(acceptButton, acceptButtonConstraints);

    dialog = new JDialog(UIBuilder.WINDOW);
    dialog.setSize(350, 250);
    dialog.setTitle("Select Brush Color");
    dialog.setResizable(false);
    dialog.setModal(true);
    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    dialog.addWindowListener(new SelectBrushColorDialog.CloseWindowController());
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

  public final Color getColor()
  {
    if (!dialogFinished)
    {
      throw new IllegalStateException("Dialog has not finished running.");
    }
    return color;
  }

  private final class ColorDisplayComponent extends JComponent
  {
    private Dimension dimensions;

    public ColorDisplayComponent(Dimension dimensions)
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
    protected void paintComponent(Graphics graphics)
    {
      super.paintComponent(graphics);
      ((Graphics2D) graphics).setColor(color);
      ((Graphics2D) graphics).fillRect(0, 0, dimensions.width, dimensions.height);
    }
  }

  private final class ColorChangeController implements ChangeListener
  {
    @Override
    public void stateChanged(ChangeEvent event)
    {
      int red = redColorSlider.getValue();
      int green = greenColorSlider.getValue();
      int blue = blueColorSlider.getValue();
      color = new Color(red, green, blue);

      redColorField.setText(Integer.toString(red));
      greenColorField.setText(Integer.toString(green));
      blueColorField.setText(Integer.toString(blue));

      colorDisplayComponent.repaint();
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
