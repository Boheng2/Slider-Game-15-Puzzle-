/**
 * Slider Game
 * Version 3
 * Wade Huang
 * ICS3U
 */

/* Dear Mr. Roller,
 * 
 * First of all I would like to apologize for this being 1 week late. Not gonna lie I thought it would be a lot easier to implement things the second time.
 * Turns out it was a lot harder than I thought and changing the logic and spreading the code to 3 classes gave me problems that weren't there before.
 * I was able to successfully recreate the program with, at least in my opinion, improved logic. The gui stayed the same because of time reasons (obviously).
 * It took way longer than I anticipated and I hope you understand, though it does not excuse my stupidity of not saving my previous version.
 * Anyways, there is nothing for me to say but sorry. I hope the features of this game can somewhat make up for it.
 * 
 * Wade
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.util.regex.*;

public class GuiMain implements ActionListener{
	
	//initialize or declare variables and arrays
	private String t = "a"; 
	private int check;
	private static int time;
	private static int x = 4;
	private static int count = 0;
	private JButton[] tiles; 
	JLabel moveCount;
	static JLabel timer;
	Slider slider;
	JPanel panel, tilepanel, buttons;
	static JFrame frame;
	static JMenuBar menuBar;
	static JMenu menu1, menu2, menu3, menu4, menu5;
	private static long startTime, currentTime, seconds, minutes, change;
	private volatile static boolean pressed = false;
	private static boolean victory = false;
	private static boolean timerMode = false;
	
	//Constructor GuiMain
	public GuiMain() throws NullPointerException{
		
		//Initializes frame and menu bar first because both can be initialized without user input
		menuBar = new JMenuBar();
		frame = new JFrame("15 Puzzle");
		
		//Asks for user input, and unless it is a number, it will not stop asking the user
		Pattern pattern = Pattern.compile("[0-9]");
		Matcher matcher = pattern.matcher(t);
		while (!matcher.find()) {
			
			t = JOptionPane.showInputDialog(frame,"number of tiles you wish to solve"); 
			if (t == null) {
				
				t = "" +4;
				JOptionPane.showMessageDialog(frame,"To default");
				
			}
			matcher = pattern.matcher(t);
		
		}
		
		x = Integer.parseInt(t);
		
		//If the number is under 4 or over 15 (this limit is set because under 4 has no challenge while over 15 makes the frame too big to play), then the program will switch to the default number, which is 4
		if (!(x >= 4 && x <= 15)) {
			  
			x = 4;
			JOptionPane.showMessageDialog(frame,"To default");
			
		}else {
			
			x = Integer.parseInt(t);
			
		}
		
		JOptionPane.showMessageDialog(frame,"The classic 15 puzzle game. Note that the correct order of tiles go from left to right. Additionally, the empty tile must be on the top left.");//Instructions
		slider = new Slider(x);//initializes slider
		
		tiles = new JButton[(int)Math.pow(x, 2)];//Creates the number of buttons based on the number that the user has chosen
		
		//Initializes frame and panel and adds menu bar to the panel on top 
		frame.setSize(x * 100 + 10* (x-1),x * 100 + 10* (x-1));
		frame.setResizable(false);
		panel = new JPanel (new BorderLayout());
		panel.setSize(x * 100 + 10* (x-1),x * 100 + 10* (x-1));
		panel.add(menuBar,BorderLayout.PAGE_START);
		
		//Creates a new panel with labels added that will be used to track moves and time. This panel will be at the bottom
		buttons = new JPanel();
		moveCount = new JLabel("Move Count: " + count);
		moveCount.setBounds(20,20,40,20);//Makes the label visible on the panel
		buttons.add(moveCount);
		timer = new JLabel("Minutes: " + minutes + " Seconds: " + seconds);
		timer.setBounds(20,20,40,20);//Makes the label visible on the panel
		buttons.add(timer);
		panel.add(buttons,BorderLayout.PAGE_END);
		
		//The main game board, this will be at the center
		tilepanel = new JPanel(new GridLayout(x, x, 5, 5));
		panel.add(tilepanel,BorderLayout.CENTER);
		tilepanel.setBackground(new Color(160,82,45));
		
		//The panel is added and the frame is set visible
		frame.add(panel);
		frame.setVisible(true);  
		
		//Create and add all the J buttons, they will have different colors (black or white) depending if they are even or odd
		for(int q = 0; q < tiles.length; q++) {//Number of JButtons made is based on the user inputed button which affects the tiles length
			
		int p = slider.getButton(q).getPn();	
		tiles[p] = new JButton("" +slider.getButton(q).getIn()); //Instantiates the Button class
		tiles[p].addActionListener(this);
		tiles[p].setSize(100,100);
		tiles[p].setFont(new Font("Helvetica", Font.BOLD, 36));
		
		if (slider.getButton(q).getPn() % 2 == 0) {		
		
			tiles[p].setBackground(Color.black);
			tiles[p].setForeground(Color.white);
		
		}else {
			
			tiles[p].setBackground(Color.white);
			tiles[p].setForeground(Color.black);
			
		}
		
		tiles[p].setVisible(true);
		tilepanel.add(tiles[p]); 
		
		}
		
		//Gets the button that should be blank using getBlank and makes it not visible
		tiles[slider.getBlank()].setVisible(false);
		
		//First menu, when pressed it will use the scramble method, which will scramble the game board
		menu1 = new JMenu("Scramble");
		menu1.setMnemonic(KeyEvent.VK_A);
		menu1.getAccessibleContext().setAccessibleDescription("Resets the tiles");
		menuBar.add(menu1);
		menu1.addMenuListener(new MenuListener() {

	        @Override
	        public void menuSelected(MenuEvent e) {
	        	
	        	if(e.getSource()!=null) {
	        		
	        		scramble();
	        		
	        	}
	        	
	        }

			@Override
			public void menuCanceled(MenuEvent arg0) {
				
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
				
				
			}
			
		});
		
		//Filler menu button for space
		menu5 = new JMenu("                             ");
		menuBar.add(menu5);
		
		//Sets the time when pressed and the user must complete the puzzle within the time they set or they lose
		menu3 = new JMenu("Set Time");
		menuBar.add(menu3);
		menu3.setMnemonic(KeyEvent.VK_A);
		menu3.getAccessibleContext().setAccessibleDescription("Set your time for a challenge");
		menuBar.add(menu3);
		menu3.addMenuListener(new MenuListener() {

	        @Override
	        public void menuSelected(MenuEvent e){
	        	
	        	if(e.getSource() != null) {
	        		
	        		//Same procedure as the user input for the number of tiles
	        		t = "a";
		        	Pattern pattern = Pattern.compile("[0-9]");
		    		Matcher matcher = pattern.matcher(t);
		    		while (!matcher.find()) {
		    			
		    			t = JOptionPane.showInputDialog(frame,"Enter the time in seconds");
		    			if (t == null) {
		    				
		    				t =  "1000000";//The default
		    				JOptionPane.showMessageDialog(frame,"To default");
		    				
		    			}
		    			
		    			matcher = pattern.matcher(t);
		    			
		    		}
		    		
		    		time = Integer.parseInt(t);//Sets the time
		    		
		    		setTime();//turns on timer mode
	        	
	        	}
	    		
	        }

			@Override
			public void menuCanceled(MenuEvent arg0) {
				
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
				
				
			}
			
		});
		
		//Filler menu for space
		menu4 = new JMenu("                             ");
		menuBar.add(menu4);
		
		//Restarts the program
		menu2 = new JMenu("Change Difficulty");
		menu2.setMnemonic(KeyEvent.VK_A);
		menu2.getAccessibleContext().setAccessibleDescription("Changes the amount of tiles that needs to be solved");
		menuBar.add(menu2);
		menu2.addMenuListener(new MenuListener() {

	        @Override
	        public void menuSelected(MenuEvent e) {
	        	
	        	if(e.getSource() != null) {
	        		
	        		victory = true;
	        		frame.getContentPane().removeAll();
	        		count = 0;
	        		new GuiMain();
	        	
	        	}

	        }

			@Override
			public void menuCanceled(MenuEvent arg0) {
				
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
				
				
			}
			
		});
		
		//These two are important booleans and such they will be made sure to be false
		victory = false;
		pressed = false;
		
	}//End constructor
	
	//actionPerformed method which will activate when a button is pressed
	public void actionPerformed(ActionEvent e) {
		
		//Activates timer
		if(pressed != true) {
			
			pressed = true;
			
		}

		//Finds the button that is pressed, then checks if the surrounding buttons have its availability integer as 0. If so, then they will swap. 
		int n = 0;
		while (e.getSource() != tiles[n]) {
		
			n += 1;
			
		}
		
		if (((n + 1) % x) != 0){
			
			int n2;
			n2 = n + 1;
			swap(n,n2);
			
		}
		
		if ((n % x) != 0){
			
			int n2;
			n2 = n - 1;
			swap(n,n2);
			
		}
		
		if ((n + x) < ((int)Math.pow(x, 2))){
			
			int n2;
			n2 = n + x;
			swap(n,n2);
			
		}
		
		if ((n - x) >= 0){
			
			int n2;
			n2 = n - x;
			swap(n,n2);
			
		}

	}//End method
	
	//Method update that accepts a string and JLabel and it is used to increase the move counter whenever a move is done
	public void update(String s, JLabel label){
		
		count++;
		label.setText(s + count);
		
	}//End method
	
	//Method scramble, basically the chunk of code in the constructor, except slider isn't instantiated and no user input is neede
	public void scramble() {
		
		pressed = false;
		frame.getContentPane().removeAll();
		count = 0;
		slider = new Slider(x);
		tiles = new JButton[(int)Math.pow(x, 2)];
		frame.setSize(x * 100 + 10* (x-1),x * 100 + 10* (x-1));
		frame.setResizable(false);
		JPanel panel = new JPanel (new BorderLayout());
		panel.setSize(x * 100 + 10* (x-1),x * 100 + 10* (x-1));
		JPanel buttons = new JPanel();
		moveCount = new JLabel("Move Count: " + count);
		moveCount.setBounds(20,20,40,20);
		panel.add(menuBar,BorderLayout.PAGE_START);
		panel.add(buttons,BorderLayout.PAGE_END);
		buttons.add(moveCount);
		JPanel tilepanel = new JPanel(new GridLayout(x, x, 5, 5));
		panel.add(tilepanel,BorderLayout.CENTER);
		tilepanel.setBackground(new Color(160,82,45));
		frame.add(panel);
		frame.setVisible(true);  
		
		for(int q = 0; q < tiles.length; q++) {
			
		int p = slider.getButton(q).getPn();	
		tiles[p] = new JButton("" +slider.getButton(q).getIn());
		tiles[p].addActionListener(this);
		tiles[p].setSize(100,100);
		tiles[p].setFont(new Font("Helvetica", Font.BOLD, 36));
		
		if (slider.getButton(q).getPn() % 2 == 0) {		
		
			tiles[p].setBackground(Color.black);
			tiles[p].setForeground(Color.white);
		
		}else {
			
			tiles[p].setBackground(Color.white);
			tiles[p].setForeground(Color.black);
			
		}
		
		tiles[p].setVisible(true);
		tilepanel.add(tiles[p]); 
		
		}
		   
		tiles[slider.getBlank()].setVisible(false);
		
		
		timer = new JLabel("Minutes: " + minutes + " Seconds: " + seconds);
		timer.setBounds(20,20,40,20);
		buttons.add(timer);
		
		victory = false;
		
	}//End method
	
	//Method check that will run through the arrays of in and pn to see if they match, if they all do, then the user wins
	public void check() {
		 
		check = 0;
		
		//Checks through the arrays, check will increase by when whenever they match
		for (int c = 0; c < tiles.length; c++){
			
			if (slider.getButton(c).getIn() == slider.getButton(c).getPn()) {
				
				check++;
		
			}
			
		}
		
		//If all of them match, check should be the same as the area of the user inputed number. Therefore, they win, and the game board is reset.
		if (check == (int)Math.pow(x, 2)) {
			
			JOptionPane.showMessageDialog(frame,"You Win");
			victory = true;
			scramble();
			
		}
		
	}//End method
	
	//Method swap take in two integers and swaps the buttons with that matches the integers provided
	public void swap(int n, int n2){
		
		if (slider.getButton(n2).getAvailability() == 0) {
			//Swaps visually (the empty button gets the text and becomes visible, while the visible button becomes invisible
			tiles[n2].setText(tiles[n].getText());	
			tiles[n].setVisible(false);
			tiles[n2].setVisible(true);
			//Swaps literally (the empty button has its availability and image number swapped with the visible button)
			slider.getButton(n2).setAvailability();
			slider.getButton(n).setBlank();
			int temp = slider.getButton(n2).getIn();
			slider.getButton(n2).setIn(slider.getButton(n).getIn());
			slider.getButton(n).setIn(temp);
			//Update the move count and checks if the user wins with this move
			update("Move Count: ", moveCount );
			check();
			
		}
		
	}//End method
	
	//Method setTime that takes an integer a
	public void setTime(){
		
		JOptionPane.showMessageDialog(frame,"You have until the time you set to complete the 15 puzzle. Good Luck!");//informs the user
		scramble();//Reset the board
		timerMode = true;//Sets timermode
	
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		new GuiMain();//Instantiates the gui
		
		while(pressed == false){//Waits until a button is pressed
			
		  Thread.sleep(1000);
		  
		}
		
		while (victory == false) {//Waits until a button is pressed, otherwise the start time will keep resetting (for scramble or changing difficulty)
			startTime = System.currentTimeMillis();
			
			while (pressed == true) {
			
				currentTime = System.currentTimeMillis() - startTime;//The current time starting from the start time
				seconds = currentTime / 1000;//converting to seconds from milliseconds
				
					if(seconds == 60){//If there is 60 seconds
					
						minutes = seconds / 60;//convert to minutes
						startTime += 60000;//add the seconds to start time so the seconds are subtracted
						
					}
				
				//Refresh the timer at a 1 second interval	
				change = currentTime % 1000;
		 
				if (change == 0){	
					
					timer.setText("Minutes: " + minutes + "Seconds: " + seconds);
				
				}
				
				//If timer is set, then and the timer is up, then the program will inform that the user has lost and disables time mode and resets the game
				if (timerMode == true && seconds == time){
					
					JOptionPane.showMessageDialog(frame,"You lose");
					timerMode = false;
					frame.getContentPane().removeAll();
					new GuiMain();
				
				}
		
			}
		
		}
		
	}//End main method

}//End class