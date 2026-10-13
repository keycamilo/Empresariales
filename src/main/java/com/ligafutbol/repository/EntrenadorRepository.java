package com.ligafutbol.repository;

import com.ligafutbol.model.Entrenador;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntrenadorRepository extends MongoRepository<Entrenador, String> {
}
