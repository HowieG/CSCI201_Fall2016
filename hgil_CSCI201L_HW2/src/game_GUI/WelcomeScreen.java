package game_GUI;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import game_logic.GameData;
import game_logic.GamePlay;
import main.Main;

public class WelcomeScreen extends JFrame implements ActionListener, ChangeListener, DocumentListener {

	public static final long serialVersionUID = 1;
	static private final String newline = "\n";

	static WelcomeScreen welcomeScreen;

	String gameFile;

	JFileChooser fc;

	static JButton chooseFileButton, startJeopardyButton, clearChoicesButton, exitButton;
	static JLabel textFileLabel;
	static JSlider numTeamsSlider;

	int numTeams = 1;
	static String[] teamNames = new String[4];
	// declares an array of JPanels
	static JPanel[] quarterPanels = new JPanel[4];
	// declares an array of JTextFields
	static JTextField[] teamNameTextFields = new JTextField[4];

	public WelcomeScreen() {
		super("Jeopardy!!");
		createGUI();

		fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		fc.setFileFilter(filter);
		fc.setMultiSelectionEnabled(false);
		addEvents();

	}

	protected void addEvents() {
		chooseFileButton.addActionListener(this);
		startJeopardyButton.addActionListener(this);
		clearChoicesButton.addActionListener(this);
		exitButton.addActionListener(this);
		numTeamsSlider.addChangeListener(this);
		teamNameTextFields[0].getDocument().addDocumentListener(this);
		teamNameTextFields[1].getDocument().addDocumentListener(this);
		teamNameTextFields[2].getDocument().addDocumentListener(this);
		teamNameTextFields[3].getDocument().addDocumentListener(this);

	}

	public static void addComponentsToPane(Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.setBackground(InitializeComponents.darkBlue);

		JPanel panel1 = InitializeComponents.addAPanel(pane, InitializeComponents.lightBlue);
		JLabel label1 = InitializeComponents.addALabel("Welcome to Jeopardy!", panel1, InitializeComponents.lightBlue);
		label1.setFont(new Font("Serif", Font.BOLD, 30));
		JPanel panel2 = InitializeComponents.addAPanel(pane, InitializeComponents.lightBlue);
		JLabel label2 = InitializeComponents.addALabel(
				"Choose the game file, number of teams, and team names before starting the game.", panel2,
				InitializeComponents.lightBlue);
		label2.setFont(new Font("Serif", Font.BOLD, 18));

		// FIX: ALIGN LEFT
		JPanel panel3 = InitializeComponents.addAPanel(pane);
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
		panel3.setBorder(new EmptyBorder(10, 10, 10, 10));
		InitializeComponents.addALabel("Please choose a game file.", panel3);
		panel3.add(Box.createRigidArea(new Dimension(40, 0)));
		chooseFileButton = InitializeComponents.addAButton("Choose File", panel3);
		// chooseFileButton.addActionListener(this);
		chooseFileButton.setBorder(new EmptyBorder(10, 35, 10, 35));
		panel3.add(Box.createRigidArea(new Dimension(40, 0)));

		textFileLabel = InitializeComponents.addALabel("", panel3);

		InitializeComponents.addALabel("Please choose the number of teams that will be playing on the slider below.",
				pane);

		numTeamsSlider = InitializeComponents.addSlider(4, pane);

		addMiddlePanel(pane);
		addBottomPanel(pane);

	}

	public static JPanel addTeamNamingBox(Container pane, GridBagConstraints c, int gridx, int gridy, int teamNumber) {

		JPanel quarterPanel = new JPanel();
		quarterPanel.setLayout(new BoxLayout(quarterPanel, BoxLayout.Y_AXIS));
		quarterPanel.setOpaque(false);
		quarterPanel.setBorder(new EmptyBorder(10, 30, 50, 30));

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = gridx;
		c.gridy = gridy;
		pane.add(quarterPanel, c);

		JLabel teamNameLabel = InitializeComponents.addALabel("Please name Team " + teamNumber, quarterPanel,
				InitializeComponents.darkGray);
		teamNameLabel.setBorder(new EmptyBorder(20, 50, 20, 50));
		teamNameLabel.setFont(new Font("Serif", Font.BOLD, 20));
		// label.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		teamNameLabel.setOpaque(true);

		quarterPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		teamNameTextFields[teamNumber - 1] = InitializeComponents.addATextField(quarterPanel,
				InitializeComponents.lightBlue);
		teamNameTextFields[teamNumber - 1].setBorder(new EmptyBorder(15, 5, 15, 5));
		teamNameTextFields[teamNumber - 1].setFont(new Font("Serif", Font.BOLD, 22));
		teamNameTextFields[teamNumber - 1].setMaximumSize(new Dimension(275, 60));

		return quarterPanel;
	}

	public static void addMiddlePanel(Container pane) {

		JPanel middlePanel = new JPanel(new GridBagLayout());
		middlePanel.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();

		for (int i = 1; i <= 4; i++) {

			// adds each quarter panel
			// (i + 1) % 2 returns alternating 0101
			// i / 3 returns 0011

			quarterPanels[i - 1] = addTeamNamingBox(middlePanel, c, (i + 1) % 2, i / 3, i);
		}

		pane.add(middlePanel);
		resetMiddlePanel(1);
	}

	public static void addBottomPanel(Container pane) {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(new EmptyBorder(15, 10, 50, 10));
		bottomPanel.setOpaque(false);

		startJeopardyButton = InitializeComponents.addAButton("Start Jeopardy", bottomPanel);
		startJeopardyButton.setBorder(new EmptyBorder(20, 50, 20, 50));
		startJeopardyButton.setPreferredSize(new Dimension(250, 30));
		startJeopardyButton.setEnabled(false);

		bottomPanel.add(Box.createRigidArea(new Dimension(2, 0)));

		clearChoicesButton = InitializeComponents.addAButton("Clear Choices", bottomPanel);
		clearChoicesButton.setBorder(new EmptyBorder(20, 50, 20, 50));
		clearChoicesButton.setPreferredSize(new Dimension(250, 30));

		bottomPanel.add(Box.createRigidArea(new Dimension(2, 0)));

		exitButton = InitializeComponents.addAButton("Exit", bottomPanel);
		exitButton.setBorder(new EmptyBorder(20, 50, 20, 50));
		exitButton.setPreferredSize(new Dimension(250, 30));

		pane.add(bottomPanel);
	}

	protected void createGUI() {
		setSize(800, 825);

		int screenWidth = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();

		setLocation((screenWidth - 800) / 2, 0);
		addComponentsToPane(getContentPane());
		setVisible(true);
	}

	public static void showFileErrorMessage(String displayText) {
		JFrame dialogFrame = new JFrame("Alert");
		JOptionPane.showMessageDialog(dialogFrame, "Your file could not be opened for the following reason:" + "\n \n"
				+ displayText + "\n \n" + "Please select another file.", "File Error", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showDialogBox(String displayText, String displayTitle) {
		JFrame dialogFrame = new JFrame("Alert");
		JOptionPane.showMessageDialog(dialogFrame, displayText, displayTitle, JOptionPane.INFORMATION_MESSAGE);
	}

	private static void resetMiddlePanel(int numTeams) {
		for (int i = 0; i < 4; i++) {
			quarterPanels[i].setVisible(false);
		}

		for (int i = 0; i < numTeams; i++) {
			quarterPanels[i].setVisible(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// Handle open button action.
		if (e.getSource() == chooseFileButton) {

			int returnVal = fc.showOpenDialog(WelcomeScreen.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				GameData gameData = new GameData("data/" + file.getName());
				textFileLabel.setText(file.getName());
				gameFile = file.getName();
			}
		} else if (e.getSource() == startJeopardyButton) {

			if (gameFile != null && !gameFile.isEmpty()) {
				setTeamNames(numTeams);

				boolean gameShouldBegin = GamePlay.findDuplicateTeamNames(teamNames, numTeams);

				if (gameShouldBegin) {
					setVis(false);
					// this.dispatchEvent(new WindowEvent(this,
					// WindowEvent.WINDOW_CLOSING));
					Main.main(gameFile, teamNames, numTeams);
				}
			} else {
				showDialogBox("You must choose a game file.", "Error: No Game File");
			}
			// call main?
		} else if (e.getSource() == clearChoicesButton) {
			setNumTeams(1);
			// empties the teamNames array by assigning it to a new array
			teamNames = new String[4];
			resetMiddlePanel(numTeams);
			for (int i = 0; i < 4; i++) {
				teamNameTextFields[i].setText("");
			}
			numTeamsSlider.setValue(1);
		} else if (e.getSource() == exitButton) {
			System.exit(0);
		}
	}

	public void setTeamNames(int numTeams) {
		for (int i = 0; i < numTeams; i++) {
			teamNames[i] = teamNameTextFields[i].getText();
		}
	}

	@Override
	/** Listen to the slider. */
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (source.getValueIsAdjusting()) {
			setNumTeams(source.getValue());
			resetMiddlePanel(numTeams);
			textFieldChange();
		}
	}

	public void setNumTeams(int numTeams) {
		this.numTeams = numTeams;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		textFieldChange();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		textFieldChange();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		textFieldChange();
	}

	// checks all text fields and enables the Start Jeopardy button once all
	// teams have entered names
	public void textFieldChange() {

		int numEmptyTextFields = numTeams;

		for (int i = 0; i < numTeams; i++) {
			if (!teamNameTextFields[i].getText().equals("")) {
				numEmptyTextFields--;
			}
		}

		if (numEmptyTextFields == 0) {
			startJeopardyButton.setEnabled(true);
		} else {
			startJeopardyButton.setEnabled(false);
		}

	}

	public static void setVis(boolean visible) {
		welcomeScreen.setVisible(visible);
	}

	public static void main(String[] args) {
		welcomeScreen = new WelcomeScreen();
		welcomeScreen.setVisible(true);
	}

}
