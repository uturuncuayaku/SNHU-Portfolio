/*
 * Andres Trujillo
 * 10/13/2024
 * CS 320
 * Prof. Parul Hirpara
 * Contact for our mobile application
 */

public class Contact {

    private final String contactId;    // immutable field
    private String firstName;          // can be updated
    private String lastName;           // can be updated
    private String phoneNumber;        // can be updated
    private String address;            // can be updated

    // Constructor to initialize all the fields with validation
    public Contact(String contactId, String firstName, String lastName, String phoneNumber, String address) {
        this.contactId = validateContactId(contactId);
        this.firstName = validateFirstName(firstName);
        this.lastName = validateLastName(lastName);
        this.phoneNumber = validatePhoneNumber(phoneNumber);
        this.address = validateAddress(address);
    }

    // Validation for each field
    public String validateContactId(String contactId) {
        if (contactId == null || contactId.length() > 10) {
            throw new IllegalArgumentException("Contact ID must not be null and must be less than or equal to 10 characters.");
        }
        return contactId;
    }

    public String validateFirstName(String firstName) {
        if (firstName == null || firstName.length() > 10) {
            throw new IllegalArgumentException("First name must not be null and must be less than or equal to 10 characters.");
        }
        return firstName;
    }

    public String validateLastName(String lastName) {
        if (lastName == null || lastName.length() > 10) {
            throw new IllegalArgumentException("Last name must not be null and must be less than or equal to 10 characters.");
        }
        return lastName;
    }

    public String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 10) {  // Assuming a 10-digit phone number
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }
        
        char[] phone = phoneNumber.toCharArray();
        for (int i = 0; i < phone.length; ++i) {
        	if (!Character.isDigit(phone[i])) {
        		throw new IllegalArgumentException("Phone number must be comprised of 10 digits.");
        	}
        }
        return phoneNumber;
    }

    public String validateAddress(String address) {
        if (address == null || address.length() > 30) {
            throw new IllegalArgumentException("Address must not be null and must be less than or equal to 30 characters.");
        }
        return address;
    }

    // Getters
    public String getContactId() {
        return contactId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    // Setters with validation for updateable fields
    public void setFirstName(String firstName) {
        this.firstName = validateFirstName(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = validateLastName(lastName);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = validatePhoneNumber(phoneNumber);
    }

    public void setAddress(String address) {
        this.address = validateAddress(address);
    }
}

