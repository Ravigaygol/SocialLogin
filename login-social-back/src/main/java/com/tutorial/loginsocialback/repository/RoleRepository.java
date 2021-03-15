package com.tutorial.loginsocialback.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tutorial.loginsocialback.entity.Role;
import com.tutorial.loginsocialback.enums.ERole;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  Optional<Role> findByRolNombre(ERole rolNombre);

  boolean existsByRolNombre(ERole rolNombre);
}
