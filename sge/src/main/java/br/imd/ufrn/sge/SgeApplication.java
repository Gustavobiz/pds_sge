package br.imd.ufrn.sge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgeApplication.class, args);
	}

}
