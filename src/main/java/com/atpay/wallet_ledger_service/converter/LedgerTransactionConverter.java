package com.atpay.wallet_ledger_service.converter;

import com.atpay.wallet_ledger_service.DTO.transaction.TransactionResponse;
import com.atpay.wallet_ledger_service.DTO.wallet.WalletAccountRequest;
import com.atpay.wallet_ledger_service.DTO.wallet.WalletAccountResponse;
import com.atpay.wallet_ledger_service.entity.LedgerTransaction;
import com.atpay.wallet_ledger_service.entity.WalletAccount;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LedgerTransactionConverter {

    LedgerTransactionConverter INSTANCE = Mappers.getMapper(LedgerTransactionConverter.class);

    TransactionResponse toResponse(LedgerTransaction transaction);
}
