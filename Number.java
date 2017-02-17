/*	
 * 	Number class represents numbers as a doubly linked list.
 * 	Basic number arithmetic are implemented, such as add, subtract, multiply, and factorial.
 */
public class Number {
	
	// Initialization of class fields.
	private Node low, high;
	private int digitCount = 0;
	private int decimalPlaces = 0;
	private boolean negative = false;
	
	// Default constructor sets the number to 0;
	public Number(){
		
		// Builds linked list representation of number 0
		accept("0");
	}
	
	// Constructor for a Number. Will throw exception if the number string is not a number
	public Number(String str){
		
		// Throws exception if the number is not valid.
		if(!valid(str))
			throw new IllegalArgumentException("Invalid Argument: [" + str + "] is NOT a Number!!!");
	
		// Builds linked list representation of the number
		accept(str);		
	}

	// Returns the Number of the sum of two numbers.
	public Number add(Number n){
		
		Number sum = new Number();
		
		// Numbers have the same sign. Add absolute. If they were negative, reverse the sign of the sum.
		if(this.isNegative() == n.isNegative()){
			sum = addAbsolute(n);
			
			if(this.isNegative())
				sum.reverseSign();
		}
		
		// This is negative but n isn't. Subtract absolute them. If absolute value of this > n, sum is negative.
		else if(this.isNegative() && !n.isNegative()){
			sum = this.subtractAbsolute(n);
			
			if(this.compareToAbsolute(n) > 0)
				sum.reverseSign();
		}
		
		// N is negative but this isn't. Subtract absolute them. If absolute value of this < n, sum is negative.
		else if(!this.isNegative() && n.isNegative()){
			sum = this.subtractAbsolute(n);
		
			if(this.compareToAbsolute(n) < 0)
				sum.reverseSign();
		}
		
		return sum; // Returns the resulting Number
	}

	// Returns the Number of the subtraction of two numbers.
	public Number subtract(Number n){
		
		Number subtraction = new Number();
		
		// Numbers are both negative. Add absolute. If this absolute > n, subtraction is negative.
		if(this.isNegative() && n.isNegative()){
			subtraction = subtractAbsolute(n);
			
			if(this.compareToAbsolute(n) > 0)
				subtraction.reverseSign();
		}
		
		// This is positive but n isn't. Add absolute. Is this absolute < n, subtraction is negative.
		else if(!this.isNegative() && n.isNegative()){ 
			subtraction = this.addAbsolute(n);
			
			if(this.compareToAbsolute(n) < 0)
				subtraction.reverseSign();
		}
		
		// This is negative but n isn't. Add absolute. Subtraction is negative.
		else if(this.isNegative() && !n.isNegative()){
			subtraction = this.addAbsolute(n);
			
			subtraction.reverseSign();
		}
		
		// Both this and n are positive. Subtract absolute. If absolute value of this < n, subtraction is negative.
		else if(!this.isNegative() && !n.isNegative()){
			subtraction = this.subtractAbsolute(n);
		
			if(this.compareToAbsolute(n) < 0)
				subtraction.reverseSign();
		}
		
		return subtraction; // Returns the resulting Number
	}

	// Returns the Number of the multiplication of two numbers.
	public Number multiply(Number n){
		
		// Multiply absolute the two numbers. Is their sign differed, result is negative.
		Number multiplication = this.multiplyAbsolute(n);
		
		if(this.isNegative() != n.isNegative())
			multiplication.reverseSign();
		
		return multiplication; // Returns the resulting Number
	}

	// Returns the Number of the factorial of this number.
	public Number factorial(){
		
		if((this.decimalPlaces > 0) || this.isNegative())
			throw new IllegalArgumentException("Invalid Argument: Factorial can ONLY be performed on a positive Integer!!!");
		
		Number iteration = this;
		Number factorial = this;
		
		// 0! is 1
		if(this.compareToAbsolute(new Number()) == 0)
				return new Number("1");
		
		// this! = [(this) * (this - 1) * (this - 2) * ... * (this - (this - 1))]
		// We start the iteration at this. multiply factorial by iteration - 1 until iteration = 1. 
		while(iteration.compareToAbsolute(new Number("1")) != 0){
			iteration = iteration.subtract(new Number("1"));
			factorial = factorial.multiply(iteration);
			
		}
		
		return factorial; // Returns the resulting Number
	}
	
	// Builds a list representation of the number
	private void accept(String strNumber){
		
		// Initialization of variables.
		Node ptr = null;
		int strLength = strNumber.length();
		int shiftDueToNegativeSign = 0;
		int shiftDueToDecimalPoint = 0;
		boolean keepgoing = true;

		// Checks if first char is - sign. If it is, we have a negative number.
		if(strNumber.charAt(0) == '-'){
			negative = true;
			shiftDueToNegativeSign++;
		}
		
		// Checks if we have a number of the form .xxx or -.xxx.
		if(strNumber.charAt(shiftDueToNegativeSign) == '.')
			shiftDueToDecimalPoint++;
		
		// Add each digit to the linked list up until the first decimal '.', or until we have reached the end of the number.
		// Increment digit count accordingly.
		for(int i = this.digitCount + shiftDueToNegativeSign; i < strLength && strNumber.charAt(i) != '.'; i++){
			if(this.digitCount > 0){				
				// Create tmp node, and set it to the digit, with previous node to ptr according to constructor
				// Node(char digit, Node previous). Set current ptr's next Node to tmp. Set ptr to tmp (the last node 
				// in the list.
				Node tmp = new Node(strNumber.charAt(i) - 48, ptr);
				ptr.setNext(tmp);
				ptr = tmp;
			}
			else{ 
				// Digit count == 0. Sets first node to high. Set current ptr to high node.
				high = new Node(strNumber.charAt(shiftDueToNegativeSign + shiftDueToDecimalPoint) - 48);
				ptr = high;
			}
			
			// Increment digit count because we have a new digit in the list.
			digitCount++;
		}
		
		
		// Add each digit to the linked list after the decimal places. Increment digit count and decimal places accordingly.
		for(int i = this.digitCount + shiftDueToNegativeSign; i < strLength; i++){ // We start at char after decimal point (+ 1) and 
														// take into account shift due to negative sign (+ shiftDueToNegativeSign)
			// Skips if char is the decimal point.
			if(strNumber.charAt(i) == '.')
				i++;
			
			if(i >= strNumber.length())
				keepgoing = false;
			
			if(this.digitCount > 0 && keepgoing){
				// Create tmp node, and set it to the digit, with previous node to ptr according to constructor
				// Node(char digit, Node previous). Set current ptr's next Node to tmp. Set ptr to tmp (the last node 
				// in the list).
				Node tmp = new Node(strNumber.charAt(i) - 48, ptr);
				ptr.setNext(tmp);
				ptr = tmp;
			}
			else if(keepgoing){ 
				// Digit count == 0. Sets first node to high. Set current ptr to high node
				high = new Node(strNumber.charAt(shiftDueToNegativeSign + shiftDueToDecimalPoint) - 48);
				ptr = high;
			}
			
			if(keepgoing){
			// Increment digit count and decimal places because we have a new digit in the list after decimal point.
			digitCount++;
			decimalPlaces++;
			}
		}
		
		// last pointer is low.
		low = ptr;
	}
	
	// Adds two positive numbers
	public Number addAbsolute(Number n){
		
		// Let this = 7554.1, and n = 23.456
		// Step 1) We will start by inserting extra decimal places in the sum that are in one number but not the other.
		// --> 56, sum = "56"
		// Step 2) Then, we will add the digit places that are both in this and n, taking into account the carry, and updating the 
		// sum as we go.
		// --> 541 + 234, sum = "775" + sum = "77556"
		// Step 3) Finally, we will insert the last digits before decimal points that are in one number but not the other taking 
		// into account the carry.
		// --> 75, sum = "75" + sum = "7577556"
		// We end up by placing the decimal point at the appropriate place.
		// --> sum = "7577.556" return new Number("7577.556")
	
		
		if(this.compareToAbsolute(new Number("0")) == 0)
			return n;
		else if(n.compareToAbsolute(new Number("0")) == 0)
			return this;
			
		// Initialization of variables.
		StringBuffer addAbsolute = new StringBuffer(); // To be used for insert method when sum has been calculated
		String sum = "";									// to insert the decimal point in sum.
		
		Node thisPtr = this.low; // Sums in addition are calculated by starting from the right most digit.
		Node nPtr = n.low;
		
		int carry = 0;
		int lastExcessDigitToAdd = 0;
		
		// decimal places to add is the number of decimal point digit places that are in both this and n.
		int decimalPlacesToAdd = (this.decimalPlaces > n.decimalPlaces) ? 
				n.decimalPlaces : this.decimalPlaces;
		
		// digit places to add is the number of digit before the decimal point that are in both this and n.
		int digitPlacesToAdd = ((this.digitCount - this.decimalPlaces) > (n.digitCount - n.decimalPlaces)) ?
				(n.digitCount - n.decimalPlaces) : (this.digitCount - this.decimalPlaces);

		// Step 1	
		// this has more decimal places then n. Those digits are transposed added to the sum appropriate digit places.
		if(this.decimalPlaces > n.decimalPlaces){
			for(int i = 0; i < (this.decimalPlaces - n.decimalPlaces); i++){
				// each extra digit in the decimal place is transposed to the sum
				sum = thisPtr.getNode() + sum;
				thisPtr = thisPtr.getPrevious();
			}
		}
			
		// n has more decimal places then n. Those digits are transposed added to the sum appropriate digit places.
		else if(n.decimalPlaces > this.decimalPlaces){
			for(int i = 0; i < (n.decimalPlaces - this.decimalPlaces); i++){
				// each extra digit in the decimal place is transposed to the sum
				sum = nPtr.getNode() + sum;
				nPtr = nPtr.getPrevious();
			}
		}

		// Step 2	
		// We are now adding the digit and decimal places that are both in this and n.
		for(int i = 0; i < (decimalPlacesToAdd + digitPlacesToAdd); i++){
			// Individual sum is thisPtr + nPtr + carry.
			// carry is the new sum / 10
			// new digit in the sum is the new sum % 10
			int newSum = thisPtr.getNode() + nPtr.getNode() + carry;
			carry = newSum / 10;
			sum = (newSum % 10) + sum;
			
			thisPtr = thisPtr.getPrevious();
			nPtr = nPtr.getPrevious();
		}
			
		// Step 3	
		// this had more digit before decimal point than n. We add the remaining one to the sum.
		if((this.digitCount - this.decimalPlaces) > (n.digitCount - n.decimalPlaces)){
			
			lastExcessDigitToAdd = (this.digitCount - this.decimalPlaces) - (n.digitCount - n.decimalPlaces);
						
			for(int i = 0; i < lastExcessDigitToAdd; i++){
					sum = ((thisPtr.getNode() + carry) % 10) + sum;
					carry = (thisPtr.getNode() + carry) / 10;
					thisPtr = thisPtr.getPrevious();
			}
			
			if(carry == 0){
				// We now add the decimal point to the sum. It it located at the same decimal location 
				// as the number with highest digit.
				addAbsolute = new StringBuffer(sum);
				addAbsolute.insert((this.digitCount - this.decimalPlaces), '.');
			}
			else{  // There was a final carry that needs to be added to the sum
				sum = carry + sum;
						
				// We now add the decimal point to the sum. It it located at the same decimal location +1
				// as the number with highest digit due to final carry.
				addAbsolute = new StringBuffer(sum);
				addAbsolute.insert((this.digitCount - this.decimalPlaces + 1), '.');
			}
		}
		
		else{ // n had the excess digits, or they both had the same amount of digits to be added.
			lastExcessDigitToAdd = (n.digitCount - n.decimalPlaces) - (this.digitCount - this.decimalPlaces);
			for(int i = 0; i < lastExcessDigitToAdd; i++){
				sum = ((nPtr.getNode() + carry) % 10) + sum;
				carry = (nPtr.getNode() + carry) / 10;
				nPtr = nPtr.getPrevious();
			}
			
			if(carry == 0){
				// We now add the decimal point to the sum. It it located at the same decimal location 
				// as the number with highest digit.
				addAbsolute = new StringBuffer(sum);
				addAbsolute.insert((n.digitCount - n.decimalPlaces), '.');
			}
			else{ // There was a final carry that needs to be added to the sum
				sum = carry + sum;
						
				// We now add the decimal point to the sum. It it located at the same decimal location + 1
				// as the number with highest digit due to final carry.
				addAbsolute = new StringBuffer(sum);
				addAbsolute.insert((n.digitCount - n.decimalPlaces + 1), '.');
			}
		}
				
		return new Number(trim(addAbsolute.toString()));
	}

	// Subtracts smaller number from larger number.
	private Number subtractAbsolute(Number n){
		
		// Let this = 7554.1, and n = 23.456
		// Step 1) 	if larger number has more decimal places:
		//		- We will start by inserting extra decimal places in the result.
		// --> 127.123 - 11.1, result is "23"
		//		   	else, smaller number has more decimal places:
		//		- Subtract those extra decimals from 10 taking into account the borrow.
		// --> 111.1 - 17.123, result is = "77"
		// Step 2) Then, we will subtract the digit places that are both in large and and small number, 
		//			taking into account the borrow, and updating the result as we go.
		// --> 127.123 - 11.1, result = "016" + result = "01623"
		// Step 3) Finally, we will insert the last digits before decimal points that are in the large number if there are
		// 			extras, taking into account the borrow
		// --> 127.123 - 11.1, result = "1" + result = "116023"
		// We end up by placing the decimal point at the appropriate place.
		// --> result = "116.023" return new Number("116.023")
	
			
		// Initialization of variables.
		StringBuffer subtractAbsolute = new StringBuffer(); // To be used for insert method when result has been calculated
		String result = "";									// to insert the decimal point in result.
		
		Number largeNumber = new Number();
		Number smallNumber = new Number();
		
		if(this.compareToAbsolute(n) > 0){
			largeNumber = new Number(this.toString());
			smallNumber = new Number(n.toString());
		}
		else if(this.compareToAbsolute(n) < 0){
			largeNumber = new Number(n.toString());
			smallNumber = new Number(this.toString());
		}
			
		else
			return new Number("0");
		
		Node largePtr = largeNumber.getLow(); // Sums in addition are calculated by starting from the right most digit.
		Node smallPtr = smallNumber.getLow();
		
		int borrow = 0;
		int lastExcessDigitToAdd = 0;
		
		// decimal places to subtract is the number of decimal point digit places that are in both large number
		// and small number.
		int decimalPlacesToSubtract = (largeNumber.decimalPlaces > smallNumber.decimalPlaces) ? 
				smallNumber.decimalPlaces : largeNumber.decimalPlaces;
		
		// digit places to subtract is the number of digit before the decimal point that are in both large number 
		// and small number.
		int digitPlacesToSubtract = ((largeNumber.digitCount - largeNumber.decimalPlaces) > (smallNumber.digitCount - smallNumber.decimalPlaces)) ?
				(smallNumber.digitCount - smallNumber.decimalPlaces) : (largeNumber.digitCount - largeNumber.decimalPlaces);
		

		// Step 1	
		// Large number has more decimal places then small number. Those digits are transposed added to the result appropriate digit places.
		if(largeNumber.decimalPlaces > smallNumber.decimalPlaces){
			for(int i = 0; i < (largeNumber.decimalPlaces - smallNumber.decimalPlaces); i++){
				// each extra digit in the decimal place is transposed to the result
				result = largePtr.getNode() + result;
				largePtr = largePtr.getPrevious();
			}
		}
		
		// Large number has less decimal places then small number. 
		// Small extra decimal digits subtracted from zero are transposed added to the result appropriate digit places with appropriate borrow operations.			
		else if(largeNumber.decimalPlaces < smallNumber.decimalPlaces){
			for(int i = 0; i < (smallNumber.decimalPlaces - largeNumber.decimalPlaces); i++){
				// each extra digit in the decimal place is transposed to the sum
				result = ((10 - smallPtr.getNode() - borrow) % 10) + result;
				smallPtr = smallPtr.getPrevious();
				borrow = 1;
			}
			
		}				
		
		// Step 2	
		// We are now adding the digit and decimal places that are both in this and n.
		for(int i = 0; i < (decimalPlacesToSubtract + digitPlacesToSubtract); i++){
			// Individual sum is thisPtr + nPtr + carry.
			// carry is the new sum / 10
			// new digit in the sum is the new sum % 10
			int newResult;
			if(largePtr.getNode() < (smallPtr.getNode() + borrow)){
				newResult = 10 + largePtr.getNode() - smallPtr.getNode() - borrow;
				borrow = 1;
			}
			else{
				newResult = largePtr.getNode() - smallPtr.getNode() - borrow;
				borrow = 0;
			}
			
			result = (newResult % 10) + result;
			
			largePtr = largePtr.getPrevious();
			smallPtr = smallPtr.getPrevious();
		}
		

		// Step 3	
		// large number had more digit before decimal point than small number. We add the remaining one to the result taking into
		// account borrows.
		if((largeNumber.digitCount - largeNumber.decimalPlaces) > (smallNumber.digitCount - smallNumber.decimalPlaces)){
			lastExcessDigitToAdd = (largeNumber.digitCount - largeNumber.decimalPlaces) - (smallNumber.digitCount - smallNumber.decimalPlaces);
			for(int i = 0; i < lastExcessDigitToAdd; i++){
				// each extra digit in the decimal place is transposed to the result taking into account borrows.
				int newResult;
				if(largePtr.getNode() < borrow){
					newResult = 10 + largePtr.getNode() - borrow;
					borrow = 1;
				}
				else{
					newResult = largePtr.getNode() - borrow;
					borrow = 0;
				}
				
				result = (newResult % 10) + result;
				
				largePtr = largePtr.getPrevious();
			}

		}
	
		
		// We now add the decimal point to the result. It it located at the same decimal location
		// as the larger digit.
		subtractAbsolute = new StringBuffer(result);
		subtractAbsolute.insert((largeNumber.digitCount - largeNumber.decimalPlaces), '.');
		
		return new Number(trim(subtractAbsolute.toString()));
		
	}

	// Multiplies two positive numbers.
	public Number multiplyAbsolute(Number n){
		
		// We point to low digit it n, and multiply it by the whole this number.
		// The result of that operation is then multiplied by the 10th n power of the decimal place of the digit (done by simply concatenating
		// n-zeros). We add the result of this operation to the current result. Once we have multiplied each n-digit by this, we add the decimal
		// place to the result. There are always (this.decimal place + n.decimal place) digits after the decimal place in the result of a 
		// multiplication.
		
		if(this.compareToAbsolute(new Number()) == 0 || n.compareToAbsolute(new Number()) == 0) // By default. new Number() represents 0
			return new Number(); // If we multiply by 0, result is simply 0.
		
		// Initialization of variables.
		StringBuffer multiplicationResult = new StringBuffer(); // To be used for insert method when sum has been calculated
		String updateResult = "";			// to insert the decimal point in sum.
		
		Number result = new Number(); // Number is 0 by default.
		
		Node thisPtr = this.low; // We start by multiplying the left most n digit by the this number, which will first be done
		Node nPtr = n.low; // by multiplying it by the left most this digit.
		
		int carry = 0;
	
		// We will have to do the multiplication for each n digit by the whole this number.
		for(int i = 0; i < n.digitCount; i++){
			
			// We multiply each of this digits by the nPtr digit to get our new updated result and we update
			// the carry as needed.
			for(int j = 0; j < this.digitCount; j++){
				int newSum = nPtr.getNode() * thisPtr.getNode() + carry;
				carry = newSum / 10;
				updateResult = (newSum % 10) + updateResult;
				thisPtr = thisPtr.getPrevious();		
			}
			
			// We add the last carry to the result, reset carry to 0. 
			if(carry > 0){
				updateResult = carry + updateResult;
				carry = 0;
			}
			
			// We concatenate by n-zeros to represent the 10th power of the decimal place of the nPtr.
			for(int k = 0; k < i; k++)
				updateResult = updateResult.concat("0");

			// We add to the result the new found result of the next operation.
			result = new Number(result.addAbsolute(new Number(updateResult)).toString());
			
			// Move on to next nPtr to be multiplied. thisPtr points to low because we will multiple the whole number by nPtr.
			nPtr = nPtr.getPrevious();
			thisPtr = this.getLow();
			updateResult = "";
		}
			
		updateResult = result.toString();
		
		if(updateResult.length() < this.digitCount + n.digitCount){
			for(int i = 0; i < ((this.digitCount + n.digitCount) - result.toString().length()); i++)
				updateResult = '0' + updateResult;
		}
		
		// We insert the decimal places. There are this.decimal places + n.decimal places in the result.
		multiplicationResult = new StringBuffer(updateResult);
		multiplicationResult.insert((multiplicationResult.length() - (this.decimalPlaces + n.decimalPlaces)),
				".");

		// Returns the number of the result of the multiplication
		return new Number(trim(multiplicationResult.toString()));
	}
	
	// Returns a string representation of the number
	public String toString(){
		// Initialization of variables
		String str = "";
		
		Node ptr = high;
		
		// If negative, add the negative sign
		if(negative)
			str += "-";
		
		
		// Adds digits up to the dot, if there is one.
		for(int i = 0; i < (digitCount - decimalPlaces); i++){
			str += ptr.getNode();			// Get node element to the string, then point to the next
			ptr = ptr.getNext();
		}
		
		// If there are any decimal places, add the ".", then add the remaining decimal digits.
		if(decimalPlaces > 0){
			str += ".";
			
			for(int i = 0; i < decimalPlaces; i++){
				str += ptr.getNode();		// Get node element to the string, then point to the next
				ptr = ptr.getNext();
			}
		}
		
		 	
		return str; // Returns string representation of the number
	}
	
	// Returns true if input string is a number.
	private boolean valid(String str){

		// Checks each character. Can either be a digit or a decimal point. 
		// Besides for the first char that can either be a digit, decimal, or negative:
		// If a character is not a digit or there are more than 1 decimal point, we do not have a number. 
		// Will then return false
		int decimalPoint = 0;
		int shiftDueToNegativeSign = 0;
		
		// We first check if number is negative.
		if(str.charAt(0) == '-')
			shiftDueToNegativeSign++;
		
		// Special edge case
		if(str.equals("."))
			return false;
		
		// Validates each char
		for(int i = shiftDueToNegativeSign; i < str.length(); i++){
			if(str.charAt(i) == '.')
				decimalPoint++;
			if(!checkValidChar(str.charAt(i)) || decimalPoint > 1)
				return false;
		}
		
		return true;
	}
	
	// Returns true only if char is a digit or a dot.
	private boolean checkValidChar(char theChar){
	return ((theChar - 48 >= 0 && theChar - 48 <= 9) || theChar == '.');
}

	// Returns Node that points to low
	private Node getLow(){
		return low;
	}
	
	// Switches the sign of the number
	public void reverseSign(){
		if(negative)
			negative = false;
		else
			negative = true;
	}
	
	// Returns if Number is negative
	private boolean isNegative(){
		return negative;
	}

	// Trims leading and trailing zeros.
	public String trim(String number){
		
		int  i = 0;
		boolean keepgoing = true;
		
		// Special edge case
		if(number.equals("."))
			return "0";
		
		// Trims leading zeros
		while(keepgoing && i < number.length()){
			if(number.charAt(i) == '0' && number.charAt(i + 1) != '.'){
				number = number.substring(1);
			}
			else{
				keepgoing = false;
				if(number.charAt(i) == '0')
					number = number.substring(1);
			}
		}
		
		// We take out trailing zeros if it contains decimal places.
		if(number.contains(".")){
			keepgoing = true;
			
			while(keepgoing){
				if(number.charAt(number.length() - 1) == '0')
					number = number.substring(0, number.length() - 1);
				else{
					keepgoing = false;
				}
			}
		}
		
		// Special edge case
		if(number.equals("."))
			return "0";
		
		
		return number;
	}
	
	// Compares absolute value to that of number n
	private int compareToAbsolute(Number n){
		
		if((this.digitCount - this.decimalPlaces) > (n.digitCount - n.decimalPlaces))
			return 1; // this has more digits before decimal point and is therefore larger.
		else if((this.digitCount - this.decimalPlaces) < (n.digitCount - n.decimalPlaces))
			return -1; // this has less digits before decimal point and is therefore smaller.
		
		else{// We start by comparing the left most digits, then go right one at a time.
			Node thisPtr = this.high;
			Node nPtr = n.high;
			
			// Break when a digit is larger than the other, or when we have reached the last digit of one of the two
			while(thisPtr.getNode() == nPtr.getNode() && thisPtr != this.low && nPtr != n.low){
				thisPtr = thisPtr.getNext();
				nPtr = nPtr.getNext();
			}
			
			
			if(thisPtr.getNode() > nPtr.getNode())
				return 1; // this current digit is larger, and therefore this is the larger number.
			else if(thisPtr.getNode() < nPtr.getNode())
				return -1; // this current digit is smaller, and therefore this is the smaller number.
			else if(this.decimalPlaces > n.decimalPlaces)
				return 1; // there are more decimal places in this, therefore it is the larger one.
			else if(this.decimalPlaces < n.decimalPlaces)
				return -1; // there are less decimal places in this, therefore it is the smaller one.
			else // digits are equal.
				return 0;
		}
	}
}
