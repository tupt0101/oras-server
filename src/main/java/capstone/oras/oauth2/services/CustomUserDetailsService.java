/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capstone.oras.oauth2.services;

import capstone.oras.dao.IAccountRepository;
import capstone.oras.entity.AccountEntity;
import capstone.oras.oauth2.controller.TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author developer
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private IAccountRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity accountEntity = repo.findAccountEntitiesByEmailEquals(username).get();
        boolean active = false;

        if ( accountEntity.getActive() == true && accountEntity.getConfirmMail() == true ) {
            active = true;
        }
        boolean finalActive = active;
        return repo
                .findAccountEntitiesByEmailEquals(username)
                .map(u -> new org.springframework.security.core.userdetails.User(
                        u.getEmail(),
                        u.getPassword(),
                        true,
                        true,
                        true,
                        true,
                        AuthorityUtils.createAuthorityList(new String[]{})))
                .orElseThrow(() -> new UsernameNotFoundException("No user with "
                        + "the name " + username + "was found in the database"));
    }

    public String getOpenJobToken() {
        final String uri = "https://openjob-server.herokuapp.com/login?username=admin@gmail.com&password=password";
        //TODO: Autowire the RestTemplate in all the examples
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, TokenDto.class).getToken();
        return result;
    }
}
