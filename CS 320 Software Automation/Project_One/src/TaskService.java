/*
 * Author: Andres Trujillo
 * SNHU
 * 10/13/2024
 * Prof. Parul Hirpara
 * 
 * Task Service Requirements
 * ---------------------------------
 *  The task service shall be able to add tasks with a unique ID.
 *  
 *  The task service shall be able to delete tasks per task ID.
 *  
 *  The task service shall be able to update task fields per 
 *  task ID. The following fields are updatable: Name, Description
 * ---------------------------------
 */

import java.util.HashMap;

/*
 * Our task service object contains a hash map implementing an in-memory database
 * 
 */
public class TaskService {
	
	//Field members
	private HashMap<String, Task> task_map;
	
	/*
	 * Constructor 
	 * Generates our hash map, that will act as our database.
	 */
	public TaskService() {
		//Creates and initializes the database
		task_map = new HashMap<String, Task>();
		
	}
	
	/*
	 * Function to add a task to our database
	 */
	public void addTasks(String id, Task task) {
		//Search through the hash map for a task with the id given
		if (id != null && task != null && id == task.getId() && !task_map.containsKey(id)) {
			//Place our task within the mapping of our unique id			
			task_map.put(id, task);
			
		}else {
			throw new IllegalArgumentException();
		}
	}
	
	/*
	 * Function to delete a task
	 * @throws IllegalArgumentException if there is no id to delete
	 */
	public void deleteTasks(String id) {
		//Search for the key within the hash mapping.
		if (task_map.containsKey(id)) {
			//Remove the task if we find it.
			task_map.remove(id);
			
		}else {
			throw new IllegalArgumentException();
		}
	}
	
	/*
	 * Function updates a task's name
	 * Will set the name but first we must check if it exists
	 * and we must also validate the name as well.
	 * @throws	IllegalArgumentException 
	 */
	public void updateTaskName(String id, String name){
		//Search for the task.
		if (task_map.containsKey(id)) {
			//update the task name if found.
			Task updateTask = task_map.get(id);
			updateTask.setName(name);
		}else {
			throw new IllegalArgumentException();
		}
	}
	
	/*
	 * Function that updates the description according the 
	 * requirements for a description length.
	 * @throws	IllegalArgumentException
	 */
	public void updateTaskDescription(String id, String desc) {
		//Searches for the task.
		if (task_map.containsKey(id)) {
			//Updates the task description.
			Task updateTask = task_map.get(id);
			updateTask.setName(desc);
		}else {
			throw new IllegalArgumentException();
		}
	}
}
