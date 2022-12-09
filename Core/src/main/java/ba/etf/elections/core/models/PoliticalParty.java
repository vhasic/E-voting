package ba.etf.elections.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PoliticalParty {
    private int id;
    private int number;
    private String partyName;
}
