package ru.javabegin.backend.orderflow.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javabegin.backend.orderflow.entity.Tenant;
import ru.javabegin.backend.orderflow.repository.TenantRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TenantService {
    private TenantRepository tenantRepository;

    public List<Tenant> getAllTenants(){
        return tenantRepository.findAll();
    }

    public Tenant findByName(String name){
        return tenantRepository.findByName(name);
    }

    public Tenant findByDomain(String domain){
        return tenantRepository.findByDomain(domain);
    }

    public Tenant findById(Long id){
        return tenantRepository.findById(id).get();
    }
    public boolean existsById(Long tenantId) {
        return tenantRepository.existsById(tenantId);
    }

    public Tenant save(Tenant tenant){
        return tenantRepository.save(tenant);
    }
    public Tenant update(Tenant tenant){
        return tenantRepository.save(tenant);
    }
    public void delete(Long id){
        tenantRepository.deleteById(id);
    }
}
