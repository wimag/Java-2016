package com.spbau.wimag.hw3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 21.03.2016.
 */
public class Collections {
    public static <U, T> List<T> map(Function1<? super U, T> func, Iterable<U> collection) {
        List<T> result = new ArrayList<>();
        for (U item : collection) {
            result.add(func.apply(item));
        }
        return result;
    }

    public static <T> List<T> filter(Predicate<? super T> pred, Iterable<T> collection) {
        List<T> result = new ArrayList<>();
        for (T item : collection) {
            if (pred.apply(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public static <T> List<T> takeWhile(Predicate<? super T> pred, Iterable<T> collection) {
        List<T> result = new ArrayList<>();
        for (T item : collection) {
            if (!pred.apply(item)) {
                break;
            }
            result.add(item);
        }
        return result;
    }

    public static <T> List<T> takeUnless(Predicate<? super T> pred, Iterable<T> collection) {
        return takeWhile(pred.not(), collection);
    }

    public static <T, U> U foldr(Function2<? super T, ? super U, U> func, U init, Iterable<T> collection) {
        List<T> listRepresentation = new ArrayList<>();
        for (T item : collection) {
            listRepresentation.add(item);
        }
        //Who the hell names package "Collections"? Seriously? =)
        java.util.Collections.reverse(listRepresentation);

        U result = init;
        for (T item : listRepresentation) {
            result = func.apply(item, result);
        }

        return result;
    }

    public static <T, U> T foldl(Function2<? super T, ? super U, T> func, T init, Iterable<U> collection) {
        T result = init;
        for (U item : collection) {
            result = func.apply(result, item);
        }
        return result;
    }
}
