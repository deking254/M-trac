package com.example.m_track;

    public class IdHandler {
//        This class handles the conveying of the id to different files and classes
        private final int message;

        public IdHandler(int message) {
            this.message = message;
        }

        public int getMessage() {
            return message;
        }
    }
