package frames;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import game_logic.GameData;
import game_logic.Overwrite;
import game_logic.TeamData;
import listeners.ExitWindowListener;
import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;

public class WinnersGUI extends JFrame
{

	private JLabel andTheWinnersAre;
	private JTextPane winners;
	private GameData gameData;
	private JButton okay;
	private JLabel instructionLabel;
	private JLabel currentRatingLabel;
	private JLabel selectedRatingLabel;
	private JSlider ratingSlider;
	private JPanel winnersPanel;
	private JPanel mainPanel;
	private JPanel ratePanel;
	private GridBagConstraints c;

	//	public WinnersGUI()
	//	{
	//		fastTrackForTesting();
	//	}

	public WinnersGUI(GameData gameData)
	{
		this.gameData = gameData;
		initializeComponents();
		createGUI();
		addListeners();
	}

	//private methods
	private void initializeComponents()
	{
		mainPanel = new JPanel();
		winnersPanel = new JPanel();
		andTheWinnersAre = new JLabel();
		winners = new JTextPane();
		okay = new JButton("Okay");
		selectedRatingLabel = new JLabel("3");
		ratingSlider = new JSlider();
		instructionLabel = new JLabel("Please rate this game file on a scale from 1 to 5");
		currentRatingLabel = new JLabel("current average rating: " + gameData.getRating());
		winners.setEditable(false);
		okay.setEnabled(false);
		//centers the text
		//sourced from: http://stackoverflow.com/questions/3213045/centering-text-in-a-jtextarea-or-jtextpane-horizontal-text-alignment
		StyledDocument doc = winners.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
	}

	private void createGUI()
	{
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, andTheWinnersAre, mainPanel);
		AppearanceSettings.setBackground(AppearanceConstants.mediumGray, winners, okay, selectedRatingLabel);
		AppearanceSettings.setBackground(Color.darkGray, selectedRatingLabel, okay);
		AppearanceSettings.setForeground(Color.lightGray, selectedRatingLabel, okay);
		AppearanceSettings.setOpaque(selectedRatingLabel, okay);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, andTheWinnersAre);
		AppearanceSettings.setFont(AppearanceConstants.fontLarge, winners);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, okay, instructionLabel, currentRatingLabel);
		AppearanceSettings.setSize(600, 100, andTheWinnersAre);
		AppearanceSettings.setMaxSize(600, 100, andTheWinnersAre);
		AppearanceSettings.setSize(100, 50, okay);
		AppearanceSettings.setSize(40, 50, selectedRatingLabel);
		AppearanceSettings.setSize(1280, 200, winners);
		AppearanceSettings.setMaxSize(1280, 250, winners); //whyyyy
		AppearanceSettings.unSetBorderOnButtons(okay);
		AppearanceSettings.setSliders(1, 5, 3, 1, AppearanceConstants.fontSmall, ratingSlider);

		List<TeamData> winnersList = gameData.getFinalistsAndEliminatedTeams().getWinners();
		//no winners
		if (winnersList.size() == 0)
		{
			andTheWinnersAre.setText("Sad!");
			winners.setText("There were no winners!");
		}
		//at least 1 winner
		else
		{
			String winnersAre = winnersList.size() == 1 ? "And the winner is..." : "And the winners are...";
			StringBuilder teamsBuilder = new StringBuilder();
			teamsBuilder.append(winnersList.get(0).getTeamName());

			for (int i = 1; i < winnersList.size(); i++)
			{
				teamsBuilder.append(" and " + winnersList.get(i).getTeamName());
			}

			andTheWinnersAre.setText(winnersAre);
			winners.setText(teamsBuilder.toString());
		}

		setSize(600, 600);
		createMainPanel();
		add(mainPanel);
	}

	private void createMainPanel()
	{
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		andTheWinnersAre.setHorizontalAlignment(JLabel.CENTER);
		mainPanel.add(AppearanceSettings.wrapInJPanelContainer(andTheWinnersAre));

		winnersPanel.setLayout(new BoxLayout(winnersPanel, BoxLayout.PAGE_AXIS));
		winnersPanel.add(winners);

		mainPanel.add(AppearanceSettings.wrapInJPanelContainer(winners));
		mainPanel.add(createRatePanel());
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(AppearanceSettings.wrapInJPanelContainer(okay));
	}

	private JPanel createRatePanel()
	{
		ratePanel = new JPanel();
		ratePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		ratePanel.setOpaque(false);
		ratePanel.setLayout(new BoxLayout(ratePanel, BoxLayout.Y_AXIS));
		ratePanel.add(AppearanceSettings.wrapInJPanelContainer(instructionLabel));
		//ratePanel.add(Box.createVerticalGlue());

		JPanel sliderPanel = new JPanel();
		sliderPanel.setOpaque(false);
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.LINE_AXIS));
		sliderPanel.add(ratingSlider);
		selectedRatingLabel.setBorder(new EmptyBorder(20, 15, 20, 15));
		sliderPanel.add(selectedRatingLabel);

		ratePanel.add(sliderPanel);
		//ratePanel.add(Box.createVerticalGlue());
		ratePanel.add(AppearanceSettings.wrapInJPanelContainer(currentRatingLabel));

		return ratePanel;
	}

	private void addListeners()
	{
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new ExitWindowListener(this, 1));

		okay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				//overwrite file with new rating

				new Overwrite(gameData.getFilename(), gameData.getLineThatRatingStartsOn(),
						gameData.getNumRatings() + 1, gameData.gettotalRating() + ratingSlider.getValue());
				dispose();
			}

		});

		ratingSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e)
			{
				selectedRatingLabel.setText(Integer.toString(ratingSlider.getValue()));
				okay.setEnabled(true);
			}
		});
	}
	//
	//	public void fastTrackForTesting()
	//	{
	//		gameData = new GameData();
	//
	//		List<String> teamNames = new ArrayList<>(numberOfTeams);
	//		teamNames.add("Team 1");
	//		teamNames.add("Team 2");
	//		teamNames.add("Team 3");
	//		gameData.setTeams(teamNames, numberOfTeams);
	//		gameData.setNumberOfQuestions(5);
	//
	//		File file = new File("game_files/a3testfile.txt");
	//
	//		try
	//		{
	//			gameData.parseFile(file.getAbsolutePath());
	//		}
	//		catch (Exception e)
	//		{
	//			JOptionPane.showMessageDialog(this, e.getMessage(), "File Reading Error", JOptionPane.ERROR_MESSAGE);
	//		}
	//
	//		new WinnersGUI(gameData);
	//	}
	//
	//	public static void main(String[] args)
	//	{
	//		new WinnersGUI();
	//	}
}
