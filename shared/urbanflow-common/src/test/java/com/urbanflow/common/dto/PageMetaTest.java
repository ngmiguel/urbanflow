package com.urbanflow.common.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PageMetaTest {

    @Test
    void shouldExposePaginationFields() {
        PageMeta meta = new PageMeta(0, 20, 42, 3);

        assertEquals(0, meta.page());
        assertEquals(20, meta.size());
        assertEquals(42, meta.totalElements());
        assertEquals(3, meta.totalPages());
    }
}
