import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String args[]){
//        JSONObject jsonObject = new JSONObject();
//        JSONObject jsonObject1 = new JSONObject();
//        jsonObject1.append("hello", "11");
//        jsonObject.append("hi", jsonObject1);
//        System.out.println(jsonObject.toString());
        List<Integer> hello = new ArrayList<>();
        hello.add(1);
        hello.add(2);
        hello.add(3);
        String categoryShort =" " + StringUtils.join(hello, " , ") + " ";
        System.out.println(categoryShort);
    }
}
