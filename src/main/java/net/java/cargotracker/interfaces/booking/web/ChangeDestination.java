package net.java.cargotracker.interfaces.booking.web;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import net.java.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import net.java.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import net.java.cargotracker.interfaces.booking.facade.dto.Location;
import net.java.cargotracker.interfaces.web.util.RequestParameter;

/**
 * Handles changing the cargo destination. Operates against a dedicated service
 * facade, and could easily be rewritten as a thick Swing client. Completely
 * separated from the domain layer, unlike the tracking user interface.
 * <p/>
 * In order to successfully keep the domain model shielded from user interface
 * considerations, this approach is generally preferred to the one taken in the
 * tracking controller. However, there is never any one perfect solution for all
 * situations, so we've chosen to demonstrate two polarized ways to build user
 * interfaces.
 *
 * @see net.java.cargotracker.interfaces.tracking.CargoTrackingController
 */
@Named
@ViewScoped
public class ChangeDestination implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    @RequestParameter
    private String trackingId;
    private CargoRoute cargo;
    private List<Location> locations;
    private String destinationUnlocode;
    @Inject
    private BookingServiceFacade bookingServiceFacade;

    public CargoRoute getCargo() {
        return cargo;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public String getDestinationUnlocode() {
        return destinationUnlocode;
    }

    public void setDestinationUnlocode(String destinationUnlocode) {
        this.destinationUnlocode = destinationUnlocode;
    }

    @PostConstruct
    public void load() {
        locations = bookingServiceFacade.listShippingLocations();        
        cargo = bookingServiceFacade.loadCargoForRouting(trackingId);
    }

    public String changeDestination() {
        bookingServiceFacade.changeDestination(trackingId, destinationUnlocode);

        return "show.xhtml?faces-redirect=true&trackingId=" + trackingId;
    }
}
