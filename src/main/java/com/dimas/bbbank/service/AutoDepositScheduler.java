package com.dimas.bbbank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AutoDepositScheduler {

    private final AccountService accountService;

//    @Scheduled(cron = "${auto-deposit.cron}")
    public void trigger() {
        log.info("Auto deposit is triggered");
        accountService.findAll().stream().parallel()//it is allowed to be paralel
                .forEach(acc -> accountService.autoPopup(acc.getId()));
        log.info("Auto deposit is completed");
    }

}
