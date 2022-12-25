package br.com.alura.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Curso;
//Isso é uma única linha comentada
public interface CursoRepository extends JpaRepository<Curso, Long> {

	Curso findByNome(String nome);

}
