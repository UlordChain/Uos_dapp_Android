package io.eblock.eos4j.blockchain.types;

import java.util.Collection;

/**
 * Created by xuhuixiang@ulord.net on 2017-09-12.
 */

public interface EosType {
    class InsufficientBytesException extends Exception {

        private static final long serialVersionUID = 1L;
    }

    interface Packer {
        void pack(EosType.Writer writer);
    }

    interface Unpacker {
        void unpack(EosType.Reader reader) throws EosType.InsufficientBytesException;
    }

    interface Reader {
        byte get() throws EosType.InsufficientBytesException;
        int getShortLE() throws EosType.InsufficientBytesException;
        int getIntLE() throws EosType.InsufficientBytesException;
        long getLongLE() throws EosType.InsufficientBytesException;
        byte[] getBytes(int size) throws EosType.InsufficientBytesException;
        String getString() throws EosType.InsufficientBytesException;

        long getVariableUint() throws EosType.InsufficientBytesException;
    }

    interface Writer {
        void put(byte b);
        void putShortLE(short value);
        void putIntLE(int value);
        void putLongLE(long value);
        void putBytes(byte[] value);
        void putString(String value);
        byte[] toBytes();
        int length();

        void putCollection(Collection<? extends Packer> collection);

        void putVariableUInt(long val);
    }
}
