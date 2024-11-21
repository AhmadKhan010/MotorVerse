package models;

public class User {
    private int id; // Optional, as it will be auto-incremented
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String role;

    // Constructor
    public User(String name, String email, String password, String phoneNumber, String address, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }
    
    // Constructor with id
	public User(int id, String name, String email, String password, String phoneNumber, String address, String role) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.role = role;
	}

	public User(int id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
		

	public User(int int1, String string, String string2, String string3, String string4) {
		// TODO Auto-generated constructor stub
		this.id = int1;
		this.name = string;
		this.email = string2;
		this.phoneNumber = string3;
		this.role = string4;
		
	}

	// Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

	public int getUserId() {
		// TODO Auto-generated method stub
		return id;
	}
}
