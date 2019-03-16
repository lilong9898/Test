package com.lilong.ipc;
import android.os.ParcelFileDescriptor;

interface ITest {
    int calculate(int number1, int number2);
    byte[] getData();
    ParcelFileDescriptor getAshmemFd();
}
