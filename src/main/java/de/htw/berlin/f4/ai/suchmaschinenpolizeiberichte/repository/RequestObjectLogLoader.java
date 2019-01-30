package de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.repository;

import de.htw.berlin.f4.ai.suchmaschinenpolizeiberichte.model.policeReport.RequestObjectLog;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RequestObjectLogLoader {
    private List<RequestObjectLog> requestObjectLogList = new ArrayList<>();


    public Optional<RequestObjectLog> findById(String searchId) {
        return requestObjectLogList
                .stream()
                .filter(requestObjectLog -> requestObjectLog.getId().equals(searchId))
                .findFirst();
    }

    public void add(RequestObjectLog requestObjectLog) {
        requestObjectLogList.add(requestObjectLog);
    }
}
