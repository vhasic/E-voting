/*
 * Copyright (c) 2023. Vahidin Hasić
 */

package ba.etf.elections.core;

import java.util.ArrayList;
import java.util.List;


public class PartyCandidates {
    private String partyName;
    private List<String> partyCandidates = new ArrayList<>();

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public List<String> getPartyCandidates() {
        return partyCandidates;
    }

    public void setPartyCandidates(List<String> partyCandidates) {
        this.partyCandidates = partyCandidates;
    }
}
