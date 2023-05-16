package hr.fer.infsus.rezervacije.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.infsus.rezervacije.models.Gost;
import hr.fer.infsus.rezervacije.models.GostProjection;
import hr.fer.infsus.rezervacije.repository.GostRepository;

@Service
public class GostService {

	@Autowired
    private GostRepository gostRepository;

    public List<GostProjection> getAllKorisniciImePrezimeIdGosta() {
        return gostRepository.findAllImePrezimeIdGosta();
    }
    
    public Gost getById(Long idGosta) {
    	return gostRepository.findById(idGosta).orElseThrow(() -> new IllegalArgumentException("Invalid Gost ID"));
    }
    
    public Gost updateBrojMobitela(Long id, String newBrojMobitela) {
        Optional<Gost> optionalGost = gostRepository.findById(id);
        
        if (optionalGost.isPresent()) {
            Gost gost = optionalGost.get();
            gost.setBrojMobitela(newBrojMobitela);
            Gost updatedGost = gostRepository.save(gost);
            
            return updatedGost;
        } else {
            throw new RuntimeException("Gost not found with id: " + id);
        }
    }

}