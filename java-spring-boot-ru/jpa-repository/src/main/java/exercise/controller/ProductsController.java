package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @GetMapping
    public ResponseEntity<List<Product>> findAll(@RequestParam(required = false) Integer min,
                                                 @RequestParam(required = false) Integer max) {
        if (min == null && max == null) {
            return ResponseEntity.ok(productRepository.findAll(Sort.by(Sort.Order.asc("price"))));
        } else if (min == null) {
            return ResponseEntity.ok(productRepository.findAllByPriceBetween(0, max).stream()
                    .sorted(Comparator.comparing(Product::getPrice)).toList());
        } else if (max == null) {
            return ResponseEntity.ok(productRepository
                    .findAllByPriceBetween(min, productRepository.findAll().size()).stream()
                    .sorted(Comparator.comparing(Product::getPrice)).toList());
        }
        return ResponseEntity.ok(productRepository.findAllByPriceBetween(min, max).stream()
                .sorted(Comparator.comparing(Product::getPrice)).toList());
    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
