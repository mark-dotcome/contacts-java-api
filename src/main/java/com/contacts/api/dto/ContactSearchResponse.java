package com.contacts.api.dto;

import com.contacts.api.model.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactSearchResponse {
    private long total;
    private int page;
    private int limit;
    private List<Contact> data;
}
