package com.devopsbuddy.backend.persistence.domain.repositories;

import com.devopsbuddy.backend.persistence.domain.backend.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by tedonema on 27/03/2016.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {


}
