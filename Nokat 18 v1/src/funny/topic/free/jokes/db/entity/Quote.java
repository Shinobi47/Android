/**
 * 
 */
package funny.topic.free.jokes.db.entity;

import java.io.Serializable;

/**
 * @author ThangTB
 *
 */
@SuppressWarnings("serial")
public class Quote implements Serializable{

	private int id;
	private String body;
	private int is_favourist;
	
	/**
	 * @param id
	 * @param body
	 * @param is_favourist
	 */
	public Quote(int id, String body,int is_favourist) {
		// TODO Auto-generated constructor stub
		
		this.id = id;
		this.body = body;
		this.is_favourist = is_favourist;
	}


	public int getId() {
		return id;
	}

	public String getBody() {
		return body;
	}


	public int getIs_favourist() {
		return is_favourist;
	}
	
	/**
	 * @param is_favourist the is_favourist to set
	 */
	public void setIs_favourist(int is_favourist) {
		this.is_favourist = is_favourist;
	}
}
