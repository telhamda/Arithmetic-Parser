/*
 * 		@Author: Tarik Hamdan
 * 
 * 		Just another node/linked list class implementation
 */

public class Node {

	private Node next;					// Next node.
	private Object t;					// Object contained within  current node.
	private boolean empty = true;		// Node is currently empty.
	private boolean hasnext = false;	// Node does not currently have next.
	
	// A new node is instantiated with its associated Object.
	public Node(){};
	public Node(Object t){ empty = false; this.t = t; }

	// Getter and setter method for next node.
	public Node getNext(){ return this.next; }
	public void setNext(Node next){ hasnext = true; this.next = next; }
	
	// Getter and setter method for current element.
	public Object getElement(){	return this.t; }
	public void setElement(Object t){ empty = false; this.t = t; }
	
	// Returns if node is empty/has a next node.
	public boolean isEmpty(){ return t == null; }
	public boolean hasNext(){ return hasnext; }
}
