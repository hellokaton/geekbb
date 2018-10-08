package io.github.biezhi.geekbb.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数组工具类
 *
 * @author biezhi
 * @date 2017/8/2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayUtils {

    public static long[] append(long[] arr, long element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }

    public static long[] remove(long[] arr, long el) {
        if (null == arr || arr.length == 0) {
            return arr;
        }
        List<Long> longs = new ArrayList<>(arr.length - 1);
        for (long l : arr) {
            if (l != el) {
                longs.add(l);
            }
        }
        long[] newArr = new long[longs.size()];
        for (int i = 0, len = newArr.length; i < len; i++) {
            newArr[i] = longs.get(i);
        }
        return newArr;
    }

}