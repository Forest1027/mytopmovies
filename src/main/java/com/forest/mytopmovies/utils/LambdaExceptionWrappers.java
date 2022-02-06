package com.forest.mytopmovies.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class LambdaExceptionWrappers {
    private static final Logger LOGGER = LoggerFactory.getLogger(LambdaExceptionWrappers.class);

    private LambdaExceptionWrappers() {
    }

    public static <T, E extends Exception> Supplier<T> supplierWrapper(Supplier<T> supplier, Class<E> clazz) {
        return () -> {
            try {
                return supplier.get();
            } catch (Exception ex) {
                try {
                    E exCast = clazz.cast(ex);
                    LOGGER.error("Exception occurred : {}", exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw ex;
                }
            }
            return null;
        };
    }
}
