package com.pks.demo.controller;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pks.demo.exception.ProductNotFoundException;
import com.pks.demo.exception.RestRequestException;
import com.pks.demo.model.Category;
import com.pks.demo.model.CategoryUtil;
import com.pks.demo.model.Product;
import com.pks.demo.model.ProductUtil;

@RestController
public class CategoryController {
	Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@RequestMapping("/categories")
	public List<Category> getCategories() {

		CategoryUtil categoryUtil = new CategoryUtil();
		List<Category> categories = categoryUtil.getCategories();
		if (categories.isEmpty()) {
			throw new ProductNotFoundException("No Categories found");
		}
		return categories;

	}

	@RequestMapping("/categories/{id}")
	public Category getCategory(@PathVariable String id) {
		Category category = null;
		CategoryUtil categoryUtil = new CategoryUtil();
		category = categoryUtil.getCategory(id);
		if (category == null) {
			logger.warn("No category found for the categoryId: " + id);
			throw new ProductNotFoundException("No category found for the categoryId: " + id);
		}
		return category;
	}

	@RequestMapping(value = "/categories", method = RequestMethod.POST)
	public String createProduct(@RequestBody Category category) throws Exception {
		CategoryUtil productUtil = new CategoryUtil();
		boolean bool = productUtil.createCategory(category);

		if (bool) {
			return category.getCategoryName() + " Category added to the database with id : " + category.getCategoryId();
		} else {
			throw new RestRequestException("Category adding failed maybe another category has the same id : "
					+ category.getCategoryId() + "!!");

		}

	}

	@RequestMapping(value = "/categories/{id}", method = RequestMethod.PUT)
	public String updateProduct(@RequestBody Category category, @PathVariable String id)
			throws ClassNotFoundException, SQLException {
		CategoryUtil categoryUtil = new CategoryUtil();
		boolean bool = categoryUtil.updateCategory(category, id);
		if (bool) {
			return "Category updated and the new id is  " + category.getCategoryId();
		}

		throw new ProductNotFoundException("category updating failed for the  categoryId : " + id
				+ "!!  No Category found with categoryId : " + id);
	}

	@RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
	public String deleteProduct(@PathVariable String id) {
		CategoryUtil categoryUtil = new CategoryUtil();
		boolean bool = categoryUtil.deleteCategory(id);
		if (bool) {
			return "Category deleted with id : " + id;
		} else {
			throw new ProductNotFoundException("Category deletion failed for the  categoryId : " + id
					+ "!!  No Category found with id : " + id + " or there are products in the category");
		}
	}

}
