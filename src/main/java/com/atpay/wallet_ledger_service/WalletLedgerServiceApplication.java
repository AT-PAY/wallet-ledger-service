package com.atpay.wallet_ledger_service;

import dev.buianhai1205.ATLogger.ATLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalletLedgerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletLedgerServiceApplication.class, args);
        ATLogger.info("Wallet Ledger Service started successfully.")
                .param("event", "application_start")
                .log();
    }
}
