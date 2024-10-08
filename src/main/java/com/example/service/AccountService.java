package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.repository.AccountRepository;

import com.example.entity.Account;

import org.springframework.beans.factory.annotation.Autowired;

@Service
@Transactional
public class AccountService {

    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        if(accountRepository.findAccountByUsername(account.getUsername()) != null) {
            return new Account(409, "CONFLICT", "CONFLICT");
        }
        if((account.getUsername() != "") && (account.getPassword().length() >= 4)) {
            return accountRepository.save(account);
        }
        return null;
    }

    public Account getAccountByUsernameAndPassword (Account account) {
        return accountRepository.findAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
    }

    public boolean checkAccountExists(int accountId) {
        if(accountRepository.findById(accountId).isPresent()) {
            return true;
        }
        return false;
    }

}
