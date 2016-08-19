package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.merchant.domain.entities.City;
import com.jifenke.lepluslive.merchant.service.CityService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wcg on 16/8/19.
 */
@RestController
@RequestMapping("/api")
public class CityController {

    @Inject
    private CityService cityService;

    @RequestMapping("/city/ajax")
    public
    @ResponseBody
    List<City> findAllCity() {
        return cityService.findAllCity();
    }


}
