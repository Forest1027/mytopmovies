package com.forest.mytopmovies.datamodels.pojos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PagePojo<T> {
    private final Integer page;

    private final Integer totalResults;

    private final Integer totalPages;

    private final List<T> results;
}
