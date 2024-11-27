package models;

public class CustomerSupport {
	
	private int ticketId;
	private int userId;
	private String description;
	private String status;
	
	public CustomerSupport(int ticketId,int userId, String description, String status) {
		this.userId = userId;
		this.description = description;
		this.status = status;
		this.ticketId = ticketId;
	}
	
	public CustomerSupport(int userId, String description, String status) {
		this.userId = userId;
		this.description = description;
		this.status = status;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getStatus() {
		return status;
	}
	
	public int getTicketId() {
		return ticketId;
	}
	
	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
}
