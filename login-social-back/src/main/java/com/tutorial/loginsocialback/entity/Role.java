package com.tutorial.loginsocialback.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.sun.istack.NotNull;
import com.tutorial.loginsocialback.enums.ERole;

@Entity
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Enumerated(EnumType.STRING)
  @NotNull
  @Column(unique = true)
  private ERole rolNombre;

  public Role() {}

  public Role(ERole rolNombre) {
    this.rolNombre = rolNombre;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ERole getRolNombre() {
    return rolNombre;
  }

  public void setRolNombre(ERole rolNombre) {
    this.rolNombre = rolNombre;
  }
}
