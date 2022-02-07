package com.forest.mytopmovies.utils;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LambdaExceptionWrappersUnitTest {

    @Test
    void canSupplierWrapperConsumeException() {
        // given

        // when
        Supplier<Integer> supplier = LambdaExceptionWrappers.supplierWrapper(() -> 1 / 0, RuntimeException.class);

        // then
        assertThat(supplier.get()).isNull();
    }
}