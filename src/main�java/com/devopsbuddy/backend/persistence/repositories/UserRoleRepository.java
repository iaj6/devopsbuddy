package com.devopsbuddy.backend.persistence.repositories;

import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by tedonema on 30/03/2016.
 */
@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
}
