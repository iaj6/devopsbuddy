package com.devopsbuddy.backend.persistence.repositories;

import com.devopsbuddy.backend.persistence.domain.backend.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by tedonema on 29/03/2016.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Returns a User entity for a username or null if the user does not exist.
     * @param usernmae The username to look for
     * @return User entity for a username or null if the user does not exist.
     */
    public User findByUsername(String usernmae);

    /**
     * It retrieves all users
     * @return All users
     */
    public Set<User> findAll();
}
