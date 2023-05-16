package com.dimas.bbbank.service;

import com.dimas.bbbank.domain.entity.Account;
import com.dimas.bbbank.exception.InsufficientFundsException;
import com.dimas.bbbank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final Map<Long, BigDecimal> initBalances = new ConcurrentHashMap<>();

    @Transactional
    public boolean doTransfer(Long idFrom, Long idTo, BigDecimal amount) {
        var accountFrom = accountRepository.lockById(idFrom).orElseThrow();
        var accountTo = accountRepository.lockById(idTo).orElseThrow();
        var balanceFrom = accountFrom.getBalance();
        var balanceTo = accountTo.getBalance();
        log.info("Before: From [id={}, balance={}]-> To [id={}, balance={}]", accountFrom.getId(), balanceFrom, accountTo.getId(), balanceTo);
        if (balanceFrom.compareTo(amount) < 0) {
            throw new InsufficientFundsException(String.format("Account %d has %s funds, but requested to withdraw %s funds", idFrom, balanceFrom, amount));
        }
        var newBalanceFrom = balanceFrom.subtract(amount);
        var newBalanceTo = balanceFrom.add(amount);
        accountFrom.setBalance(newBalanceFrom);
        accountTo.setBalance(newBalanceTo);
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
        log.info("Success transfer of {} funds: From [id={}, balance={}]-> To [id={}, balance={}]", amount, accountFrom.getId(), newBalanceFrom, accountTo.getId(), newBalanceTo);
        return true;
    }

    @Transactional
    public void autoPopup(Long id) {
        var account = accountRepository.findById(id).orElseThrow();//optimistic locking via version
        var balanceInit = account.getBalance();
        initBalances.computeIfAbsent(account.getId(), (key) -> percentage(balanceInit, new BigDecimal(207)));
        var tenPercents = percentage(balanceInit, new BigDecimal(10));
        var newBalance = balanceInit.add(tenPercents);
        log.info("Balance deposit request: Account [id={}, balance={}] -> requested={}, max={}", account.getId(), balanceInit, tenPercents, initBalances.get(account.getId()));
        if (newBalance.compareTo(initBalances.get(account.getId())) > 0) {
            log.warn("Account={} has reached its max limit={}. Ignoring.", account.getId(), initBalances.get(account.getId()));
        } else {
            account.setBalance(newBalance);
        }
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Account not found for id=%s", id)));
    }

    private BigDecimal percentage(BigDecimal base, BigDecimal pct) {
        return base.multiply(pct).divide(new BigDecimal(100), RoundingMode.HALF_UP);
    }

}
