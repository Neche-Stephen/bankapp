package com.neche.bankapp.service;

import com.neche.bankapp.payload.request.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);
}
