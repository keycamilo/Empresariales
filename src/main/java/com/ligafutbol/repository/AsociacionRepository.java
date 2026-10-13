package com.ligafutbol.repository;

import com.ligafutbol.model.Asociacion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AsociacionRepository extends MongoRepository<Asociacion, String> {
}
