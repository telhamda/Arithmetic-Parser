
public class ArithmeticParser {
	
	private static String operatorslist = "+-*/()";
	private static String operationslist = "+-*/";
	
	private static Stack numbers = new Stack();
	private static Stack operators = new Stack();	
	private static Stack parenthesis = new Stack();
	
	private static Double operand1;
	private static Double operand2;
	
	public static boolean validate(String expression){
		
		String[] token = expression.split(" ");
		
		// Expression can only be valid if first token is number of ( and last token is number or ).
		if(!isNumber(token[0]) && !token[0].equals("("))
			return false;
		
		if(!isNumber(token[token.length - 1]) && !token[token.length - 1].equals(")"))
			return false;		
		
		for(int i = 0; i < token.length; i++){

			// A number has to be followed by an operator.
			if(isNumber(token[i]) && i < token.length - 1){
				if(isOperator(token[i + 1]))
					continue;
				else return false;			
			}
			
			// An operator cannot be followed by another operation
			else if(isOperation(token[i]) && isOperation(token[i + 1]))
				return false;
			
			// A ( is always pushed on the stack
			else if(token[i].equals("("))
				parenthesis.push("(");
			
			// Invalid expression if we encounter a ) before a (.
			else if(token[i].equals(")")){
				if(parenthesis.isEmpty()) 
					return false;
				parenthesis.pop();
			}
		}

		// The expression is true if we finally have balanced parenthesis.
		if(parenthesis.isEmpty())
			return true;
		else 
			return false;
	}
	
	public static double evaluate(String expression){
		return getresult(postfix(expression));
	}	
	
	private static double getresult(String postfix){
			
		String[] token = postfix.split(" ");
		
		for(int i = 0; i < token.length; i++){
			
			if(isOperator(token[i])){
				operand2 = (Double) numbers.pop();
				operand1 = (Double) numbers.pop();
				
				Double newoperand =  calculate(operand1, operand2, token[i].charAt(0));
				numbers.push(newoperand);
			}
			else if(isNumber(token[i]))
				numbers.push(new Double(token[i]));
		}
		
		return (Double) numbers.pop();
	}
	
	private static String postfix(String expression){
		
		String[] token = expression.split(" ");
		String postfix = "";
		
		for(int i = 0; i < token.length; i++){
			
			if(isOperator(token[i])){
				char operator = token[i].charAt(0);
				
				// Operand is left parenthesis. Push it on stack.
				if(operator == '('){
					
					// Previous token is a number, with implied *.
					if(i > 0 && (isNumber(token[i - 1]) || token[i - 1].charAt(0) == ')'))
						operators.push('*');
					operators.push(operator);
				}
				
				// Operand is right parenthesis. Pop stack until we get to left parenthesis.
				else if(operator == ')'){	
					Character c;
					
					while((Character) operators.peek() != '('){
						c = (Character) operators.pop();
						postfix += " " + c;
					}
					
					operators.pop();
					
					// Next token is a number, with implied *.
					if(i < token.length - 2 && isNumber(token[i + 1]))
						operators.push('*');
				}
				
				// Regular operand. Pop each operand that is less than in precedence to current operand.
				else{
					while(!operators.isEmpty() && precedence((Character) operators.peek()) >= precedence(operator))
						postfix += " " + (Character) operators.pop();		
					operators.push(operator);
				}
			}
			
			// Number is pushed on number stack, and is appended to postfix expression.
			else if(isNumber(token[i]))
				postfix += " " + token[i];
		}
	
		// Append the last remaining numbers.
		while(!operators.isEmpty())
			postfix += " " + (Character) operators.pop();
		
		return postfix.substring(1);
	}

	private static double calculate(Double operand1, Double operand2, Character operator){
		switch(operator){
			case '*':	return operand1 * operand2;
			case '/':	return operand1 / operand2;
			case '+':	return operand1 + operand2;
			case '-':	return operand1 - operand2;
			default:	return -1;
		}
	}

	private static int precedence(char operator){
		
		switch(operator){

			case '*':	return 3;
			case '/':	return 3;
			case '+':	return 2;
			case '-':	return 2;
			case '(':	return 1;
			case ')':	return 1;
			default : 	return -1;
		}
	}
	
	private static boolean isNumber(String token){	
		
		int index = 0;
		int decimalCount = 0;
		int length = token.length();
		
		// Edge case.
		if(length == 1 && (token.charAt(0) == '-' || token.charAt(0) == '.'))
			return false;
		
		// First char corresponds to a negative sign.
		char c = token.charAt(index);
		if(c == '-')
			index++;
		
		// Go until the end or we get more than 1 decimal point.
		while(index < length && decimalCount <= 1){
				c = token.charAt(index);
				
				// If not a digit, or a decimal point, this is not a number.
				if(c < '0' || c > '9')
					if(c == '.') decimalCount++;
					else return false;
				
				index++;
		}
		
		// Number is valid if there was not more that 1 decimal point
		if(decimalCount > 1) return false;
		else return true;
		
	}

	private static boolean isOperator(String token){ 
		
		return (operatorslist.contains("" + token) && token.length() == 1); 
	}
	
	private static boolean isOperation(String token){
		
		return (operationslist.contains("" + token) && token.length() == 1);
	}

	
}
