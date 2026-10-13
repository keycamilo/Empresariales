package com.ligafutbol.repository;

import com.ligafutbol.model.Competicion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompeticionRepository extends MongoRepository<Competicion, String> {
}
