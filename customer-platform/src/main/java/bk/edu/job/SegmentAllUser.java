package bk.edu.job;

import bk.edu.data.model.ConditionInfo;
import bk.edu.data.model.SegmentInfo;
import bk.edu.utils.MySqlUtils;
import bk.edu.utils.SparkUtils;
import bk.edu.utils.TransformUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;

import java.util.List;

public class SegmentAllUser {
    protected SparkUtils sparkUtil;

    public SegmentAllUser(){
        sparkUtil = new SparkUtils("segment all user", true, true);
    }
    public static void main(String args[]){
        SegmentAllUser segmentAllUser = new SegmentAllUser();
        segmentAllUser.process();
    }

    private void process() {
        MySqlUtils mySqlUtils = new MySqlUtils();
        List<SegmentInfo> segments = mySqlUtils.getAllSegment();
        mySqlUtils.close();

        Dataset<Row> df = sparkUtil.getTableDataframe("bookshop_customer");
        df.persist(StorageLevel.MEMORY_ONLY());
        df.show();
        segments.forEach(segmentInfo -> {
            System.out.println(segmentInfo.getSegmentId());
            linkSegmentAndUser(segmentInfo, df);
        });
        df.unpersist();
    }

    private void linkSegmentAndUser(SegmentInfo segmentInfo, Dataset<Row> df){
        Dataset<Row> filterDf = df;
        List<ConditionInfo> conditions = segmentInfo.getConditions();
        if(conditions.size() == 0){
        } else {
            filterDf = TransformUtils.filterCondition(conditions.get(0), df);
            for(int i = 1; i < conditions.size(); i++){
                filterDf = TransformUtils.filterCondition(conditions.get(i), filterDf);
            }
        }
        filterDf.show();
    }
}
