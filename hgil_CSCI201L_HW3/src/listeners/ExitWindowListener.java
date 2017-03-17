package listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import other_gui.AppearanceConstants;

//pop-up for when user clicks the red X on a frame
public class ExitWindowListener extends WindowAdapter
{

	private JFrame frame;
	private int exitType;

	public ExitWindowListener(JFrame frame)
	{
		this.frame = frame;
		this.exitType = 0;
	}

	public ExitWindowListener(JFrame frame, int exitType)
	{
		this.frame = frame;
		this.exitType = exitType;
	}

	public void windowClosing(WindowEvent e)
	{
		switch (exitType)
		{
			case 0:
				areYouSure();
				break;
			case 1:
				pleaseRateGame();
				break;
			default:
				System.exit(0);
				break;
		}

	}

	public void areYouSure()
	{
		int answer = JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit?", "Quit",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, AppearanceConstants.exitIcon);

		if (answer == JOptionPane.YES_OPTION)
		{
			System.exit(0);
		}
	}

	public void pleaseRateGame()
	{
		JOptionPane.showMessageDialog(frame, "Please rate this game before exiting.");
	}

}
