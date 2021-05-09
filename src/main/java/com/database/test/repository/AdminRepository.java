package com.database.test.repository;


import com.database.test.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin,String> {

    @Query(nativeQuery = true,value = "select * from Admin where admin_email=?1")
    List<Admin> selectAdminByEmail(String email);

}
