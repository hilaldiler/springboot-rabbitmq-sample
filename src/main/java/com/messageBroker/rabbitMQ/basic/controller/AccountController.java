package com.messageBroker.rabbitMQ.basic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.messageBroker.rabbitMQ.basic.dto.AccountDTO;
import com.messageBroker.rabbitMQ.basic.dto.MoneyTransferRequest;
import com.messageBroker.rabbitMQ.basic.dto.UpdateAccountRequest;
import com.messageBroker.rabbitMQ.basic.service.AccountService;


@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable String id, @RequestBody UpdateAccountRequest updateAccountRequest) {
        return ResponseEntity.ok(accountService.updateAccount(id, updateAccountRequest));
    }

    @PutMapping("/withdraw/{id}/{amount}")
    public ResponseEntity<AccountDTO> withdrawMoney(@PathVariable String id, @PathVariable Double amount) {
        return ResponseEntity.ok(accountService.withdrawMoney(id, amount));
    }

    @PutMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody MoneyTransferRequest transferRequest) {
        accountService.transferMoney(transferRequest);
        return ResponseEntity.ok("İşleminiz başarıyla alınmıştır!");
    }


}
