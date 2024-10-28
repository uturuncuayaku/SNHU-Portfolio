/*
 * Author: Andres Trujillo
 * SNHU
 * 10/13/2024
 * Prof. Parul Hirpara
 * 
 * Task Class Requirements
 * ---------------------------------
 *  The task object shall have a required unique task ID String 
 *  	that cannot be longer than 10 characters. The task ID 
 *  	shall not be null and shall not be updatable.
 *  
 *  The task object shall have a required name String field that
 *  	cannot be longer than 20 characters. The name field shall 
 *  	not be null.
 *  
 *  The task object shall have a required description String field
 *  	that cannot be longer than 50 characters. The description
 *  	field shall not be null.
 * ---------------------------------
 */

/*
 * A task object has a unique id, with a name and description.
 */
public class Task {

	//Field members
	private final String Id;
	private String name;
	private String description;
	
	/*
	 * Constructor - Validates Id, name, and description.
	 * Creates a task object.
	 * @throws IllegalArgumentException
	 */
	public Task(String Id, String name, String description){
		
		//Validation 
		validateId(Id);
		validateName(name);
		validateDesc(description);
		
		//Initial object creation
		this.Id = Id;
		this.name = name;
		this.description = description;
	}
	
	/*
	 * Validates the Id matches requirements.
	 * Must have less than 10 characters and not be null
	 * @param	Id	A string of the unique Id
	 * @throws	IllegalArgumentException	 
	 * @return	none
	 */
	private void validateId(String Id) {
		if (Id == null || Id.length() > 10) {
			throw new IllegalArgumentException("ID is invalid, cannot be longer than 10 characters or null.");
		}
	}
	
	/*
	 * Validates the name according to requirements.
	 * Must be less than 20 characters long and not null.
	 * @param	name	A string of the name to check
	 * @throws	IllegalArgumentException
	 * @return	none
	 */
	private void validateName(String name) {
		if (name == null || name.length() > 20) {
			throw new IllegalArgumentException("Name is invalid, cannot be longer than 20 characters or null.");
		}
	}
	
	/*
	 * Validates the description for a task object.
	 * Must be less than 50 characters and not be null.
	 * @param	description		A string describing the task.
	 * @throws	IllegalArgumentException	
	 * @return	none
	 */
	private void validateDesc(String description) {
		if (description == null || description.length() > 50) {
			throw new IllegalArgumentException("Description is invalid, cannot be longer than 50 characters or null.");
		}
	
	}
	
	/*
	 * Gets the name of the task object.
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * Sets the name after validating it.
	 * @return none
	 */
	public void setName(String name) {
		validateName(name);
		this.name = name;
	}

	/*
	 * Gets the description for a task.
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * Updates the description of a task after validating it
	 * to the specified requirements.
	 * @param	description
	 * @return none
	 */
	public void setDescription(String description) {
		validateDesc(description);
		this.description = description;
	}

	/*
	 * Gets the Id for a task,
	 * @return String
	 */
	public String getId() {
		return Id;
	}
	
}