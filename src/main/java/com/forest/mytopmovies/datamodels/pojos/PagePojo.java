package com.forest.mytopmovies.datamodels.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PagePojo<T> {
    private Integer page;

    private Integer totalResults;

    private Integer totalPages;

    private List<T> results;
}
