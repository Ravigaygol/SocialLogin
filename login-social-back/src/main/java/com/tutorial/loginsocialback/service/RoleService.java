package com.tutorial.loginsocialback.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tutorial.loginsocialback.entity.Role;
import com.tutorial.loginsocialback.enums.ERole;
import com.tutorial.loginsocialback.repository.RoleRepository;

@Service
@Transactional
public class RoleService {

  @Autowired
  RoleRepository rolRepository;

  public Optional<Role> getByRolNombre(ERole rolNombre) {
    return rolRepository.findByRolNombre(rolNombre);
  }

  public boolean existsRolNombre(ERole rolNombre) {
    return rolRepository.existsByRolNombre(rolNombre);
  }

  public void save(Role rol) {
    rolRepository.save(rol);
  }
}
