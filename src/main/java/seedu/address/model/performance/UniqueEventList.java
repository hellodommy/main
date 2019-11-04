package seedu.address.model.performance;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.date.AthletickDate;
import seedu.address.model.performance.exceptions.DuplicateEventException;
import seedu.address.model.person.Person;

/**
 * A list of events that enforces uniqueness between its elements and does not allow nulls.
 * An event is considered unique by comparing using {@code Event#isSameEvent(Event)}.
 *
 * Supports a minimal set of list operations.
 *
 * @see Event#isSameEvent(Event)
 */
public class UniqueEventList implements Iterable<Event> {

    private final ObservableList<Event> internalList = FXCollections.observableArrayList();
    private final ObservableList<Event> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent event as the given argument.
     */
    public boolean contains(Event toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameEvent);
    }

    /**
     * Adds an event to the list.
     * The event must not already exist in the list.
     */
    public void add(Event toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateEventException();
        }
        internalList.add(toAdd);
    }

    public Event getEvent(String eventName) {
        for (Event event : internalList) {
            if (event.getName().equals(eventName)) {
                return event;
            }
        }
        return null;
    }

    public ArrayList<String> getAthleteEvent(Person person) {
        ArrayList<String> athleteEventList = new ArrayList<>();
        for(Event event : internalList) {
            if (event.getRecords().containsKey(person)) {
                athleteEventList.add(event.getName());
            }
        }
        return athleteEventList;
    }

    /**
     * Method for Shawn to retrieve performance records on a particular day for the Calendar feature.
     */
    public HashMap<Event, List<CalendarCompatibleRecord>> getCalendarCompatiblePerformance(AthletickDate date) {
        HashMap<Event, List<CalendarCompatibleRecord>> hm = new HashMap<>();
        for (Event event : internalList) {
            hm.put(event, event.getCalendarCompatibleRecords(date));
        }
        return hm;
    }

    /**
     * Checks if there are any recorded performances on the specified date.
     */
    public boolean hasPerformanceOn(AthletickDate date) {
        for (Event event : internalList) {
            if (event.hasPerformanceOn(date)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Replaces the contents of this list with {@code events}.
     * {@code events} must not contain duplicate events.
     */
    public void setEvents(List<Event> events) {
        requireAllNonNull(events);
        if (!eventsAreUnique(events)) {
            throw new DuplicateEventException();
        }

        internalList.setAll(events);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Event> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Event> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueEventList // instanceof handles nulls
                && internalList.equals(((UniqueEventList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code events} contains only unique events.
     */
    private boolean eventsAreUnique(List<Event> events) {
        for (int i = 0; i < events.size() - 1; i++) {
            for (int j = i + 1; j < events.size(); j++) {
                if (events.get(i).isSameEvent(events.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

}
