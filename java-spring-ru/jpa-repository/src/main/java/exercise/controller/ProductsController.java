package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getBy(@RequestParam(required = false) Integer max, @RequestParam(required = false) Integer min) {

        if (min != null && max != null) {
            return productRepository.findByPriceInOrderByPriceAsc(List.of(min, max));
        } else if (min == null && max != null) {
            return productRepository.findByPriceLessThanOrderByPriceAsc(max);
        } else if (min != null) {
            return productRepository.findByPriceGreaterThanOrderByPriceAsc(min);
        } else {
            return productRepository.findAll(Sort.by(Sort.Order.asc("price")));
        }
    }

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
