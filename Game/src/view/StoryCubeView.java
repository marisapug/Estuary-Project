package view;

import model.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * StoryCubeView contains all of the display information for the dice game, including the start screen, 
 * animation timer, images of dice, slots to drag the dice into, JTextPanes, JTextArea, buttons, 
 * button listener, and mouse listener. 
 * 
 * @author Natalie
 *
 */

public class StoryCubeView extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	DiceGame dgame;

	// Timer
	Timer diceTimer = new Timer(50, this);

	// Screen
	private int screenWidth = MainFrame.getFrameWidth();
	private int screenHeight = MainFrame.getFrameHeight();

	// Dice
	private Die[] gameDice;
	private int diceWidth = screenWidth / 10;
	private Die selectedDie;
	private int dicePlaced = 0;

	// Story Slots
	private int[] storyboardX;
	private int storyStartX;
	private int storyStartY;
	private int betweenStory = screenWidth / 120;

	// Dice Rolling Animation
	int numAnimations = 0;
	int animsToDo = 30;
	boolean isAnimDone = false;

	// Game Booleans
	private boolean isRolled = false;
	private boolean isDieSelected = false;
	private boolean canSaveStory = true;
	private boolean isDialogUp = false;

	// Buttons
	private JButton startGameButton;
	private JButton rollDiceButton;
	private JButton helpButton;
	private JButton storyButton;
	private JButton rollAgainButton;
	private JButton goBackButton;

	// Button Appearance
	private int buttonFontSize = screenWidth / 50;
	private Font buttonFont;
	private int startButtonSizeX = screenWidth / 5;
	private int startButtonSizeY = screenWidth / 25;
	private Dimension startButtonSize;
	private int buttonSizeX = screenWidth / 10;
	private int buttonSizeY = screenWidth / 25;
	private Dimension buttonSize;
	private boolean showStoryButton;
	private Color helpButtonColor;

	// Story
	private Font errorTextStyle;
	Color errorTextColor;
	private int errorFontSize = screenWidth / 40;
	private boolean isStorySaved = false;
	private int storyTextX;
	private int storyTextY;
	private ArrayList<String> storyWords = new ArrayList<String>();
	private ArrayList<String> storyLines = new ArrayList<String>();
	private int numLines = 0;
	private int maxLines = 7; // maximum number of lines the text box will fit
	private int numCharsOnLine;
	private int[] stringYCoords = new int[maxLines];
	private boolean isStoryShowing = false;
	private int diceLength;
	private int diceSpacePixels;
	private int diceSpaceWidth;
	private String diceSpaceString;

	// TextArea
	private JTextArea storyText;
	private String storyString;
	private boolean shouldEraseStory;

	// Text Panes
	private JTextPane storyPane;
	private Color storyBackground;
	private Color storyTextColor;
	private int storyFontSize = screenWidth / 40;
	private Font storyTextStyle;

	private JTextPane instructPane;
	private Color instructTextColor;
	private Color instructBackground;
	private int instructFontSize;
	private Font instructFont;
	private Border instructBorder;

	private JTextPane badWordPane;
	private Color badWordTextColor;
	private int badWordFontSize;
	private Font badWordFont;
	private Border badWordBorder;
	private boolean isBadWordPaneMade;

	// Images
	BufferedImage oceanBackground = createImage("background/dicebackground.jpg");
	BufferedImage startScreen = createImage("background/dicestartscreen.jpg");
	private String imgStrings[] = { "diceimages/bananadie.png", "diceimages/bassdie.png", "diceimages/bluecrabdie.png",
			"diceimages/bogturtledie.png", "diceimages/candie.png", "diceimages/cattailsdie.png",
			"diceimages/chipbagdie.png", "diceimages/clamdie.png", "diceimages/cleanvesseldie.png",
			"diceimages/crabscientistdie.png", "diceimages/deadfishdie.png", "diceimages/fisheggsdie.png",
			"diceimages/fishermandie.png", "diceimages/fishgroupdie.png", "diceimages/mittencrabsdie.png",
			"diceimages/oilspilldie.png", "diceimages/seagrassdie.png", "diceimages/seamonsterdie.png",
			"diceimages/toxicclouddie.png", "diceimages/troutdie.png"};

	private BufferedImage[] possibleDiceImgs;

	// Start Screen
	private boolean isStartScreenVisible = true;
	private String gameTitle = "Estuary Story Cubes!";
	private String titleFont = "Arial Narrow";
	private Color titleFontColor = new Color(10, 159, 214);
	private int titleFontSize = 28;
	private int titleX = 100;
	private int titleY = 100;

	// Mouse Listener
	private DiceListener listener = new DiceListener();

	// Constructor	
	/**
	 * The constructor creates an instance of StoryCubeView and of DiceGame, and initializes the positions
	 * and appearance of the buttons, dice used in the game, text area for the user to type their story, text 
	 * pane to display the story at the end, text pane to show the game instructions, and text pane for the 
	 * pop-up if the user types a banned word.
	 */
	public StoryCubeView() {
		dgame = new DiceGame(screenWidth, screenHeight, diceWidth);

		storyStartY = (screenHeight - diceWidth) / 2;
		storyStartX = (screenWidth - (dgame.getNumDice() * diceWidth + (dgame.getNumDice() - 1) * betweenStory)) / 2;
		storyTextX = diceWidth + 30;
		storyTextY = diceWidth + 60;

		// Initialize Button Appearance
		buttonFont = new Font(titleFont, Font.BOLD, buttonFontSize);
		storyTextStyle = new Font("Verdana", Font.PLAIN, storyFontSize);
		errorTextStyle = new Font("Verdana", Font.PLAIN, errorFontSize);
		startButtonSize = new Dimension(startButtonSizeX, startButtonSizeY);
		buttonSize = new Dimension(buttonSizeX, buttonSizeY);
		showStoryButton = false;

		// Initialize Arrays
		possibleDiceImgs = new BufferedImage[dgame.getNumImgs()];
		storyboardX = new int[dgame.getNumDice()];
		gameDice = dgame.getDice();

		// Buttons
		startGameButton = new JButton("Start Game");
		startGameButton.setFocusable(false);
		startGameButton.setFont(buttonFont);
		startGameButton.setPreferredSize(startButtonSize);
		startGameButton.setBackground(Color.black);
		startGameButton.setOpaque(true);
		startGameButton.setBorderPainted(false);
		startGameButton.setForeground(Color.white);

		rollDiceButton = new JButton("Roll Dice");
		rollDiceButton.setFocusable(false);
		rollDiceButton.setVisible(false);
		rollDiceButton.setFont(buttonFont);
		rollDiceButton.setPreferredSize(startButtonSize);
		rollDiceButton.setBackground(Color.black);
		rollDiceButton.setOpaque(true);
		rollDiceButton.setBorderPainted(false);
		rollDiceButton.setForeground(Color.white);

		helpButton = new JButton("HELP");
		helpButton.setFocusable(false);
		helpButton.setVisible(false);
		helpButton.setFont(buttonFont);
		helpButton.setPreferredSize(startButtonSize);
		helpButtonColor = new Color(0, 169, 92);
		helpButton.setBackground(helpButtonColor);
		helpButton.setOpaque(true);
		helpButton.setBorderPainted(false);
		helpButton.setForeground(Color.white);

		storyButton = new JButton("Submit");
		storyButton.setFocusable(false);
		storyButton.setVisible(false);
		storyButton.setFont(buttonFont);
		storyButton.setPreferredSize(buttonSize);
		storyButton.setBackground(Color.black);
		storyButton.setOpaque(true);
		storyButton.setBorderPainted(false);
		storyButton.setForeground(Color.white);

		rollAgainButton = new JButton("Roll Again");
		rollAgainButton.setFocusable(false);
		rollAgainButton.setVisible(false);
		rollAgainButton.setFont(buttonFont);
		rollAgainButton.setPreferredSize(startButtonSize);
		rollAgainButton.setBackground(Color.black);
		rollAgainButton.setOpaque(true);
		rollAgainButton.setBorderPainted(false);
		rollAgainButton.setForeground(Color.white);

		goBackButton = new JButton("Go Back");
		goBackButton.setFocusable(false);
		goBackButton.setVisible(false);
		goBackButton.setFont(buttonFont);
		goBackButton.setPreferredSize(startButtonSize);
		goBackButton.setBackground(Color.black);
		goBackButton.setOpaque(true);
		goBackButton.setBorderPainted(false);
		goBackButton.setForeground(Color.white);


		// Text Fields
		storyText = new JTextArea("Enter Story Here");
		storyText.setLineWrap(true);
		storyText.setVisible(false);
		storyText.setPreferredSize(new Dimension(400, 96));
		storyText.setFont(new Font(titleFont, Font.PLAIN, screenWidth / 75));
		numCharsOnLine = screenWidth / 28;
		diceLength = diceWidth * dgame.numDice;
		diceSpacePixels = ((screenWidth - 2 * diceWidth) - diceLength) / 2 ;
		diceSpaceWidth = 0;
		diceSpaceString = "";
		shouldEraseStory = true;

		// Add Buttons
		this.add(startGameButton);
		this.add(rollDiceButton);
		this.add(helpButton);
		this.add(storyText);
		this.add(storyButton);
		this.add(rollAgainButton);
		this.add(goBackButton);
		this.setupListeners();

		// Final Text Display
		storyBackground = new Color(136, 191, 246);
		storyTextColor = new Color(3, 0, 130);
		storyPane = new JTextPane();
		this.add(storyPane);
		storyPane.setVisible(false);

		// Game Instructions
		instructBackground = new Color(194, 238, 231);
		instructTextColor = Color.BLACK;
		instructFontSize = screenWidth / 30;
		instructFont = new Font("Verdana", Font.PLAIN, instructFontSize);
		instructBorder = new LineBorder(Color.black, 1);
		instructPane = new JTextPane();
		this.add(instructPane);
		instructPane.setVisible(false);
		makeInstructionPane();

		// Bad Word Pop-Up Box
		badWordTextColor = new Color(124, 37, 41);
		badWordFontSize = screenWidth / 30;
		badWordFont = new Font("Verdana", Font.PLAIN, instructFontSize);
		badWordBorder = new LineBorder(badWordTextColor, 1);
		badWordPane = new JTextPane();
		this.add(badWordPane);
		badWordPane.setVisible(false);
		isBadWordPaneMade = false;


		initializeCoordinates();
	}

	// Getters
	public int getScreenWidth() {
		return this.screenWidth;
	}

	public int getScreenHeight() {
		return this.screenHeight;
	}

	/**
	 * Sets up the coordinates where the dice should end up on the screen.
	 */
	public void initializeCoordinates() { // change coordinates
		for (int i = 0; i < dgame.getNumDice(); i++) {
			storyboardX[i] = storyStartX + (diceWidth + betweenStory) * i;
		}
	}

	/**
	 * Creates BufferedImage type from file path to image file
	 * @param fileName file path
	 * @return img BufferedImage of image file
	 */
	public BufferedImage createImage(String fileName) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	/**
	 *  Creates all possible images to be chosen from
	 */
	public void makeImages() {
		if (!isRolled)
			dgame.setDice();
		for (int i = 0; i < dgame.getNumImgs(); i++) {
			possibleDiceImgs[i] = createImage(imgStrings[i]);
		}
	}

	/**
	 *  Sets image to each die, sets isRolled to true to signify that the rolling animation is complete
	 */
	public void setDiceImgs() {
		for (int i = 0; i < dgame.getNumDice(); i++) {
			int temp = dgame.imgNums[i];
			gameDice[i].setDieImg(possibleDiceImgs[temp]);
		}
		isRolled = true;
	}

	// paintComponent
	/**
	 * Handles all images and components that are drawn onto the screen during the dice game.
	 * @param g graphics
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isStartScreenVisible) {
			g.drawImage(startScreen, 0, 0, screenWidth, screenHeight, this);
			g.setFont(new Font(titleFont, Font.BOLD, titleFontSize));
			g.setColor(titleFontColor);
			g.drawString(gameTitle, titleX, titleY);

		} else {
			g.drawImage(oceanBackground, 0, 0, screenWidth, screenHeight, this); // draws

			// Dice
			if (!isStoryShowing) {
				for (int i = 0; i < dgame.getNumDice(); i++) {
					if (isRolled) {
						g.drawImage(gameDice[i].getDieImg(), gameDice[i].getXLoc(), gameDice[i].getYLoc(), diceWidth,
								diceWidth, this); // draws
						// images
						if (isAnimDone) {
							// slots to drag dice into
							g.drawRect(storyboardX[i], storyStartY, diceWidth, diceWidth); // draws
							if (showStoryButton) {
								storyButton.setVisible(true);
								storyText.setVisible(true);
							}
						}
					}
				}
				if (isStorySaved) {
					// Color storyBackground = new Color(136, 191, 246);

					// Story Text
					if (canSaveStory) {
						saveStory();
						diceSpaceWidth = g.getFontMetrics(storyTextStyle).stringWidth(diceSpaceString);
						while(diceSpaceWidth < diceSpacePixels){
							diceSpaceString += " ";
							diceSpaceWidth = g.getFontMetrics(storyTextStyle).stringWidth(diceSpaceString);
						}
						if(diceSpaceWidth >= diceSpacePixels){
							makeStory(storyPane, dgame.getDiceStory(), diceSpaceString, storyTextColor);
							isStoryShowing = true;
						}

					} else if(isDialogUp){
						//TODO change text to make bigger and better
						if(!isBadWordPaneMade){
							makeBadWordPane(dgame.getCurseWord());
							isBadWordPaneMade = true;
						}
						rollAgainButton.setVisible(false);
						goBackButton.setVisible(true);
						badWordPane.setVisible(true);
						isStoryShowing = false;
						//isRolled = false;
						storyPane.setVisible(false);
					}

				}
			}

		}
	}

	/**
	 * Calls makeImages to create all possible images, then calls animDice to start the dice animation,
	 * then sets isRolled to true once the animation is done
	 */
	public void rollDice() {
		makeImages();
		animDice();
		isRolled = true;
		repaint();
	}

	/**
	 *  Saves Story the user entered after searching it for banned words.
	 */
	void saveStory() {
		// if !iscurseword
		dgame.setDiceStory(storyText.getText());
		dgame.separateStory(dgame.diceStory);
		if (dgame.isCurseWord()) {
			canSaveStory = false;
			isDialogUp = true;
		} else{
			canSaveStory = true;
		}
		isStorySaved = true;
	}

	/**
	 *  Adds final story and ordered dice to Text Pane and sets up display settings such as colors, sizes, and 
	 *  fonts for text pane.
	 * @param pane JTextPane for displaying the final story
	 * @param text final story
	 * @param diceSpace string of spaces so that the dice are centered
	 * @param color text color
	 */
	public void makeStory(JTextPane pane, String text, String diceSpace, Color color) {
		StyledDocument doc = pane.getStyledDocument();

		pane.setBounds(diceWidth, diceWidth, screenWidth - 2 * diceWidth, (screenHeight - (2 * diceWidth)));
		pane.setPreferredSize(new Dimension(screenWidth - 2 * diceWidth, screenHeight - (2 * diceWidth)));
		pane.setFont(storyTextStyle);
		pane.setBackground(storyBackground);
		pane.setEditable(false);

		// add dice cubes to text pane
		ImageIcon[] sizedIcons = new ImageIcon[dgame.getNumDice()];
		for (Die d: gameDice){
			if(d.getStoryIndex() == 0)
				sizedIcons[0] = new ImageIcon(d.getDieImg().getScaledInstance(diceWidth, diceWidth, Image.SCALE_DEFAULT));
			else if(d.getStoryIndex() == 1)
				sizedIcons[1] = new ImageIcon(d.getDieImg().getScaledInstance(diceWidth, diceWidth, Image.SCALE_DEFAULT));
			else if(d.getStoryIndex() == 2)
				sizedIcons[2] = new ImageIcon(d.getDieImg().getScaledInstance(diceWidth, diceWidth, Image.SCALE_DEFAULT));
			else if(d.getStoryIndex() == 3)
				sizedIcons[3] = new ImageIcon(d.getDieImg().getScaledInstance(diceWidth, diceWidth, Image.SCALE_DEFAULT));
			else if(d.getStoryIndex() == 4)
				sizedIcons[4] = new ImageIcon(d.getDieImg().getScaledInstance(diceWidth, diceWidth, Image.SCALE_DEFAULT));
		}

		Style style = pane.addStyle("Color Style", null);
		StyleConstants.setForeground(style, color);
		try {
			doc.insertString(doc.getLength(), diceSpace, style);
			for (ImageIcon icon : sizedIcons) {
				pane.insertIcon(icon);
			}

			doc.insertString(doc.getLength(), "\n", style);
			doc.insertString(doc.getLength(), text, style);
		} 
		catch (BadLocationException e) {
			e.printStackTrace();
		} 
	}

	 /**
	  *  Set up Instructions Text Pane, setting size, font, text, and colors.
	  */
	public void makeInstructionPane() {
		StyledDocument doc = instructPane.getStyledDocument();

		instructPane.setBounds(diceWidth, diceWidth, (screenWidth - 2 * diceWidth) - (diceWidth / 2), (screenHeight - (2 * diceWidth)));
		instructPane.setPreferredSize(new Dimension(screenWidth - 2 * diceWidth, screenHeight - (2 * diceWidth)));
		instructPane.setFont(instructFont);
		instructPane.setBackground(instructBackground);
		instructPane.setBorder(instructBorder);
		instructPane.setEditable(false);

		Style style = instructPane.addStyle("Color Style", null);
		StyleConstants.setForeground(style, Color.black);
		StyleConstants.setBold(style, true);
		StyleConstants.setFontSize(style, screenWidth / 25);
		try {
			doc.insertString(doc.getLength(), "  Estuary Story Cubes Instructions", style);
			doc.insertString(doc.getLength(), "\n", style);
		} 
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		StyleConstants.setBold(style, false);
		StyleConstants.setFontSize(style, screenWidth / 35);
		try {
			doc.insertString(doc.getLength(), "\n  Roll the dice, then let the icons inspire you! Starting \n  with 'Once "
					+ "upon a time...', pick the icon that catches \n  your eye first. As you create a story that links "
					+ "all the \n  icons together, click and drag each die into a box in \n  the order they appear in your "
					+ "story. When you're all \n  done, type your estuary adventure story in the box!", style);	
		} 
		catch (BadLocationException e) {
			e.printStackTrace();
		} 
		StyleConstants.setFontSize(style, screenWidth / 55);
		try {
			doc.insertString(doc.getLength(), "\n \n \n                                                 -click HELP again to exit-", style);	
		} 
		catch (BadLocationException e) {
			e.printStackTrace();
		} 
	}

	/**
	  *  Sets up bad word text pane, setting size, font, text, and colors, and passing the specific banned word
	  *  the user typed so that it's displayed on the text pane.
	  * @param badWord banned word typed by the user
	  */
	public void makeBadWordPane(String badWord) {
		StyledDocument doc = badWordPane.getStyledDocument();

		badWordPane.setBounds(diceWidth, diceWidth, (screenWidth - 2 * diceWidth) - (diceWidth / 2), (screenHeight - (2 * diceWidth)));
		badWordPane.setPreferredSize(new Dimension(screenWidth - 2 * diceWidth, screenHeight - (2 * diceWidth)));
		badWordPane.setFont(badWordFont);
		badWordPane.setBorder(badWordBorder);
		badWordPane.setEditable(false);

		Style style = badWordPane.addStyle("Color Style", null);
		StyleConstants.setForeground(style, badWordTextColor);
		StyleConstants.setBold(style, true);
		StyleConstants.setFontSize(style, screenWidth / 25);
		try {
			doc.insertString(doc.getLength(), "          Curse Word Detected", style);
			doc.insertString(doc.getLength(), "\n", style);
		} 
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		StyleConstants.setBold(style, false);
		StyleConstants.setFontSize(style, screenWidth / 35);
		try {
			doc.insertString(doc.getLength(),"\n  Inappropriate language detected! Please edit your \n  story so it doesn't" 
					+ " include this word: " + badWord, style);	
		} 
		catch (BadLocationException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Sets up all button listeners for each button used in the game.	 
	 */
	void setupListeners() {
		startGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setupMouseListener(listener);
				rollDiceButton.setVisible(true);
				startGameButton.setVisible(false);
				isStartScreenVisible = false;
				setupMouseListener(listener);

				// Story Stuff
				try {
					dgame.readCurseWordsFromFile();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				repaint();
				diceTimer.start();
			}
		});
		rollDiceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rollDice(); 
				rollDiceButton.setVisible(false);
				//rollAgainButton.setVisible(true);
				helpButton.setVisible(true);
				repaint();
			}
		});
		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!instructPane.isVisible())
					instructPane.setVisible(true);
				else
					instructPane.setVisible(false);
				repaint();
			}
		});
		storyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveStory(); // save story function showStoryButton = false;
				repaint();
				storyButton.setVisible(false);
				storyText.setVisible(false);
				rollAgainButton.setVisible(true);
				showStoryButton = false;
				storyPane.setVisible(true);
				helpButton.setVisible(false);

				repaint(); // print story function

			}
		});
		rollAgainButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isDialogUp = false;
				rollAgainButton.setVisible(false);
				isRolled = false;
				isStorySaved = false;
				numAnimations = 0;
				dicePlaced = 0;
				isAnimDone = false;
				isStoryShowing = false;
				storyPane.setVisible(false);
				dgame.setDice();
				rollDice();
				repaint();
				storyPane.setText("");
				badWordPane.setVisible(false);
				badWordPane.setText("");
				isBadWordPaneMade = false;
				shouldEraseStory = true;
				helpButton.setVisible(true);

			}
		});
		goBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goBackButton.setVisible(false);
				badWordPane.setVisible(false);
				storyText.setVisible(true);
				storyButton.setVisible(true);
				isDialogUp = false;

				repaint(); // print story function

			}
		});
	}

	/**
	 * Creates the rolling dice animation by changing the displayed image for each die every fraction of a 
	 * second (using the timer), as well as calling the Die class's throwDie function for each die to change 
	 * the x- and y-locations. 
	 */
	void animDice() {
		Random rand = new Random();
		if (!isAnimDone) {
			for (int i = 0; i < dgame.getNumDice(); i++) {
				gameDice[i].throwDie();
				gameDice[i].setDieImg(possibleDiceImgs[rand.nextInt(dgame.getNumImgs())]);
			}
		}
	}

	// Timer
	/**
	 * Handles all actions that occur at every tick of the timer during the game
	 * @param e action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (numAnimations < animsToDo) {
			if (isRolled && !isAnimDone) {
				animDice();
				repaint();
				numAnimations++;
			}
		} else if (numAnimations == animsToDo && !isAnimDone) {
			isAnimDone = true;
			animDice();
			setDiceImgs();
			repaint();
			// storyButton.setVisible(true);
			// storyText.setVisible(true);
			numAnimations++;
		}
	}

	// Set up mouse listener
	
	/**
	 * Getter that returns the game screen as the target for the mouse listener and mouse motion listener
	 * @return this (game screen)
	 */
	private Component getMouseTarget() {
		return this;
	}

	/**
	 * Sets up the mouse listener and mouse motion listener for the game, adds a mouse listener to the text 
	 * area where the user inputs their story.
	 * @param listener mouse input listener
	 */
	void setupMouseListener(MouseInputListener listener) {
		getMouseTarget().addMouseListener(listener);
		getMouseTarget().addMouseMotionListener(listener);
		storyText.addMouseListener(listener);
		//instructPane.addMouseListener(listener);
	}

	/**
	 * DiceListener is a nested class that uses the mouse listener and mouse motion listener to call certain 
	 * methods and change booleans when the user clicks, presses, drags, releases, and so on. It sets an initial 
	 * point at (0, 0) and changes this point to wherever the most recent mouse event occurs. DiceListener 
	 * implements the MouseInputListener interface.
	 * 
	 * @author Natalie
	 *
	 */
	public class DiceListener implements MouseInputListener {
		Point point = new Point(0, 0);

		/**
		 * If the boolean shouldEraseStory is true and the user clicks in the story text area, this method
		 * erases all the text in the text area.
		 * @param e mouse event
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getSource() == storyText && shouldEraseStory){
				storyText.setText("");
				shouldEraseStory = false;
			}
		}

		/**
		 * sets isDieSelected to true for the die that the user presses the mouse over and sets selectedDie to
		 * that die. Also decrements dicePlaced if the user presses a die that is currently placed in a story
		 * slot.
		 * @param e mouse event
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			point = e.getPoint();
			for (Die d : gameDice) {
				if (point.x >= d.getXLoc() && point.x <= d.getXLoc() + diceWidth && point.y >= d.getYLoc()
						&& point.y <= d.getYLoc() + diceWidth) {
					d.setSelection(true);
					isDieSelected = true;
				}
			}
			for (Die d : gameDice) {
				if (d.getSelection() == true)
					selectedDie = d;
			}
			for (int sbx : storyboardX) {
				if (point.x >= sbx && point.x <= sbx + diceWidth && point.y >= storyStartX
						&& point.y <= storyStartY + diceWidth && isDieSelected) {
					dicePlaced--;
				}
			}
		}

		/**
		 * Places the die that the user selected, either placing it in the story slot the user dragged it into,
		 * or snapping it back to its original position if the user didn't drag it into a story slot.
		 * @param e mouse event
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			point = e.getPoint();
			boolean isPlaced = false;
			if (isDieSelected) {
				if (selectedDie != null) {
					for (int i = 0; i < dgame.getNumDice(); i++) {
						// for (int xStory : storyboardX) {
						if (point.x >= storyboardX[i] && point.x <= storyboardX[i] + diceWidth && point.y >= storyStartY
								&& point.y <= storyStartY + diceWidth) {
							selectedDie.setXLoc(storyboardX[i]);
							selectedDie.setYLoc(storyStartY);
							isPlaced = true;
							selectedDie.setStoryIndex(i);
							dicePlaced++;
						}
					}
					for (int i = 0; i < dgame.getNumDice(); i++) {
						if (selectedDie != gameDice[i]) {
							if (selectedDie.getXLoc() == gameDice[i].getXLoc()
									&& selectedDie.getYLoc() == gameDice[i].getYLoc()) {
								selectedDie.setXLoc(selectedDie.getInitXLoc());
								selectedDie.setYLoc(selectedDie.getInitYLoc());
								isPlaced = false;
								dicePlaced--;
							}
						}
					}
					if (!isPlaced) {
						selectedDie.setXLoc(selectedDie.getInitXLoc());
						selectedDie.setYLoc(selectedDie.getInitYLoc());
					}

					selectedDie.setSelection(false);
					isDieSelected = false;
				}
			}
			if (dicePlaced == dgame.getNumDice() && !isStoryShowing) {
				showStoryButton = true;
			}
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		/**
		 * Updates the x- and y-locations of the selected die that the user is dragging to the position of the
		 * mouse, then calls repaint to update the graphics.
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			point = e.getPoint();
			if (isDieSelected && selectedDie != null) {
				selectedDie.setXLoc(point.x - diceWidth / 2);
				selectedDie.setYLoc(point.y - diceWidth / 2);
			}
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {}
	}
}