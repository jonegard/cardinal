package cardinal.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cardinal.Session;
import cardinal.SessionHelper;
import cardinal.drawing.DrawingManager;
import cardinal.ui.UIBuilder;

public final class SaveDrawingController implements ActionListener
{
  @Override
  public void actionPerformed(ActionEvent event)
  {
    Session session = SessionHelper.getActiveSession();
    String sessionName = session.getSessionContext().getSessionName();

    JFileChooser dialog = new JFileChooser();
    dialog.setSelectedFile(new File(sessionName));

    JFrame window = UIBuilder.WINDOW;
    if (dialog.showSaveDialog(window) == JFileChooser.APPROVE_OPTION)
    {
      String drawingFormat = null;
      File drawingFile = dialog.getSelectedFile();
      if (drawingFile.getName().endsWith(".jpg") || drawingFile.getName().endsWith(".JPG"))
      {
        drawingFormat = "jpg";
      }
      if (drawingFile.getName().endsWith(".png") || drawingFile.getName().endsWith(".PNG"))
      {
        drawingFormat = "png";
      }
      if (drawingFile.getName().endsWith(".gif") || drawingFile.getName().endsWith(".GIF"))
      {
        drawingFormat = "gif";
      }

      if (drawingFormat == null)
      {
        String errorMessageTitle = "Invalid Format";
        String errorMessage = "No file has been saved. The chosen format was invalid.\nOnly \".jpg\", \".png\" and \".gif\" formats are supported.";
        JOptionPane.showMessageDialog(window, errorMessage, errorMessageTitle, JOptionPane.ERROR_MESSAGE);
      }
      else
      {
        DrawingManager drawingManager = session.getDrawingManager();
        BufferedImage drawing = drawingManager.renderDrawing();
        try
        {
          ImageIO.write(drawing, drawingFormat, drawingFile);
        }
        catch (Exception e)
        {
          String errorMessageTitle = "Save Failed";
          String errorMessage = "No file has been saved. I/O errors prevented a successful save. The cause of this\ncould be insufficient file rights or due to an erroneous file path. Please choose\na different save location and try again.";
          JOptionPane.showMessageDialog(window, errorMessage, errorMessageTitle, JOptionPane.ERROR_MESSAGE);

          System.err.println("A drawing save attempt did not finish correctly.");
          e.printStackTrace(System.err);
        }
      }
    }
  }
}
