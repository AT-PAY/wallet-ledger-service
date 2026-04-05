package com.atpay.wallet_ledger_service.converter;

import com.atpay.wallet_ledger_service.DTO.transaction.TransactionResponse;
import com.atpay.wallet_ledger_service.entity.LedgerTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LedgerTransactionConverter {

    @Mapping(source = "walletAccount.walletAccountId", target = "walletAccountId")
    TransactionResponse toResponse(LedgerTransaction transaction);

    @Mapping(source = "walletAccount.walletAccountId", target = "walletAccountId")
    List<TransactionResponse> toResponseList(List<LedgerTransaction> transactions);
}
