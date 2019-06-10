package model;
/**
 * 
 * @author John Strauser Josh Pineda
 * <p>Class for Tags. Contains a name and value for the tag.</p>
 *
 */
public class Tag implements java.io.Serializable{
	public String name;
	public String value;
	
	/**
	 * <p>Creates a blank tag. This constructor is never called.</p>
	 */
	public Tag(){
		name="";
		value="";
	}
	/**
	 * <p>Creates a new tag with name n and value v.</p>
	 * @param n
	 * @param v
	 */
	public Tag(String n, String v){
		name=n;
		value=v;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String toString(){
		return name+","+value;
	}
}
