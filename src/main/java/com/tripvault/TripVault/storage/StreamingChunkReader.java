package com.tripvault.TripVault.storage;

import java.io.InputStream;

public class StreamingChunkReader {

    public static final int CHUNK_SIZE =
            5 * 1024 * 1024;

    public static byte[] readChunk(
            InputStream inputStream
    ) throws Exception {

        byte[] buffer = new byte[CHUNK_SIZE];

        int bytesRead =
                inputStream.read(buffer);

        if (bytesRead == -1) {
            return null;
        }

        if (bytesRead == CHUNK_SIZE) {
            return buffer;
        }

        byte[] finalChunk =
                new byte[bytesRead];

        System.arraycopy(
                buffer,
                0,
                finalChunk,
                0,
                bytesRead
        );

        return finalChunk;
    }
}