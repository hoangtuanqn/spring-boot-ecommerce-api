package mst.local.mstsoftware.modules.products.repositories;

import mst.local.mstsoftware.modules.products.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
