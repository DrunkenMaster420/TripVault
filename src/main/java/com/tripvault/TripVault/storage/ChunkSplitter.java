package com.tripvault.TripVault.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChunkSplitter {

    private static final int CHUNK_SIZE = 5 * 1024 * 1024; // 5MB

    public static List<byte[]> splitFile(MultipartFile file) throws IOException {

        byte[] data = file.getBytes();

        List<byte[]> chunks = new ArrayList<>();

        int start = 0;

        while (start < data.length) {

            int end = Math.min(start + CHUNK_SIZE, data.length);

            byte[] chunk = new byte[end - start];

            System.arraycopy(data, start, chunk, 0, chunk.length);

            chunks.add(chunk);

            start = end;
        }

        return chunks;
    }
}