package service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import proto.WinterZodiacSignGrpc;
import proto.Zodiac;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WinterImpl extends WinterZodiacSignGrpc.WinterZodiacSignImplBase {
    /*
     * We observe here that some words have an "@", this are Annotations. Annotations are used to provide supplement
     * information about a program. We can autogenerate this functions, in Intellij we can use the shortcut ctrl + O to
     * do this.
     * */
    Map<String, Pair<String, String>> zodiacSigns;

    public WinterImpl() {
        //pathZodiac = getClass().getPackage().getResource("/zodiac.txt").toString();

        //TODO Find a better way to give the path name
        this.zodiacSigns = HashMapFromTextFile("C:/Users/Ina/Desktop/Tema2/Tema-2-CNA/Server/src/main/resources/winter.txt");
    }

    public static Map<String, Pair<String, String>> HashMapFromTextFile(String pathName) {
        Map<String, Pair<String, String>> map = new HashMap<>();
        BufferedReader br = null;

        try {
            // create file object
            File file = new File(pathName);
            FileReader fileReader = new FileReader(file);

            // create BufferedReader object from the File
            br = new BufferedReader(fileReader);

            String line;

            // read file line by line
            while ((line = br.readLine()) != null) {

                // split the line by :
                String[] parts = line.split(":");

                // first part is name, second is number
                String zodiacSign = parts[0].trim();
                String[] value = parts[1].split(",");

                String start = value[0].trim();
                String end = value[1].trim();

                MutablePair<String, String> period = new MutablePair<>(start, end);

                // put name, number in HashMap if they are
                // not empty
                if (!zodiacSign.equals("") && !period.equals(""))
                    map.put(zodiacSign, period);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            // Always close the BufferedReader
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return map;
    }


    boolean isWithinRange(Date birthDate, Date startDate, Date endDate) {
        boolean isBefore = birthDate.before(endDate);
        boolean isAfter = birthDate.after(startDate);
        return isBefore && isAfter;
    }

    public String getSign(String birthdate) throws ParseException {

        for (Map.Entry<String, Pair<String, String>> entry : zodiacSigns.entrySet()) {

            DateFormat format = new SimpleDateFormat("MM/dd");
            Date userDate;
            Date zodiacBegin;
            Date zodiacEnd;

            userDate = format.parse(birthdate);
            zodiacBegin = format.parse(entry.getValue().getLeft());
            zodiacEnd = format.parse(entry.getValue().getRight());

            if (isWithinRange(userDate, zodiacBegin, zodiacEnd)) {
                return entry.getKey();
            }

        }
        return "Capricorn";
    }

    @Override

    public void getWinterSign(Zodiac.ZodiacRequest request, StreamObserver<Zodiac.WinterZodiacSignReply> responseObserver) {

        String birthdate = request.getDate();

        String zodiacSign = null;
        try {
            zodiacSign = getSign(birthdate);
        } catch (ParseException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }

        Zodiac.WinterZodiacSignReply reply = Zodiac.WinterZodiacSignReply.newBuilder().setMessage(zodiacSign).build();

        System.out.println(reply.getMessage());

        responseObserver.onNext(reply);
        responseObserver.onCompleted();

        System.out.println("Information has been delivered");

    }

}
