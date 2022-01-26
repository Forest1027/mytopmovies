package com.forest.mytopmovies.datamodels.dtos;

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
public class PageDto<T> {

    private Integer page;

    private Integer total_results;

    private Integer total_pages;

    private List<T> results;

}
