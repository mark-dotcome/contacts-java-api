package com.contacts.api.service;

import com.contacts.api.dto.ContactSearchResponse;
import com.contacts.api.model.Contact;
import com.contacts.api.repository.ContactRepository;
import com.contacts.api.repository.CustomContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private static final String DEFAULT_USER = "mark_t_womack@hotmail.com";

    private final ContactRepository contactRepository;
    private final CustomContactRepository customContactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository, CustomContactRepository customContactRepository) {
        this.contactRepository = contactRepository;
        this.customContactRepository = customContactRepository;
    }

    public List<Contact> getAllContacts() {
        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "lastName"));
        return contactRepository.findAll(pageable).getContent();
    }

    public ContactSearchResponse searchContacts(String query, int page, String sortBy, String order, int limit) {
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sortBy));

        Page<Contact> contactPage = customContactRepository.searchContacts(query, pageable);

        return new ContactSearchResponse(
                contactPage.getTotalElements(),
                page,
                limit,
                contactPage.getContent()
        );
    }

    public Optional<Contact> getContactById(String id) {
        return contactRepository.findById(id);
    }

    public Contact createContact(Contact contact) {
        LocalDateTime now = LocalDateTime.now();
        contact.setCreatedBy(DEFAULT_USER);
        contact.setCreatedDt(now);
        contact.setModifiedBy(DEFAULT_USER);
        contact.setModifiedDt(now);
        return contactRepository.save(contact);
    }

    public Optional<Contact> updateContact(String id, Contact contactDetails) {
        return contactRepository.findById(id)
                .map(existingContact -> {
                    existingContact.setFirstName(contactDetails.getFirstName());
                    existingContact.setLastName(contactDetails.getLastName());
                    existingContact.setEmail(contactDetails.getEmail());
                    existingContact.setPhone(contactDetails.getPhone());
                    existingContact.setAddress(contactDetails.getAddress());
                    existingContact.setApp(contactDetails.getApp());
                    existingContact.setModifiedBy(DEFAULT_USER);
                    existingContact.setModifiedDt(LocalDateTime.now());
                    return contactRepository.save(existingContact);
                });
    }

    public boolean deleteContact(String id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
