package com.example.test1;

import android.bluetooth.BluetoothDevice;

import java.util.List;

public interface OnTaskDetected {

    void onTaskDetected(List<BluetoothDevice> fromscanner);
}
