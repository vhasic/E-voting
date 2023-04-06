/*
 * Copyright (c) 2023. Vahidin Hasić
 */

package ba.etf.elections.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// Core module would be used at CIK for generating ballots
public class BallotGeneratorApplication {
    public static void main(String[] args) {
        // ask user to input path to JSON file and Fxml file
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object

        System.out.println("Unesite naslov glasačkog listića:");
        String ballotTitle = scanner.nextLine();  // Read user input
//        String ballotTitle = "ZASTUPNIČKI/PREDSTAVNIČKI DOM PARLAMENTA FEDERACIJE BIH - IZBORNA JEDINICA 11";
//        "OPĆINSKO VIJEĆE"
//        OPĆINSKI NAČELNIK ILIDŽA

        System.out.println("Unesite broj kolona za prikaz na glasačkom listiću:");
        int numberOfColumns = scanner.nextInt();  // Read user input
        //read enter key after reading int
        scanner.nextLine();
//        int numberOfColumns = 3;

        System.out.println("Unesite putanju do .json datoteke u kojoj se nalaze stranke i kandidati:");
        String inputFilename = scanner.nextLine();  // Read user input
//        String inputFilename = "C:\\Users\\Vahidin\\IdeaProjects\\Elections\\Core\\src\\main\\java\\ba\\etf\\elections\\core\\parties&candidates.json";

        System.out.println("Unesite putanju do .fxml datoteke u koju želite da se upišu podaci:");
        String outputFilename = scanner.nextLine();  // Read user input
//        String outputFilename = "C:\\Users\\Vahidin\\IdeaProjects\\Elections\\Client\\src\\main\\resources\\ba\\etf\\elections\\client\\page1.fxml";
//        "C:\\Users\\Vahidin\\IdeaProjects\\Elections\\Core\\src\\main\\java\\ba\\etf\\elections\\core\\ballots\\page1.fxml";

        try {
            List<PartyCandidates> partyCandidatesList = GetPartiesCandidates(inputFilename);
            createFXMLFile(numberOfColumns, ballotTitle,
                    outputFilename, partyCandidatesList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createFXMLFile(int numberOfColumns, String ballotTitle, String outputFilename, List<PartyCandidates> partyCandidatesList) throws IOException {
        File fxmlFile = new File(outputFilename);
        fxmlFile.createNewFile();
        FileWriter fileWriter = new FileWriter(fxmlFile);

        fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<?import javafx.geometry.Insets?>\n" +
                "<?import javafx.scene.control.CheckBox?>\n" +
                "<?import javafx.scene.control.Label?>\n" +
                "<?import javafx.scene.control.RadioButton?>\n" +
                "<?import javafx.scene.layout.ColumnConstraints?>\n" +
                "<?import javafx.scene.layout.GridPane?>\n" +
                "<?import javafx.scene.layout.RowConstraints?>\n" +
                "<?import javafx.scene.layout.VBox?>\n" +
                "<?import javafx.scene.text.Font?>\n" +
                "\n" +
                "<GridPane fx:id=\"gridPane\" alignment=\"CENTER\" xmlns=\"http://javafx.com/javafx/19\" xmlns:fx=\"http://javafx.com/fxml/1\">\n" +
                "   <columnConstraints>\n" +
                "      <ColumnConstraints halignment=\"CENTER\" hgrow=\"SOMETIMES\" minWidth=\"400.0\" />\n" +
                "      <ColumnConstraints halignment=\"CENTER\" hgrow=\"SOMETIMES\" minWidth=\"400.0\" />\n" +
                "      <ColumnConstraints halignment=\"CENTER\" minWidth=\"10.0\" />\n" +
                "   </columnConstraints>\n" +
                "   <rowConstraints>\n" +
                "      <RowConstraints valignment=\"CENTER\" vgrow=\"SOMETIMES\" />\n" +
                "      <RowConstraints minHeight=\"10.0\" valignment=\"CENTER\" vgrow=\"SOMETIMES\" />\n" +
                "      <RowConstraints minHeight=\"10.0\" valignment=\"CENTER\" vgrow=\"SOMETIMES\" />\n" +
                "      <RowConstraints minHeight=\"10.0\" valignment=\"CENTER\" vgrow=\"SOMETIMES\" />\n" +
                "      <RowConstraints minHeight=\"10.0\" valignment=\"CENTER\" vgrow=\"SOMETIMES\" />\n" +
                "   </rowConstraints>\n" +
                "   <children>\n");

        fileWriter.write(getBallotTitleFxml(ballotTitle));
        for (int i = 0; i < partyCandidatesList.size(); i++) {
            PartyCandidates partyCandidates = partyCandidatesList.get(i);
            int row = i / numberOfColumns + 1; // +1 because we have ballot title which is in row 0
            int column = i % numberOfColumns;
            fileWriter.write(getBallotContentFxml(partyCandidates, row, column));
        }

        fileWriter.write("   </children>\n" +
                "</GridPane>");
        fileWriter.close();
    }

    private static String getBallotContentFxml(PartyCandidates partyCandidates, int row, int column) {
        // If party has candidates, make party name bold, otherwise make it normal (this is in case of independent candidates)
        String fontType = partyCandidates.getPartyCandidates().size() > 0 ? "System Bold" : "System";

        StringBuilder s = new StringBuilder("      <VBox layoutX=\"10.0\" layoutY=\"469.0\" prefWidth=\"430.0\" GridPane.columnIndex=\"" + column + "\" GridPane.rowIndex=\"" + row + "\">\n" +
                "         <children>\n" +
                "            <RadioButton mnemonicParsing=\"false\" text=\"" + partyCandidates.getPartyName() + "\">\n" +
                "               <padding>\n" +
                "                  <Insets bottom=\"10.0\" top=\"10.0\" />\n" +
                "               </padding>\n" +
                "               <font>\n" +
                "                  <Font name=\""+fontType+"\" size=\"20.0\" />\n"+
                "               </font>\n" +
                "               <VBox.margin>\n" +
                "                  <Insets right=\"5.0\" top=\"10.0\" />\n" +
                "               </VBox.margin>\n" +
                "            </RadioButton>\n");

        for (String candidateName : partyCandidates.getPartyCandidates()) {
            s.append("            <CheckBox mnemonicParsing=\"false\" text=\"" + candidateName + "\">\n" +
                    "               <font>\n" +
                    "                  <Font size=\"18.0\" />\n" +
                    "               </font>\n" +
                    "               <VBox.margin>\n" +
                    "                  <Insets left=\"10.0\" top=\"10.0\" />\n" +
                    "               </VBox.margin>\n" +
                    "            </CheckBox>\n");

        }

        s.append("         </children>\n" +
                "         <padding>\n" +
                "            <Insets left=\"20.0\" />\n" +
                "         </padding>\n" +
                "      </VBox>");
        return s.toString();
    }

    private static String getBallotTitleFxml(String ballotTitle) {
        return "      <VBox alignment=\"CENTER\" GridPane.rowIndex=\"0\" GridPane.columnIndex=\"0\" GridPane.columnSpan=\"2147483647\">\n" +
                "         <children>\n" +
                "            <Label alignment=\"CENTER\" text=\"" + ballotTitle + "\">\n" +
                "               <font>\n" +
                "                  <Font name=\"System Bold\" size=\"18.0\" />\n" +
                "               </font>\n" +
                "               <VBox.margin>\n" +
                "                  <Insets bottom=\"10.0\" left=\"10.0\" right=\"10.0\" top=\"10.0\" />\n" +
                "               </VBox.margin>\n" +
                "            </Label>\n" +
                "         </children>\n" +
                "      </VBox>\n";
    }

    private static List<PartyCandidates> GetPartiesCandidates(String inputFilename) throws IOException {
        File file = new File(inputFilename); // open file that contains parties and candidates as JSON array of objects
        // read objects from file into a list of Java objects
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, new TypeReference<>() {});
    }
}