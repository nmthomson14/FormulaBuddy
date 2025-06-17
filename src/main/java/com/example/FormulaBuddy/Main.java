package com.example.FormulaBuddy;

public class Main {

    public static void main(String[] args) {

        GUIBuilder gui = new GUIBuilder();

        SystemMessageHandler.initializeInstance(
                new MessageReceiver[] { gui }
        );

    }
}
