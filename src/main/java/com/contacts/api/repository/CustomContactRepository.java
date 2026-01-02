package com.contacts.api.repository;

import com.contacts.api.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomContactRepository {
    Page<Contact> searchContacts(String query, Pageable pageable);
}
