package com.neche.bankapp.service.impl;

import com.neche.bankapp.domain.entity.UserEntity;
import com.neche.bankapp.payload.request.CreditAndDebitRequest;
import com.neche.bankapp.payload.request.EmailDetails;
import com.neche.bankapp.payload.request.EnquiryRequest;
import com.neche.bankapp.payload.respond.AccountInfo;
import com.neche.bankapp.payload.respond.BankResponse;
import com.neche.bankapp.repository.UserRepository;
import com.neche.bankapp.service.EmailService;
import com.neche.bankapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import static com.neche.bankapp.utils.AccountUtils.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final EmailService emailService;


    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExists =
                userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .build();
        }
        UserEntity foundUserAccount =
                userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(ACCOUNT_EXISTS_CODE)
                .responseMessage(ACCOUNT_NUMBER_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUserAccount.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountName(foundUserAccount.getFirstName() +
                                " " + foundUserAccount.getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExists =
                userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if(!isAccountExists){
            return ACCOUNT_NUMBER_NON_EXISTS_MESSAGE;
        }

        UserEntity foundUserAccount =
                userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return foundUserAccount.getFirstName() +
                " " + foundUserAccount.getLastName() +
                " " + foundUserAccount.getOtherName();


    }

    @Override
    public BankResponse creditAccount(CreditAndDebitRequest creditAndDebitRequest) {
        boolean isAccountExists =
                userRepository.existsByAccountNumber(creditAndDebitRequest.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .build();
        }
        UserEntity userToCredit =
                userRepository.findByAccountNumber(creditAndDebitRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance()
                .add(creditAndDebitRequest.getAmount()));
        userToCredit.setFirstName(creditAndDebitRequest.getFirstName());
        userToCredit.setLastName(creditAndDebitRequest.getLastName());
        userRepository.save(userToCredit);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(userToCredit.getEmail())
                .messageBody("Your account has been credited with " +
                        creditAndDebitRequest.getAmount() + " from " +
                                userToCredit.getFirstName() + " your current account balance is " +
                        userToCredit.getAccountBalance()
                        )
                .build();

        emailService.sendEmailAlert(creditAlert);

        return BankResponse.builder()
                .responseCode(ACCOUNT_NUMBER_FOUND_CODE)
                .responseMessage(ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(userToCredit.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditAndDebitRequest request) {
        boolean isAccountExists =
                userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .build();
        }

        UserEntity userToDebit =
                userRepository.findByAccountNumber(request.getAccountNumber());

        // check for insufficient balance

        BigInteger availableBalance =
                userToDebit.getAccountBalance().toBigInteger();

        BigInteger debitAmount = request.getAmount().toBigInteger();

        if(availableBalance.intValue() < debitAmount.intValue()){
            return BankResponse.builder()
                    .responseCode("006")
                    .responseMessage("Insufficient Balance")
                    .accountInfo(null)
                    .build();
        }else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance()
                    .subtract(request.getAmount()));
            userRepository.save(userToDebit);
            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("DEBIT ALERT")
                    .recipient(userToDebit.getEmail())
                    .messageBody("The sum of " + request.getAmount() +
                            " has been deducted from your account! Your current" +
                            "account balance is " + userToDebit.getAccountBalance())
                    .build();
            emailService.sendEmailAlert(debitAlert);

        }

        return BankResponse.builder()
                .responseCode("007")
                .responseMessage("Account debited successfully")
                .accountInfo(AccountInfo.builder()
                        .accountName(userToDebit.getFirstName())
                        .accountBalance(userToDebit.getAccountBalance())
                        .accountNumber(userToDebit.getAccountNumber())
                        .build())
                .build();
    }
}
