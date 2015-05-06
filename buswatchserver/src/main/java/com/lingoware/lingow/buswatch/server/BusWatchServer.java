package com.lingoware.lingow.buswatch.server;

import javax.xml.ws.Endpoint;

public class BusWatchServer {

    public static void main(String args[]) {
        Endpoint.publish("http://localhost:" + System.getenv("PORT") + "/", new BusWatchServiceImplementor());
    }
}
