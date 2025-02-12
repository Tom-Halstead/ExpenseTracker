package com.expensetracker.service;

import com.expensetracker.dto.CategoryDTO;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.User;
import com.expensetracker.exception.CategoryNotFoundException;
import com.expensetracker.exception.UserNotFoundException;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // Get all categories
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get a single category by ID
    public CategoryDTO getCategoryById(int categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        return convertToDTO(category);
    }

    // Add a new category
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        User user = userRepository.findById(categoryDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + categoryDTO.getUserId()));

        Category category = convertToEntity(categoryDTO);
        category.setUser(user);
        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    public CategoryDTO createOrGetDefaultCategory(int userId) {
        // Check if the default category already exists
        Category existingCategory = categoryRepository.findByName("Default Category");
        if (existingCategory != null) {
            return convertToDTO(existingCategory);
        }

        // If not, create a new one
        String defaultName = "Default Category";
        String defaultType = "General";  // Example type
        String defaultDescription = "Automatically created category";

        CategoryDTO newCategory = new CategoryDTO(userId, defaultName, defaultType, defaultDescription, true);
        Category savedCategory = categoryRepository.save(convertToEntity(newCategory));
        return convertToDTO(savedCategory);
    }



    // Update an existing category
    public CategoryDTO updateCategory(int categoryId, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        User user = userRepository.findById(categoryDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + categoryDTO.getUserId()));

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setType(categoryDTO.getType());
        category.setActive(categoryDTO.isActive());
        category.setUser(user); // Update user reference if needed

        Category updatedCategory = categoryRepository.save(category);
        return convertToDTO(updatedCategory);
    }

    // Delete a category by ID
    public void deleteCategory(int categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    // Convert a Category entity to CategoryDTO
    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getUser().getId(),
                category.getName(),
                category.getType(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    // Convert a CategoryDTO to Category entity
    private Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getCategoryId());
        category.setName(categoryDTO.getName());
        category.setType(categoryDTO.getType());
        category.setDescription(categoryDTO.getDescription());
        category.setActive(categoryDTO.isActive());
        return category;
    }
}
