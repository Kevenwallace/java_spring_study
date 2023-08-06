package com.kevenwallace.project.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kevenwallace.project.dto.CategoryDTO;
import com.kevenwallace.project.entities.categories;
import com.kevenwallace.project.repositories.CategoriesRepository;
import com.kevenwallace.project.services.exceptions.DataBaseException;
import com.kevenwallace.project.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoriesService {

	@Autowired
	private CategoriesRepository repository;

	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
		Page<categories> list = repository.findAll(pageRequest);
		Page<CategoryDTO> ListDto = list.map(x -> new CategoryDTO(x));
		return ListDto;
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<categories> object = repository.findById(id);
		categories entity = object.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
	}

	@Transactional()
	public CategoryDTO insertCategory(CategoryDTO dto) {
		categories entity = new categories();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional()
	public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
		try {
			categories entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("id not found" + id);
		}
	}

	public void deleteCategory(Long id) {
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
}
