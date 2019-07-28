
public class Slider {

	//Initializers/declares all variables and arrays
	private int x = 4;
	private int s = 0;
	private int b;
	private int temp;
	private double mod;
	private int[] in;
	private Button[] buttons;
	
	//Constructor Slider
	public Slider (int n) {
		
		x = n;  
		buttons = new Button [(int)Math.pow(x, 2)];//Pretty self explanatory; both are arrays created from the user inputted number
		in = new int [(int)Math.pow(x, 2)];
		scramble();//Resets the board literally but not graphically
		
	}//End constructor
	
	//Default constructor; a safety net
	public Slider () {
		
		this(4);
		 
	}//End constructor
	
	//Method scramble instantiates the buttons using the Button class and checks the if the buttons can create a 15 puzzle that is solvable
	public void scramble() {
			
			do{
				
				for (int n = 0; n < (buttons.length); n ++) {//Instantiating the buttons
				
					buttons[n] = new Button(n,x);
					
				}
				
				for (int i = 0; i < (in.length - 1); i++) {//Does a completely bubble sort and count the inversions
				
					for (int j = 0; j < (in.length - 1 - i); j++) {
					
						
						if (in[j] > in[j + 1]) // if true then swap
						
						{
							temp = in[j];
							in[j] = in[j + 1];
							in[j + 1] = temp;
							s++;//Inversion number increases by 1 
						}
					
					}
				
				}
			
				b = 0;
				
				while(buttons[b].getIn() != 0){//Finds the button with the image number of 0
				
					b++;
					
				}
				
				mod = b / x % 2;//See if it is in an even row or an odd row
				
			//If it matches these conditions,then it will be set blank and the scramble method completes, otherwise, the process repeats
			}while(!((!(x % 2== 0)) && (s % 2 == 0) || (x % 2== 0) && !(mod == 0) && (s % 2 == 0)|| (x % 2== 0) && (mod == 0) && !(s % 2 == 0)));
			
			buttons[b].setBlank();
			
	}//End method
	
	//gets the image number of the button with it's number matching the number given
	public int getButtonPo(int x) {

	return buttons[x].getIn();
	 
	}//End method
	
	//gets the image number of the empty button
	public int getBlank() {
		
		return b;
		
	}//End method
	
	//gets the button that matches the number it provides
	public Button getButton(int n) {
		
		return buttons[n];
		
	}//End method
	
		
}//End class


