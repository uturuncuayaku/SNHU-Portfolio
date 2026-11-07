/*
 * Author: Andres Trujillo
 * SNHU
 * 10/13/2024
 * Prof. Parul Hirpara
 * 
 * Task Service Test Requirements
 * ---------------------------------
 *  The task service shall be able to add tasks with a unique ID.
 *  
 *  The task service shall be able to delete tasks per task ID.
 *  
 *  The task service shall be able to update task fields per 
 *  task ID. The following fields are updatable: Name, Description
 * ---------------------------------
 */
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class TaskServiceTest {

    private TaskService task_service;
    private Task task;
    
    @Before
    public void createTaskService() {
        task_service = new TaskService();
        task = new Task("1234567890", "Task Name", "Description of task.");
        //Adding multiple tasks
        
        Task anotherTask = new Task("1234567800", "Task Name", "Description of task.");
        Task anotherTask0 = new Task("1234567810", "Task Name", "Description of task.");
        Task anotherTask1 = new Task("1234567801", "Task Name", "Description of task.");

        assertNotNull(task_service);
        assertNotNull(task);
        
        //Continue adding tasks if above doesn't fail
        assertNotNull(anotherTask);
        assertNotNull(anotherTask0);
        assertNotNull(anotherTask1);
        
        task_service.addTasks("1234567890", task);
        task_service.addTasks("1234567810", anotherTask0);
        task_service.addTasks("1234567801", anotherTask1);
                       
    }
    
    @Test
    public void testAddingTask() {
        
        // Test adding a task to the service
        try {
            task_service.addTasks(task.getId(), task);
        } catch (Exception e) {
            fail("Exception thrown while adding valid task: " + e.getMessage());
        }
        
        // Test adding a duplicate task
        try {
            task_service.addTasks("1234567890", task);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test adding a null id
        try {
            task_service.addTasks(null, task);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test adding a null task
        try {
            task_service.addTasks("1234567890", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test adding a long task id
        try {
            task_service.addTasks("19234123567890", task);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test adding two more tasks
        Task task1 = new Task("9234567890", "Task Name", "Description of task.");
        Task task2 = new Task("8234567890", "Task Name", "Description of task.");
        try {
            task_service.addTasks(task1.getId(), task1);
            task_service.addTasks(task2.getId(), task2);
        } catch (Exception e) {
            fail("Exception thrown while adding valid tasks: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeletingTask() {
        task_service.addTasks(task.getId(), task);
        
        // Test deleting a task
        try {
            task_service.deleteTasks(task.getId());
        } catch (Exception e) {
            fail("Exception thrown while deleting task: " + e.getMessage());
        }
        
        // Test ensuring task was deleted
        try {
            task_service.deleteTasks("1234567890");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test deleting too long of an Id
        try {
            task_service.deleteTasks("12334567890");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test deleting a null task
        try {
            task_service.deleteTasks(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testUpdateTaskName() {
        // Adding a valid task to the service
        task_service.addTasks(task.getId(), task);
        
        // Good name and good Id
        try {
            task_service.updateTaskName(task.getId(), "Good Name");
        } catch (Exception e) {
            fail("Exception thrown while updating task name: " + e.getMessage());
        }
        
        // Name is too long
        try {
            task_service.updateTaskName(task.getId(), "Baaadddddddddd Naaammmeee");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Name can't be null
        try {
            task_service.updateTaskName(task.getId(), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Can't have null Id
        try {
            task_service.updateTaskName(null, "Good Name");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Can't find unique Id
        try {
            task_service.updateTaskName("1", "Good Name");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Can't find too long of an Id
        try {
            task_service.updateTaskName("11111111111111111111111111", "Good Name");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testUpdateTaskDesc() {
        task_service.addTasks(task.getId(), task);
        
        try {
            task_service.updateTaskDescription(task.getId(), "Good description");
        } catch (Exception e) {
            fail("Exception thrown while updating task description: " + e.getMessage());
        }
        
        try {
            task_service.updateTaskDescription(task.getId(), "BBBBBBBBBBBBBBBBBBaaaaaaaaaaaaaaaaaaaadddddddddddddd deeeeeeescriiiiiiiiiiiiiiiiipppptiiiiiiiiionnnnnnnnnnnnnnnnnn");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            task_service.updateTaskDescription(task.getId(), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            task_service.updateTaskDescription(null, "Good description.");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }    
}