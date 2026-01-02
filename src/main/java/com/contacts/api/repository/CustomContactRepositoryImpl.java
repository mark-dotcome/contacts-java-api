package com.contacts.api.repository;

import com.contacts.api.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

@Repository
public class CustomContactRepositoryImpl implements CustomContactRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomContactRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Contact> searchContacts(String searchQuery, Pageable pageable) {
        Query query = new Query();

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            Pattern pattern = Pattern.compile(searchQuery, Pattern.CASE_INSENSITIVE);
            Criteria criteria = new Criteria().orOperator(
                    Criteria.where("firstName").regex(pattern),
                    Criteria.where("lastName").regex(pattern),
                    Criteria.where("email").regex(pattern),
                    Criteria.where("phone").regex(pattern),
                    Criteria.where("address.street").regex(pattern),
                    Criteria.where("address.city").regex(pattern),
                    Criteria.where("address.state").regex(pattern),
                    Criteria.where("address.zip").regex(pattern)
            );
            query.addCriteria(criteria);
        }

        long total = mongoTemplate.count(query, Contact.class);

        query.with(pageable);
        List<Contact> contacts = mongoTemplate.find(query, Contact.class);

        return new PageImpl<>(contacts, pageable, total);
    }
}
