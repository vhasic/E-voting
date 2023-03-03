package ba.etf.elections.client;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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

    public Vote() {
        votedCandidates = new ArrayList<>();
    }

    public static Vote createInvalidVote() {
        Vote invalidVote = new Vote();
        invalidVote.getVotedCandidates().add("Invalid");
        return invalidVote;
    }

}

