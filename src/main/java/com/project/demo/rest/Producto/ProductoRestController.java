package com.project.demo.rest.Producto;

import com.project.demo.logic.entity.Categoria.Categoria;
import com.project.demo.logic.entity.Categoria.CategoriaRepository;
import com.project.demo.logic.entity.Producto.Producto;
import com.project.demo.logic.entity.Producto.ProductoRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoRestController {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'USER')")
    public List<Producto> getProductos() {
        return productoRepository.findAll();
    }

    @PostMapping("categoria/{categoriaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Producto addProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Producto updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoRepository.findById(id)
                .map(existingProducto -> {
                    existingProducto.setNombre(producto.getNombre());
                    existingProducto.setDescripcion(producto.getDescripcion());
                    existingProducto.setPrecio(producto.getPrecio());
                    existingProducto.setStock(producto.getStock());
                    existingProducto.setCategoria(producto.getCategoria());

                    return productoRepository.save(existingProducto);
                })
                .orElseGet(() -> {
                    producto.setId(id);
                    return productoRepository.save(producto);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public void deleteProducto(@PathVariable Long id) {
        productoRepository.deleteById(id);
    }
}
