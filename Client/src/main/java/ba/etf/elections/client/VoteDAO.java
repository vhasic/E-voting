package ba.etf.elections.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class VoteDAO {
    private static VoteDAO instance;
    private Connection conn;

    private ObservableList<Vote> voteObservableList = FXCollections.observableArrayList();


    private PreparedStatement getAllVotes, insertVote;

    private VoteDAO() {
        // connect to the database
        try {
            String url = "jdbc:sqlite:votes.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // if database is empty, populate it with data from the sql file
        try {
            getAllVotes=conn.prepareStatement("SELECT * FROM vote v;");
        } catch (SQLException e) {
            regenerateDatabase();
        }

        try {
            getAllVotes=conn.prepareStatement("SELECT * FROM vote v;");
//            insertVote=conn.prepareStatement("INSERT INTO vote (candidate_id, ballot_number) VALUES (?, ?);");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Vote> getAllVotes() {
        ArrayList<Vote> list=new ArrayList<>();
        try {
            ResultSet rs=getAllVotes.executeQuery();
            while (rs.next()){
                //todo implement
//                Vote vote = new Vote(rs.getInt(1),rs.getInt(2),rs.getString(3));
//                list.add(vote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // function to insert vote
    public void insertVote(Vote vote) {
        // todo implement
//        try {
//            insertVote.setInt(1, vote.getCandidateId());
//            insertVote.setInt(2, vote.getBallotNumber());
//            insertVote.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }


    public static VoteDAO getInstance() {
        if (instance == null) instance = new VoteDAO();
        return instance;
    }

    public static void removeInstance() {
        if (instance != null) {
            try {
                instance.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        instance = null;
    }

    private void regenerateDatabase() {
        try {
            Scanner input = new Scanner(new FileInputStream("elections.sqlite"));
            String sqlQuery = "";
            while (input.hasNext()) {
                sqlQuery += input.nextLine();
                if (sqlQuery.charAt(sqlQuery.length() - 1) == ';') {
                    Statement stmt = conn.createStatement();
                    stmt.execute(sqlQuery);
                    sqlQuery = "";
                }
            }
            input.close();
        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
