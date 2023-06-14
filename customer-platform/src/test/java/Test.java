import org.json.JSONObject;

public class Test {
    public static void main(String args[]){
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.append("hello", "11");
        jsonObject.append("hi", jsonObject1);
        System.out.println(jsonObject.toString());
    }
}
