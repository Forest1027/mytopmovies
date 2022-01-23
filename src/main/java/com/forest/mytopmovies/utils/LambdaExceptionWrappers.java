package com.forest.mytopmovies.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LambdaExceptionWrappers {
    private static final Logger LOGGER = LoggerFactory.getLogger(LambdaExceptionWrappers.class);

    private LambdaExceptionWrappers() {
    }

    public static <T, E extends Exception> Consumer<T> consumerWrapper(Consumer<T> consumer, Class<E> clazz) {
        return i -> {
            try {
                consumer.accept(i);
            } catch (Exception ex) {
                try {
                    E exCast = clazz.cast(ex);
                    LOGGER.error(String.format("Exception occurred : %s", exCast.getMessage()));
                } catch (ClassCastException ccEx) {
                    throw ex;
                }
            }
        };
    }

    public static <T, E extends Exception> Supplier<T> supplierWrapper(Supplier<T> supplier, Class<E> clazz) {
        return () -> {
            try {
                return supplier.get();
            } catch (Exception ex) {
                try {
                    E exCast = clazz.cast(ex);
                    LOGGER.error(String.format("Exception occurred : %s", exCast.getMessage()));
                } catch (ClassCastException ccEx) {
                    throw ex;
                }
            }
            return null;
        };
    }
}
