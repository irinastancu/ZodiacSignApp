package main;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import service.WinterImpl;

import java.io.IOException;

public class WinterMain {
    public static void main(String[] args) {
        try {
            /* *
             * Do not forget to install maven. The grpc stub classes are generated when you run the protoc compiler
             * and it finds a service declaration in your proto file.
             * Do not forget the client must use the same port in order to connect to this server.
             * */
            Server server = ServerBuilder.forPort(8997).addService(new WinterImpl()).build();

            /*ArrayList<BindableService> allServices = new ArrayList<>();
            allServices.add(new EuropeanImpl());
            allServices.add(new WinterImpl());
            allServices.add(new SpringImpl());
            allServices.add(new SummerImpl());
            allServices.add(new FallImpl());*/

           /* int counter =1;
            for (BindableService service : allServices) {
                server = ServerBuilder.forPort(8999 - counter).addService(service).build();
                ++counter;
            }*/

            server.start();
            System.out.println("Server started at " + server.getPort());
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e);
        }
    }
}