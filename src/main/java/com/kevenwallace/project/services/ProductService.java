package com.kevenwallace.project.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kevenwallace.project.dto.CategoryDTO;
import com.kevenwallace.project.dto.ProductDTO;
import com.kevenwallace.project.entities.Product;
import com.kevenwallace.project.entities.categories;
import com.kevenwallace.project.repositories.CategoriesRepository;
import com.kevenwallace.project.repositories.ProductRepository;
import com.kevenwallace.project.services.exceptions.DataBaseException;
import com.kevenwallace.project.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoriesRepository categoryrepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> list = repository.findAll(pageRequest);
		Page<ProductDTO> ListDto = list.map(x -> new ProductDTO(x));
		return ListDto;
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> object = repository.findById(id);
		Product entity = object.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional()
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		//entity.setName(dto.getName());
		entity = repository.save(entity);
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional()
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			//entity.setName(dto.getName());
			entity = repository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("id not found" + id);
		}
	}

	public void delete(Long id) {
		try {
			if (repository.existsById(id)) {
				repository.deleteById(id);
			} else {
				throw new ResourceNotFoundException("id not found: " + id);
			}
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}
	}
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for (CategoryDTO catDTO : dto.getCategories()) {
			categories category = categoryrepository.getOne(catDTO.getId());
			entity.getCategories().add(category);
		}
	}
}
