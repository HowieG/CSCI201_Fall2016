package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import game_logic.GameData;
import listeners.ExitWindowListener;
import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;

public class StartWindowGUI extends JFrame
{

	private JPanel mainPanel;
	private JFileChooser fileChooser;
	private JButton fileChooserButton;
	private List<JTextField> teamNameTextFields;
	private List<JLabel> teamNameLabels;
	private static final int MAX_NUMBER_OF_TEAMS = 4;
	private int numberOfTeams;
	private JButton startGameButton;
	private JButton clearDataButton;
	private JButton exitButton;
	private JButton logoutButton;
	private JSlider slider;
	private JLabel fileNameLabel;
	private JLabel ratingLabel;
	private JCheckBox quickPlay;
	private Boolean haveNames = false;
	private Boolean haveValidFile = false;
	private GameData gameData;

	public StartWindowGUI()
	{

		super("Jeopardy Menu");
		numberOfTeams = -1;

		initializeComponents();
		createGUI();
		addListeners();
	}

	//private methods
	private void initializeComponents()
	{
		mainPanel = new JPanel();
		fileChooser = new JFileChooser();
		teamNameTextFields = new ArrayList<>(4);
		teamNameLabels = new ArrayList<>(4);
		fileNameLabel = new JLabel("");
		ratingLabel = new JLabel("");
		gameData = new GameData();
		quickPlay = new JCheckBox("Quick Play?");

		for (int i = 0; i < MAX_NUMBER_OF_TEAMS; i++)
		{
			teamNameTextFields.add(new JTextField());
			teamNameLabels.add(new JLabel("Please name Team " + (i + 1)));
		}

		startGameButton = new JButton("Start Jeopardy");
		clearDataButton = new JButton("Clear Choices");
		logoutButton = new JButton("Logout");
		exitButton = new JButton("Exit");
		fileChooserButton = new JButton("Choose File");
		slider = new JSlider();

	}

	private void createGUI()
	{

		//setting appearance of member variable gui components
		//setting background colors
		AppearanceSettings.setBackground(Color.darkGray, exitButton, logoutButton, clearDataButton, startGameButton,
				slider, teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2), teamNameLabels.get(3),
				fileChooserButton);

		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, teamNameTextFields.get(0),
				teamNameTextFields.get(1), teamNameTextFields.get(2), teamNameTextFields.get(3));

		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, fileNameLabel, ratingLabel, mainPanel);

		//setting fonts
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, teamNameTextFields.get(0), teamNameTextFields.get(1),
				teamNameTextFields.get(2), teamNameTextFields.get(3), teamNameLabels.get(0), teamNameLabels.get(1),
				teamNameLabels.get(2), teamNameLabels.get(3));

		AppearanceSettings.setFont(AppearanceConstants.fontSmall, fileChooserButton, exitButton, logoutButton,
				clearDataButton, startGameButton);

		AppearanceSettings.setFont(AppearanceConstants.fontSmallest, fileNameLabel, ratingLabel);

		//other
		AppearanceSettings.setForeground(Color.lightGray, exitButton, logoutButton, clearDataButton, startGameButton,
				teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2), teamNameLabels.get(3),
				fileChooserButton, fileNameLabel, ratingLabel, slider);

		AppearanceSettings.setOpaque(exitButton, logoutButton, clearDataButton, startGameButton, slider,
				teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2), teamNameLabels.get(3),
				fileChooserButton);

		AppearanceSettings.setSize(175, 70, exitButton, logoutButton, clearDataButton, startGameButton);
		AppearanceSettings.unSetBorderOnButtons(exitButton, logoutButton, clearDataButton, startGameButton,
				fileChooserButton);

		AppearanceSettings.setTextAlignment(teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2),
				teamNameLabels.get(3), fileNameLabel, ratingLabel);

		AppearanceSettings.setVisible(false, teamNameLabels.get(1), teamNameLabels.get(2), teamNameLabels.get(3),
				teamNameTextFields.get(1), teamNameTextFields.get(2), teamNameTextFields.get(3));
		//check box settings
		quickPlay.setFont(AppearanceConstants.fontSmallest);
		quickPlay.setHorizontalTextPosition(SwingConstants.LEFT);
		quickPlay.setPreferredSize(new Dimension(200, 30));

		//file chooser settings
		fileChooser.setPreferredSize(new Dimension(400, 500));
		File workingDirectory = new File(System.getProperty("user.dir"));
		fileChooser.setCurrentDirectory(workingDirectory);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		fileChooser.setFileFilter(filter);

		//slider settings
		AppearanceSettings.setSliders(1, MAX_NUMBER_OF_TEAMS, 1, 1, AppearanceConstants.fontSmallest, slider);
		slider.setSnapToTicks(true);
		slider.setPreferredSize(new Dimension(800, 50));
		startGameButton.setEnabled(false);

		createMainPanel();

		setBackground(AppearanceConstants.darkBlue);
		add(mainPanel, BorderLayout.CENTER);
		setSize(800, 825);
	}

	//creates the section of the start window with the intro labels, and the number of teams slider and label
	private JPanel createNorthPanel()
	{
		//initialize local variables

		JLabel welcomeLabel = new JLabel("Welcome to Jeopardy!");
		JLabel explainContentLabel = new JLabel(
				"Choose the game file, number of teams, and team names before starting the game.");
		JLabel numberOfTeamsLabel = new JLabel(
				"Please choose the number of teams that will be playing on the slider below.");
		JLabel chooseGameFileLabel = new JLabel("Please choose a game file.");

		JPanel northPanel = new JPanel();
		JPanel numberOfTeamsPanel = new JPanel();
		JPanel fileChooserPanel = new JPanel();
		JPanel welcomePanel = new JPanel(new BorderLayout());
		JPanel titlePanel = new JPanel(new BorderLayout());

		//set appearances on local variables
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, welcomeLabel, explainContentLabel, welcomePanel,
				titlePanel);
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, chooseGameFileLabel, numberOfTeamsLabel,
				numberOfTeamsPanel, fileChooserPanel);
		AppearanceSettings.setForeground(Color.lightGray, chooseGameFileLabel, numberOfTeamsLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, explainContentLabel, chooseGameFileLabel,
				numberOfTeamsLabel);
		AppearanceSettings.setTextAlignment(welcomeLabel, explainContentLabel, chooseGameFileLabel, numberOfTeamsLabel);
		AppearanceSettings.setSize(800, 80, fileChooserPanel, welcomePanel, explainContentLabel);

		welcomeLabel.setFont(AppearanceConstants.fontLarge);
		numberOfTeamsPanel.setPreferredSize(new Dimension(800, 100));

		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));

		//add components to containers
		welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
		welcomePanel.add(quickPlay, BorderLayout.EAST);

		titlePanel.add(welcomePanel, BorderLayout.NORTH);
		titlePanel.add(explainContentLabel, BorderLayout.SOUTH);

		numberOfTeamsPanel.add(numberOfTeamsLabel);
		numberOfTeamsPanel.add(slider);

		fileChooserPanel.add(chooseGameFileLabel);
		fileChooserPanel.add(fileChooserButton);
		fileChooserPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		fileChooserPanel.add(fileNameLabel);
		fileChooserPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		fileChooserPanel.add(ratingLabel);

		northPanel.add(titlePanel);
		northPanel.add(numberOfTeamsPanel);
		northPanel.add(fileChooserPanel);

		return northPanel;
	}

	//creates a panel with the labels and text fields for choosing team names
	private JPanel createCenterPanel()
	{

		JPanel teamNamesPanel = new JPanel();

		teamNamesPanel.setLayout(new GridLayout(2, 2));
		teamNamesPanel.setBackground(AppearanceConstants.darkBlue);

		for (int i = 0; i < MAX_NUMBER_OF_TEAMS; i++)
		{
			//creating a panel that hols the label and text field for each team name
			JPanel tempPanel = new JPanel();
			tempPanel.setBackground(AppearanceConstants.darkBlue);
			tempPanel.setPreferredSize(new Dimension(300, 100));
			AppearanceSettings.setSize(300, 50, teamNameTextFields.get(i), teamNameLabels.get(i));
			tempPanel.add(teamNameLabels.get(i));
			tempPanel.add(teamNameTextFields.get(i));
			//adding the panel to the grid layout
			teamNamesPanel.add(tempPanel);
		}

		return teamNamesPanel;
	}

	//button panel
	private JPanel createSouthPanel()
	{
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(AppearanceConstants.darkBlue);
		buttonPanel.setPreferredSize(new Dimension(800, 100));
		buttonPanel.add(startGameButton);
		buttonPanel.add(clearDataButton);
		buttonPanel.add(logoutButton);
		buttonPanel.add(exitButton);

		return buttonPanel;
	}

	private void createMainPanel()
	{

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(createNorthPanel(), BorderLayout.NORTH);
		mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
		mainPanel.add(createSouthPanel(), BorderLayout.SOUTH);
	}

	//determines whether the chosen file is valid
	private void setHaveValidFile(File file)
	{

		//if they had already chosen a valid file, but want to replace it, need to clear stored data
		if (haveValidFile)
		{
			gameData.clearData();
		}

		try
		{
			//try parsing this file; the parseFile method could throw an exception here, in which case we know it was invalid
			gameData.parseFile(file.getAbsolutePath());
			haveValidFile = true;
			fileNameLabel.setText(file.getName());
			ratingLabel.setText("average rating: " + gameData.getRating());
			//check if the user can start the game
			startGameButton.setEnabled(haveValidFile && haveNames());
		}

		catch (Exception e)
		{
			haveValidFile = false;
			startGameButton.setEnabled(false);
			fileNameLabel.setText("");
			//pop up with error message
			JOptionPane.showMessageDialog(this, e.getMessage(), "File Reading Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void addListeners()
	{

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new ExitWindowListener(this));

		//adding a document listener to each text field. This will allow us to determine if the user has entered team names
		for (int i = 0; i < MAX_NUMBER_OF_TEAMS; i++)
		{
			teamNameTextFields.get(i).getDocument().addDocumentListener(new MyDocumentListener());
		}

		fileChooserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				fileChooser.showOpenDialog(StartWindowGUI.this);
				File file = fileChooser.getSelectedFile();

				if (file != null)
					setHaveValidFile(file);
			}

		});

		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e)
			{

				//sets appropriate text fields and labels invisible
				for (int i = slider.getValue(); i < MAX_NUMBER_OF_TEAMS; i++)
				{
					teamNameTextFields.get(i).setVisible(false);
					teamNameLabels.get(i).setVisible(false);
				}
				//sets appropriate text fields and labels visible
				for (int i = 0; i < slider.getValue(); i++)
				{
					teamNameTextFields.get(i).setVisible(true);
					teamNameLabels.get(i).setVisible(true);
				}
				//check if the user can start the game
				startGameButton.setEnabled(haveNames() && haveValidFile);
			}

		});

		startGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{

				numberOfTeams = slider.getValue();
				List<String> teamNames = new ArrayList<>(numberOfTeams);
				//getting the text in each of the visible text fields and storing them in a list
				for (int i = 0; i < numberOfTeams; i++)
				{
					teamNames.add(teamNameTextFields.get(i).getText());
				}
				//initializing TeamGUIComponents objects
				gameData.setTeams(teamNames, numberOfTeams);
				gameData.setNumberOfQuestions(quickPlay.isSelected() ? 5 : 25);

				MainGUI mainGUI = new MainGUI(gameData);
				mainGUI.setVisible(true);
				dispose();

			}

		});

		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
				System.exit(0);

			}

		});

		logoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
				LoginGUI loginGUI = new LoginGUI();
				loginGUI.setVisible(true);
				//Doing it this way may use too many resources. All instances still exist. Maybe pass instances to each other?
				//Should LoginGUI be static?
			}

		});

		clearDataButton.addActionListener(new ActionListener() {

			//reseting all data
			@Override
			public void actionPerformed(ActionEvent e)
			{
				haveNames = false;
				haveValidFile = false;
				gameData.clearData();
				quickPlay.setSelected(false);
				//start index at 1, we still was to show the 0th elements (team 1)
				for (int i = 1; i < MAX_NUMBER_OF_TEAMS; i++)
				{
					teamNameLabels.get(i).setVisible(false);
					teamNameTextFields.get(i).setText("");
					teamNameTextFields.get(i).setVisible(false);
				}

				startGameButton.setEnabled(false);
				teamNameTextFields.get(0).setText("");
				slider.setValue(1);
				fileNameLabel.setText("");
			}

		});

	}

	//updates and returns the haveNames member variable of whether all teams have been named
	private boolean haveNames()
	{

		haveNames = true;
		//check to see if all relevant text fields have text in them
		for (int i = 0; i < slider.getValue(); i++)
		{

			if (teamNameTextFields.get(i).getText().trim().equals(""))
			{
				haveNames = false;
			}
		}

		return haveNames;
	}

	//document listener; in each method, simply checking whether the user can start the game
	private class MyDocumentListener implements DocumentListener
	{

		@Override
		public void insertUpdate(DocumentEvent e)
		{
			startGameButton.setEnabled(haveNames() && haveValidFile);
		}

		@Override
		public void removeUpdate(DocumentEvent e)
		{
			startGameButton.setEnabled(haveNames() && haveValidFile);
		}

		@Override
		public void changedUpdate(DocumentEvent e)
		{
			startGameButton.setEnabled(haveNames() && haveValidFile);
		}
	}
}
