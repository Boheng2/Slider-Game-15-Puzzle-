import java.util.ArrayList;
import java.util.List;

public class Button {

	//Declares or initializes all variables and array lists
	private int pn;
	private Integer in;
	private int an;
	private static List<Integer> templ = new ArrayList<Integer>();
	
	//constructor of Button that accepts 2 values; no default constructor because there is no need
	public Button (int x,int y) {
		
		setPn(x);
		setPosition(y);
		setAvailability();
		
	}//End constructor
	
	//Method setPn, sets the position of the button in the Gui using the number given
	public void setPn(int x) {
		
		pn = x;
		
	}//End method
	
	//Method setIn, sets the text displayed on the Gui using the number given
	public void setIn(int x) {
		
		in = x;
		
	}//End method
	
	//Sets the image number pseudo-randomly (once a number is chosen it cannot be chosen again
	public void setPosition(int x) {
		
		int xsq = (int)Math.pow(x, 2);//The max number
		
		if (pn == 0) {//An array list will have numbers added to up to xsq
				
			int y = 0;
			
			while (!(y == xsq)) {
				
				templ.add(y);
				y++;
				
			}
			
		}
		
		int r = (int)(Math.random()*(templ.size()));//A random number is chosen within the array list
	
		in = templ.get(r);//It is set as an in of this particular button
		templ.remove(in);//the number is removed

	}//End method
	
	//Method setAvailability makes the button a visible button
	public void setAvailability() {
		
		an = 1;
		
	}//End method
	
	//Method setAvailability makes the button an empty button
	public void setBlank() {
		
		an = 0;
		
	}//End method
	
	//Method getIn returns integer variable in (or image number)
	public Integer getIn() {
		
		return in;
		
	}//End method
	
	//Method getIn returns integer variable pn (or position number)
	public int getPn() {
		
		return pn;
		
	}//End method
	
	//Method getAvailability returns int an and informs the receiver whether or not the button is empty
	public int getAvailability() {
		
		return an;
		
	}//End method
	
}//End class







