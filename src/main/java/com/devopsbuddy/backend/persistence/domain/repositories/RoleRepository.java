package com.devopsbuddy.backend.persistence.domain.repositories;

import com.devopsbuddy.backend.persistence.domain.backend.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by tedonema on 27/03/2016.
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
