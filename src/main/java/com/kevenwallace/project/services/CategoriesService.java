package com.kevenwallace.project.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
	public List<CategoryDTO> findAll() {
		List<categories> list = repository.findAll();
		List<CategoryDTO> ListDto = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
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
