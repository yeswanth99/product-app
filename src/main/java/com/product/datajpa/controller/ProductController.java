package com.product.datajpa.controller;

import com.product.datajpa.entity.Product;
import com.product.datajpa.model.Product.ProductResponse;
import com.product.datajpa.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity addProduct(@RequestBody ProductResponse product){
        productService.addProduct(product);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/retrieveProducts")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        log.info("All Products {}", products.toString());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/categories")
    public ResponseEntity getCategories(){
        return ResponseEntity.ok(productService.findCategory());
    }

    @GetMapping
    public ResponseEntity findBySkuOrTag(@RequestParam(name = "skucode", required = false) String skucode,
                                             @RequestParam(name = "tag", required = false) String tag){
        if (skucode!=null && (!skucode.isEmpty())){
            Product products = productService.findbySku(skucode);
            return ResponseEntity.ok(products);
        }
        else if (tag!=null && !tag.isEmpty()){
            List<Product> products = productService.findbyTag(tag);
            return ResponseEntity.ok(products);
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findBy")
    public ResponseEntity getProductsByPage(@RequestParam int pageNum,
                                            @RequestParam int pageSize){
        //Page<Product> pageResult = productService.findProductsByPage(pageNum,pageSize);
        Page<Product> pageResult = productService.findProductsByPageAndSort(pageNum,pageSize);

        return ResponseEntity.ok(pageResult);
    }
    @GetMapping("/findByCategory")
    public ResponseEntity getProductsByCategory(@RequestParam int pageNum,
                                            @RequestParam int pageSize,
                                            @RequestParam String category){
        Page<Product> pageResult = productService.findProductsByPageAndSortCategory(pageNum,pageSize,category);

        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/findByCategoryAndPrice")
    public ResponseEntity getProductsByCategory(@RequestParam String category,
                                                @RequestParam int min,
                                                @RequestParam int max){
        List<Product> listResult = productService.findProductsByCategoryAndPrice(category, min, max);
        return ResponseEntity.ok(listResult);
    }

    @GetMapping("/search")
    public ResponseEntity searchProducts(
            @RequestParam(required = false) Optional<String> title,
            @RequestParam(required = false) Optional<String> category,
            @RequestParam(required = false) Optional<Double> min,
            @RequestParam(required = false) Optional<Double> max,
            @RequestParam(required = false) Optional<Double> rating,
            @RequestParam(required = false) Optional<Integer> pageNum,
            @RequestParam(required = false) Optional<Integer> pageSize) {

        if (pageNum.isPresent() && pageSize.isPresent()){
            Page<Product> productPage = productService.searchProductsByDynamicQueryPage(title, category , min ,
                    max, rating, pageNum, pageSize);
            return ResponseEntity.ok(productPage);
        }

        List<Product> products = productService.searchProductsByDynamicQuery(title, category, min, max, rating);

        return ResponseEntity.ok(products);
    }

}
