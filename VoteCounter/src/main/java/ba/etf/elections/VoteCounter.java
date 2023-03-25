package ba.etf.elections;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VoteCounter {
    public static void main(String[] args) {
        try {
            // ask user to input path to JSON file with votes
            Scanner scanner = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Enter path to JSON file with votes:");
            String path = scanner.nextLine();  // Read user input
            CountVotes(path);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void CountVotes(String filename) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        File file = new File(filename); // open file that contains votes as JSON array of objects
        // read votes from file into a list of Vote objects
        ObjectMapper mapper = new ObjectMapper();
        List<Vote> votes = mapper.readValue(file, new TypeReference<>() {});
        // create hash map: name of candidate String and number of votes Integer
        Map<String, Integer> voteCountHashMap = new LinkedHashMap<>();

        for (Vote vote: votes) {
            Boolean macHashMatch = CryptographyHelper.validateMACHash(vote.getVotedCandidates().toString(), vote.getVoteMacHash());
            if (!macHashMatch){
                throw new RuntimeException("MAC hash does not match. Vote integrity compromised.");
            }
            for (String candidate: vote.getVotedCandidates()) {
                if (voteCountHashMap.containsKey(candidate)) {
                    voteCountHashMap.put(candidate, voteCountHashMap.get(candidate) + 1);
                } else {
                    voteCountHashMap.put(candidate, 1);
                }
            }
        }
        // sort map by value
//        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
//        voteCountHashMap.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByValue())
//                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        printMap(voteCountHashMap);
    }

    private static void printMap(Map<String, Integer> map) {
        System.out.println("Ukupni glasovi:\n");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue() + " glasova");
        }
    }
}