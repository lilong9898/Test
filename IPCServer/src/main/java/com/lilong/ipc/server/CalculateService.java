package com.lilong.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.lilong.ipc.ICalculate;

public class CalculateService extends Service {

    class StubImpl extends ICalculate.Stub{

        @Override
        public int calculate(int number1, int number2) throws RemoteException {
            return number1 + number2;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new StubImpl();
    }


}
