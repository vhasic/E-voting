/*
 * Copyright (c) 2023. Vahidin Hasić
 */

package ba.etf.elections;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
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
            System.out.println("Unesite putanju do .json datoteke u kojoj se nalaze glasovi:");
            String path = scanner.nextLine();  // Read user input

            System.out.println("Unesite putanju i naziv .txt datoteke gdje želite da se spase prebrojani glasovi:");
            String filename = scanner.nextLine();

            Map<String, Integer> voteCountHashMap = CountVotes(path);
            printMap(voteCountHashMap);
            saveMapToTxtFile(filename, voteCountHashMap);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void saveMapToTxtFile(String filename, Map<String, Integer> voteCountHashMap) throws IOException {
        File file = new File(filename);
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        for (Map.Entry<String, Integer> entry : voteCountHashMap.entrySet()) {
            fileWriter.write(entry.getKey() + " => " + entry.getValue() + " glasova" + "\r\n");
        }
        fileWriter.close();
    }

    private static Map<String, Integer> CountVotes(String filename) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        File file = new File(filename); // open file that contains votes as JSON array of objects
        // read votes from file into a list of Vote objects
        ObjectMapper mapper = new ObjectMapper();
        List<Vote> votes = mapper.readValue(file, new TypeReference<>() {
        });
        // create hash map: name of candidate String and number of votes Integer
        Map<String, Integer> voteCountHashMap = new LinkedHashMap<>();

        for (Vote vote : votes) {
            Boolean macHashMatch = CryptographyHelper.validateMACHash(vote.getVotedCandidates().toString(), vote.getVoteMacHash());
            if (!macHashMatch) {
                throw new RuntimeException("MAC hash se ne podudara. Integritet glasova je kompromitovan!");
            }
            for (String candidate : vote.getVotedCandidates()) {
                if (voteCountHashMap.containsKey(candidate)) {
                    voteCountHashMap.put(candidate, voteCountHashMap.get(candidate) + 1);
                } else {
                    voteCountHashMap.put(candidate, 1);
                }
            }
        }
        // sort map by value in descending order
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        voteCountHashMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        return sortedMap;
    }

    private static void printMap(Map<String, Integer> map) {
        System.out.println("Ukupni glasovi:\n");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue() + " glasova");
        }
    }
}
