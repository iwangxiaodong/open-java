package com.openle.our.core.io;

import java.io.IOException;
import java.io.Serializable;

public class Serializer implements ObjectSerialization {

    @Override
    public byte[] dumpToByteArray(Object obj) {
        try {
            return ObjectIO.objectToBytes((Serializable) obj);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return null;
    }

    @Override
    public Object load(byte[] bytes) {
        try {
            return ObjectIO.bytesToObject(bytes);
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println(ex);
        }
        return null;
    }

    @Override
    public StreamMode streamMode() {
        return StreamMode.BINARY;
    }

}
