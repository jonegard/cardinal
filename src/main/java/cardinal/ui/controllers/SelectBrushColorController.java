package cardinal.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cardinal.Cardinal;
import cardinal.ui.dialogs.SelectBrushColorDialog;

public final class SelectBrushColorController implements ActionListener
{
  @Override
  public void actionPerformed(ActionEvent event)
  {
    SelectBrushColorDialog dialog = new SelectBrushColorDialog(Cardinal.SELECTED_COLOR);
    if (dialog.execute())
    {
      Cardinal.SELECTED_COLOR = dialog.getColor();
    }
  }
}
