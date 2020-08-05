package com.emoke.core.auxiliary.util;

public class TextUtils {
    public static boolean isEmpty(String s){
        return s == null ||"".equals(s);
    }

    public static void main(String[] args) {
        System.out.println(isEmpty("")+"");
    }
}
