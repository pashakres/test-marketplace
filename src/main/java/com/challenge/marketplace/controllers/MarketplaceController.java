package com.challenge.marketplace.controllers;

import com.challenge.marketplace.exceptions.PurchaseException;
import com.challenge.marketplace.models.Product;
import com.challenge.marketplace.models.Purchase;
import com.challenge.marketplace.models.User;
import com.challenge.marketplace.repo.ProductRepository;
import com.challenge.marketplace.repo.PurchaseRepository;
import com.challenge.marketplace.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class MarketplaceController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;

    @GetMapping("/product")
    public String getAllProducts(Model model) {
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products";
    }
    @GetMapping("/product/add")
    public String productAdd(Model model) {
        return "product-add";
    }
    @PostMapping("/product/add")
    public String postProductAdd(@RequestParam String name, @RequestParam Double price, Model model) {
        Product product = new Product(name, price);
        productRepository.save(product);
        return "redirect:/";
    }

    @GetMapping("/user")
    public String getAllUsers(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }
    @GetMapping("/user/add")
    public String userAdd(Model model) {
        return "user-add";
    }
    @PostMapping("/user/add")
    public String postUserAdd(@RequestParam String firstName, @RequestParam String lastName,
                              @RequestParam Double amountOfMoney, Model model) {
        User user = new User(firstName, lastName, amountOfMoney);
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/user/delete")
    public String userDelete(Model model) {
        return "user-delete";
    }
    @PostMapping("/user/delete")
    public String postUserDelete(@RequestParam Long userId, Model model) {
        userRepository.deleteById(userId);
        // Delete every purchase with this user
        for (Purchase purchase: purchaseRepository.findByUserId(userId)) {
            purchaseRepository.deleteById(purchase.getId());
        }
        return "user-delete";
    }

    @GetMapping("/product/delete")
    public String productDelete(Model model) {
        return "product-delete";
    }
    @PostMapping("/product/delete")
    public String postProductDelete(@RequestParam Long productId, Model model) {
        productRepository.deleteById(productId);
        // Delete every purchase with this product
        for (Purchase purchase: purchaseRepository.findByProductId(productId)) {
            purchaseRepository.deleteById(purchase.getId());
        }
        return "product-delete";
    }

    @GetMapping("/purchase")
    public String getAllPurchases(Model model) {
        Iterable<Purchase> purchases = purchaseRepository.findAll();
        model.addAttribute("purchases", purchases);
        return "purchases";
    }

    @GetMapping("/purchase/byuser")
    public String getUserPurchases(Model model) {
        return "user-purchases";
    }
    @PostMapping("/purchase/byuser")
    public String postUserPurchases(@RequestParam Long userId, Model model) {
        Iterable<Purchase> purchases = purchaseRepository.findByUserId(userId);
        model.addAttribute("purchases", purchases);
        return "user-purchases";
    }

    @GetMapping("/purchase/byproduct")
    public String getProductPurchases(Model model) {
        return "product-purchases";
    }
    @PostMapping("/purchase/byproduct")
    public String postProductPurchases(@RequestParam Long productId, Model model) {
        Iterable<Purchase> purchases = purchaseRepository.findByProductId(productId);
        model.addAttribute("purchases", purchases);
        return "product-purchases";
    }

    @GetMapping("/purchase/add")
    public String purchaseAdd(Model model) {
        return "purchase-add";
    }
    @PostMapping("/purchase/add")
    public String postPurchaseAdd(@RequestParam Long userId, @RequestParam Long productId, Model model) {
        try {
            // Getting user and product from DB
            Optional<User> optionalUser = userRepository.findById(userId);
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalUser.isEmpty() || optionalProduct.isEmpty()) {
                throw new PurchaseException("User or product doesn't exist");
            }
            User user = optionalUser.get();
            Product product = optionalProduct.get();
            // Check user solvency
            if (user.getAmountOfMoney() < product.getPrice()) {
                throw new PurchaseException("Not enough money on the user balance!");
            }
            // Create new purchase and save into DB;
            Purchase purchase = new Purchase(userId, productId);
            purchaseRepository.save(purchase);
            // Update user amount of money
            user.setAmountOfMoney(user.getAmountOfMoney() - product.getPrice());
            userRepository.save(user);
            // Show message about success on the webpage
            model.addAttribute("successText", "The purchase is successful!");
            return "success";

        } catch (PurchaseException e) {
            // Show error on the webpage
            model.addAttribute("errorText", e.getMessage());
            return "error";
        }
    }
}
