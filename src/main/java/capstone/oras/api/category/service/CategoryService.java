package capstone.oras.api.category.service;

import capstone.oras.dao.ICategoryRepository;
import capstone.oras.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private ICategoryRepository iCategoryRepository;

    @Override
    public List<CategoryEntity> getAllCategory() {
        return iCategoryRepository.findAll();
    }
}
