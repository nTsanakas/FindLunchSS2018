package edu.hm.cs.projektstudium.findlunch.webapp.service;

public interface EmailService {

    void generatePasswordRequestMail(String userEmail);

    boolean sendMail();

}
