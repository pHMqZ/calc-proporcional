package com.pms.calprop.config;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.pms.calprop.entities.Bill;
import com.pms.calprop.entities.Person;
import com.pms.calprop.repositories.BillRepository;
import com.pms.calprop.repositories.PersonRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile({ "dev", "uat" })
@RequiredArgsConstructor
public class DataConfig implements CommandLineRunner {

    private final PersonRepository personRepository;

    private final BillRepository billRepository;

    String seederClientId = "seeder-client-dev";

    @Override
    @SuppressWarnings("null")
    public void run(String... args) throws Exception {
        Person alceu = new Person("Alceu", new BigDecimal("2000.00"), 10.0, seederClientId);
        Person toalha = new Person("Toalha", new BigDecimal("2000.00"), 10.0, seederClientId);
        Person elis = new Person("Elis", new BigDecimal("2000.00"), 10.0, seederClientId);
        Person banguela = new Person("Banguela", new BigDecimal("2000.00"), 10.0, seederClientId);

        personRepository.saveAll(Arrays.asList(alceu, toalha, elis, banguela));

        Bill contaAluguel = new Bill("Aluguel", new BigDecimal("1500.00"), seederClientId);
        Bill contaLuz = new Bill("Luz", new BigDecimal("250.00"), seederClientId);
        Bill contaCondominio = new Bill("Condominio", new BigDecimal("500.00"), seederClientId);
        Bill contaInternet = new Bill("Internet", new BigDecimal("100.00"), seederClientId);

        billRepository.saveAll(Arrays.asList(contaAluguel, contaLuz, contaCondominio, contaInternet));

        System.out.println("Pessoas salvas: " + personRepository.count());
        System.out.println("Contas salvas: " + billRepository.count());
    }

}
