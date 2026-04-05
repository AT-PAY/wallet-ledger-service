package com.atpay.wallet_ledger_service.converter;

import com.atpay.wallet_ledger_service.DTO.wallet.WalletAccountRequest;
import com.atpay.wallet_ledger_service.DTO.wallet.WalletAccountResponse;
import com.atpay.wallet_ledger_service.entity.WalletAccount;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletAccountConverter {

    WalletAccount toEntity(WalletAccountRequest request);

    WalletAccountResponse toResponse(WalletAccount walletAccount);
}
