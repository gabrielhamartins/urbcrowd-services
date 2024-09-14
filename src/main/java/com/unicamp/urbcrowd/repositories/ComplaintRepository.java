package com.unicamp.urbcrowd.repositories;

import com.unicamp.urbcrowd.models.Complaint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends MongoRepository<Complaint, String> {

}
