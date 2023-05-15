package io.playdata.security.service;

import io.playdata.security.exception.UniqueUsernameException;
import io.playdata.security.exception.UsernameLengthException;
import io.playdata.security.model.Account;
import io.playdata.security.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private AccountRepository accountRepository;

//    @Bean
//    public BCryptPasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder(); // 해싱
//    }


    public Account findUserByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void join(Account account) throws Exception {
        // 중복
        if(account.getUsername().length() < 5) {
//            throw new Exception("5글자보다 긴 Username이어야 합니다.");
            throw new UsernameLengthException(5);
            // /join
        }
        if(accountRepository.existsByUsername(account.getUsername())) {
            throw new UniqueUsernameException("중복된 Username 입니다.");
            // /login
        }
        // Bcrypt 인코딩
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
    }
}
