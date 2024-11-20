//Проверка корректности даты
//        Напишите приложение, которое будет запрашивать у пользователя следующие
//данные в произвольном порядке, разделенные пробелом:
//Фамилия Имя Отчество датарождения номертелефона пол
//Форматы данных:
//фамилия, имя, отчество - строки
//дата_рождения - строка формата dd.mm.yyyy
//номер_телефона - целое беззнаковое число без форматирования
//пол - символ латиницей f или m.
//Приложение должно проверить введенные данные по количеству. Если
//количество не совпадает с требуемым, вернуть код ошибки, обработать его и
//показать пользователю сообщение, что он ввел меньше и больше данных, чем
//требуется.
//Приложение должно попытаться распарсить полученные значения и выделить из
//них требуемые параметры. Если форматы данных не совпадают, нужно бросить
//исключение, соответствующее типу проблемы. Можно использовать встроенные
//типы java и создать свои. Исключение должно быть корректно обработано,
//пользователю выведено сообщение с информацией, что именно неверно.
//Если всё введено и обработано верно, должен создаться файл с названием,
//        равным фамилии, в него в одну строку должны записаться полученные данные,
//        вида
//<Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
//Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
//Не забудьте закрыть соединение с файлом.
//При возникновении проблемы с чтением-записью в файл, исключение должно
//быть корректно обработано, пользователь должен увидеть стектрейс ошибки.

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserDataApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные (Фамилия Имя Отчество датарождения номертелефона пол):");

        String input = scanner.nextLine();
        String[] data = input.split("\\s+");

        try {
            validateInput(data);
            User user = parseUserData(data);
            writeToFile(user);
            System.out.println("Данные успешно записаны в файл.");
        } catch (InputValidationException | IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void validateInput(String[] data) throws InputValidationException {
        if (data.length < 6) {
            throw new InputValidationException("Ошибка: Введено меньше данных, чем требуется.");
        } else if (data.length > 6) {
            throw new InputValidationException("Ошибка: Введено больше данных, чем требуется.");
        }
    }

    private static User parseUserData(String[] data) throws InputValidationException {
        String lastName = data[0];
        String firstName = data[1];
        String middleName = data[2];
        String birthDate = data[3];
        String phoneNumber = data[4];
        char gender = data[5].charAt(0);

        // Проверка формата даты
        if (!birthDate.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            throw new InputValidationException("Ошибка: Неверный формат даты рождения. Ожидается dd.mm.yyyy.");
        }

        // Проверка номера телефона
        if (!phoneNumber.matches("\\d+")) {
            throw new InputValidationException("Ошибка: Номер телефона должен содержать только цифры.");
        }

        // Проверка пола
        if (gender != 'f' && gender != 'm') {
            throw new InputValidationException("Ошибка: Пол должен быть 'f' или 'm'.");
        }

        return new User(lastName, firstName, middleName, birthDate, phoneNumber, gender);
    }

    private static void writeToFile(User user) throws IOException {
        String fileName = user.getLastName() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(user.toString());
            writer.newLine();
        }
    }
}

class User {
    private String lastName;
    private String firstName;
    private String middleName;
    private String birthDate;
    private String phoneNumber;
    private char gender;

    public User(String lastName, String firstName, String middleName, String birthDate, String phoneNumber, char gender) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return lastName + firstName + middleName + birthDate + " " + phoneNumber + gender;
    }
}

class InputValidationException extends Exception {
    public InputValidationException(String message) {
        super(message);
    }
}