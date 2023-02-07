package cn.letao.online_grocery_store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.letao.online_grocery_store.dao")

public class OnlineGroceryStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineGroceryStoreApplication.class, args);
	}

}
