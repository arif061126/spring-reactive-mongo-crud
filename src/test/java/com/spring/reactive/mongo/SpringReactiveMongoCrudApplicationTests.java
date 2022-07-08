package com.spring.reactive.mongo;

import com.spring.reactive.mongo.controller.ProductController;
import com.spring.reactive.mongo.dto.ProductDto;
import com.spring.reactive.mongo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;



//@SpringBootTest
@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class SpringReactiveMongoCrudApplicationTests {

    /*@Test
    void contextLoads() {
    }*/

    /**
     * We need to inject our web-test client
     * to call the http method
     * we need to inject our service class
     * to mock that layer
     * we are not going to hit our actual database
     * that is why i just want to mock my service layer
     */

    @Autowired
    private WebTestClient webTestClient;

    @MockBean //it will not create the actual object
    private ProductService productService;

    @Test
    public void addProductTest(){
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101","mobile",2,10000.0));

        when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);

        webTestClient.post()
                .uri("/products/save")
                .body(Mono.just(productDtoMono),ProductDto.class)// this is for request body
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getProductsTest(){
        Flux<ProductDto> productDtoFlux = Flux.just(
                new ProductDto("101","mobile",2,10000.0),
                new ProductDto("102","laptop",1,20000.0),
                new ProductDto("103","tv",2,50000.0)
        );

        when(productService.getProducts()).thenReturn(productDtoFlux);

        Flux<ProductDto> responseBody = webTestClient.get()
                .uri("/products/")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new ProductDto("101","mobile",2,10000.0))
                .expectNext(new ProductDto("102","laptop",1,20000.0))
                .expectNext(new ProductDto("103","tv",2,50000.0))
                .verifyComplete();
    }

    @Test
    public void getProductByIdTest(){
        Mono<ProductDto> productDtoMono = Mono.just(
                new ProductDto("102","laptop",1,20000.0)
        );

        when(productService.getProductById(any())).thenReturn(productDtoMono);

        Flux<ProductDto> responseBody = webTestClient.get()
                .uri("/products/102")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                //.expectNext(new ProductDto("102","laptop",1,20000.0))
                .expectNextMatches(p->p.getName().equals("laptop"))
                .verifyComplete();

    }

    @Test
    public void updateProductByIdTest(){
        Mono<ProductDto> productDtoMono = Mono.just(
                new ProductDto("103","tv",5,60000.0)
        );

        when(productService.updateProduct(productDtoMono, "103")).thenReturn(productDtoMono);

        webTestClient.put()
                .uri("/products/update/103")
                .body(Mono.just(productDtoMono),ProductDto.class)// this is for request body
                .exchange()
                .expectStatus()
                .isOk();

    }

    @Test
    public void deleteProductByIdTest(){
        given(productService.deleteProductById(any())).willReturn(Mono.empty());

        webTestClient.delete()
                .uri("/products/delete/103")
                .exchange()
                .expectStatus()
                .isOk();

    }

}
