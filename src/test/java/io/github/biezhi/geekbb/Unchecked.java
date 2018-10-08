package io.github.biezhi.geekbb;

import java.util.function.Function;

/**
 * @author biezhi
 * @date 2018/5/12
 */
public final class Unchecked {

    @FunctionalInterface
    interface CheckedFunction<T, R, EX extends Exception> {
        R apply(T element) throws EX;
    }

    @FunctionalInterface
    interface NoArgFn<T> {
        T apply() throws Exception;
    }


    public static <T, R> Function<T, R> function(CheckedFunction<T, R, Exception> function) {
        return element -> {
            try {
                return function.apply(element);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public static <T> T wrap(NoArgFn<T> f){
        try {
            return f.apply();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
