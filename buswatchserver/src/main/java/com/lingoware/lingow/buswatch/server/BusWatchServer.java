package com.lingoware.lingow.buswatch.server;

import org.eclipse.jetty.server.Server;

import javax.xml.ws.Endpoint;

public class BusWatchServer {

    public static void main(String args[]) throws Exception {
        String port = System.getenv("PORT");

        Server server = new Server(Integer.parseInt(port));

        Endpoint.publish("http://localhost:" + port + "/", new BusWatchServiceImplementor());
        server.start();
        server.join();
    }
}
