package cn.anlucky.luckyadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"cn.anlucky.luckyadmin.*.mapper","cn.anlucky.luckyadmin.config","cn.anlucky.luckyadmin.*.config"})
public class LuckyAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckyAdminApplication.class, args);
    }

}
