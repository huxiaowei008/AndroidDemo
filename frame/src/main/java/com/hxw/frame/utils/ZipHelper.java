package com.hxw.frame.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

/**
 * @author jess
 * @date 16/5/10
 */
public class ZipHelper {

    private ZipHelper() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * zlib decompress 2 String
     * 解压zlib
     *
     * @param bytesToDecompress
     * @return
     */
    public static String decompressToStringForZlib(byte[] bytesToDecompress) {
        return decompressToStringForZlib(bytesToDecompress, "UTF-8");
    }

    /**
     * zlib decompress 2 String
     * 解压zlib
     *
     * @param bytesToDecompress
     * @return
     */
    public static String decompressToStringForZlib(byte[] bytesToDecompress, String charsetName) {
        byte[] bytesDecompressed = decompressForZlib(bytesToDecompress);

        String returnValue = null;

        try {
            returnValue = new String(
                    bytesDecompressed,
                    0,
                    bytesDecompressed.length,
                    charsetName
            );
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }

        return returnValue;
    }


    /**
     * zlib decompress 2 byte
     * 解压zlib
     *
     * @param bytesToDecompress
     * @return
     */
    public static byte[] decompressForZlib(byte[] bytesToDecompress) {
        byte[] returnValues = null;

        Inflater inflater = new Inflater();

        int numberOfBytesToDecompress = bytesToDecompress.length;

        inflater.setInput
                (
                        bytesToDecompress,
                        0,
                        numberOfBytesToDecompress
                );

        int bufferSizeInBytes = numberOfBytesToDecompress;

        int numberOfBytesDecompressedSoFar = 0;
        List<Byte> bytesDecompressedSoFar = new ArrayList<Byte>();

        try {
            while (!inflater.needsInput()) {
                byte[] bytesDecompressedBuffer = new byte[bufferSizeInBytes];

                int numberOfBytesDecompressedThisTime = inflater.inflate
                        (
                                bytesDecompressedBuffer
                        );

                numberOfBytesDecompressedSoFar += numberOfBytesDecompressedThisTime;

                for (int b = 0; b < numberOfBytesDecompressedThisTime; b++) {
                    bytesDecompressedSoFar.add(bytesDecompressedBuffer[b]);
                }
            }

            returnValues = new byte[bytesDecompressedSoFar.size()];
            for (int b = 0; b < returnValues.length; b++) {
                returnValues[b] = (byte) (bytesDecompressedSoFar.get(b));
            }

        } catch (DataFormatException dfe) {
            dfe.printStackTrace();
        }

        inflater.end();

        return returnValues;
    }

    /**
     * zlib compress 2 byte
     * 压缩zlib
     *
     * @param bytesToCompress
     * @return
     */
    public static byte[] compressForZlib(byte[] bytesToCompress) {
        Deflater deflater = new Deflater();
        deflater.setInput(bytesToCompress);
        deflater.finish();

        byte[] bytesCompressed = new byte[Short.MAX_VALUE];

        int numberOfBytesAfterCompression = deflater.deflate(bytesCompressed);

        byte[] returnValues = new byte[numberOfBytesAfterCompression];

        System.arraycopy
                (
                        bytesCompressed,
                        0,
                        returnValues,
                        0,
                        numberOfBytesAfterCompression
                );

        return returnValues;
    }

    /**
     * zlib compress 2 byte
     * 压缩zlib
     *
     * @param stringToCompress
     * @return
     */
    public static byte[] compressForZlib(String stringToCompress) {
        byte[] returnValues = null;

        try {
            returnValues = compressForZlib(stringToCompress.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }

        return returnValues;
    }

    /**
     * gzip compress 2 byte
     * 压缩gzip
     *
     * @param string
     * @return
     * @throws IOException
     */
    public static byte[] compressForGzip(String string) {
        ByteArrayOutputStream os = null;
        GZIPOutputStream gos = null;
        try {
            os = new ByteArrayOutputStream(string.length());
            gos = new GZIPOutputStream(os);
            gos.write(string.getBytes("UTF-8"));
            byte[] compressed = os.toByteArray();
            return compressed;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(gos, os);
        }
        return null;
    }

    /**
     * gzip decompress 2 string
     *
     * @param compressed
     * @return
     * @throws IOException
     */
    public static String decompressForGzip(byte[] compressed) {
        return decompressForGzip(compressed, "UTF-8");
    }

    /**
     * gzip decompress 2 string
     * 解压gzip
     *
     * @param compressed
     * @return
     * @throws IOException
     */
    public static String decompressForGzip(byte[] compressed, String charsetName) {

        final int bufferSize = compressed.length;
        GZIPInputStream gis = null;
        ByteArrayInputStream is = null;
        try {
            is = new ByteArrayInputStream(compressed);
            gis = new GZIPInputStream(is, bufferSize);
            StringBuilder string = new StringBuilder();
            byte[] data = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = gis.read(data)) != -1) {
                string.append(new String(data, 0, bytesRead, charsetName));
            }
            return string.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(gis, is);
        }
        return null;
    }

}
