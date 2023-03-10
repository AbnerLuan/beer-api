package com.luan.beerstock.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;

import com.luan.beerstock.dto.BeerDTO;
import com.luan.beerstock.dto.QuantityDTO;
import com.luan.beerstock.exception.BeerAlreadyRegisteredException;
import com.luan.beerstock.exception.BeerNotFoundException;
import com.luan.beerstock.exception.BeerStockExceededException;
import com.luan.beerstock.exception.BeerWithInsufficientStockException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Manages beer stock")
public interface BeerControllerDocs {

    @ApiOperation(value = "Beer creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success beer creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
    })
    BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException;

    @ApiOperation(value = "Returns beer found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success beer found in the system"),
            @ApiResponse(code = 404, message = "Beer with given name not found.")
    })
    BeerDTO findByName(@PathVariable String name) throws BeerNotFoundException;

    @ApiOperation(value = "Returns a list of all beers registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all beers registered in the system"),
    })
    List<BeerDTO> listBeers();

    @ApiOperation(value = "Delete a beer found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success beer deleted in the system"),
            @ApiResponse(code = 404, message = "Beer with given id not found.")
    })
    void deleteById(@PathVariable Long id) throws BeerNotFoundException;

    @ApiOperation(value = "Increment beer by a given id and quantity in stock")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success beer incremented in stock"),
            @ApiResponse(code = 400, message = "Beer not successfully increment in stock"),
            @ApiResponse(code = 404, message = "Beer with given id not found.")
    })    
    BeerDTO increment(Long id, @Valid QuantityDTO quantityDTO) throws BeerNotFoundException, BeerStockExceededException;

    @ApiOperation(value = "Decrement beer by a given id and quantity in stock")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success beer decremented in stock"),
            @ApiResponse(code = 400, message = "Beer not successfully decrement in stock"),
            @ApiResponse(code = 404, message = "Beer with given id not found.")
    })    
    BeerDTO decrement(Long id, @Valid QuantityDTO quantityDTO)
	    throws BeerNotFoundException, BeerStockExceededException, BeerWithInsufficientStockException;

}
