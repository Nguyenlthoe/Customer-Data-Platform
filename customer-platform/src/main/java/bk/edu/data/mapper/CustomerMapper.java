package bk.edu.data.mapper;

import bk.edu.config.Config;
import bk.edu.config.ProvinceCode;
import bk.edu.data.entity.CategoryEntity;
import bk.edu.data.entity.CustomerEntity;
import bk.edu.data.response.dto.CustomerDto;
import bk.edu.data.response.dto.RelationDto;
import bk.edu.repository.CategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerMapper {

    @Autowired
    CategoryRepository categoryRepository;

    public CustomerDto customerEntityToDto(CustomerEntity customerEntity){
        String provinceName = "";
        if(ProvinceCode.provinceCodeList.contains(customerEntity.getProvinceCode())){
            provinceName = ProvinceCode.provinceName[ProvinceCode.provinceCodeList.indexOf(customerEntity.getProvinceCode())];
        }
        CustomerDto customer = new CustomerDto(customerEntity.getUserId(), customerEntity.getName(),
                customerEntity.getEmail(), customerEntity.getPhoneNumber(), customerEntity.getGender(),
                customerEntity.getUrlAvatar(), customerEntity.getAvgBookValue(),
                customerEntity.getAvgBillValue(), customerEntity.getMinBillValue(), customerEntity.getTotalBookView(), provinceName);
        if(customerEntity.getBirthday() != null){
            String birthDay = Config.FORMAT_DATE.format(customerEntity.getBirthday());
            customer.setBirthday(birthDay);
        }
        List<String> categories = new ArrayList<>();
        if(customerEntity.getHobby() != null){
            for(String categoryStr: customerEntity.getHobby().split(",")){
                try {
                    CategoryEntity category = categoryRepository.findByCategoryId(Integer.parseInt(categoryStr.trim()));
                    categories.add(category.getName());
                } catch (Exception e){

                }
            }
        }
        customer.setHobbies(StringUtils.join(categories, ", "));

        List<String> categories2 = new ArrayList<>();
        if(customerEntity.getShortHobby() != null){
            for(String categoryStr: customerEntity.getHobby().split(",")){
                try {
                    CategoryEntity category = categoryRepository.findByCategoryId(Integer.parseInt(categoryStr.trim()));
                    categories2.add(category.getName());
                } catch (Exception e){

                }
            }
        }
        customer.setShortHobbies(StringUtils.join(categories2, ", "));
        return customer;
    }

    public List<CustomerDto> listCustomerEntityToDto(List<CustomerEntity> customerEntities){
        List<CustomerDto> customers = new ArrayList<>();
        customerEntities.forEach(customerEntity -> {
            customers.add(customerEntityToDto(customerEntity));
        });
        return customers;
    }
}
