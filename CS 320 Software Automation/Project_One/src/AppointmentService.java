/*
 * Author: Andres Trujillo
 * SNHU
 * 10/13/2024
 * Prof. Parul Hirpara
 * 
 * Appointment Service Requirements
 * --------------------------------------------
 * The appointment service shall be able to add appointments with a unique appointment ID.
 * 
 * The appointment service shall be able to delete appointments per appointment ID.
 *
 */


public class AppointmentService {
	
	private java.util.HashMap<String, Appointment> appts;
	
	//Constructs the initialization of an appointment service
	public AppointmentService() {
		this.appts = new java.util.HashMap<String, Appointment>();
	}
	
	//Constructs a valid appointment from a parameter to the service
	public AppointmentService(Appointment appt) {
		this.addAppointment(appt);
	}
	
	//Add's a valid appointment to the service mapping
	public void addAppointment(Appointment appt) {
		String apptId = appt.getId();
		if (appts.containsKey(apptId)) {
			throw new IllegalArgumentException();
		} else {
			appts.put(apptId, appt);
		}
	}
	
	//Delete's a valid appointment
	public void deleteAppointment(String id) {
		if (appts.containsKey(id)) {
			appts.remove(id);
		} else {
			throw new IllegalArgumentException();
		}
	}
    // Get an appointment by id
    public Appointment getAppointment(String apptId) {
        if (appts.containsKey(apptId)) {
            return appts.get(apptId);
        } else {
            throw new IllegalArgumentException("Appointment with ID " + apptId + " not found.");
        }
    }

    // Retrieve all appointments
    public java.util.HashMap<String, Appointment> getAllAppointments() {
        return new java.util.HashMap<>(appts);
    }	
	
}
