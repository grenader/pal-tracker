package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> entries = new HashMap();
    long maxId = 0;

    @Override
    public synchronized TimeEntry create(TimeEntry timeEntry) {
        final long key = ++maxId;
        entries.put(key, timeEntry);
        timeEntry.setId(key);
        return timeEntry;
    }

    @Override
    public TimeEntry find(long id) {
        return entries.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<TimeEntry>(entries.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (find(id) != null) {
            entries.put(id, timeEntry);
            timeEntry.setId(id);
            return timeEntry;
        }
        return null;
    }

    @Override
    public void delete(long id) {
        entries.remove(id);
    }
}
