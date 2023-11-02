package com.example.m_track;

import android.os.Bundle;

public class Update_personinfoHandler {
    private Bundle message;

    public Update_personinfoHandler(Bundle updateInfo) {
        this.message = updateInfo;
    }

    public Bundle getUpdateinfo() {
        return message;
    }
}
