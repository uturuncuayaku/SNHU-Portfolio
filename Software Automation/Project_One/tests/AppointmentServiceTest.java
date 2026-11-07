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

import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppointmentServiceTest {

    private AppointmentService service;

    @BeforeEach
    public void setUp() {
    	// Constants for units of time
    	Long HOUR = (long) (1*1000*3600);
    	
        service = new AppointmentService();
        
        // Create valid appointment objects
    	// Three unique Id's
    	// Three valid dates in the future
    	// Three valid length descriptions
        long current_time = System.currentTimeMillis();
    	Appointment appt0 = new Appointment("0123456789", new Date(current_time+(1*HOUR)), "Doctor's appointment");
    	Appointment appt1 = new Appointment("1023456789", new Date(current_time+(1*HOUR)+(30*HOUR)), "Dentist's appointment");
    	Appointment appt2 = new Appointment("1203456789", new Date(current_time+(1*HOUR)+(31*HOUR)), "Orthopedic's appointment");
    	Appointment appt3 = new Appointment("1230456789", new Date(current_time+(1*HOUR)+ (32*HOUR)), "Opthamologist's appointment");

    	service.addAppointment(appt0);
    	service.addAppointment(appt1);
    	service.addAppointment(appt2);
    	service.addAppointment(appt3);
    	
    	assertNotNull(service.getAppointment(appt0.getId()));
    	assertNotNull(service.getAppointment(appt1.getId()));
    	assertNotNull(service.getAppointment(appt2.getId()));
    	assertNotNull(service.getAppointment(appt3.getId()));
        
    }

    @Test
    public void testAddAppointment() {
        Appointment appt = new Appointment("12345", new Date(System.currentTimeMillis() + 86400000), "Doctor's appointment");
        service.addAppointment(appt);
        assertNotNull(service.getAppointment("12345"));
    }

    @Test
    public void testAddDuplicateAppointment() {
        Appointment appt1 = new Appointment("12345", new Date(System.currentTimeMillis() + 86400000), "Doctor's appointment");
        service.addAppointment(appt1);
        Appointment appt2 = new Appointment("12345", new Date(System.currentTimeMillis() + 172800000), "Dentist appointment");

        assertThrows(IllegalArgumentException.class, () -> {
            service.addAppointment(appt2);
        });
    }

    @Test
    public void testDeleteAppointment() {
        Appointment appt = new Appointment("12345", new Date(System.currentTimeMillis() + 86400000), "Doctor's appointment");
        service.addAppointment(appt);
        service.deleteAppointment("12345");
        
        assertThrows(IllegalArgumentException.class, () -> {
            service.getAppointment("12345");
        });
    }

    @Test
    public void testDeleteNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.deleteAppointment("nonexistent");
        });
    }

    @Test
    public void testGetNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.getAppointment("nonexistent");
        });
    }
}
