package dev.shop.backend.controllers;

import dev.shop.backend.domain.dto.OrderDTO;
import dev.shop.backend.domain.dto.UserDTO;
import dev.shop.backend.domain.entities.OrderEntity;
import dev.shop.backend.domain.entities.ProductEntity;
import dev.shop.backend.exceptions.InvalidOrderOwnerException;
import dev.shop.backend.exceptions.ProductOwnerSelfOrderException;
import dev.shop.backend.mappers.impl.OrderMapper;
import dev.shop.backend.service.OrderService;
import dev.shop.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final OrderMapper orderMapper;

    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        if(productService.existsBySellerId(orderDTO.getProductId(), email)){
            throw new ProductOwnerSelfOrderException("Product seller cannot buy his own product.");
        }

        orderDTO.setBuyerId(email);
        OrderEntity orderEntity = orderMapper.mapFrom(orderDTO);

        Optional<ProductEntity> res = productService.findOne(orderDTO.getProductId());

        if(res.isEmpty()){
            throw new IllegalArgumentException("Product does not exist.");
        }

        ProductEntity productEntity = res.get();
        Long updatedQuantity = productEntity.getQuantity() - orderDTO.getQuantity();

        productEntity.setQuantity(updatedQuantity);

        productService.partialUpdate(productEntity.getId(), productEntity);
        OrderEntity savedOrder = orderService.save(orderEntity);

        return new ResponseEntity<>(orderMapper.mapTo(savedOrder), HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> listOrders(){

        List<OrderEntity> orderEntities = orderService.findAll();

        return new ResponseEntity<>(orderEntities
                .stream()
                .map(orderMapper::mapTo)
                .collect(Collectors.toList()), HttpStatus.OK
        );
    }

    @GetMapping(value = "/orders", params = "email")
    public ResponseEntity<List<OrderDTO>> listOrdersByUser(@Param("email") String email){

        List<OrderEntity> orderEntities = orderService.findOrdersByUser(email);

        return new ResponseEntity<>(orderEntities
                .stream()
                .map(orderMapper::mapTo)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/orders", params = "id")
    public ResponseEntity<List<OrderDTO>> listOrdersOfProduct(@Param("id") Long productId){

        List<OrderEntity> orderEntities = orderService.findOrdersOfProduct(productId);

        return new ResponseEntity<>(orderEntities
                .stream()
                .map(orderMapper::mapTo)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id)
    {

        Optional<OrderEntity> foundOrder = orderService.findOne(id);

        return foundOrder.map(
                orderEntity -> {
                    OrderDTO orderDTO = orderMapper.mapTo(orderEntity);
                    return new ResponseEntity<>(orderDTO, HttpStatus.OK);
                }
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> partialUpdateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO){

        if(!orderService.isExists(id))
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!orderService.existsByBuyerId(id, auth.getName()))
        {
            throw new InvalidOrderOwnerException("Buyer does not match current user");
        }

        orderDTO.setBuyerId(auth.getName());

        OrderEntity order = orderService.findOne(id).get();

        if(order.getQuantity() != orderDTO.getQuantity()){
            Long diff = orderDTO.getQuantity() - order.getQuantity();

            Optional<ProductEntity> prod = productService.findOne(order.getProductId());

            if(prod.isPresent()){
                ProductEntity update = prod.get();
                update.setQuantity(update.getQuantity() - diff);

                productService.partialUpdate(update.getId(), update);
            }
        }


        OrderEntity orderEntity = orderMapper.mapFrom(orderDTO);
        OrderEntity updatedOrder = orderService.partialUpdate(id, orderEntity);

        return new ResponseEntity<>(orderMapper.mapTo(updatedOrder), HttpStatus.OK);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> deleteOrder(@PathVariable Long id){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(orderService.isExists(id) && !orderService.existsByBuyerId(id, auth.getName()))
        {
            throw new InvalidOrderOwnerException("Buyer does not match current user");
        }

        if(orderService.findOne(id).isPresent()){
            OrderEntity order = orderService.findOne(id).get();
            Optional<ProductEntity> res = productService.findOne(order.getProductId());

            if(res.isPresent()){
                ProductEntity prod = res.get();

                prod.setQuantity(prod.getQuantity() + order.getQuantity());

                productService.partialUpdate(prod.getId(), prod);
            }
        }

        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
