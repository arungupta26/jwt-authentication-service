package com.jwt.authentication.repos;

import java.util.Optional;

import com.jwt.authentication.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppUserRepo  extends MongoRepository<AppUser, String> {

	boolean existsByUserName(String username);

	Optional<AppUser> findByUserName(String username);
}
