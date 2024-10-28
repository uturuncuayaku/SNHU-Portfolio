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

import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class AppointmentTest {

	// Constants for units of time
	private static final Long TWENTY_FOUR_HOURS = (long) 86400000;
	private static final Long HOUR 				= (long) (1*1000*3600);
	
	/*
	 * Testing valid appointment dates.
	 */
    @Test
    public void testValidAppointment() {
    	
    	//Getting current time
    	long current_time = System.currentTimeMillis();
    	
        // Creating valid appointment objects:
    	// Three unique Id's
    	// Three valid dates in the future
    	// Three valid length descriptions
    	Appointment appt0 = new Appointment("0123456789", new Date(current_time+(1*HOUR)), "Doctor's appointment");
    	Appointment appt1 = new Appointment("1023456789", new Date(current_time+(1*HOUR)+(30*HOUR)), "Dentist's appointment");
    	Appointment appt2 = new Appointment("1203456789", new Date(current_time+(1*HOUR)+(31*HOUR)), "Orthopedic's appointment");
    	Appointment appt3 = new Appointment("1230456789", new Date(current_time+(1*HOUR)+ (32*HOUR)), "Opthamologist's appointment");
		
		//Testing objects
        assertNotNull(appt0);
        assertNotNull(appt1);
        assertNotNull(appt2);
        assertNotNull(appt3);
                
    	//Testing ID equality
        assertEquals("0123456789", appt0.getId());
		assertEquals("1023456789", appt1.getId());
		assertEquals("1203456789", appt2.getId());
		assertEquals("1230456789", appt3.getId());

		//Testing date to date set
        assertTrue(appt0.getDate().equals(new Date(current_time+(1*HOUR))));
        assertTrue(appt1.getDate().equals(new Date(current_time+(1*HOUR)+ (30*HOUR))));
        assertTrue(appt2.getDate().equals(new Date(current_time+(1*HOUR)+ (31*HOUR))));
        assertTrue(appt3.getDate().equals(new Date(current_time+(1*HOUR)+ (32*HOUR))));
        
        //Testing description to description set.
        assertEquals("Doctor's appointment", appt0.getDescription());
        assertEquals("Dentist's appointment", appt1.getDescription());
        assertEquals("Orthopedic's appointment", appt2.getDescription());
        assertEquals("Opthamologist's appointment", appt3.getDescription());
    }

    @Test
    public void testNullAppointmentId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment(null, new Date(System.currentTimeMillis() + TWENTY_FOUR_HOURS), "Meeting");
        });
    }

    @Test
    public void testAppointmentIdTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
			new Appointment("12345678901", new Date(System.currentTimeMillis() + TWENTY_FOUR_HOURS), "Long ID Test");
        });
    }

    @Test
    public void testNullDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment("123456789", new Date(System.currentTimeMillis() + TWENTY_FOUR_HOURS), null);
        });
    }
    
    @Test
    public void testGoodDescription() {
        assertDoesNotThrow(() -> {
            new Appointment("123456789", new Date(System.currentTimeMillis() + TWENTY_FOUR_HOURS), "Good description.");
        });
    }


    @Test
    public void testDescriptionTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment("12345", new Date(System.currentTimeMillis() + 86400000), "This is a very long description exceeding 50 characters in total length.");
        });
    }

    @Test
    public void testNullDate() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment("12345", null, "Meeting");
        });
    }

    @Test
    public void testPastDate() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment("12345", new Date(System.currentTimeMillis() - 86400000), "Past Meeting");
        });
    }
}
