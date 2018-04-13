package cardinal.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import cardinal.Cardinal;
import cardinal.Session;
import cardinal.SessionHelper;
import cardinal.drawing.DrawingListener;
import cardinal.drawing.DrawingSurface;
import cardinal.ui.controllers.ChangeSessionController;
import cardinal.ui.controllers.ExitSystemController;
import cardinal.ui.controllers.CloseWindowController;
import cardinal.ui.controllers.JoinSessionController;
import cardinal.ui.controllers.OpenSessionController;
import cardinal.ui.controllers.QuitSessionController;
import cardinal.ui.controllers.SaveDrawingController;
import cardinal.ui.controllers.SelectBrushColorController;
import cardinal.ui.controllers.SelectBrushWidthController;
import cardinal.ui.controllers.StartSessionController;

public class UIBuilder
{
  public static final JFrame WINDOW = new JFrame("Cardinal");

  public static final JTabbedPane SESSION_BAR = new JTabbedPane();

  public static final JMenuItem SAVE_DRAWING_BUTTON = new JMenuItem("Save Drawing");
  private static final JMenuItem START_SESSION_BUTTON = new JMenuItem("Start Session");
  public static final JMenuItem OPEN_SESSION_BUTTON = new JMenuItem("Open Session");
  private static final JMenuItem JOIN_SESSION_BUTTON = new JMenuItem("Join Session");
  public static final JMenuItem QUIT_SESSION_BUTTON = new JMenuItem("Quit Session");
  private static final JMenuItem EXIT_SESSION_BUTTON = new JMenuItem("Exit");

  private static final JMenuItem SELECT_BRUSH_COLOR_BUTTON = new JMenuItem("Select Brush Color");
  private static final JMenuItem SELECT_BRUSH_WIDTH_BUTTON = new JMenuItem("Select Brush Width");
  public static final JMenuItem SELECT_FILL_BRUSH_BUTTON = new JRadioButtonMenuItem("Fill Brush");
  public static final JMenuItem SELECT_LINE_BRUSH_BUTTON = new JRadioButtonMenuItem("Line Brush");
  public static final JMenuItem SELECT_ELLIPSE_BRUSH_BUTTON = new JRadioButtonMenuItem("Ellipse Brush");
  public static final JMenuItem SELECT_RETANGLE_BRUSH_BUTTON = new JRadioButtonMenuItem("Rectangle Brush");

  public static void buildWindow()
  {
    SESSION_BAR.addChangeListener(new ChangeSessionController());
    SAVE_DRAWING_BUTTON.addActionListener(new SaveDrawingController());
    START_SESSION_BUTTON.addActionListener(new StartSessionController());
    OPEN_SESSION_BUTTON.addActionListener(new OpenSessionController());
    JOIN_SESSION_BUTTON.addActionListener(new JoinSessionController());
    QUIT_SESSION_BUTTON.addActionListener(new QuitSessionController());
    EXIT_SESSION_BUTTON.addActionListener(new ExitSystemController());
    SELECT_BRUSH_COLOR_BUTTON.addActionListener(new SelectBrushColorController());
    SELECT_BRUSH_WIDTH_BUTTON.addActionListener(new SelectBrushWidthController());

    SAVE_DRAWING_BUTTON.setEnabled(false);
    OPEN_SESSION_BUTTON.setEnabled(false);
    QUIT_SESSION_BUTTON.setEnabled(false);

    SELECT_FILL_BRUSH_BUTTON.setSelected(false);
    SELECT_LINE_BRUSH_BUTTON.setSelected(true);
    SELECT_ELLIPSE_BRUSH_BUTTON.setSelected(false);
    SELECT_RETANGLE_BRUSH_BUTTON.setSelected(false);

    ButtonGroup brushes = new ButtonGroup();
    brushes.add(SELECT_FILL_BRUSH_BUTTON);
    brushes.add(SELECT_LINE_BRUSH_BUTTON);
    brushes.add(SELECT_ELLIPSE_BRUSH_BUTTON);
    brushes.add(SELECT_RETANGLE_BRUSH_BUTTON);

    JMenu selectBrushTypeMenu = new JMenu("Select Brush Type");
    selectBrushTypeMenu.add(SELECT_FILL_BRUSH_BUTTON);
    selectBrushTypeMenu.add(SELECT_LINE_BRUSH_BUTTON);
    selectBrushTypeMenu.add(SELECT_ELLIPSE_BRUSH_BUTTON);
    selectBrushTypeMenu.add(SELECT_RETANGLE_BRUSH_BUTTON);

    JMenu cardinalMenu = new JMenu("Cardinal");
    cardinalMenu.add(SAVE_DRAWING_BUTTON);
    cardinalMenu.addSeparator();
    cardinalMenu.add(START_SESSION_BUTTON);
    cardinalMenu.add(OPEN_SESSION_BUTTON);
    cardinalMenu.add(JOIN_SESSION_BUTTON);
    cardinalMenu.add(QUIT_SESSION_BUTTON);
    cardinalMenu.addSeparator();
    cardinalMenu.add(EXIT_SESSION_BUTTON);

    JMenu toolsMenu = new JMenu("Tools");
    toolsMenu.add(selectBrushTypeMenu);
    toolsMenu.add(SELECT_BRUSH_COLOR_BUTTON);
    toolsMenu.add(SELECT_BRUSH_WIDTH_BUTTON);

    JMenuBar menuBar = new JMenuBar();
    menuBar.add(cardinalMenu);
    menuBar.add(toolsMenu);

    WINDOW.setSize(800, 500);
    WINDOW.setLayout(new BorderLayout());
    WINDOW.setJMenuBar(menuBar);
    WINDOW.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    WINDOW.addWindowListener(new CloseWindowController());
    WINDOW.add(SESSION_BAR);
  }

  public static void buildTab(Session session)
  {
    DrawingListener drawingListener = new DrawingListener(session);
    Dimension dimensions = session.getSessionContext().getDrawingSize();
    DrawingSurface drawingSurface = new DrawingSurface(dimensions, session);
    drawingSurface.addMouseListener(drawingListener);
    drawingSurface.addMouseMotionListener(drawingListener);

    Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    JPanel borderedContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    borderedContainer.add(drawingSurface);
    borderedContainer.setBorder(border);

    JPanel centeredContainer = new JPanel(new GridBagLayout());
    centeredContainer.setName(session.getSessionContext().getSessionName());
    centeredContainer.add(borderedContainer);

    Cardinal.sessions.put(session, centeredContainer);
    UIBuilder.SESSION_BAR.add(centeredContainer);
    SessionHelper.setActiveSession(session);
    new Thread(drawingSurface).start();
  }
}
