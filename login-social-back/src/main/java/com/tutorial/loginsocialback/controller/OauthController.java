package com.tutorial.loginsocialback.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.tutorial.loginsocialback.dto.TokenDto;
import com.tutorial.loginsocialback.entity.Role;
import com.tutorial.loginsocialback.entity.UserEntity;
import com.tutorial.loginsocialback.enums.ERole;
import com.tutorial.loginsocialback.security.jwt.JwtProvider;
import com.tutorial.loginsocialback.service.RoleService;
import com.tutorial.loginsocialback.service.UserService;

@RestController
@RequestMapping("/oauth")
@CrossOrigin
public class OauthController {

  @Value("${google.clientId}")
  String googleClientId;

  @Value("${secretPsw}")
  String secretPsw;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  JwtProvider jwtProvider;

  @Autowired
  UserService usuarioService;

  @Autowired
  RoleService rolService;



  @PostMapping("/google")
  public ResponseEntity<TokenDto> google(@RequestBody TokenDto tokenDto) throws IOException {
    final NetHttpTransport transport = new NetHttpTransport();
    final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
    GoogleIdTokenVerifier.Builder verifier =
        new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
            .setAudience(Collections.singletonList(googleClientId));
    final GoogleIdToken googleIdToken =
        GoogleIdToken.parse(verifier.getJsonFactory(), tokenDto.getValue());
    final GoogleIdToken.Payload payload = googleIdToken.getPayload();
    UserEntity usuario = new UserEntity();
    if (usuarioService.existsEmail(payload.getEmail()))
      usuario = usuarioService.getByEmail(payload.getEmail()).get();
    else
      usuario = saveUsuario(payload.getEmail());
    TokenDto tokenRes = login(usuario);
    return new ResponseEntity(tokenRes, HttpStatus.OK);
  }

  @PostMapping("/facebook")
  public ResponseEntity<TokenDto> facebook(@RequestBody TokenDto tokenDto) throws IOException {
    Facebook facebook = new FacebookTemplate(tokenDto.getValue());
    final String[] fields = {"email", "picture"};
    User user = facebook.fetchObject("me", User.class, fields);
    UserEntity usuario = new UserEntity();
    if (usuarioService.existsEmail(user.getEmail()))
      usuario = usuarioService.getByEmail(user.getEmail()).get();
    else
      usuario = saveUsuario(user.getEmail());
    TokenDto tokenRes = login(usuario);
    return new ResponseEntity(tokenRes, HttpStatus.OK);
  }

  private TokenDto login(UserEntity usuario) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(usuario.getEmail(), secretPsw));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtProvider.generateToken(authentication);
    TokenDto tokenDto = new TokenDto();
    tokenDto.setValue(jwt);
    return tokenDto;
  }

  private UserEntity saveUsuario(String email) {
    UserEntity usuario = new UserEntity(email, passwordEncoder.encode(secretPsw));
    Role rolUser = rolService.getByRolNombre(ERole.ROLE_USER).get();
    Set<Role> roles = new HashSet<>();
    roles.add(rolUser);
    usuario.setRoles(roles);
    return usuarioService.save(usuario);
  }

}
