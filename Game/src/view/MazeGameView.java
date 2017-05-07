package view;

import model.Crab;
import model.MazeBoard;
import model.MazeCell;
import model.MazeWall;
import model.MiniMap;
import model.PowerUp;
import model.Predator;
import model.Litter;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


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

	//Maze Boards
	private MazeBoard easyBoard = new MazeBoard(0, screenWidth,screenHeight);
	private MazeBoard mediumBoard = new MazeBoard(1, screenWidth,screenHeight);
	private MazeBoard hardBoard = new MazeBoard(2, screenWidth,screenHeight);
	private MazeBoard[] boardArr = {easyBoard, mediumBoard, hardBoard};
	private int boardInd = 1;

	//create the maze board
	private MazeBoard board;
	private MazeCell[][] grids;
	private ArrayList<MazeWall> mazeWalls;
	private int numRows;
	private int numCols;

	//miniMap 
	private MiniMap miniMap;
	private int miniWidth;
	private int miniHeight;
	private MazeCell miniCharacter;


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
	private Crab testCrab = new Crab(5,0,screenWidth/2 + 10 ,screenHeight/2 + 10);
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
	private int characterXLoc = testCrab.getXLoc();
	private int characterYLoc = testCrab.getYLoc();
	private int yIncr = testCrab.getXIncr();
	private int xIncr = testCrab.getYIncr();
	private int xVel = testCrab.getXVel();
	private int yVel = testCrab.getYVel();

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

	//Features bar
	private final int featuresBarWidth = screenWidth;
	private final int featuresBarHeight = screenHeight/16;

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
	Litter[] gameLitter;
	private int litterWidth;
	private int litterHeight;
	private int xLitterMax = 0;
	private int xLitterMin = 0;


	//Predator Array List
	private ArrayList<Predator> predators;

	//Health
	private int health = testCrab.getHealth();
	private int hitTimer = 0;
	private int cantBeHitLim = 100;

	private BufferedImage healthImg = createImage("MazeExtraImgs/fullHeart.png");
	private int healthImgWidth;
	private int healthImgHeight;
	private int healthImgXLoc;
	private int healthImgYLoc;

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

	//StartScreen
	private boolean startScreenVisible;
	private JButton hCrabButton;
	private JButton bCrabButton;

	private BufferedImage startBackgroundImg = createImage("background/2D_estuary.jpg");
	private int titleFontSize = screenWidth/50;
	private String titleFontStyle = "TimesRoman";
	private String titleText = "Estuary Maze Adventure!";
	private int titleStringX = screenWidth/2 - ((titleFontSize * titleText.length())/4);
	private int titleStringY = screenHeight/4;

	//End Screen
	private boolean endScreenVisible;
	private int endTitleFontSize = screenWidth/50;
	private String endTitleFontStyle = "TimesRoman";
	private String endLoseText = "Oh no! You lost!";
	private String endWinText = "Congratulations, you won!";
	private int endTitleStringX = screenWidth/2 - ((titleFontSize * titleText.length())/4);
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

	private BufferedImage endBlueCrabImg = createImage("characters/bluecrab_0.png");
	private BufferedImage endHorshoeCrabImg = createImage("characters/horseshoe_crab_left_0.png");

	private BufferedImage[] endCrabImgArray = {endHorshoeCrabImg,endBlueCrabImg};

	//Game state
	private boolean hasWon;

	//Board Buttons
	private JButton easyButton;
	private JButton mediumButton;
	private JButton hardButton;
	private JButton startButton;

	//Age State
	private ArrayList<MazeCell> ageStateCells = new ArrayList<MazeCell>();
	private int ageStateCellOriginalCount;
	private int ageStateCellMediumCount;
	private int ageStateCellLargeCount;
	private int ageStateCellCurrentCount;

	//=================================================================//


	//Constructor
	public MazeGameView(){

		//Buttons
		hCrabButton = new JButton("Horshoe Crab");
		bCrabButton = new JButton("Blue Crab");
		hCrabButton.setFocusable(false);
		bCrabButton.setFocusable(false);

		easyButton = new JButton("Easy");
		easyButton.setFocusable(false);
		mediumButton = new JButton("Medium");
		mediumButton.setFocusable(false);
		hardButton = new JButton("Hard");
		hardButton.setFocusable(false);

		startButton = new JButton("Start Game!");
		startButton.setFocusable(false);

		//StartScreen Visibility
		startScreenVisible = true;
		this.add(bCrabButton);
		this.add(hCrabButton);
		this.add(easyButton);
		this.add(mediumButton);
		this.add(hardButton);
		this.add(startButton);

		//Button Visibility
		easyButton.setVisible(false);
		mediumButton.setVisible(false);
		hardButton.setVisible(false);
		startButton.setVisible(false);

		//Button Listeners
		bCrabButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//initializes type of crab
				testCrab.setType(1);
				testCrab.setDir(0);
				crabNumPics = 3;
				remove(hCrabButton);
				remove(bCrabButton);
				easyButton.setVisible(true);
				mediumButton.setVisible(true);
				hardButton.setVisible(true);
			}

		});

		hCrabButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//initializes type of crab
				testCrab.setType(0);
				testCrab.setDir(2);
				crabNumPics = 2;
				remove(hCrabButton);
				remove(bCrabButton);
				easyButton.setVisible(true);
				mediumButton.setVisible(true);
				hardButton.setVisible(true);
			}

		});

		easyButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boardInd = 0;
				easyButton.setVisible(false);
				mediumButton.setVisible(false);
				hardButton.setVisible(false);
				startButton.setVisible(true);
			}
		});

		mediumButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boardInd = 1;
				easyButton.setVisible(false);
				mediumButton.setVisible(false);
				hardButton.setVisible(false);
				startButton.setVisible(true);
			}
		});

		hardButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boardInd = 2;
				easyButton.setVisible(false);
				mediumButton.setVisible(false);
				hardButton.setVisible(false);
				startButton.setVisible(true);
			}
		});

		startButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				board = boardArr[boardInd];
				grids = board.getGrid(); 
				mazeWalls = board.getMazeWalls();
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
				timeRemainingLabelYLoc = featuresBarHeight - (featuresBarHeight - timeRemainingFontSize)/2;
				timeXLoc = timeRemainingLabelXLoc + timeRemainingLabel.length()*timeRemainingFontSize/2;
				timeYLoc = timeRemainingLabelYLoc;

				salinityTitleText = "Current Salinity:";
				salinityTitleX = timeXLoc + screenWidth/8;
				salinityTitleFontSize = screenWidth/60;
				salinityTitleY = featuresBarHeight - (featuresBarHeight - salinityTitleFontSize)/2;
				salinityTitleFontStyle = "TimesRoman";

				salinityWarningText = "Watch out! Make sure to monitor your salinity meter!";
				salinityWarningTimeLimit = 500;
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

				miniMap = new MiniMap();
				miniWidth = (screenWidth/4)/numRows;
				miniHeight = miniWidth;

				healthImgWidth = (featuresBarHeight*3)/5;
				healthImgHeight = healthImgWidth;
				healthImgXLoc = miniWidth * (numRows+1);
				healthImgYLoc = (featuresBarHeight - healthImgHeight)/2;

				litterWidth = gameLitter[0].getWidth();
				litterHeight = gameLitter[0].getHeight();
				predators = board.getPredators();
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
				remove(startButton);
				timeRemaining = totalTime;

				//Start Timer
				t.start();
			}
		});

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



	//paintComponent
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//START SCREEN DRAWING
		if(startScreenVisible){
			g.drawImage(startBackgroundImg,0,0,screenWidth,screenHeight,this);

			g.setFont(new Font(titleFontStyle,Font.BOLD,titleFontSize));
			g.drawString(titleText,titleStringX,titleStringY);

		}//if


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

			//Draws litter
			for(Litter lit : gameLitter){
				if(lit.getXLoc()+litterWidth > 0 && lit.getXLoc() <= screenWidth && lit.getYLoc()+litterHeight > 0 && lit.getYLoc() < screenHeight)
					g2.drawImage(litterTypes.get(lit.getType()), lit.getXLoc(), lit.getYLoc(),litterWidth, litterHeight, this);
			}

			//Draws Power Ups
			for(PowerUp pu : gamePowerUps){
				g.drawImage(powerUpImg,pu.getXLoc(),pu.getYLoc(), powerUpWidth, powerUpHeight, this);
			}
			//DRAWS PREDATORS
			for(int i = 0; i < predators.size()/2; i++){
				g.drawImage(preds[0][predators.get(i).getDirection()], predators.get(i).getXLoc(), predators.get(i).getYLoc(), predators.get(i).getWidth(), predators.get(i).getHeight(), this);
			}

			for(int i = (predators.size()/2) + 1; i < predators.size(); i++){
				g.drawImage(preds[1][predators.get(i).getDirection()], predators.get(i).getXLoc(), predators.get(i).getYLoc(), predators.get(i).getWidth(), predators.get(i).getHeight(), this);
			}


			//Features Bar Drawing
			g.setColor(Color.yellow);
			g.drawRect(0, 0, featuresBarWidth, featuresBarHeight);
			g.fillRect(0, 0, featuresBarWidth, featuresBarHeight);

			//MINIMAP DRAWING
			//background of minimap
			g.setColor(Color.BLACK);
			g.drawRect(0,0,numRows*miniWidth, numCols*miniHeight);
			g.fillRect(0,0,numRows*miniWidth, numCols*miniHeight);
			//actual lines of minimap
			for(int i = 0; i < numRows; i++){
				for(int j =0; j < numCols; j++){
					MazeCell currG = grids[i][j];
					int topLX = j*miniWidth; //top left corner x value
					int topLY = i*miniHeight; //top left corner y value
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
			miniCharacter = board.inWhichCell(characterXLoc,characterYLoc);
			g.setColor(Color.GREEN);
			g.drawRect(miniCharacter.getX() * miniWidth + miniWidth/4, miniCharacter.getY()*miniHeight + miniHeight/4,miniWidth/2,miniHeight/2);
			g.fillRect(miniCharacter.getX() * miniWidth + miniWidth/4, miniCharacter.getY()*miniHeight + miniHeight/4,miniWidth/2,miniHeight/2);

			//Time Remaining Drawing
			g.setFont(new Font(titleFontStyle,Font.BOLD,timeRemainingFontSize));
			g.setColor(Color.BLACK);
			g.drawString(timeRemainingLabel, timeRemainingLabelXLoc, timeRemainingLabelYLoc);
			g.drawString(String.valueOf(timeRemaining), timeXLoc, timeYLoc);

			//Crab Health Drawing
			g.setColor(Color.BLACK);
			g.setFont(new Font(titleFontStyle,Font.BOLD,titleFontSize));
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
				g.setColor(Color.WHITE);
				g.setFont(new Font(endTitleFontStyle,Font.BOLD,endTitleFontSize));
				g.drawString(salinityWarningText,salinityWarningTextX,salinityWarningTextY);
			}



			//END LOCATION DRAWING
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
					g.drawString(endWinTimeText + (totalTime - timeRemaining) + " seconds", endWinTimeTextX, endWinTimeTextY);
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


	public void actionPerformed(ActionEvent arg0) {

		timeCheck++;
		if(timeCheck == 100){
			timeRemaining--;
			timeCheck = 0;
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
				!board.isHittingAnyWalls(characterXLoc - xVel, characterYLoc - yVel, largeWidth, largeHeight)){
			characterWidth = largeWidth;
			characterHeight = largeHeight;
		} else if(ageStateCellCurrentCount <= ageStateCellMediumCount && characterWidth != mediumWidth && characterWidth != largeWidth &&
				!board.isHittingAnyWalls(characterXLoc - xVel, characterYLoc - yVel, mediumWidth, mediumHeight)){
			characterWidth = mediumWidth;
			characterHeight = mediumHeight;
		}

		//salinityWarningTimeTimer Tick
		if(salinityWarningTimeTimer != salinityWarningTimeLimit){
			salinityWarningTimeTimer++;
		}

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
			t.stop();
		}

		//Swim speed (rendering crab movement)
		if(swimTimer > 0){
			swimTimer--;
		}
		
		if(xVel == 0 && yVel ==0){
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
				if(pu.getType() == 0){
					health += 1;
					p.remove();
				}
				else if(pu.getType() == 1){
					powerUpSpeedTimer = 0;
					xIncr = powerUpSpeed;
					yIncr = powerUpSpeed;
					p.remove();
				}
				else{
					powerUpInvTimer = 0;
					p.remove();
				}
			}
		}

		//checks if speed power up is active
		if(powerUpSpeedTimer < powerUpSpeedLimit){
			powerUpSpeedTimer++;
		}

		//sets crabs speed back to initial (after power up)
		if(powerUpSpeedTimer == powerUpSpeedLimit){
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
				health -= 1;
				hitTimer = 0;
			}

			//checks for predator hits
			if(board.hitAnyPreds(characterXLoc, characterYLoc, characterWidth, characterHeight)){
				health -= 1;
				hitTimer = 0;
			}
		}

		//checks if crab can be hit for this period of time
		if(hitTimer < cantBeHitLim){
			hitTimer++;
		}

		//floats the litter back and forth in a cell
		if(xLitterMax + board.getGameLitter()[0].getFloatXIncr() + litterWidth <= cellWidth){
			board.floatAllLitterRight();
			xLitterMax += board.getGameLitter()[0].getFloatXIncr();
		}
		else if(xLitterMin + board.getGameLitter()[0].getFloatXIncr() + litterWidth <= cellWidth){
			board.floatAllLitterLeft();
			xLitterMin += board.getGameLitter()[0].getFloatXIncr();
		}
		else{
			xLitterMax = 0;
			xLitterMin = 0;
		}
		repaint();


		//PREDATOR TICKS
		board.setRandomDirections();
		board.moveAllPredators();

		repaint();

		//MOVES MAZE IF CRAB IS NOT HITTING WALL
		if(board.isHittingAnyWalls(characterXLoc - xVel, characterYLoc - yVel, characterWidth, characterHeight)){
			return;
		}
		else{
			repaint();
			board.moveGrid(xVel,yVel);
		}
	}

	public void up(){
		testCrab.setDir(0);
		crabDir = testCrab.getDir();

		crabIsMoving = true;
		xVel = 0;
		yVel = yIncr;
	}
	public void down(){
		//checks crab type, sets direction accordingly
		if(testCrab.getType() == 1){
			testCrab.setDir(0);
		}
		else
			testCrab.setDir(1);

		crabIsMoving = true;
		crabDir = testCrab.getDir();
		xVel = 0;
		yVel = -yIncr;
	}
	public void left(){
		//checks crab type, sets direction accordingly
		if(testCrab.getType() == 1){
			testCrab.setDir(0);
		}
		else
			testCrab.setDir(3);

		crabIsMoving = true;
		crabDir = testCrab.getDir();
		xVel = xIncr;
		yVel = 0;
	}
	public void right(){
		//checks crab type, sets direction accordingly
		if(testCrab.getType() == 1){
			testCrab.setDir(0);
		}
		else
			testCrab.setDir(2);

		crabIsMoving = true;
		crabDir = testCrab.getDir();
		xVel = -xIncr;
		yVel = 0;
	}
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

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_UP){
			yVel = 0;
		}
		else if(code == KeyEvent.VK_DOWN){
			yVel = 0;
		}
		else if(code == KeyEvent.VK_LEFT){
			xVel = 0;
		}
		else if(code == KeyEvent.VK_RIGHT){
			xVel = 0;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}



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

	public ArrayList<BufferedImage> makeLitterList(){
		ArrayList<BufferedImage> litterIcons = new ArrayList<BufferedImage>();
		litterIcons.add(createImage("MazeExtraImgs/apple.png"));
		litterIcons.add(createImage("MazeExtraImgs/chipBag.png"));
		litterIcons.add(createImage("MazeExtraImgs/soda.png"));
		litterIcons.add(createImage("mazeExtraImgs/crumbledpaper.png"));
		return litterIcons;
	}


	//Getters
	public int getScreenWidth(){
		return screenWidth;
	}

	public int getScreenHeight(){
		return screenHeight;
	}

}