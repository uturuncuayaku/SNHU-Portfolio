/*
 * Author: Andres Trujillo
 * SNHU
 * 10/13/2024
 * Prof. Parul Hirpara
 * 
 * Appointment Requirements
 * ---------------------------------
 * The appointment object shall have a required unique appointment ID string
 * 	that cannot be longer than 10 characters. The appointment ID shall not be 
 * 	null and shall not be updatable. 
 *   
 *  The appointment object shall have a required appointment Date field. The 
 *  	appointment Date field cannot be in the past. The appointment Date field 
 *  	shall not be null.
 *  
 *  The appointment object shall have a required description String field that 
 *  	cannot be longer than 50 characters. The description field shall not be null.
 * ---------------------------------
 */


import java.util.Date;

public class Appointment {
	
	//Field members
	String uniqueId;
	Date dateOfAppt;
	String description;
	
	//Public constructor
	public Appointment(String uniqueId, Date dateOfAppt, String description) {
		this.setId(uniqueId);
		this.setDate(dateOfAppt);
		this.setDescription(description);
	}
	
	//Sets id 
	public void setId(String id) {
		if ((id == null) || id.length() > 10) {
			throw new IllegalArgumentException();
		} else {
			this.uniqueId = id;
		}
	}
	
	//Sets appointment date
	public void setDate(Date appt_date) {
		if (appt_date == null || !validAppt(appt_date)) {
			throw new IllegalArgumentException();
		} else {
			this.dateOfAppt = appt_date;
		}
	}
	
	//Sets description
	public void setDescription(String desc) {
		if (desc == null || desc.length() > 50) {
			throw new IllegalArgumentException();
		} else {
			this.description = desc;
		}
	}
	
	/* Getters */
	public String getId() {return this.uniqueId;}
	public Date getDate() {return this.dateOfAppt;}
	public String getDescription() {return this.description;}
	
	//Helper method
	private boolean validAppt(Date future_date) {
		return future_date.after(new Date(System.currentTimeMillis()));
	}

}
