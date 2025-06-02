package org.eclipse.cargotracker.interfaces.tracking.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.cargotracker.application.util.DateConverter;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.Delivery;
import org.eclipse.cargotracker.domain.model.cargo.HandlingActivity;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.interfaces.Coordinates;
import org.eclipse.cargotracker.interfaces.CoordinatesFactory;
import org.eclipse.cargotracker.interfaces.util.MessageUtils;

/** View adapter for displaying a cargo in a tracking context. */
public class CargoTrackingViewAdapter {

  private final Cargo cargo;
  private final List<HandlingEventViewAdapter> events;

  public CargoTrackingViewAdapter(Cargo cargo, List<HandlingEvent> handlingEvents) {
    this.cargo = cargo;
    this.events = new ArrayList<>(handlingEvents.size());

    handlingEvents.stream().map(HandlingEventViewAdapter::new).forEach(events::add);
  }

  public String getTrackingId() {
    return cargo.getTrackingId().getIdString();
  }

  public String getOriginName() {
    return cargo.getRouteSpecification().getOrigin().getName();
  }

  public String getOriginCode() {
    return cargo.getRouteSpecification().getOrigin().getUnLocode().getIdString();
  }

  public Coordinates getOriginCoordinates() {
    return CoordinatesFactory.find(cargo.getRouteSpecification().getOrigin());
  }

  public String getDestinationName() {
    return cargo.getRouteSpecification().getDestination().getName();
  }

  public String getDestinationCode() {
    return cargo.getRouteSpecification().getDestination().getUnLocode().getIdString();
  }

  public Coordinates getDestinationCoordinates() {
    return CoordinatesFactory.find(cargo.getRouteSpecification().getDestination());
  }

  public String getLastKnownLocationName() {
    return cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString().equals("XXXXX")
        ? MessageUtils.getMessage("unknown")
        : cargo.getDelivery().getLastKnownLocation().getName();
  }

  public String getLastKnownLocationCode() {
    return cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString();
  }

  public Coordinates getLastKnownLocationCoordinates() {
    return CoordinatesFactory.find(cargo.getDelivery().getLastKnownLocation());
  }

  public String getStatusCode() {
    if (cargo.getItinerary().getLegs().isEmpty()) {
      return "NOT_ROUTED";
    }

    if (cargo.getDelivery().isUnloadedAtDestination()) {
      return "AT_DESTINATION";
    }

    if (cargo.getDelivery().isMisdirected()) {
      return "MISDIRECTED";
    }

    return cargo.getDelivery().getTransportStatus().name();
  }

  /** @return A readable string describing the cargo status. */
  public String getStatusText() {
    Delivery delivery = cargo.getDelivery();

    switch (delivery.getTransportStatus()) {
      case IN_PORT:
        return MessageUtils.getMessage("cargo.status.IN_PORT", 
            cargo.getRouteSpecification().getDestination().getName());
      case ONBOARD_CARRIER:
        return MessageUtils.getMessage("cargo.status.ONBOARD_CARRIER", 
            delivery.getCurrentVoyage().getVoyageNumber().getIdString());
      case CLAIMED:
        return MessageUtils.getMessage("cargo.status.CLAIMED");
      case NOT_RECEIVED:
        return MessageUtils.getMessage("cargo.status.NOT_RECEIVED");
      case UNKNOWN:
        return MessageUtils.getMessage("cargo.status.UNKNOWN");
      default:
        return MessageUtils.getMessage("cargo.status.UNKNOWN"); // Should never happen.
    }
  }

  public boolean isMisdirected() {
    return cargo.getDelivery().isMisdirected();
  }

  public String getEta() {
    LocalDateTime eta = cargo.getDelivery().getEstimatedTimeOfArrival();

    if (eta == null) {
      return "?";
    } else {
      return DateConverter.toString(eta);
    }
  }

  public String getNextExpectedActivity() {
    HandlingActivity activity = cargo.getDelivery().getNextExpectedActivity();

    if ((activity == null) || (activity.isEmpty())) {
      return "";
    }

    String baseKey = "activity.next.";
    HandlingEvent.Type type = activity.getType();

    if (type.sameValueAs(HandlingEvent.Type.LOAD)) {
      return MessageUtils.getMessage(baseKey + "load",
          MessageUtils.getMessage("activity.load"),
          activity.getVoyage().getVoyageNumber(),
          activity.getLocation().getName());
    } else if (type.sameValueAs(HandlingEvent.Type.UNLOAD)) {
      return MessageUtils.getMessage(baseKey + "unload",
          MessageUtils.getMessage("activity.unload"),
          activity.getVoyage().getVoyageNumber(),
          activity.getLocation().getName());
    } else {
      return MessageUtils.getMessage(baseKey + "other",
          MessageUtils.getMessage("activity." + type.name().toLowerCase()),
          activity.getLocation().getName());
    }
  }

  /** @return An unmodifiable list of handling event view adapters. */
  public List<HandlingEventViewAdapter> getEvents() {
    return Collections.unmodifiableList(events);
  }

  /** Handling event view adapter component. */
  public class HandlingEventViewAdapter {

    private final HandlingEvent handlingEvent;

    public HandlingEventViewAdapter(HandlingEvent handlingEvent) {
      this.handlingEvent = handlingEvent;
    }

    public String getTime() {
      return DateConverter.toString(handlingEvent.getCompletionTime());
    }

    public boolean isExpected() {
      return cargo.getItinerary().isExpected(handlingEvent);
    }

    public String getDescription() {
      switch (handlingEvent.getType()) {
        case LOAD:
          return MessageUtils.getMessage("event.loaded",
              handlingEvent.getVoyage().getVoyageNumber().getIdString(),
              handlingEvent.getLocation().getName());
        case UNLOAD:
          return MessageUtils.getMessage("event.unloaded",
              handlingEvent.getVoyage().getVoyageNumber().getIdString(),
              handlingEvent.getLocation().getName());
        case RECEIVE:
          return MessageUtils.getMessage("event.received",
              handlingEvent.getLocation().getName());
        case CLAIM:
          return MessageUtils.getMessage("event.claimed",
              handlingEvent.getLocation().getName());
        case CUSTOMS:
          return MessageUtils.getMessage("event.customs",
              handlingEvent.getLocation().getName());
        default:
          return MessageUtils.getMessage("unknown");
      }
    }
  }
}
