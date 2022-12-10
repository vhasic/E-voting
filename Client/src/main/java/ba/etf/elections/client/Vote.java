package ba.etf.elections.client;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Vote {
//    private String votedCandidate;
    private List<String> votedCandidates;

    public Vote() {
        votedCandidates = new ArrayList<>();
    }
}

