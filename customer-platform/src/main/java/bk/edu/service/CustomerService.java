package bk.edu.service;

import bk.edu.data.entity.CustomerEntity;
import bk.edu.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public CustomerEntity getCustomer(int id){
        return customerRepository.findByUserId(id);
    }
}
