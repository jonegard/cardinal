package cardinal.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cardinal.Cardinal;
import cardinal.ui.dialogs.SelectBrushWidthDialog;

public final class SelectBrushWidthController implements ActionListener
{
  @Override
  public void actionPerformed(ActionEvent event)
  {
    SelectBrushWidthDialog dialog = new SelectBrushWidthDialog(Cardinal.SELECTED_WIDTH);
    if (dialog.execute())
    {
      Cardinal.SELECTED_WIDTH = dialog.getWidth();
    }
  }
}
