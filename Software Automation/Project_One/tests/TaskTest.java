/*
 * Author: Andres Trujillo
 * SNHU
 * 10/13/2024
 * Prof. Parul Hirpara
 * 
 * Task Service Class Requirements
 * ---------------------------------
 *  The task service shall be able to add tasks with a unique ID.
 *  
 *  The task service shall be able to delete tasks per task ID.
 *  
 *  The task service shall be able to update task fields per 
 *  task ID. The following fields are updatable: Name, Description
 * ---------------------------------
 */
import junit.framework.TestCase;

public class TaskTest extends TestCase {

	/*
	 * Implementing a test method that successfully creates a Task 
	 * object with valid parameters to confirm that the constructor
	 *  works as expected.
	 */
    public void testValidTask() {
    	
        // Test with a valid ID
        Task validTask = new Task("validId", "name", "description");
        assertNotNull(validTask);
        
        // Test with all null values
        try {
        	Task nullTask = new Task(null, null, null);
        	assertNull(nullTask);
        	fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        	// Expected exception
        }

        // Test with one null value
        try {
        	new Task(null, "valNam", "valid desc");
        	fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        	// Expected exception
        }

        // Test with two valid values, and one null value
        try {
        	new Task("12", null, "valid description");
        	fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        	// Expected exception
        }
        
        // Test with two valid values, and one null value
        try {
        	new Task("123", "name", null);
        	fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        	// Expected exception
        }
    }
    
    public void testId() {
        Task task = new Task("1234567890", "name", "description");
        assertNotNull(task);
        
        // Tests null id
        try {
            new Task(null, "name", "description");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test getting id
        Task test_task = new Task("1234567890", "Task Name", "Task Description");
        String id = test_task.getId();
        assertEquals("1234567890", id);
        
        // Tests too long of an Id
        try {
            new Task("toolongofidthrowsexception", "name", "description");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    public void testName() {
        // Test getName function
        String validName = "valid name";
        Task validTask = new Task("validId", validName, "description");
        String getName = validTask.getName();
        assertEquals(validName, getName);

        // Test null name
        try {
            new Task("1234567890", null, "description");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Tests long name
        try {
            new Task("1234567890", "toolongofanamethrowsexception", "description");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test getting name and updating to a new name
        String original_name = "Task Name";
        Task original_task = new Task("1234567890", original_name, "Task Description");
        String old_name = original_task.getName();
        assertEquals(original_name, old_name);

        // Test updating name
        String new_name = "New Name";
        original_task.setName(new_name);
        assertEquals(new_name, original_task.getName());

        // Test updating name to null
        try {
            original_task.setName(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    public void testDesc() {
        // Test getDescription function
        String desc = "description";
        Task validTask = new Task("validId", "name", desc);
        String getDesc = validTask.getDescription();
        assertEquals(desc, getDesc);

        // Test setDescription function
        String updateDesc = "new description";
        validTask.setDescription(updateDesc);
        String desc1 = validTask.getDescription();
        assertEquals(updateDesc, desc1);

        // Test null description
        try {
            new Task("1234567890", "name", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test too long of a description
        try {
            new Task("1234567890", "name", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test getting description and updating to a new description
        String original_description = "Task description";
        Task test_task = new Task("1234567890", "Task Name", original_description);
        String get_desc = test_task.getDescription();
        assertEquals(original_description, get_desc);

        // Test updating description to valid name
        String new_desc = "New Description";
        test_task.setDescription(new_desc);
        assertEquals(new_desc, test_task.getDescription());

        // Test updating description to null
        try {
            test_task.setDescription(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}