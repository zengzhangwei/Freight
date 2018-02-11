// ITelephony.aidl
package com.zl.freight;

// Declare any non-default types here with import statements

interface ITelephony {

    boolean endCall();

    void answerRingingCall();

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
