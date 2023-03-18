package ba.etf.elections;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Vote {
    private List<String> votedCandidates;
    private String voteMacHash;

    public Vote() {
        votedCandidates = new ArrayList<>();
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

}

