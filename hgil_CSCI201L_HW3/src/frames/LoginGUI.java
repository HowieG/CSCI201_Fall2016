package frames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import game_logic.Serialization;
import game_logic.Users;
import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;
import other_gui.TextPrompt;
import other_gui.TextPrompt.Show;

public class LoginGUI extends JFrame
{
	JPanel mainPanel;
	JLabel instructionLabel, jeopardyLabel, alertLabel;
	JTextField usernameTF, passwordTF;
	JButton loginButton, createAccountButton;

	private boolean textEntered = false;
	private Users users = new Users();
	private String filename = "UserData.txt";

	public LoginGUI()
	{
		super("Login");

		deserializeUserData();
		initializeComponents();
		createGUI();
		addListeners();
	}

	private void initializeComponents()
	{
		mainPanel = new JPanel();
		instructionLabel = new JLabel("login or create an account to play");
		jeopardyLabel = new JLabel("Jeopardy!");
		alertLabel = new JLabel("");
		usernameTF = new JTextField();
		passwordTF = new JTextField();
		loginButton = new JButton("Login");
		createAccountButton = new JButton("Create Account");

		loginButton.setEnabled(false);
		createAccountButton.setEnabled(false);

		TextPrompt usernamePrompt = new TextPrompt("username", usernameTF);
		usernamePrompt.setForeground(Color.lightGray);
		usernamePrompt.changeAlpha(0.5f);
		usernamePrompt.setShow(Show.FOCUS_LOST);
		usernamePrompt.changeStyle(Font.BOLD);

		TextPrompt passwordPrompt = new TextPrompt("password", passwordTF);
		passwordPrompt.setForeground(Color.lightGray);
		passwordPrompt.changeAlpha(0.5f);
		passwordPrompt.setShow(Show.FOCUS_LOST);
		passwordPrompt.changeStyle(Font.BOLD);
	}

	private void createGUI()
	{
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, mainPanel);
		AppearanceSettings.setBackground(Color.darkGray, loginButton, createAccountButton);
		AppearanceSettings.setFont(AppearanceConstants.fontSmallest, instructionLabel, alertLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, jeopardyLabel);
		AppearanceSettings.setForeground(Color.lightGray, loginButton, createAccountButton);
		AppearanceSettings.setOpaque(loginButton, createAccountButton);
		AppearanceSettings.setSize(140, 50, loginButton, createAccountButton);
		AppearanceSettings.setSize(250, 50, usernameTF, passwordTF);
		AppearanceSettings.unSetBorderOnButtons(loginButton, createAccountButton);

		createMainPanel();
		add(mainPanel);
		setSize(300, 300);
	}

	//FIX: switch to gridbaglayout
	private void createMainPanel()
	{
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(AppearanceSettings.wrapInJPanelContainer(instructionLabel));
		mainPanel.add(AppearanceSettings.wrapInJPanelContainer(jeopardyLabel));
		mainPanel.add(AppearanceSettings.wrapInJPanelContainer(alertLabel));
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(AppearanceSettings.wrapInJPanelContainer(usernameTF));
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(AppearanceSettings.wrapInJPanelContainer(passwordTF));
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(createButtons());
	}

	private JPanel createButtons()
	{
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.add(loginButton);
		buttonPanel.add(createAccountButton);

		return buttonPanel;
	}

	private void addListeners()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		usernameTF.getDocument().addDocumentListener(new MyDocumentListener());
		passwordTF.getDocument().addDocumentListener(new MyDocumentListener());

		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				login();
			}

		});

		createAccountButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				createAccount();
			}

		});

	}

	private void deserializeUserData()
	{
		File file = new File(filename); //pathname?
		if (file.exists() && !file.isDirectory())
		{
			log("file exists");

			BufferedReader br;
			try
			{
				br = new BufferedReader(new FileReader(file));
				if (br.readLine() == null)
				{
					log("file is empty");
				}
				else
				{
					try
					{
						users = (Users) Serialization.deserialize(filename);
					}
					catch (ClassNotFoundException cnfe)
					{
						log("cnfe: " + cnfe.getMessage());
						cnfe.printStackTrace();
					}
					catch (IOException ioe)
					{
						log("ioe: " + ioe.getMessage());
						ioe.printStackTrace();
					}
				}
			}
			catch (FileNotFoundException fnfe)
			{
				log("fnfe: " + fnfe.getMessage());
				fnfe.printStackTrace();
			}
			catch (IOException ioe)
			{
				log("ioe: " + ioe.getMessage());
				ioe.printStackTrace();
			}
		}
		else
		{
			log("File does not exist! Making the file");
			//make the file
			//			try
			//			{
			//				final String dir = System.getProperty("user.dir");
			//				System.out.println("current dir = " + dir);
			//				PrintWriter writer = new PrintWriter("UserData.txt", "UTF-8");
			//				writer.close();
			//			}
			//			catch (FileNotFoundException | UnsupportedEncodingException e)
			//			{
			//				e.printStackTrace();
			//			}
		}

	}

	private void login()
	{
		log("logging in");
		if (users.checkPassword(usernameTF.getText().trim(), passwordTF.getText()))
		{
			showStartWindow();
		}
		else
		{
			alertLabel.setText("this password and username combination does not exist");
			passwordTF.setText("");
		}
	}

	private void createAccount()
	{
		log("creating account");
		if (users.containsUser(usernameTF.getText()))
		{
			alertLabel.setText("this username already exists");
			usernameTF.setText("");
			passwordTF.setText("");
		}
		else
		{
			//add user
			users.addUser(usernameTF.getText().trim(), passwordTF.getText());
			//serialize
			try
			{
				Serialization.serialize(users, filename);
			}
			catch (IOException ioe)
			{
				log("ioe: " + ioe.getMessage());
				ioe.printStackTrace();
			}
			showStartWindow();
		}
	}

	private void showStartWindow()
	{
		//close login window
		setVisible(false);
		//show start window
		StartWindowGUI startWindow = new StartWindowGUI();
		//set it visible
		startWindow.setVisible(true);
	}

	private void log(String message)
	{
		System.out.println(message);
	}

	private boolean enteredText()
	{
		textEntered = false;

		//check to see if both text fields have text in them
		boolean usernameEntered = !(usernameTF.getText().trim().equals(""));
		boolean passwordEntered = !(passwordTF.getText().trim().equals(""));

		if (usernameEntered && passwordEntered)
		{
			textEntered = true;
		}

		return textEntered;
	}

	private class MyDocumentListener implements DocumentListener
	{

		@Override
		public void insertUpdate(DocumentEvent e)
		{
			loginButton.setEnabled(enteredText());
			createAccountButton.setEnabled(enteredText());
		}

		@Override
		public void removeUpdate(DocumentEvent e)
		{
			loginButton.setEnabled(enteredText());
			createAccountButton.setEnabled(enteredText());
		}

		@Override
		public void changedUpdate(DocumentEvent e)
		{
			loginButton.setEnabled(enteredText());
			createAccountButton.setEnabled(enteredText());
		}
	}

}
