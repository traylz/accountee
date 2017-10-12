package com.gsobko.act;

import com.gsobko.act.model.Transfer;

import java.math.BigDecimal;
import java.util.Collection;

public interface TransferManager {

    Transfer doTransfer(Long accountFrom, Long accountTo,
                        BigDecimal amount, String transferId) throws ActUserException;

    Collection<Transfer> allTransfers();
}
