package bk.edu.utils;

import bk.edu.config.Config;
import bk.edu.storage.ElasticStorage;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElasticUtils {
    private final ElasticStorage elasticStore;

    public ElasticUtils() {
        ElasticStorage elasticStorage = new ElasticStorage();
        this.elasticStore = elasticStorage;
    }

    public String getLongHobby(Integer userId){
        List<Integer> categories = new ArrayList<>();
        try {
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        bool.must(QueryBuilders.termQuery("user_id", userId));

        Script script = new
                Script("doc['product.category_id']");
        sourceBuilder.size(0);
        sourceBuilder.query(bool);
        sourceBuilder.aggregation(
                AggregationBuilders.terms("hobby").script(script).size(Config.LIMIT_LONG_HOBBY)
        );

        SearchRequest request = new SearchRequest()
                .indices(Config.ELASTIC.TRACKING_INDEX)
                .source(sourceBuilder);
        System.out.println("req:"+sourceBuilder.toString());
        SearchResponse response = elasticStore.getClient().search(request, ElasticStorage.COMMON_OPTIONS);

        Terms hobbies = response.getAggregations().get("hobby");
        hobbies.getBuckets().forEach(category -> {
            String categoryId = category.getKeyAsString();
            categories.add(Integer.parseInt(categoryId));
        });

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("loi es");
        }

        String longHobbies =" " + StringUtils.join(categories, " , ") + " ";

        System.out.println(longHobbies);

        return longHobbies;
    }

    public static void main(String args[]){
        ElasticUtils elasticUtils = new ElasticUtils();
        elasticUtils.getLongHobby(1533);
        elasticUtils.close();
    }

    public void close() {
        elasticStore.close();
    }
}
