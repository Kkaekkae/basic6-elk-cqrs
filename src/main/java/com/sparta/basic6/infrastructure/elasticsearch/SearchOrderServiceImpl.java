package com.sparta.basic6.infrastructure.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.json.JsonData;
import com.sparta.basic6.application.SearchOrderService;
import com.sparta.basic6.application.dtos.OrderSearchResponse;
import com.sparta.basic6.domain.Order;
import com.sparta.basic6.infrastructure.jpa.OrderElasticSearchRepository;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchOrderServiceImpl implements SearchOrderService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final OrderElasticSearchRepository orderElasticSearchRepository;

    public SearchOrderServiceImpl(ElasticsearchOperations elasticsearchOperations, OrderElasticSearchRepository orderElasticSearchRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.orderElasticSearchRepository = orderElasticSearchRepository;
    }


    public List<OrderSearchResponse> search(String productName, Long startPrice, Long endPrice) {
        NativeQueryBuilder query = NativeQuery.builder();
        BoolQuery.Builder boolQuery = QueryBuilders.bool();

        // startPrice 조건 추가
        if (startPrice != null && endPrice != null) {
            Query priceQuery = QueryBuilders.range(field -> field.field("totalPrice").gte(JsonData.of(startPrice)).lte(JsonData.of(endPrice)));
            boolQuery.must(priceQuery);
        }

        // 문자열 포함 쿼리
        QueryBuilders.queryString(field -> field.fields(List.of("totalPrice")).query("*%s*".formatted(productName)));

        query.withQuery(boolQuery.build()._toQuery());
        SearchHits<SearchOrder> hits = elasticsearchOperations.search(query.build(), SearchOrder.class);
        return hits.map(hit -> toDto(hit.getContent())).toList();
    }

    @Override
    public void create(Order order) {
        orderElasticSearchRepository.save(SearchOrder.of(order));
    }

    private static OrderSearchResponse toDto(SearchOrder order) {
        return OrderSearchResponse.builder()
                .orderId(order.getOrderId())
                .totalPrice(order.getTotalPrice())
                .products(order.getProducts().stream()
                        .map(SearchOrderServiceImpl::toDto)
                        .toList()
                )
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .expectedDeliveryStartDate(order.getExpectedStartDate())
                .expectedDeliveryEndDate(order.getExpectedEndDate())
                .build();
    }

    private static OrderSearchResponse.ProductResponse toDto(SearchOrder.SearchProduct product) {
        return OrderSearchResponse.ProductResponse.builder()
                .name(product.getName())
                .images(product.getImages())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
}
