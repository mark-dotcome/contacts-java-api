package com.contacts.api.controller;

import com.contacts.api.dto.ContactSearchResponse;
import com.contacts.api.exception.ResourceNotFoundException;
import com.contacts.api.model.Contact;
import com.contacts.api.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contacts")
@Tag(name = "Contacts", description = "Contact management API")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/search")
    public ResponseEntity<ContactSearchResponse> searchContacts(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "sort_by", defaultValue = "lastName") String sortBy,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        if (page < 1) page = 1;
        if (limit < 1) limit = 1;
        if (limit > 100) limit = 100;

        ContactSearchResponse response = contactService.searchContacts(query, page, sortBy, order, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{contactId}")
    public ResponseEntity<Contact> getContactById(@PathVariable String contactId) {
        Contact contact = contactService.getContactById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        return ResponseEntity.ok(contact);
    }

    @PostMapping("/")
    public ResponseEntity<Contact> createContact(@Valid @RequestBody Contact contact) {
        Contact createdContact = contactService.createContact(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<Contact> updateContact(
            @PathVariable String contactId,
            @Valid @RequestBody Contact contact) {
        Contact updatedContact = contactService.updateContact(contactId, contact)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Map<String, String>> deleteContact(@PathVariable String contactId) {
        boolean deleted = contactService.deleteContact(contactId);
        if (!deleted) {
            throw new ResourceNotFoundException("Contact not found");
        }
        return ResponseEntity.ok(Map.of("message", "Contact deleted"));
    }
}
