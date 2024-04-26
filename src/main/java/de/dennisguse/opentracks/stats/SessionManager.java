package de.dennisguse.opentracks.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.dennisguse.opentracks.data.models.Run;

public class SessionManager {

    private final String sessionId;
    private final List<Run> runs;

    public SessionManager() {
        this.sessionId = UUID.randomUUID().toString(); // Generate unique session ID
        this.runs = new ArrayList<>();
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<Run> getRuns() {
        return runs;
    }
}
