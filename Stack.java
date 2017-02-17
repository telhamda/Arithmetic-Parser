/*
 * 		@Author: Tarik Hamdan
 * 
 * 		Just another Stack class implementation using linked lists.
 */
public class Stack {

	private Node top = new Node();		// Node to top of stack.
	private Node bottom = new Node();	// Node to the bottom of stack.
	private int size = 0;				// Size of stack.
	
	// Returns object at top of stack. Null if stack is empty.
	public Object peek(){
		if(isEmpty())
			return null;
		else
			return top.getElement();
	}
	
	// Pops object at top of stack.
	public Object pop(){
		
		if(isEmpty())
			return null;
		else if(!top.hasNext()){
			Object t = top.getElement();
			top.setElement(null);
			size--;
			
			return t;
		}
		else{
			Object t = top.getElement();
			top = top.getNext();
			size--;
			
			return t;
		}
	}
	
	// Pushes object to bottom of stack.
	public void push(Object t){
		
		if(isEmpty()){
			top.setElement(t);
//			bottom = top;
		}
		else{
			Node pointer = new Node(t);
			pointer.setNext(top);
			top = pointer;
			
			
			/*
			while(pointer.hasNext())
				pointer = pointer.getNext();
			pointer.setNext(new Node(t));
			bottom = pointer.getNext();
			*/
		}
		
		size++;
	}
	
	public int size(){ return size; }
	
	public boolean isEmpty(){ return size == 0; }
	
	public Object peekBottom(){ return bottom.getElement(); }
}


