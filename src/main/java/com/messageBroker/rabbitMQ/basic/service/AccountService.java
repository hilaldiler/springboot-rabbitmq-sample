package com.messageBroker.rabbitMQ.basic.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.messageBroker.rabbitMQ.basic.dto.AccountCreateDTO;
import com.messageBroker.rabbitMQ.basic.dto.AccountDTO;
import com.messageBroker.rabbitMQ.basic.dto.MoneyTransferRequest;
import com.messageBroker.rabbitMQ.basic.dto.UpdateAccountRequest;
import com.messageBroker.rabbitMQ.basic.model.Account;
import com.messageBroker.rabbitMQ.basic.model.Customer;
import com.messageBroker.rabbitMQ.basic.producer.RabbitMQJsonProducer;
import com.messageBroker.rabbitMQ.basic.producer.RabbitMQProducer;
import com.messageBroker.rabbitMQ.basic.repository.AccountRepository;

@Service
public class AccountService {

	private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final AccountCreateDTO accountCreateDTO;
        
    private final RabbitMQProducer rabbitMQProducer;
    private final RabbitMQJsonProducer rabbitMQJsonProducer;
    

	public AccountService(AccountRepository accountRepository, CustomerService customerService,
			AccountCreateDTO accountCreateDTO, RabbitMQProducer rabbitMQProducer, RabbitMQJsonProducer rabbitMQJsonProducer) {
		this.accountRepository = accountRepository;
		this.customerService = customerService;
		this.accountCreateDTO = accountCreateDTO;
		this.rabbitMQJsonProducer = rabbitMQJsonProducer;
		this.rabbitMQProducer = rabbitMQProducer;
	}
    
	public AccountDTO getAccountById(String id) {
        return accountRepository.findById(id)
                .map(accountCreateDTO::create)
                .orElse(new AccountDTO());
    }
	
    public AccountDTO updateAccount(String id, UpdateAccountRequest request) {
        Customer customer = customerService.getCustomerById(request.getCustomerId());
        if (customer.getId().equals("") ||customer.getId() == null) {
            return AccountDTO.builder().build();
        }

        Optional<Account> accountOptional = accountRepository.findById(id);
        accountOptional.ifPresent(account -> {
            account.setBalance(request.getBalance());
            account.setCurrency(request.getCurrency());
            account.setCustomerId(request.getCustomerId());
            accountRepository.save(account);
        });

        return accountOptional.map(accountCreateDTO::create).orElse(new AccountDTO());
    }
	
	public AccountDTO withdrawMoney(String id, Double amount) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        accountOptional.ifPresent(account -> {
            if (account.getBalance() > amount) {
                account.setBalance(account.getBalance() - amount);
                accountRepository.save(account);
            } else {
                System.out.println("Insufficient funds -> accountId: " + id + " balance: " + account.getBalance() + " amount: " + amount);
            }
        });

        return accountOptional.map(accountCreateDTO::create).orElse(new AccountDTO());
    }

    public void transferMoney(MoneyTransferRequest transferRequest) {
        Optional<Account> account = accountRepository.findById(transferRequest.getFromId());       
        Optional<Account> receivedAccount = accountRepository.findById(transferRequest.getToId());
        
        account.ifPresentOrElse(acct -> {
            if (acct.getBalance() > transferRequest.getAmount()) {
            	acct.setBalance(acct.getBalance() - transferRequest.getAmount());
                accountRepository.save(acct);
                rabbitMQProducer.sendMessage("Money transfered");
                rabbitMQJsonProducer.sendJsonMessage(transferRequest);
            } else {
                System.out.println("Insufficient funds -> accountId: " + transferRequest.getFromId() + " balance: " + acct.getBalance() + " amount: " + transferRequest.getAmount());
            }},
            () -> System.out.println("Account not found")
        );
        
		receivedAccount.ifPresentOrElse(acct -> {
			acct.setBalance(acct.getBalance() + transferRequest.getAmount());
			accountRepository.save(acct);
			rabbitMQProducer.sendMessage("Money transfered");
			rabbitMQJsonProducer.sendJsonMessage(transferRequest);

		}, () -> System.out.println("Account not found"));

	}

    public void updateReceiverAccount(MoneyTransferRequest transferRequest) {
        Optional<Account> accountOptional = accountRepository.findById(transferRequest.getToId());
        accountOptional.ifPresentOrElse(account -> {
                        account.setBalance(account.getBalance() + transferRequest.getAmount());
                        accountRepository.save(account);
                        rabbitMQProducer.sendMessage("Updated Account Balance");
                        rabbitMQJsonProducer.sendJsonMessage(account);
                        },
                () -> {
                    System.out.println("Receiver Account not found");
                    Optional<Account> senderAccount = accountRepository.findById(transferRequest.getFromId());
                    senderAccount.ifPresent(sender -> {
                        sender.setBalance(sender.getBalance() + transferRequest.getAmount());
                        accountRepository.save(sender);
                        rabbitMQProducer.sendMessage("Money charge back to sender");
                        rabbitMQJsonProducer.sendJsonMessage(sender);
                    });

                }
        );
    }
}
