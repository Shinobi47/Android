/**
 * 
 */
package funny.topic.free.jokes.db.entity;

/**
 * @author ThangTB
 *
 */
public class QOD {

	private int id;
	private Long time;
	private String body;
	/**
	 * 
	 */
	public QOD(int id,
	 Long time,
	 String body) {
		this.id = id;
		this.time = time;
		this.body = body;
	}
	
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @return the time
	 */
	public Long getTime() {
		return time;
	}
}
