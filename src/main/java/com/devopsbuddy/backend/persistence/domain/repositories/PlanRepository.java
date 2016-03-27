package com.devopsbuddy.backend.persistence.domain.repositories;

import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by tedonema on 27/03/2016.
 */
@Repository
public interface PlanRepository extends CrudRepository<Plan, Integer> {
}
