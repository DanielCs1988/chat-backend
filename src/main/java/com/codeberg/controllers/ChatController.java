package com.codeberg.controllers;

import com.codeberg.models.Message;
import com.codeberg.models.Profile;
import com.danielcs.webserver.socket.SocketContext;
import com.danielcs.webserver.socket.annotations.*;

import java.util.*;

@SocketController
public class ChatController {

    private String currentRoom;
    private String username = "Anon";

    private static final Set<String> allowedRooms = new HashSet<>(Arrays.asList(
            "Blue Room", "Red Room", "Green Room", "Silent Room"
    ));

    private static final Set<String> connectedUsers = new LinkedHashSet<>();

    @OnMessage(route = "name")
    public void setName(SocketContext ctx, String name) {
        ctx.setProperty("name", name);
        connectedUsers.add(name);
        username = name;
        ctx.emit("name", connectedUsers);
    }

    @OnMessage(route = "chat")
    public void chat(SocketContext ctx, String msg) {
        Message message = new Message(username, msg);
        ctx.emit("chat", message);
    }

    @OnMessage(route = "profile", type = Profile.class)
    public void createProfile(SocketContext ctx, Profile profile) {
        ctx.reply(profile);
    }

    @OnMessage(route = "rooms")
    public void getAllRooms(SocketContext ctx, String msg) {
        ctx.reply(allowedRooms);
    }

    @OnMessage(route = "room/join")
    public void joinRoom(SocketContext ctx, String room) {
        if (!allowedRooms.contains(room)) {
            return;
        }
        if (ctx.getCurrentRooms().size() > 0) {
            ctx.leaveAllRooms();
        }
        ctx.joinRoom(room);
        ctx.emitToRoom(room, "room/join", username);
        currentRoom = room;
    }

    @OnMessage(route = "room/leave")
    public void leaveRoom(SocketContext ctx, String room) {
        if (!allowedRooms.contains(room)) {
            return;
        }
        ctx.leaveRoom(room);
        ctx.emitToRoom(room, "room/leave", username);
        currentRoom = null;
    }

    @OnMessage(route = "room/chat")
    public void chatWithRoom(SocketContext ctx, String msg) {
        if (currentRoom == null) {
            return;
        }
        Message message = new Message(username, msg);
        ctx.emitToRoom(currentRoom,"room/chat", message);
    }

    @OnMessage(route = "private", type = Message.class)
    public void privateMessage(SocketContext ctx, Message msg) {
        String target = msg.getName();
        Message msgToSend = new Message(username, msg.getContent());
        ctx.sendToUser("name", target, "private", msgToSend);
    }

    @OnMessage(route = "disconnect")
    public void onDisconnect(SocketContext ctx) {
        connectedUsers.remove(username);
        System.out.println(username + " has left the server.");
        ctx.emit("name", connectedUsers);
    }
}
