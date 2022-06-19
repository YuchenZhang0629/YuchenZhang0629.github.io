package com.ucla.module5.demo04;

import java.util.Arrays;

public class Demo02Practice {
    public static void main(String[] args) {
        String str = "raho1vnouft8092nv453oa3ep4owmv";
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        for (int i = 0; i < chars.length; i++) {
            System.out.println(chars[chars.length-1-i]);
        }
    }
}
