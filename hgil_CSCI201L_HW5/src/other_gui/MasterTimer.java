package other_gui;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import main.HW5Constants;

public class MasterTimer
{
	static int timeToCountdownClient;
	static int timeToCountdownGUI;

	static String timerStringGUI = "";
	static String timerStringClient = "";
	public static JLabel labelToUpdate;
	static Timer GUItimer = new Timer();
	static Timer clientTimer = new Timer();

	public static void startTimer(int secondsToCountdown, String sessionType, server.Client client)
	{
		startClientTimer(secondsToCountdown, sessionType, client);
		startGUITimer(secondsToCountdown, sessionType);
	}

	public static void startClientTimer(int secondsToCountdown, String sessionType, server.Client client)
	{
		clientTimer.cancel();
		clientTimer = new Timer();
		timeToCountdownClient = secondsToCountdown;
		clientTimer.scheduleAtFixedRate(new TimerTask() {

			public void run()
			{
				timeToCountdownClient--;
				String formattedCountdown = String.format("%02d", timeToCountdownClient);
				timerStringClient = ":" + formattedCountdown;
				System.out.println("Client Timer: " + timerStringClient);
				if (timeToCountdownClient == 0)
				{
					clientTimer.cancel();
					if (sessionType.equals("PickingQuestion"))
					{

					}
					if (sessionType.equals("AnsweringQuestion"))
					{
						client.timerRanOut(HW5Constants.FailedToAnswerMessage);
					}
					if (sessionType.equals("BuzzIn"))
					{
						client.timerRanOut(HW5Constants.FailedToBuzzInMessage);
					}
				}
			}
		}, 0, 1000);
	}

	public static void startGUITimer(int secondsToCountdown, String sessionType)
	{
		GUItimer.cancel();
		GUItimer = new Timer();
		timeToCountdownGUI = secondsToCountdown;
		GUItimer.scheduleAtFixedRate(new TimerTask() {

			public void run()
			{
				timeToCountdownGUI--;
				String formattedCountdown = String.format("%02d", timeToCountdownGUI);
				timerStringGUI = ":" + formattedCountdown;
				System.out.println(timerStringGUI);
				labelToUpdate.setText(timerStringGUI);
				if (timeToCountdownGUI == 0)
				{
					GUItimer.cancel();
				}
			}
		}, 0, 1000);
	}
}