package ba.etf.elections.client;

import ba.etf.elections.client.helper.CryptographyHelper;
import ba.etf.elections.client.helper.ICryptographyHelper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.Console;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        invalidVote.getVotedCandidates().add("Invalid");
        return invalidVote;
    }

    public void calculateVoteMacHash(){
        ICryptographyHelper cryptographyHelper = new CryptographyHelper();
        String HmacSHA256 = null;
        try {
            HmacSHA256 = cryptographyHelper.createMACHash(votedCandidates.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        voteMacHash = HmacSHA256;
    }

}

