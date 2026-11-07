/*
 * Andres Trujillo
 * 9/22/2024
 */

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ContactServiceTest {

	private ContactService contact_service;
	private Contact test_contact;

	/*
	 * Setup method initializes ContactService and Contact class objects.
	 * This allows us to have a functioning service with an appropriate
	 * Contact object to pre-populate the service's in-memory database.
	 */
	@BeforeEach
	void setUp() {

		//Creates the service for our mobile app
		contact_service = new ContactService();

		//Creates phony test data for testing
		test_contact = new Contact("1234567890",
				"FirstN",
				"LastN",
				"1234567890",
				"123 Address, Ok 12345");

		//Adds one valid contact to the database
		contact_service.addContacts(test_contact);
	}

	@BeforeEach
	void testMultipleContacts() {

		//Creates phony test data for testing
		Contact test_contact0 = new Contact("1134567890",
				"FirstN",
				"LastN",
				"1234567890",
				"123 Address, Ok 12345");
		//Creates phony test data for testing
		Contact test_contact1 = new Contact("2234567890",
				"FirstN",
				"LastN",
				"1234567890",
				"123 Address, Ok 12345");

		//Adds one valid contact to the database
		contact_service.addContacts(test_contact1);
		contact_service.addContacts(test_contact0);
		
		//Retrieve two contacts just created		
		assertNotNull(contact_service.findContactById("1134567890"));
		assertNotNull(contact_service.findContactById("2234567890"));

	}
	/*
	 * Test to ensure we can find a Contact by their ID, if this method 
	 * doesn't work the rest of our tests won't work properly. Therefore,
	 * we need a reliable way to go into our database and pull our Contact
	 * objects that have been stored and verify that information was stored
	 * correctly. 
	 * 
	 */
       @Test
	void testFindContactById() {
		
		//We have set up our contact service with one entry, now
		//we will try to capture it back from the in-memory database.
		Contact foundContact = contact_service.findContactById("1234567890");
		assertNotNull(foundContact, "Contact found by ID.");
		assertEquals(test_contact, foundContact, "Found matches original.");

		//Finding an ID that doesn't exist
		assertThrows(IllegalArgumentException.class, () -> {
			contact_service.findContactById("Idontexist");
		}, "Can't find ID.");

		//Finding a null ID that doesn't exist
		assertThrows(IllegalArgumentException.class, () -> {
			contact_service.findContactById(null);
		}, "Null throws exception.");
	}       

	/*
	 * Tests that verify a unique contact ID is used by the ContactService
	 * class. The requirements:
	 * The contact ID must be unique, cannot be null, and be 10 characters.
	 */
	@Test
	void testUniqueId() {
		String uniqueId = contact_service.generateUniqueId();
		assertNotNull(uniqueId, "Unique ID cannot be null.");
		assertEquals(10, uniqueId.length(), "Unique ID must be 10 characters.");
	}

	/*
	 * Test to add contacts to our mobile app contact service.
	 * Requirements are that we must create a new Contact,
	 * with a valid and unique ID, and must not throw any Illegal Argument
	 * Exceptions due to invalid fields. 
	 */	 
	@Test
	void testAddContacts() {

		//We will create a valid contact
		Contact addNewContact = new Contact("1234567990",
				"NewFNam",
				"NewLNam",
				"1234567990",
				"456 N. Address, OK 12345");
		//Add the valid contact to our service
		contact_service.addContacts(addNewContact);
		assertEquals(addNewContact, contact_service.findContactById("1234567990"), "ContactService add contact method successful.");

		//Assertion tests that we cannot add a duplicate contact by contactId
		assertThrows(IllegalArgumentException.class, () -> {
			contact_service.addContacts(addNewContact);
		}, "Duplicate ID encountered.");
	}

	/*
	 * Test to verify we can delete a contact from the contact service by ID.
	 * Requirements are to delete a contact per contact ID.
	 * So, if we can't find it after we delete it from the database. The test is
	 * successful.
	 */
	@Test
	void testDeleteContact() {

		//Finding an old contact
		Contact oldContact = contact_service.findContactById("1234567890");
		assertNotNull(oldContact, "Found old contact.");

		//Since we have found our old contact we will try to delete it by its contact ID
		//due to requirements specifying we must delete contacts by the unique ID.
		contact_service.deleteContact("1234567890");
		//We attempt to delete the same contact again but
		//since we have already deleted it we get an exception
		assertThrows(IllegalArgumentException.class, () -> {
			contact_service.deleteContact("1234567990");
		}, "ID:1234567990, Contact not found.");

		//Asserting we can't delete a contact that doesn't exist
		assertThrows(IllegalArgumentException.class, () -> {
			contact_service.deleteContact("doesntexis");
		});
	}

	/*
	 * Testing whether the updated phone number is correct and that we are able to update it
	 * to a new value. Also, verifies we have the validation rules according to our requirements
	 * specification.
	 * Requirements: 
	 * The phone number must be updateable, cannot be null, and is exactly 10 digits.
	 */
       @Test
	void testUpdatePhoneNumber() {
		
		String phone = test_contact.getPhoneNumber();
		String new_number = "9876543210";

		/*Changing the phone number of a contact*/
		contact_service.updatePhoneNumber(test_contact, new_number);

		//Check it doesn't equal the previous phone number.
		String updated_phone = test_contact.getPhoneNumber();
		assertFalse(phone == updated_phone);		

		//Check the phone number is the recently updated number.
		assertEquals(new_number, test_contact.getPhoneNumber(), "Phone number is updated successfully.");

		/*Tests checking invalid phone numbers*/
		//First assertion checks a short number
		assertThrows(IllegalArgumentException.class, () -> {
			contact_service.updatePhoneNumber(test_contact, "123");
		}, "Phone number is not long enough.");
		//Second assertion checks we throw an error with alphabetic number
		assertThrows(IllegalArgumentException.class, () -> {
			contact_service.updatePhoneNumber(test_contact, "asdasdasdf");
		}, "Phone number contains alphabetic characters.");
		//Third assertion checks we throw an error with null
		assertThrows(IllegalArgumentException.class, () -> {
			contact_service.updatePhoneNumber(test_contact, null);
		}, "Phone number cannot be null");
		
	}       
       

       /*
	* Tests updating the Address of the Contact class to ensure it meets requirements.
	* The requirements:
	* Cannot be null, and must be less than 30 characters.
	*/
       @Test
       void testUpdateAddress() {
	       //Create a new address and update it
	       String new_address = "789 New Street, OK 12344";
	       String address = test_contact.getAddress();
	       assertFalse(new_address == address, "New address and old address are different");

	       //Update address
	       contact_service.updateAddress(test_contact, new_address);

	       //Checking the updated address ensures address was updated correctly
	       assertTrue(new_address == test_contact.getAddress(), "Address updated correctly.");
		//Check address can't be updated with null 
	 	assertThrows(IllegalArgumentException.class, () -> {
			contact_service.updateAddress(test_contact, null);
		}, "Address cannot be null");

       }

       /*
	* Tests first name update methods
	* Ensures updating the first name meets the first name requirements
	* First name cannot be null, and the first name must be less than or equal to 10 characters.
	*/
      @Test
     void testUpdateFirstName() {
	     String new_name = "John";
	     contact_service.updateFirstName(test_contact, new_name);
	     //Assertion the new name has been updated correctly
	     assertTrue(new_name == test_contact.getFirstName(), "First name updated, correctly.");

	     //Assertion the first name can't be too long
	     assertThrows(IllegalArgumentException.class, () -> {
			contact_service.updateFirstName(test_contact, "aasdfdasfasdfasdfasdf");
	     }, "First name must be 10 characters or less.");

	     //Assertion that the first name can't be null
	     assertThrows(IllegalArgumentException.class, () -> {
			contact_service.updateFirstName(test_contact, null);
	     }, "First name cannot be null.");

     } 

     
     /* 
      * Testing last name update methods for contact service
      * Requirements:
      * Last name cannot be null, and must be 10 or less characters
      */
     @Test
     void testUpdateLastName() {
	     String new_name = "Doe";
	     contact_service.updateLastName(test_contact, new_name);
	     assertTrue(new_name == test_contact.getLastName(), "Last name updated correctly.");

	     //Assertion the last name can't be too long
	     assertThrows(IllegalArgumentException.class, () -> {
			contact_service.updateLastName(test_contact, "asdfasdfasdfasdfasdfasdf");
	     }, "Last name must be 10 characters or less.");

	     //Assertion the last name cannot be null
	     assertThrows(IllegalArgumentException.class, () -> {
			contact_service.updateLastName(test_contact, null);
	     }, "Last name cannot be null.");
     }

}