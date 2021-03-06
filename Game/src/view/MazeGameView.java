package view;

import model.Crab;
import model.MazeBoard;
import model.MazeCell;
import model.MazeWall;
import model.MiniMap;
import model.PlayerScore;
import model.PowerUp;
import model.Predator;
import model.Litter;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


/**
 * MazeGameView contains all the information for MiniGame1, the maze game. Includes
 * images, buttons, button listeners, and timer.
 * @author Logan
 *
 */
public class MazeGameView extends JPanel implements KeyListener, ActionListener {

	//=================================================================//

	private static final long serialVersionUID = 1L;
	
	
	//Timer
	Timer t = new Timer(10,this);
	private int totalTime = 120;
	private int timeRemaining = totalTime;
	private int timeCheck = 0;

	//Screen dimensions
	static private int screenWidth = MainFrame.getFrameWidth();
	static private int screenHeight = MainFrame.getFrameHeight();

	//Background image
	private BufferedImage backGroundImg = createImage("background/underwater2.png");

	private int cellWidth;
	private int cellHeight;

	//Tutorial Board
	private MazeBoard tutBoard = new MazeBoard(screenWidth,screenHeight);
	private boolean isTutorial;
	private ArrayList<MazeWall> tutWalls;

	private int tutLitterTextIndex;
	private int tutPredatorTextIndex;
	private int tutPowerUpTextIndex;
	private int tutSalinityTextIndex;
	private int tutMiniMapTextIndex;

	private String tutLitterText;
	private String tutPredatorText;
	private String tutPowerUpText;
	private String tutSalinityText;
	private String tutMiniMapText;

	private boolean litterTextSeen;
	private boolean predatorTextSeen;
	private boolean powerUpTextSeen;
	private boolean salinityTextSeen;
	private boolean miniMapTextSeen;

	private boolean tutIsLitterHit;
	private boolean tutIsPredatorHit;

	private int tutPauseTimer;
	private int tutPauseTotal;

	private String tutFontStyle;
	private int tutFontSize;
	private int tutSalinityTextXLoc;
	private int tutMiniMapTextXLoc;
	private int tutTextYLoc;

	private BufferedImage upArrowImage = createImage("MazeExtraImgs/upArrow.png");
	private BufferedImage leftArrowImage = createImage("MazeExtraImgs/leftArrow.png");
	private int upArrowX;
	private int upArrowY;
	private int leftArrowX;
	private int leftArrowY;
	private int arrowWidth;
	private int arrowHeight;

	private BufferedImage wrongImage = createImage("MazeExtraImgs/redlinecircle.png");
	private BufferedImage rightImage = createImage("MazeExtraImgs/greencircle.png");

	//create the maze board
	MazeBoard board;
	private MazeCell[][] grids;
	private ArrayList<MazeWall> mazeWalls;
	private int numRows;
	private int numCols;

	//miniMap 
	private MiniMap miniMap;
	private int miniWidth;
	private int miniHeight;
	private MazeCell miniCharacter;
	private MazeCell miniGoal;
	private int endGoalX;
	private int endGoalY;


	//Horseshoe Crab images
	private BufferedImage crabRight0 = createImage("characters/horseshoe_crab_right_0.png");
	private BufferedImage crabRight1 = createImage("characters/horseshoe_crab_right_1.png");

	private BufferedImage crabLeft0 = createImage("characters/horseshoe_crab_left_0.png");
	private BufferedImage crabLeft1 = createImage("characters/horseshoe_crab_left_1.png");

	private BufferedImage crabDown0 = createImage("characters/horseshoe_crab_down_0.png");
	private BufferedImage crabDown1 = createImage("characters/horseshoe_crab_down_1.png");

	private BufferedImage crabUp0 = createImage("characters/horseshoe_crab_up_0.png");
	private BufferedImage crabUp1 = createImage("characters/horseshoe_crab_up_1.png");

	//Blue Crab images
	private BufferedImage bCrab0 = createImage("characters/bluecrab_0.png");
	private BufferedImage bCrab1 = createImage("characters/bluecrab_1.png");
	private BufferedImage bCrab2 = createImage("characters/bluecrab_2.png");


	//Crab images arrays
	private BufferedImage[][] crabPics = {
			{crabUp0, crabUp1},
			{crabDown0, crabDown1},
			{crabRight0,crabRight1},
			{crabLeft0, crabLeft1},
	};

	private BufferedImage[][] bCrabPics = {
			{bCrab0, bCrab1, bCrab2},
	};

	private int crabPicNum = 0;
	private int crabNumPics = 1;
	private boolean crabIsMoving = false;
	private int swimSpeed = 5;
	private int swimTimer = swimSpeed;


	//Crab
	private Crab testCrab;
	private int crabDir;
	private BufferedImage crabImg;
	private int characterWidth;
	private int characterHeight;
	private int smallWidth;
	private int smallHeight;
	private int mediumWidth;
	private int mediumHeight;
	private int largeWidth;
	private int largeHeight;
	private int characterXLoc;
	private int characterYLoc;
	private int yIncr;
	private int xIncr;
	private int xVel;
	private int yVel;

	//Predator Images
	private BufferedImage bassPredRight = createImage("characters/fish_bass_right.png");
	private BufferedImage bassPredLeft = createImage("characters/fish_bass_left.png");
	private BufferedImage bassPredUp = createImage("characters/fish_bass_up.png");
	private BufferedImage bassPredDown = createImage("characters/fish_bass_down.png");

	private BufferedImage pickPredRight = createImage("characters/fish_pickerel_right.png");
	private BufferedImage pickPredLeft = createImage("characters/fish_pickerel_left.png");
	private BufferedImage pickPredUp = createImage("characters/fish_pickerel_up.png");
	private BufferedImage pickPredDown = createImage("characters/fish_pickerel_down.png");


	//Predator Pic Array,  0 = bass, 1 = group
	private BufferedImage[][] preds = {
			{bassPredUp,bassPredDown,bassPredRight,bassPredLeft},
			{pickPredUp,pickPredDown,pickPredRight,pickPredLeft}
	};
	private int predSwitchCount;
	private int numPredImages = preds.length;

	//Features bar
	private final int featuresBarWidth = screenWidth;
	private final int featuresBarHeight = screenHeight/10;

	//Time Remaining Label
	private String timeRemainingLabel;	
	private int timeRemainingFontSize;
	private int timeRemainingLabelXLoc;
	private int timeRemainingLabelYLoc;
	private int timeXLoc;
	private int timeYLoc;

	//Salinity Meter
	private int meterWidth;
	private int meterHeight;
	private int meterX;
	private int meterY;

	private int salinityBlinkTimer = 0;
	private int salinityBlinkSpeed = 40;

	private int salinityTextSize;
	private String salinityTextStyle;

	private int currentSalinityTextX;
	private int currentSalinityTextY;

	private String currentSalinityText;
	private String correctSalinityTextBCrab;
	private String correctSalinityTextHCrab;
	private String wrongSalinityTextBCrab;
	private String wrongSalinityTextHCrab;

	private String[][] salinityTextArray;

	private String salinityTitleText;
	private int salinityTitleX;
	private int salinityTitleFontSize;
	private int salinityTitleY;
	private String salinityTitleFontStyle;

	//Salinity Warning
	private String salinityWarningText;
	private int salinityWarningTimeLimit;
	private int salinityWarningTimeTimer;
	private boolean firstTimeWrongSalinity;
	private int salinityWarningTextX;
	private int salinityWarningTextY;
	


	//Litter
	private ArrayList<BufferedImage> litterTypes = makeLitterList();
	Random rand = new Random();
	private ArrayList<Litter> gameLitter;
	private int litterWidth;
	private int litterHeight;
	private int xLitterMax;
	private int xLitterMin;


	//Predator Array List
	private ArrayList<Predator> predators;

	//Health
	private int health;
	private int hitTimer = 0;
	private int cantBeHitLim = 100;

	private BufferedImage healthImg = createImage("MazeExtraImgs/fullHeart.png");
	private int healthImgWidth;
	private int healthImgHeight;
	private int healthImgXLoc;
	private int healthImgYLoc;
	private int healthFontSize;

	//PowerUps
	private ArrayList<PowerUp> gamePowerUps;
	private int powerUpWidth;
	private int powerUpHeight;
	private BufferedImage powerUpImg = createImage("MazeExtraImgs/powerup.png");

	private String powerHealthText = "Extra Life!";
	private String powerSpeedText = "Speed Boost!";
	private String powerInvincibilityText = "Invincibility!";
	private String [] powerUpTexts = {powerHealthText, powerSpeedText, powerInvincibilityText};

	private int powerUpSpeed = 8;
	private int powerUpSpeedLimit = 1000;
	private int powerUpSpeedTimer = powerUpSpeedLimit;
	private int powerUpInvLimit = 500;
	private int powerUpInvTimer = powerUpInvLimit;

	private String[] currPowerUpStrings = {"Extra Life Added!", "Super Speed Activated! (10 Seconds)",
	"Invincibility! (5 Seconds)"};
	private int currPowerStringX;
	private int currPowerStringY;
	private int currPowerStringFontSize;
	private String currPowerStringFontStyle;
	private int currPowerTimerLimit = 150;
	private int currPowerTimerTime = currPowerTimerLimit;
	private int currPowerUp;

	//StartScreen
	private boolean startScreenVisible;

	private BufferedImage startBackgroundImg = createImage("background/2D_estuary.jpg");
	private int titleFontSize = screenWidth/20;
	private String titleFontStyle = "TimesRoman";
	private String titleText = "Estuary Maze Adventure!";
	private int titleStringX = screenWidth/2 - (((titleFontSize * titleText.length())*2)/9);
	private int titleStringY = screenHeight/4;

	//End Screen
	private boolean endScreenVisible;
	private int endTitleFontSize = screenWidth/50;
	private String endTitleFontStyle = "TimesRoman";
	private String endLoseText = "Oh no! You lost!";
	private String endWinText;
	private int endTitleStringX = screenWidth/2 - ((endTitleFontSize * titleText.length())/4);
	private int endTitleStringY = screenHeight/4;

	private String endWinTimeText = "Your Time: ";
	private int endWinTimeTextX = endTitleStringX;
	private int endWinTimeTextY = screenHeight/4 + endTitleFontSize;;

	private String endLoseTextHealth = "You have no more health!";
	private String endLoseTextTime = "You have no more time left!";
	private int endLoseTextX = endTitleStringX;
	private int endLoseTextY = screenHeight/4 + endTitleFontSize;

	//End Cell
	private MazeCell endCell;

	private BufferedImage endGrassImg = createImage("MazeExtraImgs/seagrass.png");
	private BufferedImage finishFlagImg = createImage("MazeExtraImgs/finishFlags.png");

	private BufferedImage endBlueCrabImg = createImage("characters/bluecrab_0.png");
	private BufferedImage endHorshoeCrabImg = createImage("characters/horseshoe_crab_left_0.png");

	private BufferedImage[] endCrabImgArray = {endHorshoeCrabImg,endBlueCrabImg};

	//Game state
	private boolean hasWon;
	private boolean isEasyMode;
	private boolean isMediumMode;
	private boolean isHardMode;

	//Board Buttons
	private JButton hCrabButton;
	private JButton bCrabButton;
	private JButton easyButton;
	private JButton mediumButton;
	private JButton hardButton;
	private JButton startButton;
	private JButton tutorialButton;
	private JButton goToStartButton;
	

	//Age State
	private ArrayList<MazeCell> ageStateCells = new ArrayList<MazeCell>();
	private int ageStateCellOriginalCount;
	private int ageStateCellMediumCount;
	private int ageStateCellLargeCount;
	private int ageStateCellCurrentCount;
	
	//scoreboard
	JTextArea nameTextField;
	private JButton enterNameButton;
	private int gameScore;
	private int scoreDecrementTimer;
	private int scoreDecrementLimit;
	private int scoreDecrementOnHit;
	private boolean isScoreBoardView;
	
	private String scoreFontStyle;
	private int scoreFontSize;
	private int scoreNameXLoc;
	private int scoreScoreXLoc;
	private int scoreYLoc;
	
	private String currScoreName;
	private int currScoreScore;
	
	

	//=================================================================//


	/**
	 * Constructor, creates an instance of a MazeGameView object
	 * Initalizes all buttons and apsects of the game, including game states and timers.
	 */
	public MazeGameView(){
		this.requestFocusInWindow(true);
		
		//Buttons
		hCrabButton = new JButton("Horseshoe Crab");
		hCrabButton.setFocusable(false);
		hCrabButton.setBackground(Color.ORANGE);
		hCrabButton.setOpaque(true);
		hCrabButton.setBorderPainted(false);
		
		bCrabButton = new JButton("Blue Crab");
		bCrabButton.setFocusable(false);
		bCrabButton.setBackground(Color.CYAN);
		bCrabButton.setOpaque(true);
		bCrabButton.setBorderPainted(false);

		easyButton = new JButton("Easy");
		easyButton.setFocusable(false);
		easyButton.setBackground(Color.GREEN);
		easyButton.setOpaque(true);
		easyButton.setBorderPainted(false);
		
		mediumButton = new JButton("Medium");
		mediumButton.setFocusable(false);
		mediumButton.setBackground(Color.ORANGE);
		mediumButton.setOpaque(true);
		mediumButton.setBorderPainted(false);
		
		hardButton = new JButton("Hard");
		hardButton.setFocusable(false);
		hardButton.setBackground(Color.RED);
		hardButton.setOpaque(true);
		hardButton.setBorderPainted(false);

		startButton = new JButton("Start Game!");
		startButton.setFocusable(false);
		startButton.setBackground(Color.MAGENTA);
		startButton.setOpaque(true);
		startButton.setBorderPainted(false);

		tutorialButton = new JButton("Tutorial");
		tutorialButton.setFocusable(false);
		tutorialButton.setBackground(Color.MAGENTA);
		tutorialButton.setOpaque(true);
		tutorialButton.setBorderPainted(false);
		
		enterNameButton = new JButton("Enter Score");
		enterNameButton.setFocusable(false);

		goToStartButton = new JButton("Go to Start Screen");
		goToStartButton.setFocusable(false);
		


		//StartScreen Visibility
		startScreenVisible = true;
		this.add(bCrabButton);
		this.add(hCrabButton);
		this.add(tutorialButton);
		this.add(easyButton);
		this.add(mediumButton);
		this.add(hardButton);
		this.add(startButton);
		this.add(goToStartButton);

		//Button Visibility
		easyButton.setVisible(false);
		mediumButton.setVisible(false);
		hardButton.setVisible(false);
		startButton.setVisible(false);
		tutorialButton.setVisible(false);
		goToStartButton.setVisible(false);

		//initialize crab
		testCrab = new Crab(5,0,screenWidth/2 + 10 ,screenHeight/2 + 10);
		
		//scoreboard
		nameTextField = new JTextArea("Enter Name",1,5);
		nameTextField.setBackground(Color.WHITE);


		this.add(nameTextField);
		this.add(enterNameButton);
		nameTextField.setVisible(false);
		enterNameButton.setVisible(false);

		//button listener initialization
		setupListeners();

		//initialize key
		addKeyListener(this);
		this.setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		//initialize game state
		endScreenVisible = false;
		hasWon = false;


		//background
		this.setBackground(Color.BLUE);

	}//constructor



	/**
	 * Handles all images and components that are drawn onto the screen during the maze game.
	 * @param g graphics
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//START SCREEN DRAWING
		if(startScreenVisible){
			g.drawImage(startBackgroundImg,0,0,screenWidth,screenHeight,this);
			g.setColor(Color.BLUE);
			g.setFont(new Font(titleFontStyle,Font.BOLD,titleFontSize));
			g.drawString(titleText,titleStringX,titleStringY);

		}//if
		else if(isScoreBoardView){
			g.setFont(new Font(scoreFontStyle,Font.BOLD,scoreFontSize));
			g.setColor(Color.RED);
			
			g.drawString(currScoreName, scoreNameXLoc, scoreYLoc - 2*scoreFontSize);
			g.drawString(String.valueOf(currScoreScore), scoreScoreXLoc, scoreYLoc - 2*scoreFontSize);

			g.setColor(Color.WHITE);
			g.drawString("NAME", scoreNameXLoc, scoreYLoc);
			g.drawString("SCORE", scoreScoreXLoc, scoreYLoc);
			scoreYLoc += (2*scoreFontSize);
			int currRank = 1;
			for(PlayerScore pS: board.getHighScores()){
				g.drawString(String.valueOf(currRank),scoreNameXLoc - 2*scoreFontSize, scoreYLoc);
				if(pS != null){
					g.drawString(pS.getName(), scoreNameXLoc, scoreYLoc);
					g.drawString(String.valueOf(pS.getScore()), scoreScoreXLoc, scoreYLoc);
				}else{
					g.drawString("---", scoreNameXLoc, scoreYLoc);
					g.drawString("---", scoreScoreXLoc, scoreYLoc);
				}
				scoreYLoc += scoreFontSize;
				currRank++;
			}
		}
		//EVERYTHING ELSE
		else{

			//BACKGROUND DRAWING
			g.drawImage(backGroundImg, 0, 0, screenWidth, screenHeight, this);

			// MAZE DRAWING
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke(5));
			g2.setColor(Color.CYAN);
			for (MazeWall wall: mazeWalls){
				int startX = wall.getStartX();
				int startY = wall.getStartY();
				int endX = wall.getEndX();
				int endY = wall.getEndY();
				//checks if on Screen
				if(
						((startX > 0 && startX < screenWidth) || (endX > 0 && endX < screenWidth)) ||
						((startY>0 && startY < screenHeight) || (endY > 0 && endY > screenHeight))
						)
					g.drawLine(startX, startY, endX, endY);
			}
			//Draws tutorial Walls
			if(isTutorial){
				for (MazeWall wall: tutWalls){
					int startX = wall.getStartX();
					int startY = wall.getStartY();
					int endX = wall.getEndX();
					int endY = wall.getEndY();
					//checks if on Screen
					g.drawLine(startX, startY, endX, endY);
				}
			}
			//Draws litter
			for(Litter lit : gameLitter){
				if(lit.getXLoc()+litterWidth > 0 && lit.getXLoc() <= screenWidth && lit.getYLoc()+litterHeight > 0 && lit.getYLoc() < screenHeight){
					g2.drawImage(litterTypes.get(lit.getType()), lit.getXLoc(), lit.getYLoc(),litterWidth, litterHeight, this);
					if(!litterTextSeen && board.inWhichCell(characterXLoc,characterYLoc).getX() == tutLitterTextIndex){
						g2.drawImage(wrongImage, lit.getXLoc() - lit.getWidth()/2, lit.getYLoc() - lit.getWidth()/2,
								litterWidth*2, litterHeight*2, this);
					}
				}
			}

			//Draws Power Ups
			for(PowerUp pu : gamePowerUps){
				g.drawImage(powerUpImg,pu.getXLoc(),pu.getYLoc(), powerUpWidth, powerUpHeight, this);
				if(!powerUpTextSeen && board.inWhichCell(characterXLoc,characterYLoc).getX() == tutPowerUpTextIndex){
					g2.drawImage(rightImage, pu.getXLoc() - pu.getWidth()/2, pu.getYLoc() - pu.getWidth()/2,
							pu.getWidth()*2, pu.getHeight()*2, this);
				}
			}

			//DRAWS PREDATORS
			for(Predator p: predators){
				g.drawImage(preds[predSwitchCount%numPredImages][p.getDirection()], p.getXLoc(), p.getYLoc(), p.getWidth(), p.getHeight(), this);
				predSwitchCount++;
				if(!predatorTextSeen && board.inWhichCell(characterXLoc,characterYLoc).getX() == tutPredatorTextIndex){
					g2.drawImage(wrongImage, p.getXLoc() - p.getWidth()/2, p.getYLoc() - p.getWidth()/2,
							p.getWidth()*2, p.getHeight()*2, this);
				}
			}
			predSwitchCount = 0;

			//Features Bar Drawing
			g.setColor(Color.yellow);
			g.drawRect(0, 0, featuresBarWidth, featuresBarHeight);
			g.fillRect(0, 0, featuresBarWidth, featuresBarHeight);

			//MINIMAP DRAWING
			//background of minimap
			g.setColor(Color.BLACK);
			g.drawRect(0,0,numCols*miniWidth, numCols*miniHeight);
			g.fillRect(0,0,numCols*miniWidth, numCols*miniHeight);
			//actual lines of minimap
			for(int i = 0; i < numRows; i++){
				for(int j =0; j < numCols; j++){
					MazeCell currG = grids[i][j];
					int topLX = j*miniWidth; //top left corner x value
					int topLY = i*miniHeight + miniHeight*((numCols-numRows)/2); //top left corner y value
					int topRX = topLX + miniWidth; //top right corner x value
					int topRY = topLY; //top right corner y value
					int bottomLX = topLX; //bottom left corner x value
					int bottomLY = topLY + miniHeight; //bottom left corner y value
					int bottomRX = topRX; //bottom right corner x value
					int bottomRY = bottomLY; //bottom right corner y value


					g2.setStroke(new BasicStroke(1));
					g2.setColor(Color.RED);

					if(currG.getHasTopWall()){
						g2.drawLine(topLX, topLY, topRX, topRY);
					}
					if(currG.getHasBottomWall()){
						g2.drawLine(bottomLX, bottomLY, bottomRX, bottomRY);
					}
					if(currG.getHasRightWall()){
						g2.drawLine(topRX, topRY, bottomRX, bottomRY);
					}
					if(currG.getHasLeftWall()){
						g2.drawLine(topLX, topLY, bottomLX, bottomLY);
					}
				}

			}

			//MINI MAP CHARACTER DRAWING
			g.setColor(Color.GREEN);
			g.drawRect(miniCharacter.getX() * miniWidth + miniWidth/4, miniCharacter.getY()*miniHeight + miniHeight/4 + miniHeight*((numCols-numRows)/2),miniWidth/2,miniHeight/2);
			g.fillRect(miniCharacter.getX() * miniWidth + miniWidth/4, miniCharacter.getY()*miniHeight + miniHeight/4 + miniHeight*((numCols-numRows)/2),miniWidth/2,miniHeight/2);

			//DRAWS END GOAL ON MINIMAP
			g.setColor(Color.YELLOW);
			g.drawRect(endCell.getX() * miniWidth + miniWidth/4, endCell.getY()*miniHeight + miniHeight/4 + miniHeight*((numCols-numRows)/2),miniWidth/2,miniHeight/2);
			g.fillRect(endCell.getX() * miniWidth + miniWidth/4, endCell.getY()*miniHeight + miniHeight/4 + miniHeight*((numCols-numRows)/2),miniWidth/2,miniHeight/2);

			//Time Remaining Drawing
			if(!isTutorial){
				g.setFont(new Font(titleFontStyle,Font.BOLD,timeRemainingFontSize));
				g.setColor(Color.BLACK);
				g.drawString(timeRemainingLabel, timeRemainingLabelXLoc, timeRemainingLabelYLoc);
				g.drawString(String.valueOf(timeRemaining), timeXLoc, timeYLoc);
			}
			
			//ScoreDrawing
			if(!isTutorial){
				g.setFont(new Font(titleFontStyle,Font.BOLD,timeRemainingFontSize));
				g.setColor(Color.BLACK);
				g.drawString("Score: ", timeRemainingLabelXLoc, timeRemainingLabelYLoc + timeRemainingFontSize);
				g.drawString(String.valueOf(gameScore), timeXLoc, timeYLoc + timeRemainingFontSize);
			}

			//Crab Health Drawing
			g.setColor(Color.BLACK);
			g.setFont(new Font(titleFontStyle,Font.BOLD,healthFontSize));
			g.drawString(String.valueOf(health), healthImgXLoc + healthImgWidth, healthImgYLoc + healthImgHeight);
			g.drawImage(healthImg, healthImgXLoc, healthImgYLoc, healthImgWidth, healthImgHeight, this);


			//SALINITY METER DRAWING
			g.setFont(new Font(salinityTitleFontStyle,Font.BOLD,salinityTitleFontSize));
			g.drawString(salinityTitleText, salinityTitleX, salinityTitleY);


			if(board.isOnCorrectPath(characterXLoc, characterYLoc)){
				g.setColor(Color.GREEN);
				g.fillRect(meterX,meterY,meterWidth,meterHeight);
				g.setColor(Color.BLACK);
				currentSalinityText = salinityTextArray[testCrab.getType()][0];
				g.drawString(currentSalinityText, currentSalinityTextX, currentSalinityTextY);
			}
			else{
				if(salinityBlinkTimer > salinityBlinkSpeed/2){
					g.setColor(Color.RED);
				}
				else{
					g.setColor(new Color(153,0,0));
				}
				g.fillRect(meterX,meterY,meterWidth,meterHeight);
				g.setColor(Color.BLACK);
				currentSalinityText = salinityTextArray[testCrab.getType()][1];
				g.drawString(currentSalinityText, currentSalinityTextX, currentSalinityTextY);
				if(firstTimeWrongSalinity){
					salinityWarningTimeTimer = 0;
					firstTimeWrongSalinity = false;
				}
			}

			//outline for current salinity
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(3));
			g.setFont(new Font(salinityTextStyle,Font.ITALIC,salinityTextSize));
			g.drawRect(meterX,meterY,meterWidth,meterHeight);


			if(salinityWarningTimeTimer != salinityWarningTimeLimit){
				g.setColor(Color.BLACK);
				g.setFont(new Font(endTitleFontStyle,Font.BOLD,endTitleFontSize));
				g.drawString(salinityWarningText,salinityWarningTextX,salinityWarningTextY);
				g.drawImage(upArrowImage, upArrowX, upArrowY, arrowWidth, arrowHeight, this);
			}



			//END LOCATION DRAWING
			g.drawImage(finishFlagImg, endCell.getXLoc(), endCell.getYLoc() + endCell.getHeight()/4, endCell.getWidth(), endCell.getHeight()/2, this);
			g.drawImage(endGrassImg, endCell.getXLoc(), endCell.getYLoc(), characterWidth, characterHeight, this);
			g.drawImage(endGrassImg, endCell.getXLoc(), endCell.getYLoc()+150, characterWidth, characterHeight, this);
			g.drawImage(endGrassImg, endCell.getXLoc()+150, endCell.getYLoc(), characterWidth, characterHeight, this);
			g.drawImage(endGrassImg, endCell.getXLoc()+150, endCell.getYLoc()+150, characterWidth, characterHeight, this);
			g.drawImage(endCrabImgArray[testCrab.getType()], endCell.getXLoc()+50, endCell.getYLoc()+50, characterWidth, characterHeight, this);
			g.drawImage(endCrabImgArray[testCrab.getType()], endCell.getXLoc()+125, endCell.getYLoc()+50, characterWidth, characterHeight, this);

			//CRAB DRAWING
			if(testCrab.getType() == 0){
				crabImg = crabPics[crabDir][crabPicNum];
			}
			else
				crabImg = bCrabPics[crabDir][crabPicNum];

			//Current Power Up String Drawing
			g.setFont(new Font(currPowerStringFontStyle, Font.BOLD, currPowerStringFontSize));
			g.setColor(Color.RED);
			if(currPowerTimerTime != currPowerTimerLimit){
				g.drawString(currPowerUpStrings[currPowerUp], currPowerStringX,currPowerStringY);
			}

			//tutorial text drawing
			g.setFont(new Font(tutFontStyle, Font.BOLD,tutFontSize));
			g.setColor(Color.WHITE);
			if(isTutorial){
				MazeCell tempCell = board.inWhichCell(characterXLoc, characterYLoc);
				if(tempCell.getX() == tutLitterTextIndex && !litterTextSeen){
					tutPauseTotal = 200;
					tutPauseTimer = 0;
					litterTextSeen = true;
				}
				else if(tempCell.getX() == tutSalinityTextIndex && !salinityTextSeen){
					g.drawImage(upArrowImage, upArrowX, upArrowY, arrowWidth, arrowHeight, this);
					g.drawString(tutSalinityText, tutSalinityTextXLoc, tutTextYLoc);
					tutPauseTotal = 500;
					tutPauseTimer = 0;
					salinityTextSeen = true;
				}
				else if(tempCell.getX() == tutPredatorTextIndex && !predatorTextSeen){
					tutPauseTotal = 200;
					tutPauseTimer = 0;
					predatorTextSeen = true;
				}
				else if(tempCell.getX() == tutMiniMapTextIndex && !miniMapTextSeen){
					g.drawImage(leftArrowImage, leftArrowX, leftArrowY, arrowHeight, arrowWidth, this);					
					g.drawString(tutMiniMapText, tutMiniMapTextXLoc, tutTextYLoc);
					tutPauseTotal = 500;
					tutPauseTimer = 0;
					miniMapTextSeen = true;
				}
				else if(tempCell.getX() == tutPowerUpTextIndex && !powerUpTextSeen){
					tutPauseTotal = 200;
					tutPauseTimer = 0;
					powerUpTextSeen = true;
				}
			}

			//HIT BLINKING OF CRAB
			if(powerUpInvTimer != powerUpInvLimit){
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float).5));
			}else{
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)1));
			}
			if(hitTimer%(cantBeHitLim/20) == 0){
				g.drawImage(crabImg, testCrab.getXLoc(), testCrab.getYLoc(), characterWidth, characterHeight, this);
			}


			//END SCREEN DRAWING
			if(endScreenVisible){

				g.setFont(new Font(endTitleFontStyle,Font.BOLD,endTitleFontSize));

				if(hasWon){
					g.setColor(Color.BLUE);
					g.drawString(endWinText, endTitleStringX, endTitleStringY);
					if(!isTutorial){
						g.drawString(endWinTimeText + (totalTime - timeRemaining) + " seconds", endWinTimeTextX, endWinTimeTextY);
					}
				}
				else{
					g.setColor(Color.RED);
					g.drawString(endLoseText, endTitleStringX, endTitleStringY);
					if(timeRemaining == 0){
						g.drawString(endLoseTextTime, endLoseTextX, endLoseTextY);
					}
					else{
						g.drawString(endLoseTextHealth, endLoseTextX, endLoseTextY);
					}
				}
			}

		}//else

	}//paintComponent


	/**
	 * Handles all actions that occur at every tick of the timer during the game
	 * @param arg0 action event
	 */
	public void actionPerformed(ActionEvent arg0) {
		if(tutPauseTimer < tutPauseTotal){
			tutPauseTimer++;
			return;
		}else if(salinityWarningTimeTimer < salinityWarningTimeLimit){
			salinityWarningTimeTimer++;
			return;
		}

		if(!isTutorial){
			timeCheck++;
			if(timeCheck == 100){
				timeRemaining--;
				timeCheck = 0;
			}
			if(scoreDecrementTimer >= scoreDecrementLimit){
				scoreDecrementTimer = 0;
				gameScore = gameScore - 1;
			}else{
				scoreDecrementTimer++;
			}
		}

		//curr power up timer
		if(currPowerTimerTime < currPowerTimerLimit){
			currPowerTimerTime++;
		}

		//updates Age State
		Iterator<MazeCell> m = ageStateCells.iterator();
		while(m.hasNext()){
			MazeCell temp = m.next();
			if(board.inWhichCell(characterXLoc, characterYLoc) == temp){
				m.remove();
				ageStateCellCurrentCount--;
			}
		}

		if(ageStateCellCurrentCount <= ageStateCellLargeCount && characterWidth != largeWidth && 
				!board.isHittingAnyWalls(characterXLoc - testCrab.getXVel(), characterYLoc - testCrab.getYVel(), largeWidth, largeHeight)){
			characterWidth = largeWidth;
			characterHeight = largeHeight;
		} else if(ageStateCellCurrentCount <= ageStateCellMediumCount && characterWidth != mediumWidth && characterWidth != largeWidth &&
				!board.isHittingAnyWalls(characterXLoc - testCrab.getXVel(), characterYLoc - testCrab.getYVel(), mediumWidth, mediumHeight)){
			characterWidth = mediumWidth;
			characterHeight = mediumHeight;
		}

		//updates minimap
		miniCharacter = board.inWhichCell(characterXLoc,characterYLoc);


		//blinks salinity meter if on wrong path
		if(salinityBlinkTimer < salinityBlinkSpeed)
			salinityBlinkTimer++;
		else
			salinityBlinkTimer = 0;

		//Checks if you lose
		if(health == 0 || timeRemaining == 0){
			hasWon = false;
			endScreenVisible = true;
			t.stop();
		}

		//Checks if you win
		if(testCrab.getXLoc() > endCell.getXLoc() && testCrab.getYLoc() > endCell.getYLoc()){
			hasWon = true;
			endScreenVisible = true;
			if(!isTutorial){
			nameTextField.setVisible(true);
			enterNameButton.setVisible(true);
			}else{
				goToStartButton.setVisible(true);
			}
			t.stop();
		}

		//Swim speed (rendering crab movement)
		if(swimTimer > 0){
			swimTimer--;
		}

		if(testCrab.getXVel() == 0 && testCrab.getYVel() ==0){
			crabIsMoving = false;
		}

		if(crabIsMoving && swimTimer == 0){
			crabPicNum = (crabPicNum + 1) % crabNumPics;
			swimTimer = swimSpeed;
		}

		//check for power up hit
		Iterator<PowerUp> p = gamePowerUps.iterator();
		while(p.hasNext()){
			PowerUp pu = p.next();
			if(pu.hitPowerUp(characterXLoc, characterYLoc, characterWidth, characterHeight)){
				currPowerTimerTime = 0;
				if(pu.getType() == 0){
					currPowerUp = 0;
					health += 1;
					p.remove();
					if(isTutorial){
						tutWalls.remove(0);
					}
				}
				else if(pu.getType() == 1){
					powerUpSpeedTimer = 0;
					currPowerUp = 1;
					xIncr = powerUpSpeed;
					yIncr = powerUpSpeed;
					p.remove();
					if(isTutorial){
						tutWalls.remove(0);
					}
				}
				else{
					currPowerUp = 2;
					powerUpInvTimer = 0;
					p.remove();
					if(isTutorial){
						tutWalls.remove(0);
					}
				}
			}
		}

		//checks if speed power up is active
		if(powerUpSpeedTimer < powerUpSpeedLimit){
			powerUpSpeedTimer++;
		}

		//sets crabs speed back to initial (after power up)
		if(powerUpSpeedTimer == powerUpSpeedLimit ){
			xIncr = testCrab.getXIncr();
			yIncr = testCrab.getYIncr();
		}

		//checks if invincibiltiy power up is active
		if(powerUpInvTimer < powerUpInvLimit){
			powerUpInvTimer++;
		}

		//checks for litter hits
		if(hitTimer == cantBeHitLim && powerUpInvTimer == powerUpInvLimit){
			if(board.hitAnyLitter(characterXLoc, characterYLoc, characterWidth, characterHeight)){
				if(!isTutorial){
					health -= 1;
					hitTimer = 0;
					gameScore = gameScore - scoreDecrementOnHit;
				}
				else{
					if(!tutIsLitterHit){
						hitTimer = 0;
						tutIsLitterHit = true;
					}
				}
			}

			//checks for predator hits
			if(board.hitAnyPreds(characterXLoc, characterYLoc, characterWidth, characterHeight)){
				if(!isTutorial){
					health -= 1;
					hitTimer = 0;
					gameScore = gameScore - scoreDecrementOnHit;
				}
				else{
					if(!tutIsPredatorHit){
						hitTimer = 0;
						tutIsPredatorHit = true;
					}
				}
			}
		}

		//checks if crab can be hit for this period of time
		if(hitTimer < cantBeHitLim){
			hitTimer++;
		}

		//floats the litter back and forth in a cell
		if(xLitterMax + board.getGameLitter().get(0).getFloatXIncr() + litterWidth <= cellWidth){
			board.floatAllLitterRight();
			xLitterMax += board.getGameLitter().get(0).getFloatXIncr();
		}
		else if(xLitterMin + board.getGameLitter().get(0).getFloatXIncr() + litterWidth <= cellWidth){
			board.floatAllLitterLeft();
			xLitterMin += board.getGameLitter().get(0).getFloatXIncr();
		}
		else{
			xLitterMax = 0;
			xLitterMin = 0;
		}
		repaint();


		//PREDATOR TICKS
		board.setRandomDirections();
		if(!isTutorial){
			board.moveAllPredators();
		}

		repaint();

		//MOVES MAZE IF CRAB IS NOT HITTING WALL
		if(board.isHittingAnyWalls(characterXLoc - testCrab.getXVel(), characterYLoc - testCrab.getYVel(), characterWidth, characterHeight)){
			if(board.isHittingAnyWalls(characterXLoc - testCrab.getXVel(), characterYLoc, characterWidth, characterHeight)){
				if(board.isHittingAnyWalls(characterXLoc, characterYLoc - testCrab.getYVel(), characterWidth, characterHeight)){
					return;
				}else{
					board.moveGrid(0,testCrab.getYVel());
				}
			}else{
				board.moveGrid(testCrab.getXVel(),0);
			}
			repaint();
		}
		else{
			repaint();
			board.moveGrid(testCrab.getXVel(),testCrab.getYVel());
		}
	}
	
	

	/**
	 * Moves crab image up on screen
	 */
	public void up(){
		testCrab.setDir(0);
		crabDir = testCrab.getDir();

		crabIsMoving = true;
		testCrab.setYVel(yIncr);
	}
	
	/**
	 * Moves crab image down on screen
	 */
	public void down(){
		//checks crab type, sets direction accordingly
		if(testCrab.getType() == 1){
			testCrab.setDir(0);
		}
		else
			testCrab.setDir(1);

		crabIsMoving = true;
		crabDir = testCrab.getDir();
		testCrab.setYVel(-yIncr);
	}
	
	/**
	 * Moves crab image left on screen
	 */
	public void left(){
		//checks crab type, sets direction accordingly
		if(testCrab.getType() == 1){
			testCrab.setDir(0);
		}
		else
			testCrab.setDir(3);

		crabIsMoving = true;
		crabDir = testCrab.getDir();
		testCrab.setXVel(xIncr);
	}
	
	/**
	 * Moves crab image right on screen
	 */
	public void right(){
		//checks crab type, sets direction accordingly
		if(testCrab.getType() == 1){
			testCrab.setDir(0);
		}
		else
			testCrab.setDir(2);

		crabIsMoving = true;
		crabDir = testCrab.getDir();
		testCrab.setXVel(-xIncr);
	}
	
	/**
	 * Handles events that occur when a key is pressed during the game
	 * @param arg0 key event
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_UP){
			up();
		}
		else if(code == KeyEvent.VK_DOWN){
			down();
		}
		else if(code == KeyEvent.VK_LEFT){
			left();
		}
		else if(code == KeyEvent.VK_RIGHT){
			right();
		}
	}

	/**
	 * Handles events that occur when a key is released during the game
	 * @param arg0 key event
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_UP){
			testCrab.setYVel(0);
		}
		else if(code == KeyEvent.VK_DOWN){
			testCrab.setYVel(0);
		}
		else if(code == KeyEvent.VK_LEFT){
			testCrab.setXVel(0);
		}
		else if(code == KeyEvent.VK_RIGHT){
			testCrab.setXVel(0);
		}
	}
	
	
	/**
	 * Handles events that occur when a key is typed during the game
	 * @param arg0 key event
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}


	/**
	 * Creates a buffered image from a given file 
	 * @param fileName name of file being read in
	 * @return buffered image from file
	 */
	public BufferedImage createImage(String fileName){
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(new File(fileName)); 
			return bufferedImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * creates all images for litter
	 * @return arrayList of buffered images of litter
	 */
	public ArrayList<BufferedImage> makeLitterList(){
		ArrayList<BufferedImage> litterIcons = new ArrayList<BufferedImage>();
		litterIcons.add(createImage("MazeExtraImgs/apple.png"));
		litterIcons.add(createImage("MazeExtraImgs/chipBag.png"));
		litterIcons.add(createImage("MazeExtraImgs/soda.png"));
		litterIcons.add(createImage("mazeExtraImgs/crumbledpaper.png"));
		return litterIcons;
	}
	
	/**
	 * sets up all button listeners for each button used during the game
	 */
	public void setupListeners(){
		//Button Listeners
				bCrabButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						//initializes type of crab
						testCrab.setType(1);
						testCrab.setDir(0);
						crabNumPics = 3;
						bCrabButton.setVisible(false);
						hCrabButton.setVisible(false);
						easyButton.setVisible(true);
						mediumButton.setVisible(true);
						hardButton.setVisible(true);
						tutorialButton.setVisible(true);
						tutIsLitterHit = true;
						tutIsPredatorHit = true;
						predatorTextSeen = true;
						litterTextSeen = true;
						powerUpTextSeen = true;
						salinityTextSeen = true;
						miniMapTextSeen = true;
						
						isEasyMode = false;
						isMediumMode = false;
						isHardMode = false;
					}

				});

				hCrabButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						//initializes type of crab
						testCrab.setType(0);
						testCrab.setDir(2);
						crabNumPics = 2;
						bCrabButton.setVisible(false);
						hCrabButton.setVisible(false);
						easyButton.setVisible(true);
						mediumButton.setVisible(true);
						hardButton.setVisible(true);
						tutorialButton.setVisible(true);
						tutIsLitterHit = true;
						tutIsPredatorHit = true;
						predatorTextSeen = true;
						litterTextSeen = true;
						powerUpTextSeen = true;
						salinityTextSeen = true;
						miniMapTextSeen = true;
						
						isEasyMode = false;
						isMediumMode = false;
						isHardMode = false;
					}

				});

				easyButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						board = new MazeBoard(0, screenWidth,screenHeight);
						easyButton.setVisible(false);
						mediumButton.setVisible(false);
						hardButton.setVisible(false);
						tutorialButton.setVisible(false);
						startButton.setVisible(true);
						isEasyMode = true;
					}
				});

				mediumButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						board = new MazeBoard(1, screenWidth,screenHeight);
						easyButton.setVisible(false);
						mediumButton.setVisible(false);
						hardButton.setVisible(false);
						tutorialButton.setVisible(false);
						startButton.setVisible(true);
						isMediumMode = true;
					}
				});

				hardButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						board = new MazeBoard(2, screenWidth,screenHeight);
						easyButton.setVisible(false);
						mediumButton.setVisible(false);
						hardButton.setVisible(false);
						tutorialButton.setVisible(false);
						startButton.setVisible(true);
						isHardMode = true;
					}
				});

				tutorialButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						board = tutBoard;
						easyButton.setVisible(false);
						mediumButton.setVisible(false);
						hardButton.setVisible(false);
						tutorialButton.setVisible(false);
						startButton.setVisible(true);
						tutLitterTextIndex = board.getTutLitterTextIndex();
						tutPredatorTextIndex = board.getTutPredatorTextIndex();
						tutPowerUpTextIndex = board.getTutPowerUpTextIndex();
						tutSalinityTextIndex = board.getTutSalinityTextIndex();
						tutMiniMapTextIndex = board.getTutMiniMapTextIndex();

						tutSalinityText = board.getTutSalinityText();
						tutMiniMapText = board.getTutMiniMapText();

						tutIsLitterHit = false;
						tutIsPredatorHit = false;
						predatorTextSeen = false;
						litterTextSeen = false;
						powerUpTextSeen = false;
						salinityTextSeen = false;
						miniMapTextSeen = false;

						tutPauseTotal = 200;
						tutPauseTimer = tutPauseTotal;

						tutFontStyle = "TimesRoman";
						tutFontSize = screenHeight/30;
						tutSalinityTextXLoc = screenWidth/2 -((tutFontSize * tutSalinityText.length())/4);
						tutMiniMapTextXLoc = screenWidth/2 -((tutFontSize * tutMiniMapText.length())/4);
						tutTextYLoc = screenHeight/2;
						
						//tutorial stuff
						nameTextField.setVisible(false);
						enterNameButton.setVisible(false);


					}
				});
				

				
				enterNameButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String tempName = nameTextField.getText();
						if(tempName.length() == 3){
							board.insertScore(tempName, gameScore);
							try {
								board.writeScoresToFile();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							//			board.printHighScores();
							goToStartButton.setVisible(true);
							enterNameButton.setVisible(false);
							nameTextField.setVisible(false);
							isScoreBoardView = true;
							currScoreName = tempName;
							currScoreScore = gameScore;
							repaint();
						}
					}
				});

				goToStartButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						endScreenVisible = false;
						startScreenVisible = true;
						hasWon = false;
						bCrabButton.setVisible(true);
						hCrabButton.setVisible(true);
						goToStartButton.setVisible(false);
						xLitterMax = 0;
						xLitterMin = 0;
						t.stop();
						repaint();
						board = null;
						isScoreBoardView = false;
					}
				});

				startButton.addActionListener(new ActionListener(){
					//tutorial
					
					public void actionPerformed(ActionEvent e){
						isTutorial = board.getIsTutorial();
						if(!isTutorial){
							endWinText = "Congratulations, you won!";
						}else{
							endWinText = "You've Fininshed the Tutorial, try your skills at the real game!";
						}
						
						nameTextField.setVisible(false);
						enterNameButton.setVisible(false);
						isScoreBoardView = false;
						
						scoreDecrementLimit = 1;
						scoreDecrementTimer = 0;
						scoreDecrementOnHit = board.getScoreDecrementOnHit();
						
						scoreFontStyle = "TimesRoman";
						scoreFontSize = screenHeight/30;
						scoreNameXLoc = screenWidth/2 - (4*scoreFontSize);
						scoreScoreXLoc = scoreNameXLoc + (6*scoreFontSize);
						scoreYLoc = screenHeight/7;
						
						if(isEasyMode){
							gameScore = board.getEasyScore();
						}else if(isMediumMode){
							gameScore = board.getMediumScore();
						}else if(isHardMode){
							gameScore = board.getHardScore();
						}
						

						//crab
						health = testCrab.getHealth();
						characterXLoc = testCrab.getXLoc();
						characterYLoc = testCrab.getYLoc();
						yIncr = testCrab.getXIncr();
						xIncr = testCrab.getYIncr();
						xVel = testCrab.getXVel();
						yVel = testCrab.getYVel();


						miniCharacter = board.inWhichCell(characterXLoc,characterYLoc);

						grids = board.getGrid(); 
						mazeWalls = board.getMazeWalls();
						tutWalls = board.getTutWalls();
						numRows = board.getNumRows();
						numCols = board.getNumCols();
						cellWidth = board.getCellWidth();
						cellHeight = board.getCellHeight();
						gameLitter = board.getGameLitter();

						smallWidth = board.getCharacterWidth()/2;
						smallHeight = board.getCharacterHeight()/2;
						mediumWidth = board.getCharacterWidth()*3/4;
						mediumHeight = board.getCharacterHeight()*3/4;
						largeWidth = board.getCharacterWidth();
						largeHeight = board.getCharacterHeight();
						characterWidth = smallWidth;
						characterHeight = smallHeight;

						timeRemainingLabel = "Time Remaining: ";	
						timeRemainingFontSize = screenWidth/50;
						timeRemainingLabelXLoc = (screenWidth/2) - (timeRemainingLabel.length()*timeRemainingFontSize)/4;
						timeRemainingLabelYLoc = featuresBarHeight - ((featuresBarHeight - timeRemainingFontSize)*3)/4;
						timeXLoc = timeRemainingLabelXLoc + timeRemainingLabel.length()*timeRemainingFontSize/2;
						timeYLoc = timeRemainingLabelYLoc;
						
						healthFontSize = screenHeight/20;

						salinityTitleText = "Current Salinity:";
						salinityTitleX = timeXLoc + screenWidth/8;
						salinityTitleFontSize = screenWidth/60;
						salinityTitleY = featuresBarHeight - (featuresBarHeight - salinityTitleFontSize)/2;
						salinityTitleFontStyle = "TimesRoman";

						salinityWarningText = "Watch out! Make sure to monitor your salinity meter!";
						salinityWarningTimeLimit = 300;
						salinityWarningTimeTimer = salinityWarningTimeLimit;
						firstTimeWrongSalinity = true;
						salinityWarningTextX = screenWidth/2 - (salinityWarningText.length()*endTitleFontSize)/5;
						salinityWarningTextY = screenHeight/4;

						meterX = salinityTitleX + (salinityTitleText.length() * salinityTitleFontSize)/2;
						meterY = featuresBarHeight/8;

						meterWidth = (screenWidth - 10) - meterX;
						meterHeight = (featuresBarHeight*4)/5;

						salinityTextSize = salinityTitleFontSize;
						salinityTextStyle = "TimesRoman";

						correctSalinityTextBCrab = "> 20 ppt";
						correctSalinityTextHCrab = "20-30 ppt";
						wrongSalinityTextBCrab = "Too low!";
						wrongSalinityTextHCrab = "Too high!";

						salinityTextArray = new String[2][2];
						salinityTextArray[0][0] = correctSalinityTextHCrab;
						salinityTextArray[0][1] = wrongSalinityTextHCrab;
						salinityTextArray[1][0] = correctSalinityTextBCrab;
						salinityTextArray[1][1] = wrongSalinityTextBCrab;

						currentSalinityText = salinityTextArray[testCrab.getType()][0];
						currentSalinityTextX = meterX + meterWidth/2 - (currentSalinityText.length() * salinityTextSize)/4;
						currentSalinityTextY = meterY + (meterHeight*2)/3;

						currPowerStringFontSize = screenWidth/60;
						currPowerStringFontStyle = "TimesRoman";
						currPowerStringX = testCrab.getXLoc();
						currPowerStringY = testCrab.getYLoc() - characterHeight;
						currPowerUp = 100;

						miniMap = new MiniMap();
						miniWidth = (screenWidth/4)/numCols;
						miniHeight = (screenWidth/4)/numCols;

						arrowWidth = 125;
						arrowHeight = 150;
						upArrowX = meterX + meterWidth/2 - arrowWidth/2;
						upArrowY = meterY + meterHeight;

						leftArrowX = miniWidth * numCols;
						leftArrowY = (miniHeight * numCols)/2 - arrowHeight/2;


						healthImgWidth = (featuresBarHeight*3)/5;
						healthImgHeight = healthImgWidth;
						healthImgXLoc = miniWidth * (numCols+1);
						healthImgYLoc = (featuresBarHeight - healthImgHeight)/2;

						litterWidth = gameLitter.get(0).getWidth();
						litterHeight = gameLitter.get(0).getHeight();
						predators = board.getPredators();
						predSwitchCount = 0;
						gamePowerUps = board.getGamePowerUps();
						powerUpWidth = gamePowerUps.get(0).getWidth();
						powerUpHeight = gamePowerUps.get(0).getHeight();
						endCell  = grids[board.getXEnd()][board.getYEnd()];

						for(MazeCell m: board.getCorrectPath())
							ageStateCells.add(m);

						ageStateCellOriginalCount = board.getCorrectPath().size();
						ageStateCellCurrentCount = ageStateCells.size();
						ageStateCellMediumCount = ageStateCellOriginalCount/3 * 2;
						ageStateCellLargeCount = ageStateCellOriginalCount/3;
						startScreenVisible = false;
						startButton.setVisible(false);
						timeRemaining = totalTime;


						//scoreStuff
						try {
							board.readScoresFromFile();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						
						try {
							board.readBadWordsFromFile();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						//Start Timer
						t.start();
					}
				});
				nameTextField.addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) { 
						//limits text to 3 letter and stops bad words
						if(nameTextField.getText().length() > 4){
							nameTextField.setText("");
						}
						else if(board.getBadWordsList().contains(nameTextField.getText().toLowerCase() + Character.toLowerCase(e.getKeyChar()))){
							e.consume(); 
						}
						else if (nameTextField.getText().length() >= 3){
							e.consume(); 
						}
					}  
				});



				addKeyListener(this);
	}


	//Getters
	public int getScreenWidth(){
		return screenWidth;
	}

	public int getScreenHeight(){
		return screenHeight;
	}
	
	public MazeBoard getBoard(){
		return board;
	}
	
	public Crab getCrab(){
		return testCrab;
	}

}