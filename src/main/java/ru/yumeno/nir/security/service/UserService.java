package ru.yumeno.nir.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yumeno.nir.exception_handler.exceptions.ResourceAlreadyExistException;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;
import ru.yumeno.nir.security.dto.RegistrationUserDTO;
import ru.yumeno.nir.security.entity.Role;
import ru.yumeno.nir.security.entity.User;
import ru.yumeno.nir.security.repository.RoleRepository;
import ru.yumeno.nir.security.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void injectPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByUsername(String username) {
        Optional<User> optional = userRepository.findByUsername(username);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new UsernameNotFoundException("User with username " + username + "not found");
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()));
    }

    public User addUser(RegistrationUserDTO registrationUserDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(registrationUserDTO.getUsername());
        if (optionalUser.isPresent()) {
            throw new ResourceAlreadyExistException("Пользователь с именем " + registrationUserDTO.getUsername() +
                    "уже существует");
        }
        User user = new User();
        user.setUsername(registrationUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationUserDTO.getPassword()));
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        if (optionalRole.isPresent()) {
            user.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));
            return userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("Роль с именем ROLE_USER не найдена");
        }
    }
}
