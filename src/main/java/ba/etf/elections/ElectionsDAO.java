package ba.etf.elections;

import ba.etf.elections.models.Ballot;
import ba.etf.elections.models.Candidate;
import ba.etf.elections.models.PoliticalParty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ElectionsDAO {
    private static ElectionsDAO instance;
    private Connection conn;

    private ObservableList<Candidate> projectList = FXCollections.observableArrayList();
    private ObservableList<PoliticalParty> politicalParties = FXCollections.observableArrayList();
    private ObservableList<Ballot> ballots = FXCollections.observableArrayList();


    private PreparedStatement getAllCandidates, getAllPoliticalParties, getPoliticalParty, getCandidatesForPoliticalParty, getAllBallots, insertBallot;

    private ElectionsDAO() {
        // connect to the database
        try {
            String url = "jdbc:sqlite:elections.sqlite";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // if database is empty, populate it with data from the sql file
        try {
            getAllCandidates=conn.prepareStatement("SELECT * FROM Candidate c;");
        } catch (SQLException e) {
            regenerateDatabase();
        }

        try {
            getAllCandidates=conn.prepareStatement("SELECT * FROM Candidate c;");
            getAllPoliticalParties=conn.prepareStatement("SELECT * FROM PoliticalParty p;");
            getCandidatesForPoliticalParty=conn.prepareStatement("SELECT * FROM Candidate c WHERE c.political_party_id=?;");
            getPoliticalParty=conn.prepareStatement("SELECT * FROM PoliticalParty p WHERE p.id=?;");
//            getAllBallots=conn.prepareStatement("SELECT * FROM Ballot b;");
//            insertBallot=conn.prepareStatement("INSERT INTO Ballot (political_party_id, candidate_id, ballot_number) VALUES (?, ?, ?);");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Candidate> getAllCandidatesForPoliticalParty(int politicalPartyId) {
        ArrayList<Candidate> list=new ArrayList<>();
        try {
            PoliticalParty politicalParty=getPoliticalParty(politicalPartyId);
            getCandidatesForPoliticalParty.setInt(1, politicalPartyId);
            ResultSet rs=getCandidatesForPoliticalParty.executeQuery();
            while (rs.next()){
                Candidate candidate= new Candidate(rs.getInt(1),rs.getInt(2),rs.getString(3),politicalParty);
                list.add(candidate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public PoliticalParty getPoliticalParty(int politicalPartyId) {
        PoliticalParty politicalParty=null;
        try {
            getPoliticalParty.setInt(1, politicalPartyId);
            ResultSet rs=getPoliticalParty.executeQuery();
            if (rs.next()){
                politicalParty=new PoliticalParty(rs.getInt(1),rs.getInt(2),rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return politicalParty;
    }

    public List<PoliticalParty> getAllPoliticalParties() {
        List<PoliticalParty> list=new ArrayList<>();
        try {
            ResultSet rs=getAllPoliticalParties.executeQuery();
            while (rs.next()){
                PoliticalParty politicalParty=new PoliticalParty(rs.getInt(1),rs.getInt(2),rs.getString(3));
                list.add(politicalParty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ElectionsDAO getInstance() {
        if (instance == null) instance = new ElectionsDAO();
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
