/*
 * Copyright (c) 2023. Vahidin Hasić
 */

package ba.etf.elections;

import java.util.ArrayList;
import java.util.List;

public class Vote {
    private List<String> votedCandidates;
    private String voteMacHash;

    public Vote() {
        votedCandidates = new ArrayList<>();
    }

    public Vote(List<String> votedCandidates, String voteMacHash) {
        this.votedCandidates = votedCandidates;
        this.voteMacHash = voteMacHash;
    }

    public static Vote createInvalidVote() {
        Vote invalidVote = new Vote();
        invalidVote.getVotedCandidates().add("Nevažeći");
        return invalidVote;
    }

    public void calculateVoteMacHash() {
        String HmacSHA256 = null;
        try {
            HmacSHA256 = CryptographyHelper.createMACHash(votedCandidates.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        voteMacHash = HmacSHA256;
    }

    public List<String> getVotedCandidates() {
        return votedCandidates;
    }

    public void setVotedCandidates(List<String> votedCandidates) {
        this.votedCandidates = votedCandidates;
    }

    public String getVoteMacHash() {
        return voteMacHash;
    }

    public void setVoteMacHash(String voteMacHash) {
        this.voteMacHash = voteMacHash;
    }
}
