package com.codeberg;

import com.danielcs.webserver.Server;
import com.danielcs.webserver.socket.SocketServer;

public class Run {

    public static void main(String[] args) {
        Server server = new SocketServer(8080, "com.codeberg.controllers");
        server.start();
    }

}
