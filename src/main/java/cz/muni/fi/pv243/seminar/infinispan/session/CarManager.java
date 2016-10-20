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
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import java.util.ArrayList;
import java.util.List;

/**
 * Adds, retrieves, removes new cars from the cache. Also returns a list of cars stored in the cache.
 *
 * @author Martin Gencur
 * @author Jiri Holusa
 */
@Model
public class CarManager {

    public static final String CACHE_NAME = "carcache";

    private RemoteCache<String, Car> carCache;

    private Car car = new Car();

    /**
     * Runs only once when the beans is created. Retrieves the cache and saves it into an attribute for further usage.
     */
    @PostConstruct
    public void init() {
        // create configuration for RemoteCache that will connect to our local Infinispan server
        Configuration configuration = new ConfigurationBuilder()
                .addServer()
                    .host("127.0.0.1")
                    .port(11322)
                .build();

        // create cache manager and retrieve desired cache from it
        // the cache must be configured in the server's standalone.xml !!!
        carCache = new RemoteCacheManager(configuration).getCache(CACHE_NAME);
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
        return new ArrayList<>(carCache.keySet());
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
}
