package bk.edu.data.model;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * Thuộc tính của 1 bảng event
 * <br>
 * <a href="https://docs.snowplow.io/docs/understanding-your-pipeline/canonical-event/#overview">Structure data Snowplow</a>
 */
@Getter
@Setter
public class EventKafka {
    //Application fields
    /**
     * Application ID
     */
    private String app_id;
    /**
     * Platform. Example: "web"
     */
    private String platform;

    //Date / time fields
    /**
     * Time event được lưu bởi collector
     */
    private long collector_tstamp;
    /**
     * Time event được tạo từ thiết bị client
     */
    private long dvce_created_tstamp;
    /**
     * Time event được gửi từ thiết bị client
     */
    private long dvce_sent_tstamp;
    /**
     * Time khi event được validated và enriched
     */
    private long etl_tstamp;
    private String os_timezone;
    private long derived_tstamp;
    private long true_tstamp;

    //Event / transaction fields
    /**
     * Loại event
     */
    private String event;
    /**
     * Event ID
     */
    private String event_id;
    /**
     * Transaction ID
     */
    private int txn_id;
    private String event_fingerprint;

    //Snowplow version fields
    /**
     * Tracker namespace
     */
    private String name_tracker;
    /**
     * Tracker version
     */
    private String v_tracker;
    /**
     * Collector version. Example: "ssc-2.1.0-kinesis"
     */
    private String v_collector;
    /**
     * ETL version. Example: "snowplow-micro-1.1.0-common-1.4.2"
     */
    private String v_etl;
    private String etl_tags;

    //User-related fields
    /**
     * Unique ID được tạo bởi business
     */
    private String user_id;
    /**
     * User ID - định danh bên thứ nhất 1st
     */
    private String domain_userid;
    /**
     * User ID - định danh bên thứ ba 3rd
     */
    private String network_userid;
    /**
     * User IP address
     */
    private String user_ipaddress;
    /**
     * Số lần thăm / session cookie. Session cookie tính từ lúc tạo cho đến lúc cookie thay đổi
     */
    private int domain_sessionIdx;
    private String domain_sessionid;


    //Device and operating system fields
    private String useragent;
    /**
     * Type of device. Example: 'Computer'
     */
    private String dvce_type;
    private boolean dvce_ismobile;
    private int dvce_screenwidth;
    private int dvce_screenheight;
    private String os_name;
    private String os_family;
    private String os_manufacturer;

    //Location fields
    private String geo_country;
    private String geo_region;
    private String geo_city;
    private String geo_zipcode;
    private String geo_latitude;
    private String geo_longitude;
    private String geo_region_name;
    private String geo_timezone;


    //IP address-based fields
    private String ip_isp;
    private String ip_organization;
    private String ip_domain;
    private String ip_netspeed;


    //Metadata fields
    private String event_vendor;
    private String event_name;
    private String event_format;
    private String event_version;


    //Web-specific fields
    /**
     * Page url
     */
    private String page_url;
    /**
     * Protocal
     */
    private String page_urlscheme;
    /**
     * Domain
     */
    private String page_urlhost;
    /**
     * Port (mặc định là 80)
     */
    private int page_urlport;
    /**
     * Đường dẫn đến page. Example: '/product/index.html'
     */
    private String page_urlpath;
    /**
     * Query String (giống truyền tham số trong phương thức GET). Example: 'id=GTM-DLRG'
     */
    private String page_urlquery;
    /**
     *
     */
    private String page_urlfragment;
    /**
     * URL tham chiếu đến
     */
    private String page_referrer;
    /**
     * Web page title. Example: 'Snowplow Docs - Understanding the structure of Snowplow data'
     */
    private String page_title;
    /**
     * Referer protocal
     */
    private String refr_urlscheme;
    /**
     * Referer host
     */
    private String refr_urlhost;
    private int refr_urlport;
    private String refr_urlpath;
    private String refr_urlquery;
    private String refr_urlfragment;
    private String refr_medium;
    private String refr_source;
    private String refr_term;
    private String refr_domain_userid;
    private long refr_device_tstamp;

    //Document fields
    /**
     * Mã hóa ký tự của trang
     */
    private String doc_charset;
    private int doc_width;
    private int doc_height;

    //Marketing / traffic source fields
    /**
     * Loại nguồn truy cập
     */
    private String mkt_medium;
    /**
     * Nơi truy cập dến từ. Example: 'Google', 'Facebook'
     */
    private String mkt_source;
    /**
     *
     */
    private String mkt_term;
    /**
     * Nội dung quảng cáo
     */
    private String mkt_content;
    /**
     * Camppaign id
     */
    private String mkt_campaign;
    private String mkt_clickid;
    private String mkt_network;
    private JSONObject contexts;

    //Browser fields
    /**
     * được tạo bằng cách xem các tính năng của trình duyệt
     */
    private String user_fingerprint;
    /**
     * Browser name
     */
    private String br_name;
    /**
     * Browser family
     */
    private String br_family;
    /**
     * Browser version
     */
    private String br_version;
    private String br_type;
    /**
     * Browser rendering engine
     */
    private String br_renderengine;
    /**
     * Ngôn ngữ của browser
     */
    private String br_lang;
    /**
     * Trình duyệt có nhận ra tệp PDF hay không
     */
    private boolean br_features_pdf;
    /**
     * Flash được cài đặt hay không
     */
    private boolean br_features_flash;
    /**
     * Java được cài đặt hay không
     */
    private boolean br_features_java;
    /**
     * Adobe Shockwave đã được cài đặt chưa
     */
    private boolean br_features_director;
    private boolean br_features_quicktime;
    private boolean br_features_realplayer;
    private boolean br_features_windowsmedia;
    private boolean br_features_gears;
    private boolean br_features_silverlight;
    /**
     * Cookies được cho phép hay không
     */
    private boolean br_cookies;
    /**
     * Độ sâu bit của bảng màu trình duyệt
     */
    private int br_colordepth;
    private int br_viewwidth;
    private int br_viewheight;


    //Custom structured events
    /**
     * Category of event. Example: 'ecomm', 'video'
     */
    private String se_category;
    /**
     * Action performed / event name. Example: 'add-to-basket', 'play-video'
     */
    private String se_action;
    private String se_label;
    private String se_property;
    private String se_value;
    private JSONObject unstruct_event;

    //E-commerce transactions
    /**
     * Order ID
     */
    private String tr_orderid;
    /**
     * Nơi diễn ra transaction. Example: 'web'
     */
    private String tr_affiliation;
    /**
     * Tổng giá trị transaction
     */
    private double tr_total;
    /**
     * Tổng thuế trong transaction
     */
    private double tr_tax;
    /**
     * Phí ship
     */
    private double tr_shipping;
    /**
     * Tổng số tiền cơ bản
     */
    private double tr_total_base;
    private double tr_tax_base;
    private double tr_shipping_base;
    /**
     * Địa chỉ giao hàng - Thành phố
     */
    private String tr_city;
    /**
     * Địa chỉ giao hàng - Huyện
     */
    private String tr_state;
    /**
     * Địa chỉ giao hàng - Nước
     */
    private String tr_country;
    /**
     * Loại tiền tệ
     */
    private String tr_currency;
    /**
     * Order id
     */
    private String ti_orderid;
    /**
     * Mã sản phẩm
     */
    private String ti_sku;
    /**
     * Tên sản phẩm
     */
    private String ti_name;
    /**
     * Loại sản phẩm
     */
    private String ti_category;
    /**
     * Giá sản phẩm
     */
    private double ti_price;
    private double ti_price_base;
    /**
     * Số lượng sản phẩm trong transaction
     */
    private int ti_quantity;
    private String ti_currency;
    private String base_currency;

    //Page pings
    private int pp_xoffset_min;
    private int pp_xoffset_max;
    private int pp_yoffset_min;
    private int pp_yoffset_max;
    private String derived_contexts;

    /**
     * Object đại diện cho 1 event lấy từ kafka
     * @param event_data event sau khi đã enrich
     */
    public EventKafka(String event_data) {
        parseData(event_data);
    }

    public void parseData(String event_data) {
        String[] propertyIndex = event_data.split("\t");

        app_id = propertyIndex[0];
        platform = propertyIndex[1];
        etl_tstamp = Timestamp.valueOf(propertyIndex[2]).getTime();
        collector_tstamp = Timestamp.valueOf(propertyIndex[3]).getTime();
        dvce_created_tstamp = Timestamp.valueOf(propertyIndex[4]).getTime();
        event = propertyIndex[5];
        event_id = propertyIndex[6];

        try {
            txn_id = Integer.parseInt(propertyIndex[7]);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        name_tracker = propertyIndex[8];
        v_tracker = propertyIndex[9];
        v_collector = propertyIndex[10];
        v_etl = propertyIndex[11];
        user_id = propertyIndex[12];
        user_ipaddress = propertyIndex[13];
        user_fingerprint = propertyIndex[14];
        domain_userid = propertyIndex[15];
        domain_sessionid = propertyIndex[16];
        network_userid = propertyIndex[17];
        geo_country = propertyIndex[18];
        geo_region = propertyIndex[19];
        geo_city = propertyIndex[20];
        geo_zipcode = propertyIndex[21];
        geo_latitude = propertyIndex[22];
        geo_longitude = propertyIndex[23];
        geo_region_name = propertyIndex[24];
        ip_isp = propertyIndex[25];
        ip_organization = propertyIndex[26];
        ip_domain = propertyIndex[27];
        ip_netspeed = propertyIndex[28];
        page_url = propertyIndex[29];
        page_title = propertyIndex[30];
        page_referrer = propertyIndex[31];
        page_urlscheme = propertyIndex[32];
        page_urlhost = propertyIndex[33];
        page_urlport = Integer.parseInt(propertyIndex[34]);
        page_urlpath = propertyIndex[35];
        page_urlquery = propertyIndex[36];
        page_urlfragment = propertyIndex[37];
        refr_urlscheme = propertyIndex[38];
        refr_urlhost = propertyIndex[39];
        try {
            refr_urlport = Integer.parseInt(propertyIndex[40]);
        } catch (Exception ignore) {
        }

        refr_urlpath = propertyIndex[41];
        refr_urlquery = propertyIndex[42];
        refr_urlfragment = propertyIndex[43];
        refr_medium = propertyIndex[44];
        refr_source = propertyIndex[45];
        refr_term = propertyIndex[46];
        mkt_medium = propertyIndex[47];
        mkt_source = propertyIndex[48];
        mkt_term = propertyIndex[49];
        mkt_content = propertyIndex[50];
        mkt_campaign = propertyIndex[51];
        System.out.println(propertyIndex[52]);
        contexts = new JSONObject(propertyIndex[52]);
        se_category = propertyIndex[53];
        se_action = propertyIndex[54];
        se_label = propertyIndex[55];
        se_property = propertyIndex[56];
        se_value = propertyIndex[57];

        try {
            System.out.println(propertyIndex[58]);
            unstruct_event = new JSONObject(propertyIndex[58]);
        } catch (Exception e) {
        }

        tr_orderid = propertyIndex[59];
        tr_affiliation = propertyIndex[60];
        try {
            tr_total = Double.parseDouble(propertyIndex[61]);
            tr_tax = Double.parseDouble(propertyIndex[62]);
            tr_shipping = Double.parseDouble(propertyIndex[63]);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        tr_city = propertyIndex[64];
        tr_state = propertyIndex[65];
        tr_country = propertyIndex[66];
        ti_orderid = propertyIndex[67];
        ti_sku = propertyIndex[68];
        ti_name = propertyIndex[69];
        ti_category = propertyIndex[70];

        try {
            ti_price = Double.parseDouble(propertyIndex[71]);
            ti_quantity = Integer.parseInt(propertyIndex[72]);
            pp_xoffset_min = Integer.parseInt(propertyIndex[73]);
            pp_xoffset_max = Integer.parseInt(propertyIndex[74]);
            pp_yoffset_min = Integer.parseInt(propertyIndex[75]);
            pp_yoffset_max = Integer.parseInt(propertyIndex[76]);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        useragent = propertyIndex[77];
        br_name = propertyIndex[78];
        br_family = propertyIndex[79];
        br_version = propertyIndex[80];
        br_type = propertyIndex[81];
        br_renderengine = propertyIndex[82];
        br_lang = propertyIndex[83];

        try {
            br_features_pdf = Boolean.parseBoolean(propertyIndex[84]);
            br_features_flash = Boolean.parseBoolean(propertyIndex[85]);
            br_features_java = Boolean.parseBoolean(propertyIndex[86]);
            br_features_director = Boolean.parseBoolean(propertyIndex[87]);
            br_features_quicktime = Boolean.parseBoolean(propertyIndex[88]);
            br_features_realplayer = Boolean.parseBoolean(propertyIndex[89]);
            br_features_windowsmedia = Boolean.parseBoolean(propertyIndex[90]);
            br_features_gears = Boolean.parseBoolean(propertyIndex[91]);
            br_features_silverlight = Boolean.parseBoolean(propertyIndex[92]);
            br_cookies = Boolean.parseBoolean(propertyIndex[93]);
            br_colordepth = Integer.parseInt(propertyIndex[94]);
            br_viewwidth = Integer.parseInt(propertyIndex[95]);
            br_viewheight = Integer.parseInt(propertyIndex[96]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        os_name = propertyIndex[97];
        os_family = propertyIndex[98];
        os_manufacturer = propertyIndex[99];
        os_timezone = propertyIndex[100];
        dvce_type = propertyIndex[101];
        try {
            dvce_ismobile = Boolean.parseBoolean(propertyIndex[102]);
            dvce_screenwidth = Integer.parseInt(propertyIndex[103]);
            dvce_screenheight = Integer.parseInt(propertyIndex[104]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        doc_charset = propertyIndex[105];

        doc_width = Integer.parseInt(propertyIndex[106]);
        doc_height = Integer.parseInt(propertyIndex[107]);
        tr_currency = propertyIndex[108];
        try {
            tr_total_base = Double.parseDouble(propertyIndex[109]);
            tr_tax_base = Double.parseDouble(propertyIndex[110]);
            tr_shipping_base = Double.parseDouble(propertyIndex[111]);
            ti_price_base = Double.parseDouble(propertyIndex[113]);
        } catch (Exception e) {

        }

        ti_currency = propertyIndex[112];

        base_currency = propertyIndex[114];
        geo_timezone = propertyIndex[115];
        mkt_clickid = propertyIndex[116];
        mkt_network = propertyIndex[117];
        etl_tags = propertyIndex[118];
        dvce_sent_tstamp = Timestamp.valueOf(propertyIndex[119]).getTime();
        refr_domain_userid = propertyIndex[120];
        try {
            refr_device_tstamp = Timestamp.valueOf(propertyIndex[121]).getTime();
        } catch (Exception e) {

        }

        derived_contexts = propertyIndex[122];
        domain_sessionid = propertyIndex[123];
        derived_tstamp = Timestamp.valueOf(propertyIndex[124]).getTime();
        event_vendor = propertyIndex[125];
        event_name = propertyIndex[126];
        event_format = propertyIndex[127];
        event_version = propertyIndex[128];
        event_fingerprint = propertyIndex[129];
        try {
            true_tstamp = Timestamp.valueOf(propertyIndex[130]).getTime();
        } catch (Exception e) {

        }

    }
}

