package bk.edu.job;

import bk.edu.data.model.SegmentInfo;
import bk.edu.utils.MySqlUtils;
import bk.edu.utils.SparkUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

public class SegmentAllUser {
    protected static SparkUtils sparkUtil;

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
        df.show();
    }
}
