package com.kevenwallace.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kevenwallace.project.entities.categories;

@Repository
public interface CategoriesRepository extends JpaRepository<categories, Long>{

}
