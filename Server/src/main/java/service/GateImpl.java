package service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import proto.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GateImpl extends GateGrpc.GateImplBase {
    /*
     * We observe here that some words have an "@", this are Annotations. Annotations are used to provide supplement
     * information about a program. We can autogenerate this functions, in Intellij we can use the shortcut ctrl + O to
     * do this.
     * */

    String getSeason(String userMonth) {
        String month = "";
        switch(userMonth) {
            case "12":
            case "1":
            case "2":
            case "01":
            case "02": {
                month = "winter";
                break;
            }
            case "3":
            case "4":
            case "5":
            case "03":
            case "04":
            case "05": {
                month = "spring";
                break;
            }
            case "6":
            case "7":
            case "8":
            case "06":
            case "07":
            case "08": {
                month = "summer";
                break;
            }
            case "9":
            case "10":
            case "11":
            case "09": {
                month = "fall";
                break;
            }
        }

        return month;

    }

    @Override
    public void getZodiacSign( Zodiac.InfoRequest request, StreamObserver<Zodiac.InfoReply> responseObserver) {

        String birthdate = request.getDate();
        int iend = birthdate.indexOf("/");

        String season;

        season= birthdate.substring(0 , iend);

        season = getSeason(season);

        String zodiacSign="";

        switch(season){
            case "winter": {
                //TODO CALL THE WINTER SERVICE

                ManagedChannel channelWinter = ManagedChannelBuilder.forAddress("localhost", 8997).usePlaintext().build();
                WinterZodiacSignGrpc.WinterZodiacSignBlockingStub winterStub = WinterZodiacSignGrpc.newBlockingStub(channelWinter);

                Zodiac.WinterZodiacSignReply reply = winterStub.getWinterSign(Zodiac.WinterZodiacSignRequest.newBuilder().
                        setDate(request.getDate()).build());

                zodiacSign = reply.getMessage();
                System.out.println(reply.getMessage());

            }

            case "spring":{
                //TODO CALL THE SPRING SERVICE
                ManagedChannel channelSpring = ManagedChannelBuilder.forAddress("localhost", 8996).usePlaintext().build();
                SpringZodiacSignGrpc.SpringZodiacSignBlockingStub springStub = SpringZodiacSignGrpc.newBlockingStub(channelSpring);

                Zodiac.SpringZodiacSignReply reply = springStub.getSpringSign(Zodiac.SpringZodiacSignRequest.newBuilder().
                        setDate(request.getDate()).build());

                zodiacSign = reply.getMessage();
                System.out.println(reply.getMessage());
            }

            case "summer":{
                //TODO CALL THE SUMMER SERVICE
                ManagedChannel channelSummer = ManagedChannelBuilder.forAddress("localhost", 8995).usePlaintext().build();
                SummerZodiacSignGrpc.SummerZodiacSignBlockingStub summerStub = SummerZodiacSignGrpc.newBlockingStub(channelSummer);

                Zodiac.SummerZodiacSignReply reply = summerStub.getSummerSign(Zodiac.SummerZodiacSignRequest.newBuilder().
                        setDate(request.getDate()).build());

                zodiacSign = reply.getMessage();
                System.out.println(reply.getMessage());
            }

            case "fall":{
                //TODO CALL THE FALL SERVICE
                ManagedChannel channelFall = ManagedChannelBuilder.forAddress("localhost", 8994).usePlaintext().build();
                FallZodiacSignGrpc.FallZodiacSignBlockingStub fallStub = FallZodiacSignGrpc.newBlockingStub(channelFall);

                Zodiac.FallZodiacSignReply reply = fallStub.getFallSign(Zodiac.FallZodiacSignRequest.newBuilder().
                        setDate(request.getDate()).build());

                zodiacSign = reply.getMessage();
                System.out.println(reply.getMessage());
            }
        }


        Zodiac.InfoReply reply = Zodiac.InfoReply.newBuilder().setMessage("Hello " + request.getName()
                + " your zodiac sign is "+ zodiacSign + '\n').build();
        /* We can call multiple times onNext function if we have multiple replies, ex. in next commits */
        responseObserver.onNext(reply);
        responseObserver.onCompleted();

    }
}
