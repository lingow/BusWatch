package com.lingoware.lingow.buswatch.server;

import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import javax.xml.ws.Endpoint;

public class BusWatchServer {

    public static void main(String args[]) throws Exception {
        int port = Integer.parseInt(System.getenv("PORT"));
        System.out.println("Puerto " + port);
        System.setProperty("org.eclipse.jetty.LEVEL","DEBUG");

        System.setProperty("com.sun.net.httpserver.HttpServerProvider", JettyHttpServer.class.getName());
        Server server = new Server(port);
        JettyHttpServer jettyServer = new JettyHttpServer(server, true);
        ContextHandlerCollection collection = new ContextHandlerCollection();
        server.setHandler(collection);
        Endpoint endpoint = Endpoint.create(new BusWatchServiceImplementor());
        endpoint.publish(jettyServer.createContext("/"));
        server.start();
        server.join();

    }
}
