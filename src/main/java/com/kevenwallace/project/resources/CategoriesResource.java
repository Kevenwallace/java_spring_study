package com.kevenwallace.project.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevenwallace.project.entities.categories;

@RestController
@RequestMapping(value = "/categories")
public class CategoriesResource {
	
	@GetMapping
	public ResponseEntity<List<categories>> findAll(){
		List<categories> list = new ArrayList<categories>();
		list.add(new categories(1L, "books"));
		list.add(new categories(2L, "stands"));
		return ResponseEntity.ok().body(list);
	}
}
