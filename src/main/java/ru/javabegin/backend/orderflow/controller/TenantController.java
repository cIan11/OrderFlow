package ru.javabegin.backend.orderflow.controller;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.backend.orderflow.entity.Tenant;
import ru.javabegin.backend.orderflow.service.TenantService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@RequestMapping("/tenants")
public class TenantController {
    private TenantService tenantService;
    //Подробнее узнать про .get в findById

    //1) Получить магазины ok
    @GetMapping
    public ResponseEntity<List<Tenant>> getAllTenants(){
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    //2) Получить магазин по id/имени/домену
    @PostMapping("/{id}")
    public ResponseEntity<Tenant> getTenantById(@PathVariable Long id){
        Tenant tenant = null;
        try {
             tenant = tenantService.findById(id);
        } catch (NoSuchElementException e) {
            return new ResponseEntity("Tenant with id: "+id+" not found",HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tenant);
    }

    //Объединить эти два метода
    @GetMapping("/byName")
    public ResponseEntity<Tenant> getTenantByName(@RequestParam String name){
        Tenant tenant = null;
        try {
            tenant = tenantService.findByName(name);
        } catch (NoSuchElementException e) {
            return new ResponseEntity("Tenant with name: "+name+" not found",HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tenant);
    }

    @GetMapping("/byDomain")
    public ResponseEntity<Tenant> getTenantByDomain(@RequestParam String domain){
        Tenant tenant = null;
        try {
            tenant = tenantService.findByDomain(domain);
        } catch (NoSuchElementException e) {
            return new ResponseEntity("Tenant with domain: "+domain+" not found",HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tenant);
    }

    //3) Создать магазин no
    @PostMapping("/save")
    public ResponseEntity<Tenant> save(@RequestBody Tenant tenant){
        //Проверка полей
        //id
        if(tenant.getId() != null && tenant.getId() != 0){
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.BAD_REQUEST);
        }
        //name
        if(tenant.getName() == null || tenant.getName().trim().length() == 0){
            return new ResponseEntity("Tenant name must not be empty", HttpStatus.BAD_REQUEST);
        }
        //domain
        if (tenant.getDomain() == null || tenant.getDomain().trim().length() == 0){
            return new ResponseEntity("Tenant domain must not be empty", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(tenantService.save(tenant));
    }
    //4) Изменить параметры магазина no
    @PutMapping("/update")
    public ResponseEntity<Tenant> update(@RequestBody Tenant tenant){
        //Проверка полей
        //id
        if(tenant.getId() == null || tenant.getId() == 0){
            return new ResponseEntity("ID must be provided for update", HttpStatus.BAD_REQUEST);
        }
        //name
        if(tenant.getName() == null || tenant.getName().trim().length() == 0){
            return new ResponseEntity("Tenant name must not be empty", HttpStatus.BAD_REQUEST);
        }
        //domain
        if (tenant.getDomain() == null || tenant.getDomain().trim().length() == 0){
            return new ResponseEntity("Tenant domain must not be empty", HttpStatus.BAD_REQUEST);
        }
        tenantService.save(tenant);
        return new ResponseEntity(HttpStatus.OK);

    }

    //5) Удаление магазина ok
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        try {
            tenantService.delete(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }


}
