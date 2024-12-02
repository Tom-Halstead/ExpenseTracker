package com.expensetracker.cli.commands;

import com.expensetracker.dto.CategoryDTO;
import com.expensetracker.dto.UserDTO;
import com.expensetracker.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.List;
import java.util.Scanner;

@Component
@CommandLine.Command(
        name = "category",
        description = "Manage categories."
)
public class CategoryCommand implements Command {

    @Autowired
    private CategoryService categoryService;


    @CommandLine.Option(names = {"-a", "--add"}, description = "Add a new category")
    private boolean add;

    @CommandLine.Option(names = {"-d", "--delete"}, description = "Delete a category")
    private boolean delete;

    @CommandLine.Option(names = {"-l", "--list"}, description = "List all categories")
    private boolean list;

    @CommandLine.Option(names = {"-u", "--update"}, description = "Update an existing category")
    private boolean update;

    private Scanner scanner = new Scanner(System.in); // Scanner for user input

    public CategoryCommand(UserDTO loggedInUser) {
    }

    @Override
    public void execute() {
        if (add) {
            System.out.println("Adding a new category...");
            addCategory();
        } else if (delete) {
            System.out.println("Deleting a category...");
            deleteCategory();
        } else if (list) {
            System.out.println("Listing all categories...");
            listCategories();
        } else if (update) {
            System.out.println("Updating an existing category...");
            updateCategory();
        } else {
            System.out.println("Please specify an option: --add, --delete, --list, or --update");
        }
    }

    private void addCategory() {
        System.out.print("Enter user ID: ");
        int userId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter category name: ");
        String name = scanner.nextLine();

        System.out.print("Enter category type: ");
        String type = scanner.nextLine();

        System.out.print("Enter description (optional): ");
        String description = scanner.nextLine();

        boolean isActive = true; // Default is active

        // Use the constructor that includes user ID
        CategoryDTO categoryDTO = new CategoryDTO(userId, name, type, description, isActive);
        try {
            CategoryDTO newCategory = categoryService.addCategory(categoryDTO);
            System.out.println("Category added: " + newCategory.toString());
        } catch (Exception e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    private void deleteCategory() {
        System.out.print("Enter the ID of the category to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            categoryService.deleteCategory(id);
            System.out.println("Category deleted: ID = " + id);
        } catch (Exception e) {
            System.out.println("Error deleting category: " + e.getMessage());
        }
    }

    private void listCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
        } else {
            categories.forEach(category -> System.out.println(category.toString()));
        }
    }

    private void updateCategory() {
        System.out.print("Enter the ID of the category to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        // Fetch the existing category details
        CategoryDTO existingCategory = categoryService.getCategoryById(id);
        if (existingCategory == null) {
            System.out.println("Category not found.");
            return;
        }

        System.out.println("Current category details: " + existingCategory.toString());

        System.out.print("Enter new name (leave blank to keep current: " + existingCategory.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            existingCategory.setName(name);
        }

        System.out.print("Enter new type (leave blank to keep current: " + existingCategory.getType() + "): ");
        String type = scanner.nextLine();
        if (!type.isEmpty()) {
            existingCategory.setType(type);
        }

        System.out.print("Enter new description (leave blank to keep current: " + existingCategory.getDescription() + "): ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            existingCategory.setDescription(description);
        }

        System.out.print("Is the category active? (current: " + existingCategory.isActive() + ", true/false, leave blank to keep current): ");
        String isActiveInput = scanner.nextLine();
        if (!isActiveInput.isEmpty()) {
            boolean isActive = Boolean.parseBoolean(isActiveInput);
            existingCategory.setActive(isActive);
        }

        // Update the category
        CategoryDTO updatedCategory = categoryService.updateCategory(id, existingCategory);
        System.out.println("Category updated: " + updatedCategory.toString());
    }
}