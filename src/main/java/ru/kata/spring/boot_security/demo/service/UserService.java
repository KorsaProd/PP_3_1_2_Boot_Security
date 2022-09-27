package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {

 private final UserRepository userRepository;
 private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

 @Autowired
 public UserService(UserRepository userRepository) {
  this.userRepository = userRepository;
 }

 public List<User> getAllUsers() {
  return userRepository.findAll();
 }

 @Transactional
 public User saveUser(User user, Role role) {
  user.addRole(role);
  user.setPassword(passwordEncoder.encode(user.getPassword()));
  user.setName(user.getName());
  return userRepository.save(user);
 }

 public void removeUserById(Integer id) {
  userRepository.deleteById(id);
 }

 public User getUserById(int id) {
  return userRepository.getById(id);
 }

 public User findByUsername(String username) {
  return userRepository.findByUsername(username);
 }

 @Override
 @Transactional
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
  User user = findByUsername(username);
  if (user == null) {
   throw new UsernameNotFoundException(String.format("User '%s' not found", username));
  }
  return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
          mapRolesToAuthorities(user.getRoles()));
 }

 private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
  return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
 }
}
