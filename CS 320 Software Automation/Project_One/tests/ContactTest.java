/*
 * Andres Trujillo
 * 10/13/2024
 * CS 320
 * Prof. Parul Hirpara
 * Contact test cases for our mobile application
 */

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ContactTest {

	/*
	 * Boundary testing for our Contact class.
	 * Ensuring we can not create a null Contact object, this tests that our constructor
	 * throws an illegal argument exception when it encounters a null field, and to 
	 * ensure an object isn't created. Later on we introduce null fields for single
	 * tests but this is the first null test out of those combinations of valid fields
	 * with invalid null fields.
	 */
	@Test
	void testNullContact() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact(null,null,null,null,null);
		});
	}
	
	@Test
	void testContactIDLength() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact(
					"012345678900000", 
					"firstn", 
					"latn", 
					"1234567890",
					"123 address correct, ok 123457");		});
	}

	@Test
	void testValidContactIDLength() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact(
					"0123456789", 
					"firstn", 
					"latn", 
					"1234567890",
					"123 address correct, ok 123457");		});
	}
	
	/*
	 * Testing our first name Contact object to ensure we meet the requirements.
	 * The requirements:
	 * The first name cannot be null, has to be 10 characters or less, and we
	 * can update it.
	 */
	@Test
	void testFirstName() {
		
		Contact settingFirstName = 	new Contact(
					"1", 
					"firstn", 
					"latn", 
					"1234567890",
					"123 address correct, ok 123457");
		//Tests updating the first name to an invalid first name length
		assertThrows(IllegalArgumentException.class, () -> {
			settingFirstName.setFirstName("toolongofaname-fail");
		});

		//Tests creating a Contact with an invalid first name length
		assertThrows(IllegalArgumentException.class,() -> {
			new Contact("1234567890", 
					"firstnametoolong", 
					"latn", 
					"5554567890",
					"123 address correct, ok 123457");
		});
		
		//Tests creating a Contact with a null last name
		assertThrows(IllegalArgumentException.class,() -> {
			new Contact("1234567890", 
					null, 
					"latn", 
					"5554567890",
					"123 address correct, ok 123457");
		});
	}
	
	/*
	 * Testing our Contact class ensuring it meets the requirements.
	 * Requirements for our last name within the Contact class.
	 * The last name cannot be null, we can update it, and the length of the string 
	 * must be 10 characters or less.
	 * If any assertions fail our Contact class doesn't meet these requirements.
	 */
	@Test
	void testLastName() {
		
		//Assertion updating the last name to an invalid last name length
		Contact settingLastName = 	new Contact(
					"1", 
					"firstn", 
					"latn", 
					"1234567890",
					"123 address correct, ok 123457");
		
		assertThrows(IllegalArgumentException.class, () -> {
			settingLastName.setLastName("toolongofaname-fail");
		});
		
		//Assertion creating a Contact with an invalid last name
		assertThrows(IllegalArgumentException.class,() -> {
			new Contact("1234567890", 
					"firg", 
					"lastnametoolong-fail", 
					"5554567890",
					"123 address correct, ok 123457");
		});
		
		//Assertion creating a Contact with a null last name
		assertThrows(IllegalArgumentException.class,() -> {
			new Contact("1234567890", 
					"firstn", 
					null, 
					"5554567890",
					"123 address correct, ok 123457");
		});
	}
	
	
	/*
	 * This test will determine if we have met our requirements for a valid phone number.
	 * Otherwise our Contact class will throw an illegal argument exception.
	 * Requirements for our phone number are that every character must be a digit
	 * and the field is exactly 10 characters.
	 */
	@Test
	void testPhoneNumber() {
		
		//Assertion creating a Contact with a null phone number
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact("1234567890",
						"FirstN",
						"LastN",
						null,
						"123 Valid Address, OK 123457");
		});
		
		//Creating a valid Contact to test the phone number field
		//This object will only be used to assert our tests from the requirements
		Contact updatePhone = new Contact("1234567890",
				"FirstN",
				"LastN",
				"1234567890",
				"123 Valid Address, OK 123457");
		
		//Assertion updating a Contact with an invalid phone number
		//Phone number can not be null
		assertThrows(IllegalArgumentException.class, () -> {
			updatePhone.setPhoneNumber(null);
		});
		
		//Assertion updating a Contact with an invalid length phone number
		//The phone number is 3 digits and 10 required
		assertThrows(IllegalArgumentException.class, () -> {
			updatePhone.setPhoneNumber("123");
		});
		
		//Assertion updating a Contact with an invalid length phone number
		//The phone number is 11 digits and 10 are required
		assertThrows(IllegalArgumentException.class, () -> {
			updatePhone.setPhoneNumber("12345678901");
		});

		
		//Assertion creating a Contact with an invalid length phone number
		//Phone number is tested with 11 digits.
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact("1234567890",
						"FirstN",
						"LastN",
						"12345678900",
						"123 Valid Address, OK 123457");
		});	
		
		//Assertion creating a Contact with an invalid length phone number
		//Phone number is tested with 3 digits.
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact("1234567890",
						"FirstN",
						"LastN",
						"123",
						"123 Valid Address, OK 123457");
		});	
		
		//Assertion creating a Contact with an invalid phone number
		//Phone number is correct length with an alphabetical character
		//instead of a digit.
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact("1234567890",
						"FirstN",
						"LastN",
						"123a567890",
						"123 Valid Address, OK 123457");
		});
		
		//Assertion creating a Contact with an invalid phone number
		//Phone number is all alphabet characters but is the correct length
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact("1234567890",
						"FirstN",
						"LastN",
						"abcdefghij",
						"123 Valid Address, OK 123457");
		});		
	}

}
