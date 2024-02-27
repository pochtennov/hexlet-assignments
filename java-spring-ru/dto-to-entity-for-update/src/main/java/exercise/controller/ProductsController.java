package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path = "")
    public List<ProductDTO> index() {
        var products = productRepository.findAll();
        return products.stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping(path = "/{id}")
    public ProductDTO show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        var productDTO = toDTO(product);
        return productDTO;
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductCreateDTO productData) {
        var product = toEntity(productData);
        productRepository.save(product);
        var productDto = toDTO(product);
        return productDto;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO update(@PathVariable long id, @RequestBody ProductUpdateDTO productData) {
        var product =  productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        var updatedProduct = toEntity(product, productData);
        productRepository.save(updatedProduct);
        return toDTO(updatedProduct);
    }


    private Product toEntity(ProductCreateDTO productDto) {
        var product = new Product();
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setVendorCode(productDto.getVendorCode());
        return product;
    }

    private Product toEntity(Product product, ProductUpdateDTO productDto) {
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        return product;
    }


    private ProductDTO toDTO(Product product) {
        var dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());
        dto.setVendorCode(product.getVendorCode());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}
