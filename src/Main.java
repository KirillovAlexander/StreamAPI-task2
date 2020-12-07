import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final int MINOR_AGE = 18;
    private static final int MAX_SOLDIER_AGE = 27;
    private static final int MAX_PENSION_AGE_MAN = 65;
    private static final int MAX_PENSION_AGE_WOMEN = 60;

    public static void main(String[] args) throws InterruptedException {

        Collection<Person> persons = getPersons();

        findMinorPersons(persons);
        sleep(3);

        findSoldiers(persons);
        sleep(3);

        potentiallyWorkablePersons(persons);
    }

    private static void sleep(int seconds) throws InterruptedException {
        TimeUnit.SECONDS.sleep(seconds);
    }

    private static Collection<Person> getPersons() {
        List<String> names = Arrays.asList("Jack", "Connor", "Harry", "George", "Samuel", "John");
        List<String> families = Arrays.asList("Evans", "Young", "Harris", "Wilson", "Davies", "Adamson", "Brown");
        Collection<Person> persons = new ArrayList<>();
        for (int i = 0; i < 10_000_000; i++) {
            persons.add(new Person(
                    names.get(new Random().nextInt(names.size())),
                    families.get(new Random().nextInt(families.size())),
                    new Random().nextInt(100),
                    Sex.values()[new Random().nextInt(Sex.values().length)],
                    Education.values()[new Random().nextInt(Education.values().length)])
            );
        }
        return persons;
    }

    private static void findMinorPersons(Collection<Person> persons) {
        Stream<Person> stream = persons.stream();
        long countMinorPersons = stream.filter(person -> person.getAge() > MINOR_AGE)
                .count();
        System.out.println("Количество совершеннолетних: " + countMinorPersons);
    }

    private static void findSoldiers(Collection<Person> persons) {
        Stream<Person> stream = persons.stream();
        List<String> soldiers = stream.filter(person -> person.getSex().equals(Sex.MAN))
                .filter(person -> person.getAge() > MINOR_AGE)
                .filter(person -> person.getAge() < MAX_SOLDIER_AGE)
                .map(Person::getFamily)
                .collect(Collectors.toList());
        System.out.println("Призывники: ");
        for (String soldierFamily : soldiers) {
            System.out.println(soldierFamily);
        }
    }

    private static void potentiallyWorkablePersons(Collection<Person> persons) {
        Stream<Person> stream = persons.stream();
        Comparator<Person> personComparator = Comparator.comparing(Person::getFamily);
        List<Person> workablePersons = stream.filter(person -> {
            int age = person.getAge();
            if (age < MINOR_AGE || !person.getEducation().equals(Education.HIGHER)) return false;
            return (person.getSex().equals(Sex.MAN) && age < MAX_PENSION_AGE_MAN)
                    || (person.getSex().equals(Sex.WOMEN) && age < MAX_PENSION_AGE_WOMEN);
        })
                .sorted(personComparator)
                .collect(Collectors.toList());
        for (Person person : workablePersons) {
            System.out.println(person);
        }
    }
}
