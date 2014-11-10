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

import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * Implementation of {@link CacheContainerProvider} creating a programmatically configured DefaultCacheManager.
 * Libraries of Infinispan need to be bundled with the application - this is called "library" mode.
 *
 * @author Martin Gencur
 * @author Jiri Holusa
 */
@ApplicationScoped
public class CacheContainerProvider {

    private Logger log = Logger.getLogger(this.getClass().getName());

    private EmbeddedCacheManager manager;

    @Produces
    public EmbeddedCacheManager getCacheContainer() {
        if (manager == null) {

            GlobalConfiguration glob = new GlobalConfigurationBuilder()
                    .globalJmxStatistics().allowDuplicateDomains(true)
                    .build();

            Configuration loc = new ConfigurationBuilder()
                    //TODO: ****** alter the configuration here ******
                    .build();

            manager = new DefaultCacheManager(glob, loc); //true means start the cache manager immediately
            log.info("=== Using DefaultCacheManager (library mode) ===");
        }
        return manager;
    }

    @PreDestroy
    public void cleanUp() {
        manager.stop();
        manager = null;
    }
}
