package bk.edu.job;

import bk.edu.config.ProvinceCode;
import bk.edu.data.response.dto.ProvinceDto;
import bk.edu.utils.MySqlUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mifmif.common.regex.Generex;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class VietnameseNameGenerator {
    private static final char[] SOURCE_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É',
            'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
            'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
            'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
            'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
            'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
            'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
            'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
            'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
            'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
            'ữ', 'Ự', 'ự',};

    private static final char[] DESTINATION_CHARACTERS = {'A', 'A', 'A', 'A', 'E',
            'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
            'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
            'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
            'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
            'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
            'U', 'u', 'U', 'u',};

    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    public static String removeAccent(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }

    private class UserInfo{
        String name;
        String email;
        String phoneNumber;
        Integer gender = 2;
        Integer provideCode;
        Long birthday;
    }

    private static final String[] MALE_FIRST_NAMES = {
            "Nguyễn", "Trần", "Lê", "Phạm", "Huỳnh", "Hoàng", "Phan", "Vũ", "Đặng", "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý"
            // Danh sách các họ người Việt phổ biến cho nam
    };

    private static final String[] FEMALE_FIRST_NAMES = {
            "Nguyễn", "Trần", "Lê", "Phạm", "Huỳnh", "Hoàng", "Phan", "Vũ", "Đặng", "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý"
            // Danh sách các họ người Việt phổ biến cho nữ
    };

    private static final String[] MIDDLE_NAMES = {
            "Thị", "Văn", "Xuân", "Ngọc", "Công", "Quang", "Vinh", "Hữu", "Hoàng", "Minh", "Đức"
            // Danh sách các tên đệm người Việt phổ biến
    };

    private static final String[] LAST_NAMES = {
            "Hải", "Linh", "Hùng", "Dũng", "Trung", "Mai", "Tú", "Thành", "Thu", "Phương", "Hiền", "Lan", "Ngọc", "Yến", "Nhung"
            // Danh sách các tên người Việt phổ biến
    };

    public UserInfo generateUser() {
        // Tạo một đối tượng Random để sinh số ngẫu nhiên
        Random random = new Random();

        // Sinh ngẫu nhiên giới tính
        boolean isMale = random.nextBoolean();

        // Sinh ngẫu nhiên các tên người Việt
        String firstName = "";
        if (isMale) {
            firstName = MALE_FIRST_NAMES[random.nextInt(MALE_FIRST_NAMES.length)];
        } else {
            firstName = FEMALE_FIRST_NAMES[random.nextInt(FEMALE_FIRST_NAMES.length)];
        }
        String middleName = MIDDLE_NAMES[random.nextInt(MIDDLE_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];

        // Kết hợp các phần tên để tạo tên đầy đủ
        String fullName = firstName + " " + middleName + " " + lastName;
        // In ra tên người Việt sinh ngẫu nhiên
        System.out.println("Tên người Việt: " + fullName);

        UserInfo user = new UserInfo();
        user.name = fullName;
        if(isMale){
            user.gender = 1;
        }
        user.birthday = getRandomBirthday();
        user.email = removeAccent(lastName).toLowerCase()
                + removeAccent(firstName + middleName).toLowerCase()
                + new SimpleDateFormat("yyyyddMM").format(new Date(user.birthday))
                + "@gmail.com";
        Generex generex = new Generex("(0)(3[2-9]5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}");

        generex.getMatchedString(2);
        user.phoneNumber = generex.random();

        user.provideCode = ProvinceCode.provinceCode[random.nextInt(63)];

        return user;
    }
    public void insertUser(List<UserInfo> user){
        String sql = "INSERT INTO `customer-data-platform`.`bookshop_customer` (`birthday`, `created_at`, `email`, `gender`, `name`, `phone_number`, `updated_at`, `province_code`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        MySqlUtils mySqlUtils = new MySqlUtils();
        Connection connection = mySqlUtils.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for(int i = 0; i < user.size(); i++){
                UserInfo userr = user.get(i);
                Timestamp birthday = new Timestamp(userr.birthday);
                Timestamp timeNow = new Timestamp(System.currentTimeMillis());
                preparedStatement.setTimestamp(1, birthday);
                preparedStatement.setTimestamp(2, timeNow);
                preparedStatement.setString(3, userr.email);
                preparedStatement.setInt(4, userr.gender);
                preparedStatement.setString(5, userr.name);
                preparedStatement.setString(6, userr.phoneNumber);
                preparedStatement.setTimestamp(7, timeNow);
                preparedStatement.setInt(8, userr.provideCode );
                preparedStatement.execute();
            }

            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        mySqlUtils.close();
    }

    public Long getRandomBirthday(){
        Random random = new Random();
        Calendar calendar = Calendar.getInstance();
        int age = random.nextInt(20) + 12;
        int month = random.nextInt(11);
        int day = random.nextInt(30);
        for(int i = 0; i < age; i++){
            calendar.add(Calendar.YEAR, -1);
        }
        for(int i = 0; i < month; i++){
            calendar.add(Calendar.MONTH, -1);
        }
        for(int i = 0; i < day; i++){
            calendar.add(Calendar.DATE, -1);
        }
        return calendar.getTimeInMillis();
    }
    public static void main(String[] args) throws JsonProcessingException {
//        VietnameseNameGenerator vietnameseNameGenerator = new VietnameseNameGenerator();
//        List<UserInfo> users = new ArrayList<>();
//        for(int i = 0; i < 1000; i++){
//            users.add(vietnameseNameGenerator.generateUser());
//        }
//        vietnameseNameGenerator.insertUser(users);
        MySqlUtils mySqlUtils = new MySqlUtils();
        mySqlUtils.getTime();
        mySqlUtils.close();
        System.out.println(System.currentTimeMillis());
    }
}