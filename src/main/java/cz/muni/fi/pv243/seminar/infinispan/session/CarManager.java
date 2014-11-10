/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cz.muni.fi.pv243.seminar.infinispan.session;

import cz.muni.fi.pv243.seminar.infinispan.model.Car;
import org.infinispan.Cache;
import org.infinispan.CacheCollection;
import org.infinispan.manager.EmbeddedCacheManager;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.infinispan.query.Search;
import org.infinispan.query.dsl.*;

/**
 * Adds, retrieves, removes new cars from the cache. Also returns a list of cars stored in the cache.
 *
 * @author Martin Gencur
 * @author Jiri Holusa
 */
@Model
public class CarManager {

    public static final String CACHE_NAME = "carcache";

    @Inject
    private EmbeddedCacheManager cacheManager;

    private Cache<String, Car> carCache;

    private List<Car> searchResults;
    private Car car = new Car();

    /**
     * Runs only once when the beans is created. Retrieves the cache and saves it into an attribute for further usage.
     */
    @PostConstruct
    public void init() {
        carCache = cacheManager.getCache(CACHE_NAME);
    }

    public String search() {
        QueryFactory qf = Search.getQueryFactory(carCache);

        //TODO: ****** Create a search query to be able to search by number plates (and also substrings of them) ******
        Query query = null;

        //TODO: ****** Invoke the query and save the results ******
        searchResults = null;

        return "searchresults";
    }

    public String clearSearchResults() {
        searchResults = Collections.emptyList();

        return "home";
    }

    public String addNewCar() {
        carCache.put(car.getNumberPlate(), car);

        return "home";
    }

    public String showCarDetails(String numberPlate) {
        car = carCache.get(numberPlate);

        return "showdetails";
    }

    public List<String> getCarList() {
        CacheCollection<Car> cars = carCache.values();
        List<String> carsPlates = new ArrayList<>();
        for (Car car: cars) {
            carsPlates.add(car.getNumberPlate());
        }

        /* Just FYI, the whole code in this method above can be replaced by line below using Java 8 Stream API. Looks nicer, huh? */
        //List<String> carsPlates = carCache.values().stream().map(Car::getNumberPlate).collect(Collectors.toList());

        return carsPlates;
    }

    public void removeCar(String numberPlate) {
        carCache.remove(numberPlate);
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    public List<Car> getSearchResults() {
        return searchResults;
    }
}
