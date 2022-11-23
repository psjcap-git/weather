package zerobase.weather.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import zerobase.weather.domain.Memo;

@Repository
public interface JpaMemoRepository extends JpaRepository<Memo, Integer> {
    public List<Memo> findAll();
    public Optional<Memo> findById(int id);    
}
