package com.lilong.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import com.lilong.ipc.ITest;

import java.io.FileDescriptor;
import java.lang.reflect.Method;

import static com.lilong.ipc.server.MainActivity.TAG;


public class TestService extends Service {

    class StubImpl extends ITest.Stub{

        @Override
        public int calculate(int number1, int number2) throws RemoteException {
            Log.i(TAG, "Server calculate currentThread = " + Thread.currentThread().getName());
            return number1 + number2;
        }

        @Override
        public byte[] getData() throws RemoteException {
            return new byte[]{1, 2, 3};
        }

        @Override
        public ParcelFileDescriptor getAshmemFd() {
            MemoryFile memoryFile = null;
            try{
                memoryFile = new MemoryFile("test_memory", 1024);
                memoryFile.getOutputStream().write(new byte[]{4, 5, 6});
                Method method = MemoryFile.class.getDeclaredMethod("getFileDescriptor");
                method.setAccessible(true);
                FileDescriptor fd = (FileDescriptor) method.invoke(memoryFile);
                return ParcelFileDescriptor.dup(fd);
            }catch (Exception e){}
            return null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return new StubImpl();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        return super.onUnbind(intent);
    }
}
