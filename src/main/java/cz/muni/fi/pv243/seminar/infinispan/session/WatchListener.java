package cz.muni.fi.pv243.seminar.infinispan.session;

import cz.muni.fi.pv243.seminar.infinispan.model.Car;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;

/**
 * Listener that prints informational message about the entry being added/modified/created.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
@Listener
public class WatchListener {

    @CacheEntryCreated
    public void entryCreated(CacheEntryCreatedEvent event) {
        if (!event.isPre()) {
            Car car = (Car) event.getValue();
            System.out.println("Hey man, got a new car! " + car);
        }
    }

    @CacheEntryRemoved
    public void entryRemoved(CacheEntryRemovedEvent event) {
        if (event.isPre()) {
            Car car = (Car) event.getValue();
            System.out.println("Oh no, you ruined the car! " + car);
        }
    }
}
