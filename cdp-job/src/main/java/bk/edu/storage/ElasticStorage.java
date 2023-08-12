package bk.edu.storage;

import bk.edu.config.Config;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.concurrent.TimeUnit;

public class ElasticStorage {
    private RestHighLevelClient client;
    private BulkProcessor bulkProcessor = null;
    public static RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory
                        .HeapBufferedResponseConsumerFactory(200 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    public ElasticStorage() {
        HttpHost[] httpHosts = null;
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        try {

            String[] hosts = Config.ELASTIC.HOSTS;
            String port = Config.ELASTIC.PORT;
            String user = Config.ELASTIC.USER;
            String pass = Config.ELASTIC.PASSWORD;

            int numHost = hosts.length;
            httpHosts = new HttpHost[numHost];
            for (int i = 0; i < numHost; i++) {
                httpHosts[i] = new HttpHost(hosts[i], Integer.parseInt(port), "http");
            }
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pass));

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }

        this.client = new RestHighLevelClient(
                RestClient
                        .builder(httpHosts)
                        .setRequestConfigCallback(
                                builder -> builder
                                        .setConnectTimeout(2 * 60 * 1000)
                                        .setSocketTimeout(5 * 60 * 1000)
                                        .setConnectionRequestTimeout(0)
                        )
                        .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider))
        );

    }

    public BulkProcessor getBulk() {

        if (this.bulkProcessor == null){
            BulkProcessor.Listener listener = new BulkProcessor.Listener() {
                @Override
                public void beforeBulk(long l, BulkRequest bulkRequest) {
                    System.out.println("elastic search before bulk size: " + bulkRequest.numberOfActions());
                }

                @Override
                public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {

                }

                @Override
                public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {

                }
            };
            this.bulkProcessor = BulkProcessor.builder(
                            (request, bulkListener) ->
                                    client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                            listener)
                    .setBulkActions(2000)
                    .setBulkSize(new ByteSizeValue(10L, ByteSizeUnit.MB))
                    .setConcurrentRequests(1)
                    .setFlushInterval(TimeValue.timeValueMinutes(1))
                    .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueMinutes(5), 3))
                    .build();
        }

        return this.bulkProcessor;
    }

    public RestHighLevelClient getClient() {
        return client;
    }

    public void close() {
        try {
            if (this.bulkProcessor != null) {
                bulkProcessor.awaitClose(10, TimeUnit.SECONDS);
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
