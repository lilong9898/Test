package com.lilong.javatest;

/**
 * Created by lilong on 06/01/2020.
 */
public class FinalTest {

    class GrandParents{
        void fun(){}
    }

    class Parents extends GrandParents{
        @Override
        final void fun() {
            super.fun();
        }
    }

    class Children extends Parents{
    }
}
