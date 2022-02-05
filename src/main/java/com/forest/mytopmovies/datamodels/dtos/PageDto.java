package com.forest.mytopmovies.datamodels.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Data
@Jacksonized
public class PageDto<T> {

    private final Integer page;

    private final Integer total_results;

    private final Integer total_pages;

    private final List<T> results;

}
