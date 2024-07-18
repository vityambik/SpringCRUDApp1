package ru.klishin.springcourse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klishin.springcourse.models.Person;
import ru.klishin.springcourse.repositories.PeopleRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        person.setCreatedAt(new Date());
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

//    public List<Person> findByName(String name) {
//        return peopleRepository.findByName(name);
//    }
//
//    public List<Person> findByNameOrderByAge(String name) {
//        return peopleRepository.findByNameOrderByAge(name);
//    }
//
//    public List<Person> findByEmail(String email) {
//        return peopleRepository.findByEmail(email);
//    }
//
//    public List<Person> findByNameStartingWith(String startingWith) {
//        return peopleRepository.findByNameStartingWith(startingWith);
//    }
//
//    public List<Person> findByNameOrEmail(String name, String email) {
//        return peopleRepository.findByNameOrEmail(name, email);
//    }

    public void test() {
        System.out.println("Testing here with debug. Inside Hibernate Transaction");
    }
}
