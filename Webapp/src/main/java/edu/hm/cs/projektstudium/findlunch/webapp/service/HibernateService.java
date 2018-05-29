package edu.hm.cs.projektstudium.findlunch.webapp.service;

public interface HibernateService {

    <T> T initializeAndUnproxy(T entity);
}
