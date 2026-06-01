package com.sds2.country.service;

import com.sds2.country.client.UserServiceClient;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RemoteUserDetailsService implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    public RemoteUserDetailsService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = userServiceClient.getUserPassword(username);
        String role = userServiceClient.getUserRole(username);

        if (password == null || role == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new User(
                username,
                password,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}