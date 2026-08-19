package com.starkIndustries.models;

import java.util.Collection;
import java.util.Collections;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class UserPrincipal implements UserDetails{

  public Users users;

  public UserPrincipal(Users users){
    this.users=users;
  }

  public UserPrincipal(){

  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority("USER"));
  }

  @Override
  public @Nullable String getPassword() {
    return this.users.getPassword();
  }

  @Override
  public String getUsername() {
    return this.users.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
  return true;
  }

  @Override
  public boolean isAccountNonLocked() {
  return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
  return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
  
}
