package com.greenfrvr.hashtagview;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by greenfrvr
 */
final class SortUtil {

    private SortUtil() {
        throw new AssertionError();
    }

    public static <T> void symmetricSort(List<T> list) {
        @SuppressWarnings("unchecked") T[] array = (T[]) new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if (i % 2 == 0) {
                array[(list.size() - i) / 2 + (list.size() % 2 - 1)] = list.get(i);
            } else {
                array[(list.size() + i) / 2] = list.get(i);
            }
        }

        int j = 0;
        ListIterator<T> it = list.listIterator();
        while (it.hasNext()) {
            it.next();
            it.set(array[j++]);
        }
    }
}
