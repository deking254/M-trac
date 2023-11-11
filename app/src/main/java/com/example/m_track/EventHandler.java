package com.example.m_track;

    public class EventHandler {
//        It handles creating an object which contains a string that specifies the type of event in the eventbus library
        private final String message;

        public EventHandler(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
