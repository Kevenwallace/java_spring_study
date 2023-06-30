package com.kevenwallace.project.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kevenwallace.project.dto.CategoryDTO;
import com.kevenwallace.project.entities.categories;
import com.kevenwallace.project.repositories.CategoriesRepository;


import com.kevenwallace.project.services.exceptions.EntityNotFoundException;

@Service
public class CategoriesService {
	
	@Autowired
	private CategoriesRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List <categories> list =  repository.findAll();
		List<CategoryDTO> ListDto = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		return ListDto;
	}
 
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<categories> object = repository.findById(id);
		categories entity = object.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
	}
	
	@Transactional()
	public CategoryDTO insertCategory(CategoryDTO dto) {
		categories entity = new categories();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}
}
