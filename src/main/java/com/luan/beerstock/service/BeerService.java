package com.luan.beerstock.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.luan.beerstock.dto.BeerDTO;
import com.luan.beerstock.entity.Beer;
import com.luan.beerstock.exception.BeerAlreadyRegisteredException;
import com.luan.beerstock.exception.BeerNotFoundException;
import com.luan.beerstock.exception.BeerStockExceededException;
import com.luan.beerstock.exception.BeerWithInsufficientStockException;
import com.luan.beerstock.mapper.BeerMapper;
import com.luan.beerstock.repository.BeerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeerService {

	private final BeerRepository beerRepository;
	private final BeerMapper beerMapper = BeerMapper.INSTANCE;

	public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
		verifyIfIsAlreadyRegistered(beerDTO.getName());
		Beer beer = beerMapper.toModel(beerDTO);
		Beer savedBeer = beerRepository.save(beer);
		return beerMapper.toDTO(savedBeer);
	}

	public BeerDTO findByName(String name) throws BeerNotFoundException {
		Beer foundBeer = beerRepository.findByName(name).orElseThrow(() -> new BeerNotFoundException(name));
		return beerMapper.toDTO(foundBeer);
	}

	public List<BeerDTO> listAll() {
		return beerRepository.findAll().stream().map(beerMapper::toDTO).collect(Collectors.toList());
	}

	public void deleteById(Long id) throws BeerNotFoundException {
		verifyIfExists(id);
		beerRepository.deleteById(id);
	}

	private void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
		Optional<Beer> optSavedBeer = beerRepository.findByName(name);
		if (optSavedBeer.isPresent()) {
			throw new BeerAlreadyRegisteredException(name);
		}
	}

	private Beer verifyIfExists(Long id) throws BeerNotFoundException {
		return beerRepository.findById(id).orElseThrow(() -> new BeerNotFoundException(id));
	}

	public BeerDTO increment(Long id, int quantityToIncrement)
			throws BeerNotFoundException, BeerStockExceededException {
		Beer beerToIncrementStock = verifyIfExists(id);
		int quantityAfterIncrement = quantityToIncrement + beerToIncrementStock.getQuantity();
		if (quantityAfterIncrement <= beerToIncrementStock.getMax()) {
			beerToIncrementStock.setQuantity(beerToIncrementStock.getQuantity() + quantityToIncrement);
			Beer incrementedBeerStock = beerRepository.save(beerToIncrementStock);
			return beerMapper.toDTO(incrementedBeerStock);
		}
		throw new BeerStockExceededException(id, quantityToIncrement);
	}

	public BeerDTO decrement(Long id, int quantityToDecrement)
			throws BeerNotFoundException, BeerWithInsufficientStockException {
		Beer beerToDecrementStock = verifyIfExists(id);
		int quantityInStock = beerToDecrementStock.getQuantity();
		int quantityAfterDecrement = quantityInStock - quantityToDecrement;
		if (quantityAfterDecrement >= 0) {
			beerToDecrementStock.setQuantity(quantityInStock - quantityToDecrement);
			Beer decrementedBeerStock = beerRepository.save(beerToDecrementStock);
			return beerMapper.toDTO(decrementedBeerStock);
		}
		throw new BeerWithInsufficientStockException(id, quantityInStock, quantityToDecrement);
	}
}
