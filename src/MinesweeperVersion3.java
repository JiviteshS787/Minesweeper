import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.sound.sampled.*;

public class MinesweeperVersion3 extends JPanel implements MouseListener, MouseMotionListener, KeyListener, Runnable{
	
	public static int gameState = 0;
	// Game States
	// GS0 -> Title screen
	// GS1 -> Username Screen
	// GS2 -> Game Screen
	// GS3 -> Game Over
	// GS4 -> HighScore Access Screen
	// GS5 -> HighScore screen
	// GS6 -> Settings screen
	// GS7 -> Credits screen
	// GS8 -> Instructions

	// Board size and number of mines
	public static int size = 17;
	public static int totalMines;
	
	public static boolean closed;
	
	// If a rightclick was detected
	public static boolean rightclick = false;
	
	// Image coordinates used to print the images on the screen
	public static int XImage = 37;
	public static int YImage = 50;
	
	// Mouse click position
	public static int posX;
	public static int posY;
	
	// Used to convert mouse click coordinates to coordinated on the 2D array
	public static int tableX;
	public static int tableY;
	
	// Number of clicks
	public static int clicks = 0;
	
	// For timer
	public static int time = 0;
	public static int framecounter = 0;
	
	// Enter a username in the console
	public static String username = "";
	
	// Board arrays
	public static int [][] board; 
	public static int [][] referenceboard;
	public static boolean[][] trueboard;
	public static boolean[][] ismine;
	public static boolean[][] isflag;
	public static int [][] minecontactboard;
	
	// Images
	public static BufferedImage[] mineNumber = new BufferedImage[10];
	public static BufferedImage[] gamestateBG = new BufferedImage[9];
	public static BufferedImage[] hover = new BufferedImage[10];
	public static String[] hoverimgName = {"Settinghover.png", "Instructionhover.png", "Highscorehover.png", "Credithover.png", "Easyhover.png", "Mediumhover.png", "Hardhover.png", "Menuhover.png", "HSAccesshover.png", "Restarthover.png"};
	public static String[] imgName = {"MinesweeperStart.png", "Username Screen.png", "Minebackground.png", "Game Over.png", "Highscore Access.png", "Highscore.png", "Medium.png", "Credits.png", "Instructions.png"};
	public static String[] imgNumber = {"Mine0.png", "Mine1.png", "Mine2.png", "Mine3.png", "Mine4.png", "Mine5.png", "Mine6.png", "Mine7.png", "Mine8.png", "Mine.png"};
	public static BufferedImage musicOn;
	public static BufferedImage musicOff;
	public static BufferedImage pause;
	public static BufferedImage play;
	public static BufferedImage flag;
	public static BufferedImage flagpress;
	public static BufferedImage MineStart;
	public static BufferedImage noMine;
	public static BufferedImage easyselect;
	public static BufferedImage mediumselect;
	public static BufferedImage hardselect;
	public static BufferedImage soundon;
	public static BufferedImage soundoff;
	public static BufferedImage cursor;
	
	// High score array
	public static int[] highscore = new int[5];
	public static String[] usernamehighscore = new String[5];

	// General Variables
	
	public static int minesFound;
	
	// Used for random mines placing, as math.random() requires a double
	public static double row;
	public static double col;
	
	// Used to count how many mines each square is touching
	public static int minecontact = 0;
	
	// Used to print out the board in the console.
	public static int index = 0;
	
	// x position of where to draw the username
	public static int usernameX = 250;
	
	// If there was a rightclick
	public static boolean isRightClick = false;
	
	// Used to wait for some time after the game ends, to ensure the user has
	// enough time to review the board, and that it does not change the another gameState
	// instantly
	public static int gameOvertransitiontime = 0;
	
	// Same function as gameOvertransitiontime.
	public static int wintransitiontime = 0;
	
	// Used to check if a mine was clicked
	public static boolean mineClicked = false;
	
	// Number of flags.
	public static int flags;
		
	// Decides what level is to be played, default is medium hence it is already true.
	public static boolean easy = false;
	public static boolean hard = false;
	public static boolean medium = true;
	public static boolean easyhighscore = false;
	public static boolean mediumhighscore = false;
	public static boolean hardhighscore = false;
	
	// If sound is on or off
	public static boolean sound = true;
	
	// If sound is played
	public static boolean soundplayed = false;

	// To make the cursor work properly
	public static int cursorappear = 0;
	public static int cursortime = 0;
	public static int cursorX = 250;
	public static boolean draw = false;
	
	// Hovering
	public static boolean settings;
	public static boolean instructions;
	public static boolean highscores;
	public static boolean credits;
	public static boolean easyhover;
	public static boolean mediumhover;
	public static boolean hardhover;
	public static boolean menuhover;
	public static boolean hsAccesshover;
	public static boolean flaghover;
	public static boolean restarthover;
	public static boolean firstClick;
	
	// Pausing
	public static boolean pausePlay;

	// Music
	public static Clip lose;
	
	// Code Starts
	public MinesweeperVersion3(){
		// Setting the screen size and adding the MousListener and KeyLisytener
		setPreferredSize(new Dimension(500,500));
		addMouseListener(this);
		addMouseMotionListener(this);
		Thread thread = new Thread(this);
		thread.start();
		addKeyListener(this);
		this.setFocusable(true);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(gameState == 0)
		{
			// Title screen
			g.drawImage(gamestateBG[gameState],0,0,null);
			
			if(flaghover)
			{
				// if the mouse is hovering on the flag
				g.drawImage(flagpress,186,126,null);
			}
			
			// If the mouse if hovering on these buttons, import the hover image
			if(settings)
			{
				g.drawImage(hover[0],27,260,null);
			}
			else if(instructions)
			{
				g.drawImage(hover[1],261,262,null);
			}
			else if(highscores)
			{
				g.drawImage(hover[2],259,358,null);
			}
			else if(credits)
			{
				g.drawImage(hover[3],28,361,null);
			}
		}
		else if(gameState == 1)
		{
			// User name screen
			g.drawImage(gamestateBG[gameState], 0, 0, null);
			g.setFont(new Font("Courier New", Font.BOLD, 28));
			g.setColor(Color.white);
			g.drawString("Enter a username below", 68, 68);
			// Username to be entered in this rectangle
			g.drawRect(150, 150, 200, 50);
			cursor();
			g.setFont(new Font("Courier New", Font.BOLD, 20));
			g.setColor(Color.white);
			// Drawing the username in the box
			if(cursorappear % 20 == 0)
			{	
				cursortime++;
				if(cursortime <= 17)
				{
					draw = true;
					g.drawImage(cursor,cursorX,160,null);
				}
				else
				{
					cursortime = 0;
					cursorappear ++;
					draw = false;
				}
			}
			g.drawString(username, usernameX, 180);
			g.setFont(new Font("Courier New", Font.BOLD, 28));
			g.setColor(Color.white);
			g.drawString("Right-Click or press ENTER", 34, 320);
			g.drawString("to start", 180, 370);
			if(menuhover)
			{
				g.drawImage(hover[7],148,414,null);
			}
		}
		else if(gameState == 2)
		{
			// Game Screen
			gameState2();
			g.drawImage(gamestateBG[gameState], 0, 0, null);
			g.setFont(new Font("Courier New", Font.BOLD, 20));
			g.setColor(Color.white);
			// Drawing the username in the top left corner and the time in the top
			// right corner.
			if(sound)
			{
				g.drawImage(musicOn,0,0,null);
			}
			else if(!sound)
			{
				g.drawImage(musicOff,0,0,null);
			}
			if(pausePlay)
			{
				g.drawImage(play,470,0,null);
			}
			else if(pausePlay == false)
			{
				g.drawImage(pause,470,0,null);
			}
			g.drawString(username, 37, 34);
			if(clicks < 1)
			{
				if(easy)
				{
					flags = 20;
				}  
				else if(medium)
				{
					flags = 40;
				}
				else if(hard)
				{
					flags = 60;
				}
			}
			g.drawString("Flags:" + flags, 203,34);
			if(time > 99)
			{
				g.drawString("Time:" + time, 365, 34);
			}
			else
			{
				g.drawString("Time:" + time, 375, 34);
			}
			if(clicks == 0)
			{
				// If no clicks are detected draw the board to start the game
				YImage = 50;
				for(int i = 0; i < size; i ++)
				{
					XImage = 37;
					for(int z = 0; z < size; z ++)
					{
						g.drawImage(MineStart,XImage,YImage, null);
						XImage += 25;
					}
					YImage += 25;
				}
			}
			else if(pausePlay)
			{
				// If the game is paused cover up the whole board so people are not
				// able to cheat
				YImage = 50;
				for(int i = 0; i < size; i ++)
				{
					XImage = 37;
					for(int z = 0; z < size; z ++)
					{
						g.drawImage(MineStart,XImage,YImage, null);
						XImage += 25;
					}
					YImage += 25;
				}
			}
			else if(clicks > 0)
			{
				// If click detected and it is within the bounds of the table
				if(tableX >= 0 && tableX < size && tableY >= 0 && tableY < size)
				{
					// If they did not click on a 9 or they rightclicked or they clicked on a 
					// box that has been flaged already
					if(referenceboard[tableX][tableY] != 9 || isRightClick || isflag[tableX][tableY] && mineClicked == false)
					{
						// If they did not click on a mine
						XImage = 37;
						YImage = 50;
						for(int i = 0; i < size; i ++)
						{
							for(int z = 0; z < size; z ++)
							{
								if(isflag[z][i])
								{
									g.drawImage(flag, XImage, YImage, null);
									XImage += 25;
								}
								// If there is a mine in the box, display the MineStart image, which
								// is an empty dark green square
								// Or if there if the box is not touching any mines and it has not been visited
								// already display the same dark green image.
								else if(referenceboard[z][i] == 9 || referenceboard[z][i] == 0 && trueboard[z][i] == false && isflag[z][i] == false)
								{
									g.drawImage(MineStart,XImage,YImage, null);
									XImage += 25;
								}
								// Display the image of how many mines the box is touching. 
								// Ex. 1, 2, 3, 4, 5, 6, 7, 8.
								else
								{
									g.drawImage(mineNumber[referenceboard[z][i]],XImage,YImage, null);
									XImage += 25;
								}
							}
							YImage += 25;
							XImage = 37;
						}
					}
					// If they clicked on a 9 and they did not right click
					else if(referenceboard[tableX][tableY] == 9 && isRightClick == false && !isflag[tableX][tableY])
					{
						XImage = 37;
						YImage = 50;
						for(int i = 0; i < size; i ++)
						{
							for(int z = 0; z < size; z ++)
							{
								if(isflag[z][i] && referenceboard[z][i] == 9)
								{
									g.drawImage(flag, XImage, YImage, null);
									XImage += 25;
								}
								else if(isflag[z][i] && referenceboard[z][i] != 9)
								{
									g.drawImage(noMine, XImage, YImage, null);
									XImage += 25;
									trueboard[z][i] = true;
								}
								// If the box has not been visited and the box does not
								// contain a mine, the image on the box will remain the same
								else if(trueboard[z][i] == false && referenceboard[z][i] != 9)
								{
									g.drawImage(MineStart, XImage, YImage, null);
									XImage += 25;
								}
								// Reveals where all the mines are, draws them on the screen
								else
								{
									g.drawImage(mineNumber[referenceboard[z][i]],XImage,YImage, null);
									XImage += 25;
								}
							}
							YImage += 25;
							XImage = 37;
						}
					}
				}
				else
				{
					// If click is outside the bounds of the board, draw the board unchanged.
					XImage = 37;
					YImage = 50;
					for(int i = 0; i < size; i ++)
					{
						for(int z = 0; z < size; z ++)
						{
							if(isflag[z][i])
							{
								g.drawImage(flag, XImage, YImage, null);
								XImage += 25;
							}
							// If there is a mine in the box, display the MineStart image, which
							// is an empty dark green square
							// Or if there if the box is not touching any mines and it has not been visited
							// already display the same dark green image.
							else if(referenceboard[z][i] == 9 || referenceboard[z][i] == 0 && trueboard[z][i] == false && isflag[z][i] == false)
							{
								g.drawImage(MineStart,XImage,YImage, null);
								XImage += 25;
							}
							// Display the image of how many mines the box is touching. 
							// Ex. 1, 2, 3, 4, 5, 6, 7, 8.
							else
							{
								g.drawImage(mineNumber[referenceboard[z][i]],XImage,YImage, null);
								XImage += 25;
							}
						}
						YImage += 25;
						XImage = 37;
					}
				}
			}
		}
		else if(gameState == 3)
		{
			// Game over screen
			g.drawImage(gamestateBG[gameState],0,0,null);
			g.setFont(new Font("Courier New", Font.BOLD, 48));
			g.setColor(Color.red);
			// How many mines were correctly identified
			if(minesFound < 10)
			{
				g.drawString(String.valueOf(minesFound), 48, 257);
				g.drawString(String.valueOf(totalMines), 274, 257);
			}
			else
			{
				g.drawString(String.valueOf(minesFound), 26, 257);
				g.drawString(String.valueOf(totalMines), 271, 257);
			}
			
			if(menuhover)
			{
				g.drawImage(hover[7],8,409,null);
			}
			else if(restarthover)
			{
				g.drawImage(hover[9],288,406,null);
			}
		}
		else if(gameState == 4)
		{
			// Highscore Access Screen
			g.drawImage(gamestateBG[gameState],0,0,null);
			
			// If mouse is hovering on the burrons, draw the hover images
			if(easyhover)
			{
				g.drawImage(hover[4],170,119,null);
			}
			else if(mediumhover)
			{
				g.drawImage(hover[5],171,210,null);
			}
			else if(hardhover)
			{
				g.drawImage(hover[6],170,310,null);
			}
			
			if(menuhover)
			{
				g.drawImage(hover[7],147,415,null);
			}
		}
		else if(gameState == 5)
		{
			// High score screen
			g.drawImage(gamestateBG[gameState],0,0,null);
			g.setFont(new Font("Courier New", Font.BOLD, 30));
			g.setColor(Color.white);
			// Importing text files based on the level setting selected
			if(easyhighscore)
			{
				// Importing text files
				try 
				{
					Scanner easyinputFile = new Scanner(new File("easyhighscore.txt"));
					highscore[0] = easyinputFile.nextInt();
					highscore[1] = easyinputFile.nextInt();
					highscore[2] = easyinputFile.nextInt();
					highscore[3] = easyinputFile.nextInt();
					highscore[4] = easyinputFile.nextInt();
					Scanner easyusernameInput = new Scanner(new File("easyusername.txt"));
					usernamehighscore[0] = easyusernameInput.nextLine();
					usernamehighscore[1] = easyusernameInput.nextLine();
					usernamehighscore[2] = easyusernameInput.nextLine();
					usernamehighscore[3] = easyusernameInput.nextLine();
					usernamehighscore[4] = easyusernameInput.nextLine();
					
					g.drawString(usernamehighscore[0] + " : " + highscore[0] + " seconds", 7, 135);
					g.drawString(usernamehighscore[1] + " : " + highscore[1] + " seconds", 7, 200);
					g.drawString(usernamehighscore[2] + " : " + highscore[2] + " seconds", 7, 265);
					g.drawString(usernamehighscore[3] + " : " + highscore[3] + " seconds", 7, 330);
					g.drawString(usernamehighscore[4] + " : " + highscore[4] + " seconds", 7, 395);
					easyinputFile.close();
					easyusernameInput.close();
				}
				catch(Exception e) {}
				
			}
			else if(mediumhighscore)
			{
				try
				{
					// Medium Level
					Scanner mediuminputFile = new Scanner(new File("mediumhighscore.txt"));
					highscore[0] = mediuminputFile.nextInt();
					highscore[1] = mediuminputFile.nextInt();
					highscore[2] = mediuminputFile.nextInt();
					highscore[3] = mediuminputFile.nextInt();
					highscore[4] = mediuminputFile.nextInt();
					Scanner mediumusernameInput = new Scanner(new File("mediumusername.txt"));
					usernamehighscore[0] = mediumusernameInput.nextLine();
					usernamehighscore[1] = mediumusernameInput.nextLine();
					usernamehighscore[2] = mediumusernameInput.nextLine();
					usernamehighscore[3] = mediumusernameInput.nextLine();
					usernamehighscore[4] = mediumusernameInput.nextLine();
					
					g.drawString(usernamehighscore[0] + " : " + highscore[0] + " seconds", 7, 135);
					g.drawString(usernamehighscore[1] + " : " + highscore[1] + " seconds", 7, 200);
					g.drawString(usernamehighscore[2] + " : " + highscore[2] + " seconds", 7, 265);
					g.drawString(usernamehighscore[3] + " : " + highscore[3] + " seconds", 7, 330);
					g.drawString(usernamehighscore[4] + " : " + highscore[4] + " seconds", 7, 395);
					mediuminputFile.close();
					mediumusernameInput.close();
				}
				catch(Exception e) {}
			}
			else if(hardhighscore)
			{
				try
				{
					Scanner hardinputFile = new Scanner(new File("hardhighscore.txt"));
					highscore[0] = hardinputFile.nextInt();
					highscore[1] = hardinputFile.nextInt();
					highscore[2] = hardinputFile.nextInt();
					highscore[3] = hardinputFile.nextInt();
					highscore[4] = hardinputFile.nextInt();
					Scanner hardusernameInput = new Scanner(new File("hardusername.txt"));
					usernamehighscore[0] = hardusernameInput.nextLine();
					usernamehighscore[1] = hardusernameInput.nextLine();
					usernamehighscore[2] = hardusernameInput.nextLine();
					usernamehighscore[3] = hardusernameInput.nextLine();
					usernamehighscore[4] = hardusernameInput.nextLine();
					
					g.drawString(usernamehighscore[0] + " : " + highscore[0] + " seconds", 7, 135);
					g.drawString(usernamehighscore[1] + " : " + highscore[1] + " seconds", 7, 200);
					g.drawString(usernamehighscore[2] + " : " + highscore[2] + " seconds", 7, 265);
					g.drawString(usernamehighscore[3] + " : " + highscore[3] + " seconds", 7, 330);
					g.drawString(usernamehighscore[4] + " : " + highscore[4] + " seconds", 7, 395);
					hardinputFile.close();
					hardusernameInput.close();
				}
				catch(Exception e) {}
			}
			if(menuhover)
			{
				g.drawImage(hover[7],277,417,null);
			}
			else if(hsAccesshover)
			{
				g.drawImage(hover[8],21,414,null);
			}
		}
		else if(gameState == 6)
		{
			g.drawImage(gamestateBG[gameState],0,0,null);
	
			// Settings screen
			// Draws the red rectangle on the setting selected
			if(easy)
			{
				g.drawImage(easyselect,0,0,null);
			}
			else if(medium)
			{
				g.drawImage(mediumselect,0,0,null);
			}
			else if(hard)
			{
				g.drawImage(hardselect,0,0,null);
			}
			
			if(sound)
			{
				g.drawImage(soundon,267,129,null);
			}
			else if(!sound)
			{
				g.drawImage(soundoff,377,129,null);
			}
			if(menuhover)
			{
				g.drawImage(hover[7],148,418,null);
			}
		}
		else if(gameState == 7)
		{
			// Credits screen
			g.drawImage(gamestateBG[gameState],0,0,null);
			if(menuhover)
			{
				g.drawImage(hover[7],148,413,null);
			}
		}
		else if(gameState == 8)
		{
			// Instructions screen
			g.drawImage(gamestateBG[gameState],0,0,null);
			if(menuhover)
			{
				g.drawImage(hover[7],148,413,null);
			}
		}
	}
	
	public static void revealCells(int x, int y)
	{	
		// If already visited.
		if(trueboard[x][y])
		{
			return;
		}
		// If it is a mine
		if(minecontactboard[x][y] == 9)
		{
			return;
		}
		// If it is not a mine
		if(minecontactboard[x][y] >= 0 && minecontactboard[x][y] != 9)
		{
			// Copy the number at the index [x][y] in the minecontact board array
			// to the referenceboard array.
			// Set the trueboard value at [x][y] true, as the index has been visited. 
			referenceboard[x][y] = minecontactboard[x][y];
			trueboard[x][y] = true;
		}
		
		// Calling the method on the 8 square the clicked square it touching.
		if(x < size - 1 && y > 0)
		{
			// Only call if the there is a 0 at the index [x][y] in the 
			// minecontactboard array
			if(minecontactboard[x][y] == 0)
			{
				// Top right cell
				revealCells(x+1, y-1);
			}
		}
		if(y > 0)
		{
			// Only call if the there is a 0 at the index [x][y] in the 
			// minecontactboard array
			if(minecontactboard[x][y] == 0)
			{
				// Cell that is directly above
				revealCells(x, y-1);
			}
		}
		if(y > 0 & x > 0)
		{
			// Only call if the there is a 0 at the index [x][y] in the 
			// minecontactboard array
			if(minecontactboard[x][y] == 0)
			{
				// Top left cell
				revealCells(x-1, y-1);
			}
		}
		if(x > 0)
		{
			// Only call if the there is a 0 at the index [x][y] in the 
			// minecontactboard array
			if(minecontactboard[x][y] == 0)
			{
				// Left cell
				revealCells(x-1, y);
			}
		}
		if(y < size - 1 && x > 0)
		{
			// Only call if the there is a 0 at the index [x][y] in the 
			// minecontactboard array
			if(minecontactboard[x][y] == 0)
			{
				// Bottom left cell
				revealCells(x-1, y+1);
			}
			
		}
		if(y < size - 1)
		{
			// Only call if the there is a 0 at the index [x][y] in the 
			// minecontactboard array
			if(minecontactboard[x][y] == 0)
			{
				// Cell that is directly underneath
				revealCells(x,y+1);
			}
		}
		if(x < size - 1 && y < size - 1)
		{
			// Only call if the there is a 0 at the index [x][y] in the 
			// minecontactboard array
			if(minecontactboard[x][y] == 0)
			{
				// Bottom right cell
				revealCells(x+1, y+1);
			}
		}
		if(x < size - 1)
		{
			// Only call if the there is a 0 at the index [x][y] in the 
			// minecontactboard array
			if(minecontactboard[x][y] == 0)
			{
				// Right cell
				revealCells(x+1, y);
			}
		}
	}

	public static void highscoreUpdate() throws IOException
	{	
		// Only imports the text file of the level chosen, and then updates the
		// text file based on the score of the user.
		if(easy)
		{
			try 
			{
				Scanner easyinputFile = new Scanner(new File("easyhighscore.txt"));
				highscore[0] = easyinputFile.nextInt();
				highscore[1] = easyinputFile.nextInt();
				highscore[2] = easyinputFile.nextInt();
				highscore[3] = easyinputFile.nextInt();
				highscore[4] = easyinputFile.nextInt();
				Scanner easyusernameInput = new Scanner(new File("easyusername.txt"));
				usernamehighscore[0] = easyusernameInput.nextLine();
				usernamehighscore[1] = easyusernameInput.nextLine();
				usernamehighscore[2] = easyusernameInput.nextLine();
				usernamehighscore[3] = easyusernameInput.nextLine();
				usernamehighscore[4] = easyusernameInput.nextLine();
			}
			catch(Exception e) {}
			
			if(time < highscore[0] || highscore[0] == 0)
			{
				// In this case the highscore has to be < and not > because 
				// it checks who finishes the game the fastest
				// Although, if the highscore at the index is 0, it just replaces it
				// with the time, as you cannot finish the game in 0 seconds.
				// Changes the gamestate to 4, which is the highscore screen
				if(time < highscore[0] && highscore[0] != 0)
				{
					highscore[4] = highscore[3];
					highscore[3] = highscore[2];
					highscore[2] = highscore[1];
					highscore[1] = highscore[0];
					highscore[0] = time;
					usernamehighscore[4] = usernamehighscore[3];
					usernamehighscore[3] = usernamehighscore[2];
					usernamehighscore[2] = usernamehighscore[1];
					usernamehighscore[1] = usernamehighscore[0];
					usernamehighscore[0] = username;
					gameState = 4;
				}
				else if(highscore[0] == 0)
				{
					usernamehighscore[0] = username;
					highscore[0] = time;
					gameState = 4;
				}
				
			}
			else if(time < highscore[1] || highscore[1] == 0)
			{
				if(time < highscore[1] && highscore[1] != 0)
				{
					highscore[4] = highscore[3];
					highscore[3] = highscore[2];
					highscore[2] = highscore[1];
					highscore[1] = time;
					usernamehighscore[4] = usernamehighscore[3];
					usernamehighscore[3] = usernamehighscore[2];
					usernamehighscore[2] = usernamehighscore[1];
					usernamehighscore[1] = username;
					gameState = 4;
				}
				else if(highscore[1] == 0)
				{
					
					usernamehighscore[1] = username;
					highscore[1] = time;
					gameState = 4;
				}
				
			}
			else if(time < highscore[2] || highscore[2] == 0)
			{
				if(time < highscore[2] && highscore[2] != 0)
				{
					highscore[4] = highscore[3];
					highscore[3] = highscore[2];
					highscore[2] = time;
					usernamehighscore[4] = usernamehighscore[3];
					usernamehighscore[3] = usernamehighscore[2];
					usernamehighscore[2] = username;
					gameState = 4;
				}
				else if(highscore[2] == 0)
				{
					usernamehighscore[2] = username;
					highscore[2] = time;
					gameState = 0;
				}
				
			}
			else if(time < highscore[3] || highscore[3] == 0)
			{
				if(time < highscore[3] && highscore[3] != 0)
				{
					highscore[4] = highscore[3];
					highscore[3] = time;
					usernamehighscore[4] = usernamehighscore[3];
					usernamehighscore[3] = username;
					gameState = 4;
				}
				else if(highscore[3] == 0)
				{
					usernamehighscore[3] = username;
					highscore[3] = time;
					gameState = 4;
				}
				
			}
			else if(time < highscore[4] || highscore[4] == 0)
			{
				if(time < highscore[4] && highscore[4] != 0)
				{
					usernamehighscore[4] = username;
					highscore[4] = time;
					gameState = 4;
				}
				else if(highscore[4] == 0)
				{
					usernamehighscore[4] = username;
					highscore[4] = time;
					gameState = 4;
				}
				
			}			
			
			PrintWriter easyusernameFile = new PrintWriter(new FileWriter("easyusername.txt"));
			PrintWriter easyoutputFile = new PrintWriter(new FileWriter("easyhighscore.txt"));
			for(int i = 0; i < 5; i ++)
			{
				easyoutputFile.println(highscore[i]);
			}
			easyoutputFile.close();
			
			for(int i = 0; i < 5; i ++)
			{
				if(usernamehighscore[i].length() != 12)
				{
					easyusernameFile.printf("%12s%n", usernamehighscore[i]);
				}
				else
				{
					easyusernameFile.println(usernamehighscore[i]);
				}
			}
			easyusernameFile.close();
		}
		else if(medium)
		{
			try
			{
				// Medium Level
				Scanner mediuminputFile = new Scanner(new File("mediumhighscore.txt"));
				highscore[0] = mediuminputFile.nextInt();
				highscore[1] = mediuminputFile.nextInt();
				highscore[2] = mediuminputFile.nextInt();
				highscore[3] = mediuminputFile.nextInt();
				highscore[4] = mediuminputFile.nextInt();
				Scanner mediumusernameInput = new Scanner(new File("mediumusername.txt"));
				usernamehighscore[0] = mediumusernameInput.nextLine();
				usernamehighscore[1] = mediumusernameInput.nextLine();
				usernamehighscore[2] = mediumusernameInput.nextLine();
				usernamehighscore[3] = mediumusernameInput.nextLine();
				usernamehighscore[4] = mediumusernameInput.nextLine();
			}
			catch(Exception e) {}
						
			if(time < highscore[0] || highscore[0] == 0)
			{
				if(time < highscore[0] && highscore[0] != 0)
				{
					highscore[4] = highscore[3];
					highscore[3] = highscore[2];
					highscore[2] = highscore[1];
					highscore[1] = highscore[0];
					highscore[0] = time;
					usernamehighscore[4] = usernamehighscore[3];
					usernamehighscore[3] = usernamehighscore[2];
					usernamehighscore[2] = usernamehighscore[1];
					usernamehighscore[1] = usernamehighscore[0];
					usernamehighscore[0] = username;
					gameState = 4;
				}
				else if(highscore[0] == 0)
				{
					usernamehighscore[0] = username;
					highscore[0] = time;
					gameState = 4;
				}
				
			}
			else if(time < highscore[1] || highscore[1] == 0)
			{
				if(time < highscore[1] && highscore[1] != 0)
				{
					highscore[4] = highscore[3];
					highscore[3] = highscore[2];
					highscore[2] = highscore[1];
					highscore[1] = time;
					usernamehighscore[4] = usernamehighscore[3];
					usernamehighscore[3] = usernamehighscore[2];
					usernamehighscore[2] = usernamehighscore[1];
					usernamehighscore[1] = username;
					gameState = 4;
				}
				else if(highscore[1] == 0)
				{
					
					usernamehighscore[1] = username;
					highscore[1] = time;
					gameState = 4;
				}
				
			}
			else if(time < highscore[2] || highscore[2] == 0)
			{
				if(time < highscore[2] && highscore[2] != 0)
				{
					highscore[4] = highscore[3];
					highscore[3] = highscore[2];
					highscore[2] = time;
					usernamehighscore[4] = usernamehighscore[3];
					usernamehighscore[3] = usernamehighscore[2];
					usernamehighscore[2] = username;
					gameState = 4;
				}
				else if(highscore[2] == 0)
				{
					usernamehighscore[2] = username;
					highscore[2] = time;
					gameState = 0;
				}
				
			}
			else if(time < highscore[3] || highscore[3] == 0)
			{
				if(time < highscore[3] && highscore[3] != 0)
				{
					highscore[4] = highscore[3];
					highscore[3] = time;
					usernamehighscore[4] = usernamehighscore[3];
					usernamehighscore[3] = username;
					gameState = 4;
				}
				else if(highscore[3] == 0)
				{
					usernamehighscore[3] = username;
					highscore[3] = time;
					gameState = 4;
				}
				
			}
			else if(time < highscore[4] || highscore[4] == 0)
			{
				if(time < highscore[4] && highscore[4] != 0)
				{
					usernamehighscore[4] = username;
					highscore[4] = time;
					gameState = 4;
				}
				else if(highscore[4] == 0)
				{
					usernamehighscore[4] = username;
					highscore[4] = time;
					gameState = 4;
				}
				
			}		
			
			PrintWriter mediumusernameFile = new PrintWriter(new FileWriter("mediumusername.txt"));
			PrintWriter mediumoutputFile = new PrintWriter(new FileWriter("mediumhighscore.txt"));
			for(int i = 0; i < 5; i ++)
			{
				mediumoutputFile.println(highscore[i]);
			}
			mediumoutputFile.close();
			
			for(int i = 0; i < 5; i ++)
			{
				if(usernamehighscore[i].length() != 12)
				{
					mediumusernameFile.printf("%12s%n", usernamehighscore[i]);
				}
				else
				{
					mediumusernameFile.println(usernamehighscore[i]);
				}
			}
			mediumusernameFile.close();			
		}
		else if(hard)
		{
			try
			{
				Scanner hardinputFile = new Scanner(new File("hardhighscore.txt"));
				highscore[0] = hardinputFile.nextInt();
				highscore[1] = hardinputFile.nextInt();
				highscore[2] = hardinputFile.nextInt();
				highscore[3] = hardinputFile.nextInt();
				highscore[4] = hardinputFile.nextInt();
				Scanner hardusernameInput = new Scanner(new File("hardusername.txt"));
				usernamehighscore[0] = hardusernameInput.nextLine();
				usernamehighscore[1] = hardusernameInput.nextLine();
				usernamehighscore[2] = hardusernameInput.nextLine();
				usernamehighscore[3] = hardusernameInput.nextLine();
				usernamehighscore[4] = hardusernameInput.nextLine();
			}
			catch(Exception e) {}
			
			if(time < highscore[0] || highscore[0] == 0)
			{
				if(time < highscore[0] && highscore[0] != 0)
				{
					highscore[4] = highscore[3];
					highscore[3] = highscore[2];
					highscore[2] = highscore[1];
					highscore[1] = highscore[0];
					highscore[0] = time;
					usernamehighscore[4] = usernamehighscore[3];
					usernamehighscore[3] = usernamehighscore[2];
					usernamehighscore[2] = usernamehighscore[1];
					usernamehighscore[1] = usernamehighscore[0];
					usernamehighscore[0] = username;
					gameState = 4;
				}
				else if(highscore[0] == 0)
				{
					usernamehighscore[0] = username;
					highscore[0] = time;
					gameState = 4;
				}
				
			}
			else if(time < highscore[1] || highscore[1] == 0)
			{
				if(time < highscore[1] && highscore[1] != 0)
				{
					highscore[4] = highscore[3];
					highscore[3] = highscore[2];
					highscore[2] = highscore[1];
					highscore[1] = time;
					usernamehighscore[4] = usernamehighscore[3];
					usernamehighscore[3] = usernamehighscore[2];
					usernamehighscore[2] = usernamehighscore[1];
					usernamehighscore[1] = username;
					gameState = 4;
				}
				else if(highscore[1] == 0)
				{
					
					usernamehighscore[1] = username;
					highscore[1] = time;
					gameState = 4;
				}
				
			}
			else if(time < highscore[2] || highscore[2] == 0)
			{
				if(time < highscore[2] && highscore[2] != 0)
				{
					highscore[4] = highscore[3];
					highscore[3] = highscore[2];
					highscore[2] = time;
					usernamehighscore[4] = usernamehighscore[3];
					usernamehighscore[3] = usernamehighscore[2];
					usernamehighscore[2] = username;
					gameState = 4;
				}
				else if(highscore[2] == 0)
				{
					usernamehighscore[2] = username;
					highscore[2] = time;
					gameState = 0;
				}
				
			}
			else if(time < highscore[3] || highscore[3] == 0)
			{
				if(time < highscore[3] && highscore[3] != 0)
				{
					highscore[4] = highscore[3];
					highscore[3] = time;
					usernamehighscore[4] = usernamehighscore[3];
					usernamehighscore[3] = username;
					gameState = 4;
				}
				else if(highscore[3] == 0)
				{
					usernamehighscore[3] = username;
					highscore[3] = time;
					gameState = 4;
				}
				
			}
			else if(time < highscore[4] || highscore[4] == 0)
			{
				if(time < highscore[4] && highscore[4] != 0)
				{
					usernamehighscore[4] = username;
					highscore[4] = time;
					gameState = 4;
				}
				else if(highscore[4] == 0)
				{
					usernamehighscore[4] = username;
					highscore[4] = time;
					gameState = 4;
				}
				
			}		
			
			PrintWriter hardusernameFile = new PrintWriter(new FileWriter("hardusername.txt"));
			PrintWriter hardoutputFile = new PrintWriter(new FileWriter("hardhighscore.txt"));
			for(int i = 0; i < 5; i ++)
			{
				hardoutputFile.println(highscore[i]);
			}
			hardoutputFile.close();
			
			for(int i = 0; i < 5; i ++)
			{
				if(usernamehighscore[i].length() != 12)
				{
					hardusernameFile.printf("%12s%n", usernamehighscore[i]);
				}
				else
				{
					hardusernameFile.println(usernamehighscore[i]);
				}
			}
			hardusernameFile.close();
		}
		
	}

	public static void main(String[] args)
	{	
		// Setting board array lengths
		board = new int[size][size];
		trueboard = new boolean[size][size];
		referenceboard = new int[size][size];
		minecontactboard = new int[size][size];
		ismine = new boolean[size][size];
		isflag = new boolean[size][size];
				
		try
		{
			// Image importation
			for(int i = 0; i < 9; i++)
			{
				gamestateBG[i] = ImageIO.read(new File(imgName[i]));
			}
			for(int i = 0; i < 10; i++)
			{
				mineNumber[i] = ImageIO.read(new File(imgNumber[i]));
			}
			for(int i = 0; i < 10; i ++)
			{
				hover[i] = ImageIO.read(new File(hoverimgName[i]));
			}
			noMine = ImageIO.read(new File("NoMine.png"));
			MineStart = ImageIO.read(new File("MineStart.png"));
			easyselect = ImageIO.read(new File("Easy.png"));
			soundon = ImageIO.read(new File("onselect.png"));
			soundoff = ImageIO.read(new File("offselect.png"));
			pause = ImageIO.read(new File("Pause.png"));
			play = ImageIO.read(new File("Play.png"));
			mediumselect = ImageIO.read(new File("Medium.png"));
			hardselect = ImageIO.read(new File("Hard.png"));
			flag = ImageIO.read(new File("MineFlag.png"));
			flagpress = ImageIO.read(new File("Flagclicked.png"));
			cursor = ImageIO.read(new File("Cursor.png"));
			musicOn = ImageIO.read(new File("Musicon.png"));
			musicOff = ImageIO.read(new File("Musicoff.png"));
			
		}
		catch(Exception e) {}
		JFrame frame = new JFrame("MinesweeperVersion3");
		MinesweeperVersion3 panel = new MinesweeperVersion3();
		frame.add(panel);
		frame.setVisible(true);
		frame.pack();
		// Makes the JFrame appear in the center of the screen
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

	}
	
	public static boolean win()
	{
		// Checks if every mine is marked with a flag and that all the boxes without
		// mines have been revealed.
		firstClick = false;
		for(int i = 0; i < size; i ++)
		{
			for(int j = 0; j < size; j ++)
			{
				if(referenceboard[j][i] == 9 && !isflag[j][i] || referenceboard[j][i] != 9 && isflag[j][i])
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public static void music()
	{
		try
		{
			lose = AudioSystem.getClip();
			AudioInputStream audio = AudioSystem.getAudioInputStream(new File("MineExploding.wav"));
			lose.open(audio);
			lose.start();
		}
		catch(Exception e){}
	}
	
	public static void gameState2()
	{
		if(gameState == 2)
		{
			if(!pausePlay)
			{
				if(clicks > 0) {
					// Counts the frames every time the screen refreshes
					framecounter++;
				}
				
				// We can only check if there is a mines at the tableX and tableY
				// index if their values are greater than 0, which is what
				// we are checking here.
				if(tableX >= 0 && tableY >= 0 && tableX < size && tableY < size)
				{
					// Ensures that after losing or winning a little time is given to the user to review the board
					// before repainting another gameState.
					if(referenceboard[tableX][tableY] == 9 && !isRightClick && !isflag[tableX][tableY])
					{
						gameOvertransitiontime++;
						if(gameOvertransitiontime == 120)
						{
							for(int i = 0; i < size; i ++)
							{
								for(int j = 0; j < size; j ++)
								{
									if(isflag[j][i] && referenceboard[j][i] == 9)
									{
										minesFound++;
									}
								}
							}
							gameState = 3;
						}
					}
					else if(clicks > 1 && win())
					{
						wintransitiontime++;
						if(wintransitiontime == 80)
						{
							try
							{
								highscoreUpdate();
							}
							catch(Exception e) {}
							gameState = 5;
						}
					}
					// Checking if framecounter is 40 and not 50 because when I check for 50 it counts way too slow. I want the time to 
					// display seconds and the 50 is just too slow. Only checks until a mine is clicked, if a mine is clicked
					// the timer will stop
					else if(framecounter == 40 && (referenceboard[tableX][tableY] != 9 || isRightClick || referenceboard[tableX][tableY] == 9 && isflag[tableX][tableY]))
					{
						if(firstClick || clicks > 0) {
							time++;
						}
						framecounter = 0;
					}
				}
				else
				{
					// Just keep counting time.
					if(framecounter == 40 || clicks > 0)
					{
						if(firstClick) {
							time++;
						}
						framecounter = 0;
					}
				}
			}
		}
	}
	
	public static void cursor()
	{
		if(gameState == 1)
		{
			if(!draw)
			{
				cursorappear++;	
			}		
		}
	}
	
	public void run() {
		while(true)
		{
			try 
			{
				// Repaint the screen every 0.02 seconds. 50 fps.
				Thread.sleep(20);
				repaint();
			}
			catch(Exception e) {}
		}
	}
	
	public void keyPressed(KeyEvent e) 
	{
		if(gameState == 1)
		{
			// Checks if the space bar is pressed, changes the gameState to the
			// game screen
			if(e.getKeyCode() >= 65 && e.getKeyCode() <= 90 || e.getKeyCode() >= 48 & e.getKeyCode() <= 57)
			{			
				// This checks the character limit, no more than 12 characters can
				// fit in the given box.
				if(username.length() < 12)
				{
					username += e.getKeyChar();
					// The cursor starts in the middle of the box, and every character
					// is approximately 6 pixels in length, so the X position of the 
					// username String when it is being draw has to be changed to ensure
					// it stays in the middle of the given box.
					usernameX = usernameX - 6;
					cursorX = cursorX + 6;
				}
				
			}
			if(e.getKeyCode() == 8 && username.length() > 0)
			{
				// If the backpsace button is presses the username String has 
				// to remove one character each time the button is presses.
				// To do this the substring from the start of the String the one character
				// before the end has to be grabbed.
				username = username.substring(0,username.length()-1);
				// The X position of the username String has to add 6 pixels now as one
				// character is lost when the backspace is presses. This is to ensure
				// the username stays in the center of the box when it being drawn on the 
				// screen.
				usernameX = usernameX + 6;
				cursorX = cursorX - 6;
			}	
			if(username.length() > 0)
			{
				if(e.getKeyCode() == 10)
				{
					gameState = 2;
				}
			}
		}
	}
	
	public void mousePressed(MouseEvent e)
	{
		if(gameState == 0)
		{
			// Title screen
			posX = e.getX();
			posY = e.getY();
			if(posX >= 186 && posX <= 312 && posY >= 129 && posY <= 249)
			{
				gameState = 1;
				flaghover = false;
			}
			else if(posX >= 40 && posX <= 235 && posY >= 275 && posY <= 335)
			{
				// Settings button
				gameState = 6;
				settings = false;
			}
			else if(posX >= 270 && posX <= 465 && posY >= 275 && posY <= 335)
			{
				// Instructions screen
				gameState = 8;
				instructions = false;
			}
			else if (posX >= 40 && posX <= 235 && posY >= 370 && posY <= 425)
			{
				// Credits screen
				gameState = 7;
				credits = false;
			}
			else if(posX >= 270 && posX <= 465 && posY >= 370 && posY <= 425)
			{
				// Highscore Access screen
				gameState = 4;
				highscores = false;
			}
		}
		else if(gameState == 1)
		{
			// Username screen
			posX = e.getX();
			posY = e.getY();
			if(posX >= 160 && posX <= 340 && posY >= 422 && posY <= 475)
			{
				if(e.getButton() != MouseEvent.BUTTON3)
				gameState = 0;
				menuhover = false;
			}
			if(username.length() > 0 && e.getButton() == MouseEvent.BUTTON3)
			{
				gameState = 2;
			}
		}
		else if(gameState == 2)
		{	
			// Game screen
			posX = e.getX();
			posY = e.getY();
			if(!mineClicked)
			{
				firstClick = true;
				if(posX >= 0 && posX <= 25 && posY >= 0 && posY <= 25)
				{
					sound = !sound;
				}
				if(posX >= 470 && posX <= 500 && posY >= 0 && posY <= 28)
				{
					pausePlay = !pausePlay;
				}
			}
			if(pausePlay == false)
			{
				if(clicks < 1)
				{
					// Checking how many mines need to be placed on the board.
					if(easy)
					{
						totalMines = 20;
						flags = totalMines;
						easyhighscore = true;
						mediumhighscore = false;
						hardhighscore = false;
					}
					else if(medium)
					{
						totalMines = 40;
						flags = totalMines;
						easyhighscore = false;
						mediumhighscore = true;
						hardhighscore = false;
					}
					else if(hard)
					{
						totalMines = 60;
						easyhighscore = false;
						mediumhighscore = false;
						hardhighscore = true;
						flags = totalMines;
					}
				}
				if(posX >= 37 && posX <= 463 && posY >= 50 && posY <= 475)
				{
					// If the mouse click is within the board, then it adds a click
					clicks ++;
				}
				if(clicks == 1)
				{
	 				tableX = (posX-37)/25;
					tableY = (posY-50)/25;
					if(tableX >= 0 && tableX < size && tableY >= 0 && tableY < size)
					{
						// Random mine placing on the board array, this array will never be changed
						// after this.
						for(int i = 0; i < totalMines; )
						{
							row = Math.random() * (size-0)+0;
							col = Math.random() * (size-0)+0;
								
							int x = (int)col;
							int y = (int)row;
							
							// Not placing a mine on the square that was clicked, and the 8 squares around it.
							if(x == tableX && y == tableY)
							{
								i = i;
							}
							else if(x < size - 1 && x == tableX + 1 && y == tableY)
							{
								i = i;
							}
							else if(x < size - 1 && y > 0 && x == tableX + 1 && y == tableY - 1)
							{
								i = i;
							}
							else if(y > 0 && y == tableY - 1 && x == tableX)
							{
								i = i;
							}
							else if(x > 0 & y > 0 && x == tableX - 1 && y == tableY - 1)
							{
								i = i;
							}
							else if(x > 0 && y == tableY && x == tableX - 1)
							{
								i = i;
							}
							else if(x > 0 && y < size - 1 && x == tableX - 1 && y == tableY + 1)
							{
								i = i;
							}
							else if(y < size - 1 && x == tableX && y == tableY + 1)
							{
								i = i;
							}
							else if(x < size - 1 && y < size - 1 && x == tableX + 1 && y == tableY + 1)
							{
								i = i;
							}
							else if(board[x][y] == 0)
							{
								board[x][y] = 9;
								i++;
							}
						}
							
						// Copy all the values in the main array to reference array. This is the
						// array which will be used when it comes to graphics.
						for(int i = 0; i < size; i ++)
						{
							for(int j = 0; j < size; j ++)
							{
								referenceboard[j][i] = board[j][i];
							}
						}
							
						// Counting how many mines are touched by each square and it stores
						// that value at the same index in the minecontactboard array.
						for(int y = 0; y < size; y ++)
						{
							for(int x = 0; x < size; x ++)
							{
								minecontact = 0;
								if(board[x][y] == 9)
								{
									minecontactboard[x][y] = 9;
								}
								else if(board[x][y] == 0)
								{
									if(x < size - 1)
									{
										if(board[x + 1][y] == 9)
										{
											minecontact++;
										}
									}
									if(y < size - 1)
									{
										if(board[x][y+1] == 9)
										{
											minecontact++;
										}
									}
									if(x < size - 1 && y < size - 1)
									{
										if(board[x + 1][y + 1] == 9)
										{
											minecontact++;
										}
									}
									if(y > 0 && x < size - 1)
									{
										if(board[x + 1][y - 1] == 9)
										{
											minecontact++;
										}
									}
									if(y > 0)
									{
										if(board[x][y-1] == 9)
										{
											minecontact++;
										}
									}
									if(x > 0 && y > 0)
									{
										if(board[x - 1][y - 1] == 9)
										{
										minecontact++;	
										}
									}
									if(x > 0)
									{
										if(board[x - 1][y] == 9)
										{
											minecontact++;
										}
									}
									if(x > 0 && y < size - 1)
									{
										if(board[x - 1][y + 1] == 9)
										{
											minecontact++;							
										}				
									}	
									minecontactboard[x][y] = minecontact;
								}
							}
						}						
						revealCells(tableX, tableY);
					}
				}
				if(clicks > 1)
				{
					// Checks for a right click
					if(mineClicked == false)
					{
						if(e.getButton() == MouseEvent.BUTTON3)
						{
							tableX = (posX-37)/25;
							tableY = (posY-50)/25;
							// If the user has already right clicked in the given square
							// the boolean will be changed to false, and vice versa.
							if(tableX >= 0 && tableX < size && tableY >= 0 && tableY < size)
							{
								if(trueboard[tableX][tableY])
								{
									isflag[tableX][tableY] = false;
								}
								else
								{
									if(isflag[tableX][tableY] == true)
									{
										isflag[tableX][tableY] = false;
										flags++;
									}
									else if(isflag[tableX][tableY] == false)
									{
										isflag[tableX][tableY] = true;
										flags--;
									}
									isRightClick = true;
								}	
							}					
						}
						else
						{
							isRightClick = false;
							tableX = (posX-37)/25;
							tableY = (posY-50)/25;
							// Checks if there is no mine present on the referenceboard array where 
							// the click occurred, only then does it run the revealCells() method
							if(tableX >= 0 && tableX < size && tableY >= 0 && tableY < size)
							{
								if(referenceboard[tableX][tableY] == 9 && !isRightClick && !isflag[tableX][tableY])
								{
									mineClicked = true;
									if(!soundplayed && sound)
									{
										music();
										soundplayed = true;
									}
								}
								else if(referenceboard[tableX][tableY] != 9 && !isflag[tableX][tableY])
								{
									revealCells(tableX, tableY);
								}
							}
						}
					}
				}
			}
		}
		else if(gameState == 3)
		{
			// Game Over screen
			posX = e.getX();
			posY = e.getY();
			firstClick = false;
			if(posX >= 20 && posX <= 200 && posY >= 418 && posY <= 468)
			{
				gameState = 0;
				for(int i = 0; i < size; i ++)
				{
					for(int j = 0; j < size; j ++)
					{
						board[j][i] = 0;
						minecontactboard[j][i] = 0;
						referenceboard[j][i] = 0;
						isflag[j][i] = false;
						trueboard[j][i] = false;
					}
				}
				clicks = 0;
				flags = totalMines;
				minesFound = 0;
				rightclick = false;
				mineClicked = false;
				soundplayed = false;
				settings = false;
				instructions = false;
				highscores = false;
				credits = false;
				easyhover = false;
				hardhover = false;
				mediumhover = false;
				menuhover = false;
				restarthover = false;
				draw = false;
				hsAccesshover = false;
				cursorappear = 0;
				cursortime = 0;
				gameOvertransitiontime = 0;
				wintransitiontime = 0;
				time = 0;
				flags = 0;
				framecounter = 0;
			}
			else if(posX >= 300 && posX <= 480 && posY >= 418 && posY <= 468)
			{
				gameState = 1;
				for(int i = 0; i < size; i ++)
				{
					for(int j = 0; j < size; j ++)
					{
						board[j][i] = 0;
						minecontactboard[j][i] = 0;
						referenceboard[j][i] = 0;
						isflag[j][i] = false;
						trueboard[j][i] = false;
					}
				}
				clicks = 0;
				flags = totalMines;
				minesFound = 0;
				rightclick = false;
				mineClicked = false;
				soundplayed = false;
				settings = false;
				instructions = false;
				highscores = false;
				credits = false;
				easyhover = false;
				hardhover = false;
				mediumhover = false;
				menuhover = false;
				restarthover = false;
				draw = false;
				hsAccesshover = false;
				cursorappear = 0;
				cursortime = 0;
				gameOvertransitiontime = 0;
				wintransitiontime = 0;
				time = 0;
				flags = 0;
				framecounter = 0;
			}
		}
		else if(gameState == 4)
		{
			// HS Access screen
			// Checks for which highscore to display
			posX = e.getX();
			posY = e.getY();
			
			if(posX >= 182 && posX <= 320 && posY >= 130 && posY <= 190)
			{
				easyhighscore = true;
				mediumhighscore = false;
				hardhighscore = false;
				easyhover = false;
				gameState = 5;
			}
			else if(posX >= 182 && posX <= 320 && posY >= 223 && posY <= 283)
			{
				mediumhighscore = true;
				mediumhover = false;
				easyhighscore = false;
				hardhighscore = false;
				gameState = 5;
			}
			else if(posX >= 182 && posX <= 320 && posY >= 322 && posY <= 382)
			{
				hardhighscore = true;
				hardhover = false;
				easyhighscore = false;
				mediumhighscore = false;
				gameState = 5;
			}
			if(posX >= 160 && posX <= 340 && posY >= 424 && posY <= 477)
			{
				gameState = 0;
				menuhover = false;
			}
		}
		else if(gameState == 5)
		{
			// Highscore screen
			// Reset everything to default settings so the user can restart the game
			// without closing the window.
			posX = e.getX();
			posY = e.getY();
			if(posX >= 30 && posX <= 210 && posY >= 426 && posY <= 478)
			{
				gameState = 4;
				for(int i = 0; i < size; i ++)
				{
					for(int j = 0; j < size; j ++)
					{
						board[j][i] = 0;
						minecontactboard[j][i] = 0;
						referenceboard[j][i] = 0;
						isflag[j][i] = false;
						trueboard[j][i] = false;
					}
				}
				clicks = 0;
				flags = totalMines;
				minesFound = 0;
				rightclick = false;
				mineClicked = false;
				soundplayed = false;
				settings = false;
				instructions = false;
				highscores = false;
				credits = false;
				draw = false;
				hsAccesshover = false;
				cursorappear = 0;
				cursortime = 0;
				gameOvertransitiontime = 0;
				wintransitiontime = 0;
				time = 0;
				flags = 0;
			}
			if(posX >= 290 && posX <= 470 && posY >= 426 && posY <= 478)
			{
				gameState = 0;
				for(int i = 0; i < size; i ++)
				{
					for(int j = 0; j < size; j ++)
					{
						board[j][i] = 0;
						minecontactboard[j][i] = 0;
						referenceboard[j][i] = 0;
						isflag[j][i] = false;
						trueboard[j][i] = false;
					}
				}
				clicks = 0;
				flags = totalMines;
				minesFound = 0;
				rightclick = false;
				mineClicked = false;
				soundplayed = false;
				settings = false;
				instructions = false;
				highscores = false;
				credits = false;
				easyhighscore = false;
				mediumhighscore = false;
				hardhighscore = false;
				menuhover = false;
				hsAccesshover = false;
				gameOvertransitiontime = 0;
				wintransitiontime = 0;
				time = 0;
				flags = 0;
				framecounter = 0;
			}
		}
		else if(gameState == 6)
		{
			// Settings screen
			posX = e.getX();
			posY = e.getY();
			
			// Menu button
			if(posX >= 162 && posX <= 340 && posY >= 425 && posY <= 476)
			{
				gameState = 0;
				menuhover = false;
			}
			
			// Sound
			if(posX >= 267 && posX <= 348 && posY >= 129 && posY <= 170)
			{
				sound = true;
			}
			else if(posX >= 377 && posX <= 457 && posY >= 129 && posY <= 170)
			{
				sound = false;
			}
			
			// Difficulty
			if(posX >= 28 && posX <= 162 && posY >= 243 && posY <= 298)
			{
				easy = true;
				medium = false;
				hard = false;
			}
			else if(posX >= 182 && posX <= 316 && posY >= 243 && posY <= 298)
			{
				medium = true;
				easy = false;
				hard = false;
			}
			else if(posX >= 339 && posX <= 473 && posY >= 243 && posY <= 298)
			{
				hard = true;
				easy = false;
				medium = false;
			}
		}
		else if(gameState == 7)
		{
			// Credits screen
			posX = e.getX();
			posY = e.getY();
			
			if(posX >= 160 && posX <= 340 && posY >= 420 && posY <= 472)
			{
				gameState = 0;
				menuhover = false;
			}
		}
		else if(gameState == 8)
		{
			// Instructions screen
			posX = e.getX();
			posY = e.getY();
						
			if(posX >= 162 && posX <= 340 && posY >= 425 && posY <= 476)
			{
				gameState = 0;
				menuhover = false;
			}
		}
	}	

	public void mouseMoved(MouseEvent e) 
	{
		if(gameState == 0)
		{
			posX = e.getX();
			posY = e.getY();
			
			if(posX >= 189 && posX <= 312 && posY >= 129 && posY <= 249)
			{
				flaghover = true;
			}
			else
			{
				flaghover = false;
			}
			
			if(posX >= 40 && posX <= 235 && posY >= 275 && posY <= 335)
			{
				// Settings button
				settings = true;
				instructions= false;
				highscores = false;
				credits = false;
			}
			else if(posX >= 270 && posX <= 465 && posY >= 275 && posY <= 335)
			{
				// Instructions screen
				settings = false;
				instructions = true;
				highscores = false;
				credits = false;
			}
			else if (posX >= 40 && posX <= 235 && posY >= 370 && posY <= 425)
			{
				// Credits screen
				settings = false;
				instructions= false;
				highscores = false;
				credits = true;
			}
			else if(posX >= 270 && posX <= 465 && posY >= 370 && posY <= 425)
			{
				// Highscore Access screen
				settings = false;
				instructions= false;
				highscores = true;
				credits = false;
			}
			else
			{
				settings = false;
				instructions = false;
				highscores = false;
				credits = false;
			}
		}
		else if(gameState == 1)
		{
			posX = e.getX();
			posY = e.getY();
			if(posX >= 160 && posX <= 340 && posY >= 422 && posY <= 475)
			{
				menuhover = true;
			}
			else
			{
				menuhover = false;
			}
		}
		else if(gameState == 2)
		{
			
		}
		else if(gameState == 3)
		{
			posX = e.getX();
			posY = e.getY();
			if(posX >= 20 && posX <= 200 && posY >= 418 && posY <= 468)
			{
				menuhover = true;
			}
			else if(posX >= 300 && posX <= 480 && posY >= 418 && posY <= 468)
			{
				restarthover = true;
			}
			else
			{
				restarthover = false;
				menuhover = false;
			}
		}
		else if(gameState == 4)
		{
			posX = e.getX();
			posY = e.getY();
			
			if(posX >= 182 && posX <= 320 && posY >= 130 && posY <= 190)
			{
				easyhover = true;
				mediumhover = false;
				hardhover = false;
			}
			else if(posX >= 182 && posX <= 320 && posY >= 223 && posY <= 283)
			{
				mediumhover = true;
				easyhover = false;
				hardhover = false;
			}
			else if(posX >= 182 && posX <= 320 && posY >= 322 && posY <= 382)
			{
				hardhover = true;
				easyhover = false;
				mediumhover = false;
			}
			else if(posX >= 160 && posX <= 340 && posY >= 424 && posY <= 477)
			{
				menuhover = true;
			}
			else
			{
				menuhover = false;
				hsAccesshover = false;
				easyhover = false;
				mediumhover = false;
				hardhover = false;
			}
		}
		else if(gameState == 5)
		{
			posX = e.getX();
			posY = e.getY();
			if(posX >= 30 && posX <= 210 && posY >= 426 && posY <= 478)
			{
				hsAccesshover = true;
			}
			else if(posX >= 290 && posX <= 470 && posY >= 426 && posY <= 478)
			{
				menuhover = true;
			}
			else
			{
				menuhover = false;
				hsAccesshover = false;
			}
		}
		else if(gameState == 6)
		{
			posX = e.getX();
			posY = e.getY();
			if(posX >= 162 && posX <= 340 && posY >= 425 && posY <= 476)
			{
				menuhover = true;
			}
			else
			{
				menuhover = false;
			}
		}
		else if(gameState == 7)
		{
			posX = e.getX();
			posY = e.getY();
			
			if(posX >= 160 && posX <= 340 && posY >= 420 && posY <= 472)
			{
				menuhover = true;
			}
			else
			{
				menuhover = false;
			}
		}
		else if(gameState == 8)
		{
			posX = e.getX();
			posY = e.getY();
						
			if(posX >= 162 && posX <= 340 && posY >= 425 && posY <= 476)
			{
				menuhover = true;
			}
			else
			{
				menuhover = false;
			}
		}
	}
		
	// Useless Methods
	public void mouseClicked(MouseEvent e)
	{
		
	}
	public void mouseReleased(MouseEvent e) {

		
	}
	public void mouseEntered(MouseEvent e) {

		
	}
	public void mouseExited(MouseEvent e) {
		
	}
	public void keyTyped(KeyEvent e) {

		
	}
	public void keyReleased(KeyEvent e) {
		
	}
	public void mouseDragged(MouseEvent e) {
		
	}
}