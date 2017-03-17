package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import game_logic.Category;
import game_logic.GameData;
import listeners.ExitWindowListener;
import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;
import other_gui.QuestionGUIElement;
import other_gui.TeamGUIComponents;

public class MainGUI extends JFrame
{

	private JPanel mainPanel;
	private JPanel currentPanel;

	private JPanel questionsPanel;
	private GameData gameData;
	private JButton[][] questionButtons;

	private static final int QUESTIONS_LENGTH_AND_WIDTH = 5;

	private JTextArea updatesTextArea;
	private JMenuBar menuBar;
	private JMenu menu;

	private JMenuItem exitButton;
	private JMenuItem logoutButton;
	private JMenuItem restartThisGameButton;
	private JMenuItem chooseNewGameFileButton;
	private JScrollPane updatesScrollPane;

	public MainGUI(GameData gameData)
	{

		super("Jeopardy!!");
		this.gameData = gameData;

		initializeComponents();
		createGUI();
		addListeners();

		setVisible(true);
	}

	// public methods

	public void addUpdate(String update)
	{
		updatesTextArea.append("\n" + update);
		JScrollBar vertical = updatesScrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}

	// this method changes the current panel to the provided panel
	public void changePanel(JPanel panel)
	{
		remove(currentPanel);
		currentPanel = panel;
		add(currentPanel, BorderLayout.CENTER);
		// must repaint or the change won't show
		repaint();
	}

	public void showMainPanel()
	{
		changePanel(mainPanel);
	}

	// private methods
	private void initializeComponents()
	{
		mainPanel = new JPanel();
		currentPanel = mainPanel;
		exitButton = new JMenuItem("Exit Game");
		logoutButton = new JMenuItem("Logout");
		restartThisGameButton = new JMenuItem("Restart This Game");
		chooseNewGameFileButton = new JMenuItem("Choose New Game File");
		updatesTextArea = new JTextArea();
		menu = new JMenu("Menu");
		questionButtons = new JButton[QUESTIONS_LENGTH_AND_WIDTH][QUESTIONS_LENGTH_AND_WIDTH];
		menuBar = new JMenuBar();
		questionsPanel = new JPanel(new GridLayout(QUESTIONS_LENGTH_AND_WIDTH, QUESTIONS_LENGTH_AND_WIDTH));

	}

	private void createGUI()
	{
		createMenu();
		createMainPanel();

		add(mainPanel, BorderLayout.CENTER);
		add(createProgressPanel(), BorderLayout.EAST);
		setSize(1500, 825);
	}

	// creating the JMenuBar
	private void createMenu()
	{

		menu.add(restartThisGameButton);
		menu.add(chooseNewGameFileButton);
		menu.add(exitButton);
		menu.add(logoutButton);
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	// creating the main panel (the game board)
	private void createMainPanel()
	{
		mainPanel.setLayout(new BorderLayout());

		// getting the panel that holds the 'jeopardy' label
		JPanel jeopardyPanel = createJeopardyPanel();
		// getting the cateogries panel
		JPanel categoriesPanel = createCategoriesAndQuestionsPanels();
		JPanel northPanel = new JPanel();

		northPanel.setLayout(new BorderLayout());
		northPanel.add(jeopardyPanel, BorderLayout.NORTH);
		northPanel.add(categoriesPanel, BorderLayout.SOUTH);

		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(questionsPanel, BorderLayout.CENTER);
	}

	// creates the panel with the jeopardy label
	private JPanel createJeopardyPanel()
	{
		JPanel jeopardyPanel = new JPanel();
		JLabel jeopardyLabel = new JLabel("Jeopardy");
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, jeopardyPanel, jeopardyLabel);
		jeopardyLabel.setFont(AppearanceConstants.fontLarge);
		jeopardyPanel.add(jeopardyLabel);

		return jeopardyPanel;
	}

	// creates both the categories panel and the questions panel
	private JPanel createCategoriesAndQuestionsPanels()
	{
		JPanel categoriesPanel = new JPanel(new GridLayout(1, QUESTIONS_LENGTH_AND_WIDTH));
		AppearanceSettings.setBackground(Color.darkGray, categoriesPanel, questionsPanel);

		Map<String, Category> categories = gameData.getCategories();

		JLabel[] categoryLabels = new JLabel[QUESTIONS_LENGTH_AND_WIDTH];

		// iterate through the map of categories, and place them in the correct
		// index
		for (Map.Entry<String, Category> category : categories.entrySet())
		{
			categoryLabels[category.getValue().getIndex()] = category.getValue().getCategoryLabel();
		}

		// place the question buttons in the proper indices in 'questionButtons'
		for (QuestionGUIElement question : gameData.getQuestions())
		{
			// adding action listeners to the question
			question.addActionListeners(this, gameData);
			questionButtons[question.getX()][question.getY()] = question.getGameBoardButton();
		}

		// actually adding the categories and questions into their respective
		// panels
		for (int i = 0; i < QUESTIONS_LENGTH_AND_WIDTH; i++)
		{
			categoryLabels[i].setPreferredSize(new Dimension(100, 70));
			categoriesPanel.add(categoryLabels[i]);

			for (int j = 0; j < QUESTIONS_LENGTH_AND_WIDTH; j++)
			{
				questionsPanel.add(questionButtons[j][i]);
			}
		}

		return categoriesPanel;
	}

	// creates the panel with the team points, and the Game Progress area
	private JPanel createProgressPanel()
	{
		// create panels
		JPanel pointsPanel = new JPanel(new GridLayout(gameData.getNumberOfTeams(), 2));
		JPanel southEastPanel = new JPanel(new BorderLayout());
		JPanel eastPanel = new JPanel();
		// other local variables
		JLabel updatesLabel = new JLabel("Game Progress");
		updatesScrollPane = new JScrollPane(updatesTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// setting appearances
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, southEastPanel, updatesLabel, updatesScrollPane,
				updatesTextArea);
		AppearanceSettings.setSize(400, 400, pointsPanel, updatesScrollPane);
		AppearanceSettings.setTextComponents(updatesTextArea);

		updatesLabel.setFont(AppearanceConstants.fontLarge);
		pointsPanel.setBackground(Color.darkGray);
		updatesLabel.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		updatesScrollPane.setBorder(null);

		updatesTextArea.setText("Welcome to Jeopardy!");
		updatesTextArea.setFont(AppearanceConstants.fontSmall);
		updatesTextArea.append("\n" + "The team to go first will be " + gameData.getCurrentTeam().getTeamName());

		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS));
		// adding components/containers
		southEastPanel.add(updatesLabel, BorderLayout.NORTH);
		southEastPanel.add(updatesScrollPane, BorderLayout.CENTER);

		// adding team labels, which are stored in the TeamGUIComponents class,
		// to the appropriate panel
		for (int i = 0; i < gameData.getNumberOfTeams(); i++)
		{
			TeamGUIComponents team = gameData.getTeamDataList().get(i);
			pointsPanel.add(team.getMainTeamNameLabel());
			pointsPanel.add(team.getTotalPointsLabel());
		}

		eastPanel.add(pointsPanel);
		eastPanel.add(southEastPanel);

		return eastPanel;
	}

	// adding even listeners
	private void addListeners()
	{

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new ExitWindowListener(this));

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

		restartThisGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				updatesTextArea.setText("Game has been restarted.");
				// reset all data
				gameData.restartGame();
				// repaint the board to show updated data
				showMainPanel();
				repaint();
				updatesTextArea.append("The team to go first will be " + gameData.getCurrentTeam().getTeamName());
			}

		});

		chooseNewGameFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
				new StartWindowGUI().setVisible(true);
			}

		});
	}

	@Override
	protected void processWindowEvent(WindowEvent e)
	{
		// TODO Auto-generated method stub
		super.processWindowEvent(e);
	}

	//	public MainGUI()
	//	{
	//		fastTrackForTesting();
	//	}

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
	//		new MainGUI(gameData);
	//	}

	//	public static void main(String[] args)
	//	{
	//		new MainGUI();
	//	}

}
