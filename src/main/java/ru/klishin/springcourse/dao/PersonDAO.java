package ru.klishin.springcourse.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.klishin.springcourse.models.Person;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final SessionFactory sessionFactory;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate) {
        this.sessionFactory = sessionFactory;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public List<Person> index() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("FROM Person", Person.class).getResultList();
    }

    public Optional<Person> show(String email) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE email=?", new Object[]{email},
                        new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    @Transactional(readOnly = true)
    public Person show(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class, id);
    }

    @Transactional
    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        Session session = sessionFactory.getCurrentSession();

        Person personToBeUpdated = session.get(Person.class, id);
        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setAge(updatedPerson.getAge());
        personToBeUpdated.setEmail(updatedPerson.getEmail());
        personToBeUpdated.setAddress(updatedPerson.getAddress());
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(session.get(Person.class, id));
    }

                    ////////////////////////////////////////////////////
                    //// Тестируем производительность Batch update ////
                    //////////////////////////////////////////////////

    public void testMultipleUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();
        for(Person person : people) {
            jdbcTemplate.update("INSERT INTO Person VALUES (?, ?, ?, ?)", person.getId(), person.getName(),
                                                                              person.getAge(), person.getEmail());
        }
        long after = System.currentTimeMillis();

        System.out.println("Time : " + (after - before));
    }

    public void testBatchUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();
        jdbcTemplate.batchUpdate("INSERT INTO Person VALUES (?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, people.get(i).getId());
                ps.setString(2, people.get(i).getName());
                ps.setInt(3, people.get(i).getAge());
                ps.setString(4, people.get(i).getEmail());
            }

            @Override
            public int getBatchSize() {
                return people.size();
            }
        });
        long after = System.currentTimeMillis();

        System.out.println("Time : " + (after - before));
    }

    private List<Person> create1000People() {
        List<Person> people = new ArrayList<>();

        for(int i = 1; i <=1000; i++) {
            people.add(new Person(i, "Name" + i, 27, "name" + i + "@mail.ru", "some address"));
        }

        return people;
    }
}
