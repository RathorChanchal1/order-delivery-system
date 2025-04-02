

package com.example.demo.Service;

import com.example.demo.Model.Shopping;
import com.example.demo.Repository.ShoppingRepo;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Service
//public class ShoppingService {
//
//    @Autowired
//    private ShoppingRepo shoppingRepo;
//
//    public List<Shopping> getAllProducts(Long id) {
//        List<Shopping> shopings = new ArrayList<>();
//        if (id == null) {
//            shopings.addAll(shoppingRepo.findAll());
//        } else {
//            shopings.addAll(shoppingRepo.findByproductId(id));
//        }
//        return shopings;
//    }
//
//    public Optional<Shopping> getProductById(long id) {
//        return shoppingRepo.findById(id);
//    }
//
//    public Shopping createProduct(Shopping customer) {
//        if (customer.isShopNotShop()) {
//            return shoppingRepo.save(new Shopping(customer.getProductId(), customer.getProductName(), customer.getProductPrice(), true));
//        } else {
//            return shoppingRepo.save(new Shopping(customer.getProductId(), customer.getProductName(), customer.getProductPrice(), false));
//        }
//    }
//
//    public List<Shopping> findByshopNotShop() {
//        return shoppingRepo.findByshopNotShop(true);
//    }
//}


@Service
public class ShoppingService {

    @Autowired
    private ShoppingRepo shoppingRepo;

    public List<Shopping> getAllProducts(Long id) {
        if (id == null) {
            return shoppingRepo.findAll();
        } else {
            return shoppingRepo.findByProductId(id);
        }
    }

    public Optional<Shopping> getProductById(long id) {
        return shoppingRepo.findById(id);
    }

    public Shopping createProduct(Shopping product) {
        return shoppingRepo.save(product);
    }

    public List<Shopping> findByShopNotShop() {
        return shoppingRepo.findByShopNotShop(true);
    }


    //16 sept update
    public Shopping updateProduct(Shopping product) {
        Optional<Shopping> productDb = this.shoppingRepo.findById(product.getProductId());

        if(productDb.isPresent()) {
            Shopping productUpdate = productDb.get();
            productUpdate.setProductId(product.getProductId());
            productUpdate.setProductName(product.getProductName());
            productUpdate.setProductPrice(product.getProductPrice());
            productUpdate.setShopNotShop(product.isShopNotShop());

            shoppingRepo.save(productUpdate);
            return productUpdate;
        }else {
            throw new ResourceNotFoundException("Record not found with id : " + product.getProductId());
        }
    }
}

