package ru.yumeno.nir.security.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.dto.NewsResponseDTO;
import ru.yumeno.nir.security.dto.AuthRequestDTO;
import ru.yumeno.nir.security.dto.AuthResponseDTO;
import ru.yumeno.nir.security.dto.RegistrationUserDTO;
import ru.yumeno.nir.security.dto.UserDTO;
import ru.yumeno.nir.security.entity.Role;
import ru.yumeno.nir.security.entity.User;
import ru.yumeno.nir.security.jwt.JwtTokenProvider;
import ru.yumeno.nir.security.service.UserService;

import java.util.List;

@RestController
@CrossOrigin("*")
@Slf4j
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/auth")
    public AuthResponseDTO userAuthentication(@RequestBody AuthRequestDTO authRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(),
                authRequestDTO.getPassword()));
        UserDetails userDetails = userService.loadUserByUsername(authRequestDTO.getUsername());
        String token = jwtTokenProvider.generateToken(userDetails);
        return new AuthResponseDTO(token);
    }

    @PostMapping(value = "/registration")
    public UserDTO userRegistration(@RequestBody RegistrationUserDTO registrationUserDTO) {
        if (!registrationUserDTO.getPassword().equals(registrationUserDTO.getConfirmPassword())) {
            throw new BadCredentialsException("Пароли не совпадают");
        }
        User user = userService.addUser(registrationUserDTO);
        return new UserDTO(user.getId(), user.getUsername());
    }

    @GetMapping(value = "/get_roles")
    public List<String> getRoles(@RequestParam(name = "username") String username) {
        log.info("Try to get roles of user: " + username);
        return userService.getUserByUsername(username).getRoles().stream().map(Role::getName).toList();
    }
}
