package com.example.m_track;

import android.os.Bundle;

public class Update_infoHandler {
    private Bundle message;

    public Update_infoHandler(Bundle updateInfo) {
        this.message = updateInfo;
    }

    public Bundle getUpdateinfo() {
        return message;
    }
}
