package com.neche.bankapp.service;

import com.neche.bankapp.payload.request.CreditAndDebitRequest;
import com.neche.bankapp.payload.request.EnquiryRequest;
import com.neche.bankapp.payload.respond.BankResponse;

public interface UserService {
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditAndDebitRequest creditAndDebitRequest);
    BankResponse debitAccount(CreditAndDebitRequest creditAndDebitRequest);
}
