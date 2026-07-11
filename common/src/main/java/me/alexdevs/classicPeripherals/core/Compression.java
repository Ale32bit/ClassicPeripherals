package me.alexdevs.classicPeripherals.core;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Compression {
    private static final Charset CHARSET = StandardCharsets.ISO_8859_1;

    public static String inflate(String data) throws DataFormatException {
        var inflater = new Inflater();
        inflater.setInput(data.getBytes(CHARSET));

        var baos = new ByteArrayOutputStream();
        var buffer = new byte[1024];
        while (!inflater.finished()) {
            if (inflater.needsInput() || inflater.needsDictionary()) {
                throw new DataFormatException("Incomplete or invalid deflate data");
            }
            var count = inflater.inflate(buffer);
            baos.write(buffer, 0, count);
        }

        inflater.end();

        return baos.toString(CHARSET);
    }

    public static String deflate(String data) {
        var bytes = data.getBytes(CHARSET);
        var deflater = new Deflater();
        deflater.setInput(bytes);
        deflater.finish();

        var baos = new ByteArrayOutputStream();
        var buffer = new byte[1024];
        while (!deflater.finished()) {
            var count = deflater.deflate(buffer);
            baos.write(buffer, 0, count);
        }

        deflater.end();

        return baos.toString(CHARSET);
    }
}
