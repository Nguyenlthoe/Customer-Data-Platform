package bk.edu.data.mapper;

import bk.edu.config.Config;
import bk.edu.data.entity.CategoryEntity;
import bk.edu.data.entity.CustomerEntity;
import bk.edu.data.response.dto.CustomerDto;
import bk.edu.data.response.dto.RelationDto;
import bk.edu.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerMapper {

    @Autowired
    CategoryRepository categoryRepository;

    public CustomerDto customerEntityToDto(CustomerEntity customerEntity){
        CustomerDto customer = new CustomerDto(customerEntity.getUserId(), customerEntity.getName(),
                customerEntity.getEmail(), customerEntity.getPhoneNumber(), customerEntity.getGender(),
                customerEntity.getUrlAvatar(), customerEntity.getAvgBookValue(),
                customerEntity.getAvgBillValue(), customerEntity.getMinBillValue());
        if(customerEntity.getBirthday() != null){
            String birthDay = Config.FORMAT_DATE.format(customerEntity.getBirthday());
            customer.setBirthday(birthDay);
        }
        List<RelationDto> categories = new ArrayList<>();
        if(customerEntity.getHobby() != null){
            for(String categoryStr: customerEntity.getHobby().split(",")){
                try {
                    CategoryEntity category = categoryRepository.findByCategoryId(Integer.parseInt(categoryStr.trim()));
                    categories.add(new RelationDto(category.getCategoryId(), category.getName()));
                } catch (Exception e){

                }
            }
        }
        customer.setHobbies(categories);
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
