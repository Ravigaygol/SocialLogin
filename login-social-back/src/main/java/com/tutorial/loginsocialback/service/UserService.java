package com.tutorial.loginsocialback.service;

import com.tutorial.loginsocialback.entity.UserEntity;
import com.tutorial.loginsocialback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository usuarioRepository;

    public Optional<UserEntity> getByEmail(String email){
        return usuarioRepository.findByEmail(email);
    }

    public boolean existsEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public UserEntity save(UserEntity usuario){
        return usuarioRepository.save(usuario);
    }
}
