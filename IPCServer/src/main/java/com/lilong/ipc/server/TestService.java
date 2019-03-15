package com.lilong.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.lilong.ipc.ITest;


public class TestService extends Service {

    class StubImpl extends ITest.Stub{

        @Override
        public int calculate(int number1, int number2) throws RemoteException {
            return number1 + number2;
        }

        @Override
        public byte[] getData() throws RemoteException {
            return new byte[]{1, 2, 3};
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new StubImpl();
    }


}
