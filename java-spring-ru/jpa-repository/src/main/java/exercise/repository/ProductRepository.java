package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import exercise.model.Product;

import org.springframework.data.domain.Sort;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByPriceLessThanOrderByPriceAsc(Integer price);
    List<Product> findByPriceGreaterThanOrderByPriceAsc(Integer price);
    List<Product> findByPriceInOrderByPriceAsc(Collection<Integer> ages);
}
