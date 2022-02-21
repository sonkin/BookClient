package com.luxoft.highperformance.bookclient.model;


import lombok.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Getter @Setter @ToString
public class Book {
    private Integer id;
    private String title;
    private String keyword1;
    private String keyword2;
    private String keyword3;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return title != null ? title.equals(book.title) : book.title == null;
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }


    public byte[] toBinary() {
        ByteBuffer buffer = ByteBuffer.allocate(
                Long.BYTES +
                        Integer.BYTES +
                        Integer.BYTES +
                        title.length() +
                        Integer.BYTES +
                        keyword1.length() +
                        Integer.BYTES +
                        keyword2.length() +
                        Integer.BYTES +
                        keyword3.length());
        buffer.putInt(id);
        buffer.putInt(title.length());
        buffer.put(title.getBytes());
        buffer.putInt(keyword1.length());
        buffer.put(keyword1.getBytes());
        buffer.putInt(keyword2.length());
        buffer.put(keyword2.getBytes());
        buffer.putInt(keyword3.length());
        buffer.put(keyword3.getBytes());
        return buffer.array();
    }

    public static Book fromBinary(ByteBuffer buffer) {
        Book b = new Book();
        long id = buffer.getInt();

        int lengthTitle = buffer.getInt();
        byte[] titleBytes = new byte[lengthTitle];
        buffer.get(titleBytes);
        b.setTitle(new String(titleBytes, StandardCharsets.UTF_8));

        int lengthKeyword1 = buffer.getInt();
        byte[] keyword1Bytes = new byte[lengthKeyword1];
        buffer.get(keyword1Bytes);
        b.setKeyword1(new String(keyword1Bytes, StandardCharsets.UTF_8));

        int lengthKeyword2 = buffer.getInt();
        byte[] keyword2Bytes = new byte[lengthKeyword2];
        buffer.get(keyword2Bytes);
        b.setKeyword2(new String(keyword2Bytes, StandardCharsets.UTF_8));

        int lengthKeyword3 = buffer.getInt();
        byte[] keyword3Bytes = new byte[lengthKeyword3];
        buffer.get(keyword3Bytes);
        b.setKeyword3(new String(keyword3Bytes, StandardCharsets.UTF_8));

        return b;
    }

    public static Book fromBinary(byte[] bytes) {
        Book b = new Book();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long id = buffer.getInt();

        int lengthTitle = buffer.getInt();
        byte[] titleBytes = new byte[lengthTitle];
        buffer.get(titleBytes);
        b.setTitle(new String(titleBytes, StandardCharsets.UTF_8));

        int lengthKeyword1 = buffer.getInt();
        byte[] keyword1Bytes = new byte[lengthKeyword1];
        buffer.get(keyword1Bytes);
        b.setKeyword1(new String(keyword1Bytes, StandardCharsets.UTF_8));

        int lengthKeyword2 = buffer.getInt();
        byte[] keyword2Bytes = new byte[lengthKeyword2];
        buffer.get(keyword2Bytes);
        b.setKeyword2(new String(keyword2Bytes, StandardCharsets.UTF_8));

        int lengthKeyword3 = buffer.getInt();
        byte[] keyword3Bytes = new byte[lengthKeyword3];
        buffer.get(keyword3Bytes);
        b.setKeyword3(new String(keyword3Bytes, StandardCharsets.UTF_8));

        return b;
    }

}
