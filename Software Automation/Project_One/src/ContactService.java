
/*
 * Andres Trujillo
 * 9/22/2024
 */

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ContactService {
	
	private Map<String, Contact> contacts;

	public ContactService() {
		contacts = new HashMap<>();
	}

	public String generateUniqueId() {
		return UUID.randomUUID().toString().substring(0,10);
	}

	public void addContacts(Contact aContact) {
		if (contacts.containsKey(aContact.getContactId())) {
			throw new IllegalArgumentException("Contact Id already exists!");
		}
		contacts.put(aContact.getContactId(), aContact);
	}

	public void deleteContact(String contactId) {
		if (!contacts.containsKey(contactId)) {
			throw new IllegalArgumentException("Contact not found.");
		}
		contacts.remove(contactId);
	}

	public void updateFirstName(Contact contact, String fName) {
		contact.setFirstName(contact.validateFirstName(fName));
		
	}

	public void updateLastName(Contact contact, String lName) {
		contact.setLastName(contact.validateLastName(lName));
	}

	public void updateAddress(Contact contact, String address) {
		contact.setAddress(contact.validateAddress(address));
	}

	public void updatePhoneNumber(Contact contact, String phone) {
		contact.setPhoneNumber(contact.validatePhoneNumber(phone));
	}

	public Contact findContactById(String contactId) {
		if (!contacts.containsKey(contactId)) {
			throw new IllegalArgumentException("ID "+contactId +": Contact not found.");
		}
		return contacts.get(contactId);
	}
}
