package com.example.demo.Service;

//public class CustomerService {
//}

import com.example.demo.Model.Customer;
import com.example.demo.Repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    public ResponseEntity<List<Customer>> getAllCustomers(Long id) {
        try {
            List<Customer> customers = new ArrayList<>();
            if (id == null) {
                customerRepo.findAll().forEach(customers::add);
            } else {
                customerRepo.findByUserId(id).forEach(customers::add);
            }

            if (customers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Customer> getCustomerById(long id) {
        Optional<Customer> customerData = customerRepo.findById(id);
        if (customerData.isPresent()) {
            return new ResponseEntity<>(customerData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Customer> createCustomer(Customer customer) {
        try {
            Customer _customer = customerRepo.save(new Customer(customer.getUserId(), customer.getName(), customer.getEmail(), customer.getPassword()));
            return new ResponseEntity<>(_customer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
