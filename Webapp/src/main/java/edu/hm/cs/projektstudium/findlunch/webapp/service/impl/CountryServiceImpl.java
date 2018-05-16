package edu.hm.cs.projektstudium.findlunch.webapp.service.impl;

import edu.hm.cs.projektstudium.findlunch.webapp.model.Country;
import edu.hm.cs.projektstudium.findlunch.webapp.service.CountryService;
import edu.hm.cs.projektstudium.findlunch.webapp.service.DbReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Alexander Carl on 07.07.2017.
 */
@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private DbReaderService dbReaderService;

    @Override
    public List<Country> getAllCountries() {
        return dbReaderService.getAllCountries();
    }

}
