package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.Area;
import com.jifenke.lepluslive.merchant.domain.entities.City;
import com.jifenke.lepluslive.merchant.repository.AreaRepository;
import com.jifenke.lepluslive.merchant.repository.CityRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wcg on 16/3/17.
 */
@Service
@Transactional(readOnly = true)
public class CityService {

    @Inject
    private CityRepository cityRepository;

    @Inject
    private AreaRepository areaRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<City> findCitiesByPage(Pageable pageable) {
        return cityRepository.findAll(pageable).getContent();
    }

    public City findCityById(Long id) {

        City city = cityRepository.findOne(id);
        if (city == null) {
            throw new RuntimeException("不存在的城市");
        }

        return city;
    }

    public void createCity(City city) {
        if (city.getId() != null) {
            throw new RuntimeException("新建城市ID不为null");
        }
        cityRepository.save(city);
    }

    public void editCity(City city) {
        City cityOri = cityRepository.findOne(city.getId());
        if (cityOri == null) {
            throw new RuntimeException("不存在的商户");
        }
        cityOri.setName(city.getName());
        cityOri.setSid(city.getSid());

        cityRepository.save(cityOri);
    }

    public void deleteCity(Long id) {
        cityRepository.delete(id);
    }

    /**
     * 获取某个城市的所有地区列表
     * @param id 城市id
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Area> findAreaListByCity(Long id) {
        City city = new City();
        city.setId(id);
        return areaRepository.findAllByCity(city);
    }
}
