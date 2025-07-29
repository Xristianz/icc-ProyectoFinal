package ec.edu.ups.Estructura.dao;

import java.util.List;

import ec.edu.ups.Estructura.models.AlgorithmResult;

public interface  AlgorithmResultDAO {
    void save(AlgorithmResult paramAlgorithmResult);
    List<AlgorithmResult> findAll();
    void clear();

}
