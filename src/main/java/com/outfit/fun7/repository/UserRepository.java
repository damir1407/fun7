package com.outfit.fun7.repository;

import com.outfit.fun7.model.User;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.Optional;

public interface UserRepository extends DatastoreRepository<User, Long> {
    Optional<User> findById(Long id);
}
