package org.helio.fullrestapispring.services;

import org.helio.fullrestapispring.dto.CreateUserDto;
import org.helio.fullrestapispring.dto.LoginUserDto;
import org.helio.fullrestapispring.dto.RecoveryJwtTokenDto;
import org.helio.fullrestapispring.entities.Role;
import org.helio.fullrestapispring.entities.User;
import org.helio.fullrestapispring.enums.RoleName;
import org.helio.fullrestapispring.repositories.UserRepository;
import org.helio.fullrestapispring.security.authentication.JwtTokenService;
import org.helio.fullrestapispring.security.config.SecurityConfiguration;
import org.helio.fullrestapispring.security.userdetails.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    // Método responsável por autenticar um usuário e retornar um token JWT
    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    // Método responsável por criar um usuário
    public void createUser(CreateUserDto createUserDto) {


        // Define a role padrão como "ROLE_CUSTOMER"
        Role rolePadrao = Role.builder().name(RoleName.valueOf("ROLE_CUSTOMER")).build();

        // Cria um novo usuário com os dados fornecidos
        User newUser = User.builder()
                .email(createUserDto.email())
                // Codifica a senha do usuário com o algoritmo bcrypt
                .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                // Atribui ao usuário uma permissão específica
                .roles(Collections.singletonList(rolePadrao))
                .build();

        // Salva o novo usuário no banco de dados
        userRepository.save(newUser);
    }
}
