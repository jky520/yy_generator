package cn.gzyinyuan.yy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.gzyinyuan.yy")
public class YyGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(YyGeneratorApplication.class, args);
	}
}
