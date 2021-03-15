package com.tutorial.loginsocialback.security;

import com.tutorial.loginsocialback.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipalFactory {

    public static UserPrincipal build(UserEntity usuario){
        List<GrantedAuthority> authorities =
                usuario.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol.getRolNombre().name())).collect(Collectors.toList());
        return new UserPrincipal(usuario.getEmail(), usuario.getPassword(), authorities);
    }
}
