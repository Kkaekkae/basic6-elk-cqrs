package com.sparta.basic6.infrastructure.jpa;

import com.sparta.basic6.infrastructure.elasticsearch.SearchOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OrderElasticSearchRepository extends ElasticsearchRepository<SearchOrder, Long> {
}
