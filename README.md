# Тестовое задание для стажера (Java-разработчик в команду корпоративной шины данных и микосервисов) 
***
# Описание задачи 
In memory simple DB  
В процессе реализации сервиса проверки данных возникла необходимость в 
организации
*in memory кэша* с возможностью быстрого поиска по разным полям.  
<br/>
Структура данных представлена следующим набором полей:  
{  
  "account": "234678", //long  
  "name": "Иванов Иван Иванович", //string  
  "value": "2035.34" //double  
}  
Количество записей заранее не определено и может меняться динамически.  
**ВОПРОС**: Необходимо организовать хранение этих записей в памяти с 
соблюдением требований:
1. предоставить возможность добавлять новые записи;
2. предоставить возможность удалять более не нужные записи;
3. предоставить возможность изменять запись;
4. получать полный набор записи по любому из полей с одинаковой 
алгоритмической сложностью (не медленнее log(n));
5. выбрать наиболее экономный способ хранения данных в памяти.

**Важно**: Нужно обосновать выбор структур данных и алгоритмов 
относительно требований ТЗ.  
**Ограничение**: для реализации используйте один из указанных языков: Java, 
C#, C++, Python.
***
# Решение
# Выбор алгоритмов и структур данных для решения поставленной задачи относительно требований ТЗ
Для начала нужно задуматься над выбором *структуры данных*, которая оптимизирована для поиска записей по ключу (***не медленее log(n)***), а уже потом выбирать среди них те, которые удовлетворяют другим пунктам **ТЗ**.  
Давайте перечислим основные структуры данных, которые могут в теории подойти для нашей задачи:
1) HashMap (основана на хэш-таблице)
2) Деревья
   - Бинарное дерево поиска
   - Красно-чёрное дерево
   - AVL-деревья
   - Префиксное дерево
     
Из всех этих структур данных выберём для решения нашей задачи именно **красно-чёрное дерево**. Рассмотрим причины, по которым другие структуры данных нам не подойдут. 

***Префиксное дерево***  
*Данное дереве в базовой реализации в узлах хранит символы, то есть хорошо используется в основном для поиска, где ключами являются строки. Но в нашей же задаче есть ещё и типы данных long и double, которые также выступают в качестве ключей для наших записей, то есть такое дерево нам не подойдёт.*  

***Бинарное дерево поиска***  
*Хоть данное дерево и обеспечивает быстрый поиск, вставку и удаление элементов за счет свойства дерева, которое гарантирует, что для каждого узла все элементы в левом поддереве меньше, а в правом поддереве - больше значения текущего узла, оно не является самобалансирующем бинарным деревом поиска, то есть в процессе добавления новых узлов оно может выродиться в список и скорость поиска станет O(n).*    

***AVL-деревья***  
*Данное дерево уже является вариантом самобалансирующегося бинарного дерева поиска, который поддерживает баланс дерева для быстрого поиска. Данные деревья обеспечивают более быстрый поиск по сравнению с красно-чёрными деревьями, благодаря более строгому подддержанию баланса дерева, но в основном операции поиска в AVL-деревьях и красно-чёрных удовлетворяют 4-ому пункт в ТЗ. Но вот что действительно важно, так это то, что AVL-деревья в своих узлах хранят ещё и высоту поддерева, в отличие от красно-чёрных, поэтому из-за 5-ого пункта по выбору наиболее экономного хранения в памяти мы не берём AVL-деревья (про операции вставки и удаления не сказано, так как в ТЗ не говорится об алгоритмической сложности данных операций, но в AVL-деревьях и красно-чёрных деревьях они составляет также O(log(n), причём в красно-чёрных деревьях данные операции более быстрее по сравнению с AVL-деревьями, потому что нет такой строгой балансировки дерева после удаления или вставки нового узла).*   

***HashMap***   
*Данная структура данных основана на хэш-таблицах, то есть у нас операции вставки, удаления и поиска амортизировано O(1) в среднем. Это означает, что в долгосрочной перспективе у нас всё будет работать быстро, но в какой-то один момент может немного ухудшится время из-за коллизий, перехешировани и т.д.. То есть если нам нужно, чтобы система в 100% случаях работала быстро, то не стоит использовать её, а если можно немного и полагать в какой-то момент, то хороший выбор. Мы же попытаемся выбрать такую структуру данных, которая всегда работет быстро, поэтому пока что не берём HashMap. А вторая причина по которой я не беру данную структуру данных, это то, что нужно будет хранить в качестве ключа тип данных double. То есть для вычисления хэша нужно будет производить арифметические операции с типом double, что небезопасно, может возникать много коллизий, так как данный тип данных представлен в памяти с некоторой погрешностью. Также проблема может возникать и с округлением double в операциях, что может привести к предсказуемым результатам и проблемам с точностью (а вот в красно-чёрных деревьях сравнения double происходит в соответствии с естественным порядком, что намного эффективнее, чем производить арифметические операции с double).*       

Таким образом мы определились с тем, что больше всего нам подходит для решения задачи именно структура данных **красно-чёрное дерево**. Оно позволяет находить записи по ключам за O(log(n)), а также удалять (в частном случае изменять) и вставлять с такой же алгоритмической сложностью.   

Теперь обратимся к ТЗ и заметим важную деталь, а именно то, что нигде не сказано, что поле - уникально. То есть исходя из требований ТЗ можно предположить, что значение каждого поля может повторяться. Поэтому нам нужна структура данных, которая основана на красно-чёрном дереве и в своих узлах хранит список наших записей.  

Для этого воспользуемся такой структурой данных в Java как **TreeMap** - реализация интерфейса Map в Java, которая хранит элементы в отсортированном порядке и основана на красно-чёрном дереве.  

То есть создадим три TreeMap, каждая из которых будет своим ключом содержать поле account, name, value соответственно. И в целях экономии памяти будем каждый объект хранить в единственном экземпляре и в каждом из 3-ёх деревьев хранить ссылку на данный экземпляр.   

Язык программирования - **Java** 
***


# Код для задачи
На всякий случай загрузил код на GitHub, если так удобнее его смотреть, то можно тыкнуть по [ссылочке](https://github.com/DenisStepanidenko/AtonProblems/tree/master/DB).
# Класс Record (класс, который представляет из себя сущность запись)
```java

import java.util.Objects;

public class Record {
    private long account;
    private String name;
    private double value;

    public Record(long account, String name, double value) {
        this.account = account;
        this.name = name;
        this.value = value;
    }

    public long getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public void setAccount(long account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return account == record.account && Double.compare(value, record.value) == 0 && Objects.equals(name, record.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, name, value);
    }

    @Override
    public String toString() {
        return "Record{" +
                "account=" + account +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
```

# Класс MemoryDB (основной класс, где реализованы методы поиска, вставки, удаления и изменения записей)
```java
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


public class MemoryDB {
    /**
     * Так как в ТЗ не сказано, что какое-то поле это primary key, то есть значение полей могут повторяться
     * Поэтому используем именно TreeMap, а не TreeSet, чтобы для каждого из значений полей хранить список записей, которые соответствуют этому значению
     */
    private final TreeMap<Long, List<Record>> redBlackTreeAccount = new TreeMap<>();
    private final TreeMap<String, List<Record>> redBlackTreeName = new TreeMap<>();
    private final TreeMap<Double, List<Record>> redBlackTreeValue = new TreeMap<>();


    /**
     * Метод, который возвращает все записи по ключу name
     *
     * @param name ключ, по которому возвращаются все записи
     * @return список записей (пустой список, если записей не было найдено)
     */
    public List<Record> getAllRecordByName(String name) {
        boolean flag = isNameExists(name);
        if (!flag) {
            // сюда мы попадаем, если у нас нет записей с ключом name
            return new ArrayList<>();
        } else {
            return redBlackTreeName.get(name);
        }
    }


    /**
     * Метод, который возвращает все записи по ключу value
     *
     * @param value ключ, по которому возвращаются все записи
     * @return список записей (пустой список, если записей не было найдено)
     */
    public List<Record> getAllRecordByValue(double value) {
        boolean flag = isValueExists(value);
        if (!flag) {
            // сюда мы попадаем, если у нас нет записей с ключом value
            return new ArrayList<>();
        } else {
            return redBlackTreeValue.get(value);
        }
    }


    /**
     * Метод, который возвращает все записи по ключу account
     *
     * @param account ключ, по которому возвращаются все записи
     * @return список записей (пустой список, если записей не было найдено)
     */
    public List<Record> getAllRecordByAccount(long account) {

        boolean flag = isAccountExists(account);
        if (!flag) {
            // сюда мы попадаем, если у нас нет записей с ключом account
            return new ArrayList<>();
        } else {
            return redBlackTreeAccount.get(account);
        }
    }


    /**
     * Метод, который удаляет все записи по значению value
     *
     * @param value значение, по которую нужно удалить все записи
     * @return возвращаем true, если успешно прошло удаление и false, если записей с value не было найдено
     */
    public boolean deleteAllByValue(double value) {
        boolean flag = isValueExists(value);
        if (!flag) {
            // сюда мы попадаем, если у нас нет записей с ключом value
            return false;
        }

        List<Record> recordList = redBlackTreeValue.get(value);

        // удаляем все записи, где value равен заданному
        while (!recordList.isEmpty()) {
            deleteRecord(recordList.get(0));
        }

        return true;
    }

    /**
     * Метод, который удаляет все записи по значению name
     *
     * @param name значение, по которую нужно удалить все записи
     * @return возвращаем true, если успешно прошло удаление и false, если записей с name не было найдено
     */
    public boolean deleteAllByName(String name) {
        boolean flag = isNameExists(name);
        if (!flag) {
            // сюда мы попадаем, если у нас нет записей с ключом name
            return false;
        }

        List<Record> recordList = redBlackTreeName.get(name);

        // удаляем все записи, где name равен заданному
        while (!recordList.isEmpty()) {
            deleteRecord(recordList.get(0));
        }

        return true;
    }


    /**
     * Метод, который удаляет все записи по значению account
     *
     * @param account значение, по которую нужно удалить все записи
     * @return возвращаем true, если успешно прошло удаление и false, если записей с account не было найдено
     */
    public boolean deleteAllByAccount(long account) {
        boolean flag = isAccountExists(account);
        if (!flag) {
            // сюда мы попадаем, если у нас нет записей с ключом account
            return false;
        }


        List<Record> recordList = redBlackTreeAccount.get(account);

        // удаляем все записи, где account равен заданному
        while (!recordList.isEmpty()) {
            deleteRecord(recordList.get(0));
        }

        return true;
    }


    /**
     * Метод, который изменяет значение поля value у записи
     *
     * @param oldRecord старая запись
     * @param value     новое значение для поля value
     * @return возвращаем true, если успешно прошло изменение и false, если записи oldRecord и не было в нашей БД
     */
    public boolean updateRecordByValue(Record oldRecord, double value) {
        boolean flag = isRecordExists(oldRecord);
        if (!flag) {
            // сюда мы попадаем, если записи oldRecord не было в нашей БД
            return false;
        }

        // удаляем запись
        deleteRecord(oldRecord);

        // устанавливаем новое значение для поля value
        oldRecord.setValue(value);

        // добавляем новую запись
        addRecord(oldRecord);
        return true;
    }

    /**
     * Метод, который изменяет значение поля name у записи
     *
     * @param oldRecord старая запись
     * @param name      новое значение для поля name
     * @return возвращаем true, если успешно прошло изменение и false, если записи oldRecord и не было в нашей БД
     */
    public boolean updateRecordByName(Record oldRecord, String name) {
        boolean flag = isRecordExists(oldRecord);
        if (!flag) {
            // сюда мы попадаем, если записи oldRecord не было в нашей БД
            return false;
        }

        // удаляем запись
        deleteRecord(oldRecord);

        // устанавливаем новое значение для поля name
        oldRecord.setName(name);

        // добавляем новую запись
        addRecord(oldRecord);
        return true;
    }

    /**
     * Метод, который изменяет значение поля account у записи
     *
     * @param oldRecord  старая запись
     * @param newAccount новое значение для поля account
     * @return возвращаем true, если успешно прошло изменение и false, если записи oldRecord и не было в нашей БД
     */
    public boolean updateRecordByAccount(Record oldRecord, long newAccount) {
        boolean flag = isRecordExists(oldRecord);
        if (!flag) {
            // сюда мы попадаем, если записи oldRecord не было в нашей БД
            return false;
        }

        // удаляем запись
        deleteRecord(oldRecord);

        // устанавливаем новое значение account
        oldRecord.setAccount(newAccount);

        // добавляем новую запись
        addRecord(oldRecord);
        return true;
    }

    /**
     * Метод, который изменяет запись на новую, которая подаётся в параметрах
     *
     * @param oldRecord старая запись
     * @param newRecord новая запись
     * @return возвращаем true, если успешно прошло изменение и false, если записи oldRecord и не было в нашей БД
     */
    public boolean updateRecord(Record oldRecord, Record newRecord) {
        boolean flag = isRecordExists(oldRecord);
        if (!flag) {
            // сюда мы попадаем, если записи oldRecord не было в нашей БД
            return false;
        }

        // удаляем текущую запись
        deleteRecord(oldRecord);

        // добавляем новую запись
        addRecord(newRecord);
        return true;
    }

    /**
     * Метод, который будет удалять из нашей БД запись
     *
     * @param record запись, которую нужно удалить
     * @return возвращаем true, если удаление прошло успешно, и возвращаем false, если такой записи не было изначально в нашей БД перед удалением
     */
    public boolean deleteRecord(Record record) {
        // для начала проверяем существует ли такая запись в нашей БД
        boolean flag = isRecordExists(record);
        if (!flag) {
            // это означает, что такой записи нет в нашей БД
            return false;
        }

        // теперь нужно удалить запись record из трёх TreeMap
        deleteFromTreeAccount(record);
        deleteFromTreeValue(record);
        deleteFromTreeName(record);


        return true;
    }


    /**
     * Метод для добавления новой записи
     *
     * @param record запись, которую мы хотим добавить
     * @return возвращаем значение true, если всё успешно добавилось и false - если такая запись уже существует
     */
    public boolean addRecord(Record record) {
        // сначала проверим, есть ли такая запись
        boolean flag = isRecordExists(record);
        if (flag) {
            // это означает, что такая запись уже есть, и нам её не нужно добавлять
            return false;
        }

        // если такой записи ещё нет, значит мы должны теперь добавить данную запись в каждую из трёх TreeMap
        addToTreeByAccount(record);
        addToTreeByValue(record);
        addToTreeByName(record);
        return true;
    }

    /**
     * Getter для поля redBlackTreeAccount
     * @return красно-чёрное дерево для записей по полю account
     */
    public TreeMap<Long, List<Record>> getRedBlackTreeAccount() {
        return redBlackTreeAccount;
    }

    /**
     * Getter для поля redBlackTreeName
     * @return красно-чёрное дерево для записей по полю name
     */
    public TreeMap<String, List<Record>> getRedBlackTreeName() {
        return redBlackTreeName;
    }


    /**
     * Getter для поля redBlackTreeValue
     * @return красно-чёрное дерево для записей по полю value
     */
    public TreeMap<Double, List<Record>> getRedBlackTreeValue() {
        return redBlackTreeValue;
    }


    /**
     * Метод, который проверяет, существует ли уже данная запись в БД
     *
     * @param record запись, которую мы хотим проверить на уникальность
     * @return возвращаем true, если такая запись уже есть, и false в обратном случае
     */
    private boolean isRecordExists(Record record) {
        // так как при добавлении мы в каждую из TreeMap добавляем запись, то можно проверить наличие этой записи
        // в любой из трёх TreeMap. Сделаем это для поля account.

        if (redBlackTreeAccount.containsKey(record.getAccount())) {
            List<Record> recordListByAccount = redBlackTreeAccount.get(record.getAccount());

            // теперь нужно проверить на уникальность записи record в списке recordListByAccount
            return recordListByAccount.contains(record);

        } else {
            // если в TreeMap нет даже ключа record.getAccount(), то такой записи точно нет в нашей БД
            return false;
        }
    }


    /**
     * Метод, который удаляет запись из дерева, где ключ - name
     *
     * @param record запись, которую нужно удалить
     */
    private void deleteFromTreeName(Record record) {
        List<Record> recordList = redBlackTreeName.get(record.getName());
        recordList.remove(record);

        if (recordList.isEmpty()) {
            // если список после удаления остался пустым, это означает
            // что в вершине, где ключ = record.getName() нет данных и его можно удалить, чтобы не заполнять память
            redBlackTreeName.remove(record.getName());
        }
    }


    /**
     * Метод, который удаляет запись из дерева, где ключ - value
     *
     * @param record запись, которую нужно удалить
     */
    private void deleteFromTreeValue(Record record) {
        List<Record> recordList = redBlackTreeValue.get(record.getValue());
        recordList.remove(record);

        if (recordList.isEmpty()) {
            // если список после удаления остался пустым, это означает
            // что в вершине, где ключ = record.getValue() нет данных и его можно удалить, чтобы не заполнять память
            redBlackTreeValue.remove(record.getValue());
        }
    }

    /**
     * Метод, который удаляет запись из дерева, где ключ - account
     *
     * @param record запись, которую нужно удалить
     */
    private void deleteFromTreeAccount(Record record) {
        List<Record> recordList = redBlackTreeAccount.get(record.getAccount());
        recordList.remove(record);

        if (recordList.isEmpty()) {
            // если список после удаления остался пустым, это означает
            // что в вершине, где ключ = record.getAccount() нет данных и его можно удалить, чтобы не заполнять память
            redBlackTreeAccount.remove(record.getAccount());
        }
    }

    /**
     * Проверяет, есть ли в БД записи со значением name
     *
     * @param name ключ, который мы хотим проверять в записях
     * @return возвращаем true, если записи со значением name имеются, и false в обратном случае
     */
    private boolean isNameExists(String name) {
        return redBlackTreeName.containsKey(name);
    }

    /**
     * Проверяет, есть ли в БД записи со значением value
     *
     * @param value ключ, который мы хотим проверять в записях
     * @return возвращаем true, если записи со значением name имеются, и false в обратном случае
     */
    private boolean isValueExists(double value) {
        return redBlackTreeValue.containsKey(value);
    }

    /**
     * Проверяет, есть ли в БД записи со значением account
     *
     * @param account ключ, который мы хотим проверять в записях
     * @return возвращаем true, если записи со значением account имеются, и false в обратном случае
     */
    private boolean isAccountExists(long account) {
        return redBlackTreeAccount.containsKey(account);
    }

    /**
     * Метод, который добавляет в дерево, где ключ - name, новую запись
     *
     * @param record запись, которую нужно добавить
     */
    private void addToTreeByName(Record record) {
        // сначала проверяем, есть ли ключ record.getName()
        if (redBlackTreeName.containsKey(record.getName())) {
            List<Record> recordList = redBlackTreeName.get(record.getName());
            recordList.add(record);
        } else {
            // если ключа record.getName() нет в нашем дереве, то создаём новую пару в нашем TreeMap
            List<Record> recordList = new ArrayList<>();
            recordList.add(record);
            redBlackTreeName.put(record.getName(), recordList);
        }
    }


    /**
     * Метод, который добавляет в дерево, где ключ - value, новую запись
     *
     * @param record запись, которую нужно добавить
     */
    private void addToTreeByValue(Record record) {
        // сначала проверяем, есть ли ключ record.getValue()
        if (redBlackTreeValue.containsKey(record.getValue())) {
            List<Record> recordList = redBlackTreeValue.get(record.getValue());
            recordList.add(record);
        } else {
            // если ключа record.getValue() нет в нашем дереве, то создаём новую пару в нашем TreeMap
            List<Record> recordList = new ArrayList<>();
            recordList.add(record);
            redBlackTreeValue.put(record.getValue(), recordList);
        }
    }

    /**
     * Метод, который добавляет в дерево, где ключ - account, новую запись
     *
     * @param record запись, которую нужно добавить
     */
    private void addToTreeByAccount(Record record) {
        // сначала проверяем, есть ли ключ record.getAccount()
        if (redBlackTreeAccount.containsKey(record.getAccount())) {
            List<Record> recordList = redBlackTreeAccount.get(record.getAccount());
            recordList.add(record);
        } else {
            // если ключа record.getAccount() нет в нашем дереве, то создаём новую пару в нашем TreeMap
            List<Record> recordList = new ArrayList<>();
            recordList.add(record);
            redBlackTreeAccount.put(record.getAccount(), recordList);
        }
    }
}
```
***
<br/>
<br/>

# Тестовое задание для стажера (Java-разработчик в команду технологий фонт-офиса)
# Задача 1
# Описание задачи
Надо написать программу, в которой 6 тредов: Chandler, Joey, Monica, Phoebe, Rachel и Ross разыгрывают в 
консоли сценки из ситкома. Каждый печатает свое имя и реплику. Например, так:

![image](https://github.com/DenisStepanidenko/AtonProblems/assets/110686828/edd50ef0-bd57-49bb-b9f0-004144907284)

Сценка дана в тексте такого формата:

![image](https://github.com/DenisStepanidenko/AtonProblems/assets/110686828/c153f232-fa6d-4ead-b238-439747338ce0) 
 
Понятно, что можно просто напечатать текст в одном потоке, но такое решение не будет принято. Цель задания -
показать, что вы умеете работать с многопоточностью. Язык программирования может быть любой, но очевидно, там 
должны быть треды и способы синхронизации (т.е. JavaScript, например, не подойдет). 
Предпочтительны языки для JVM.

# Решение задачи
На всякий случай загрузил код на GitHub, если так удобнее его смотреть, то можно тыкнуть по [ссылочке](https://github.com/DenisStepanidenko/AtonProblems/tree/master/Multithreading).   
Язык программирования - **Java**.

# Класс Character (Класс, который представляет собой персонажа из реплики)
```java
import java.util.concurrent.Semaphore;

/**
 * Класс, который представляет собой персонажа из реплики
 */
public class Character implements Runnable {
    private final Semaphore currentSemaphore;
    private final Semaphore[] nextSemaphores;
    private final String[] words;
    private final String name;
    private int currentReplica;

    public Character(String name, Semaphore currentSemaphore, Semaphore[] nextSemaphores, String[] words) {
        this.name = name;
        this.currentSemaphore = currentSemaphore;
        this.nextSemaphores = nextSemaphores;
        this.words = words;
    }

    @Override
    public void run() {
        while (currentReplica < words.length) {
            try {
                /**
                 * Захватываем ресурс для текущего семафора
                 */
                currentSemaphore.acquire();
                System.out.println(name + ": " + words[currentReplica]);


                if(currentReplica == nextSemaphores.length){
                    /**
                     * Можно заметить, что для каждой реплики есть следующая, кроме последней - после неё ничего нет
                     * Именно этот случай мы обрабатываем здесь, чтобы избежать ArrayIndexOutOfBoundsException и успешно завершить этот поток
                     */
                    break;
                }

                /**
                 * Освобождаем ресурс семафора для следующей реплики
                 */
                nextSemaphores[currentReplica].release();
                currentReplica++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```

# Класс Main (Класс, в котором иницализирует все персонажи, реплики и происходит тестирование)
```java
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {



        /**
         * Инициализируем семафоры для всех персонажей
         */
        Semaphore semaphoreJoey = new Semaphore(1);
        Semaphore semaphoreChandler = new Semaphore(0);
        Semaphore semaphorePhoebe = new Semaphore(0);
        Semaphore semaphoreMonica = new Semaphore(0);

        /**
         * Инициализируем массив строк со словами всех персонажей
         */
        String[] wordsOfJoey = {"Hey , hey.", "Yes, I am. As of today. I am officially Joey Tribbiani, actor slash model.", "You know those posters for the City Free Clinic?", "No, but I hear lyme disease is open, so... (crosses fingers)", "Thanks."};
        String[] wordsOfChandler = {"Hey.", "And this from the cry-for-help department. Are you wearing makeup?", "That's so funny, cause I was thinking you look more like Joey Tribianni, man slash woman.", " Do you know which one you're gonna be?", "Good luck. man. I hope you get it."};
        String[] wordsOfPhoebe = {"Hey.", "What were you modeling for?", "You know, the asthma guy was really cute.",};
        String[] wordsOfMonica = {"Oh, wow, so you're gonna be one of those \"healthy, healthy, healthy guys\"?"};


        /**
         * Создаём персонажей
         */
        Character joey = new Character("Joey", semaphoreJoey, new Semaphore[]{semaphoreChandler, semaphoreChandler, semaphoreMonica, semaphoreChandler}, wordsOfJoey);
        Character chandler = new Character("Chandler", semaphoreChandler, new Semaphore[]{semaphorePhoebe, semaphoreJoey, semaphorePhoebe, semaphoreJoey, semaphoreJoey}, wordsOfChandler);
        Character phoebe = new Character("Phoebe", semaphorePhoebe, new Semaphore[]{semaphoreChandler, semaphoreJoey, semaphoreChandler}, wordsOfPhoebe);
        Character monica = new Character("Monica", semaphoreMonica, new Semaphore[]{semaphorePhoebe}, wordsOfMonica);


        /**
         * На основе созданных персонажей создаём потоки
         */
        Thread threadJoey = new Thread(joey);
        Thread threadChandler = new Thread(chandler);
        Thread threadPhoebe = new Thread(phoebe);
        Thread threadMonica = new Thread(monica);


        /**
         * Запускаем все потоки
         */
        threadJoey.start();
        threadChandler.start();
        threadMonica.start();
        threadPhoebe.start();
    }
}
```

<br/>
<br/>

# Задача 2
# Описание задачи
Представим на время, что вы black hat, и у вас в распоряжении оказались данные ~30 млн. заказов некоторого сервиса
доставки еды за некоторый период 2021 и 2022 года.
Поверхностный анализ показал, что данные содержат 18 758 328 уникальных телефонов с полным именем клиента, а
средняя длина полного имени - 20 символов (латинских или кириллических).
Допустим, вы хотите развернуть веб-сервис, который позволит по номеру телефона найти полное имя клиента. Но вы не
хотите оставлять следы на диске или в базе данных - придется все держать в памяти. Но еще вы не хотите зря тратить
крипту на слишком большой сервер.
Поэтому давайте оценим, сколько памяти займут эти данные:
  1) если мы хотим реализовать поиск за постоянное время, т.е. O(1)?
  2) если мы хотим занять как можно меньше памяти?
     
Самое главное - объяснить, как вы пришли к той или иной числовой оценке.  
Можно выбрать любой язык программирования/платформу.
 
# Решение задачи
# 1) Если мы хотим реализовать поиск за постоянное время, т.е. O(1)?
Если мы хотим реализовать поиск по номеру телефона за постоянное время, то следует использовать хэш-таблицу, в Java можно воспользоваться **HashMap** - структура данных, которая реализует интерфейс *Map* (стоит заметить, что в HashMap все ключи должны быть уникальны, но данное требование в рамках нашей задачи выполняется, потому что в условии говорится об уникальности телефонов пользователей).  

Данная структура хранит пары **ключ-значение**. Понятное дело, что в качестве значения мы используем полное имя клиента, то есть очевидно это тип данных **String**.  
А вот над ключом, который представляет из себя номер телефона можно порассуждать. В теории его можно хранить либо в типе данных **String**, либо **int** или же **long**.  
Но вот давайте посмотрим на максимальное значение int, оно равняется **2147483647**, здесь хранится 10 знаков, но конечно есть номера, в которых более 10 цифр, и так как в задании не сказано телефоны каких стран/городов используются, то нужно рассуждать от самого наихудшего случая, что точно будет номер, который содержит больше, чем 10 цифр. По этой причине мы не берём тип int в качестве ключа.   
Рассмотрим тип данных **String** и кодировку **UTF-8**, где 1 цифра кодируется 1 байтом. И также рассмотрим самый худший случай, что будет много телефонных номеров, где количество цифр > 10, то есть в теории каждый номер будет занимать в худшем случае > 10 байт, если использовать тип данных **String**. Но как мы можем заметить, тип **long** точно покрывает все телефонные номера (если верить интернету, то самый длинный телефонный номер около 16-18 знаков, а это влезает в long), и занимает данный тип всего лишь 8 байт.   
Поэтому выбор в качестве ключа для экономии памяти пал на тип данных **long**.     
Далее идёт полное имя клиента, то есть у нас это тип String. Возьмём UTF-8, где латинский символ кодируется одним байтом и кириллица двумя байтами. Также рассуждаем в самом худшем случае, что у нас все 20 символов кириллица, то есть полное имя одного клиента будет занимать **20 * 2 = 40 байт** (рассуждаем в худшем случае, чтобы точно хватило памяти даже в самых крайних случаях)

То есть по итогу мы в HashMap будем хранить пары <Long , String>. И таких пар будет ровно 18_758_328, так как столько уникальных ключей, а каждый ключ создаёт новую пару.    
      
Каждая пара в HashMap представляет из себя узел (Node), который хранит в себе 4 поля:
 1) final int hash
 2) final K key
 3) V value
 4) Node<K,V> next

Поле hash типа int, то есть занимает **4 байта**. Ключ key как мы выяснили ранее типа long, то есть занимает **8 байт**. Значение value у нас это полное имя клиента, то есть как вы выяснили ранее это **40 байт**. И также ссылка next на следующий узел, возьмём **32-bit JVM**, там каждая ссылка занимает **4 байта**. По итогу у нас выходит **56 байт** на одну Node.  

**У нас всего 18_758_328 узлов, то есть всего будет занято 18_758_328 * 56 байт = 105046638 Б = 1025846,0625 КБ = 1001,8028 МБ = 0.97 ГБ**
(*значение на практике может быть меньше, так как мы старались брать в самом худшем случае, чтобы памяти точно хватило. Например если какие-то полные имена клиентов совпадают, то благодаря String Pool будет просто две ссылки на один и тот же объект типа String, или же многие имена будут на латинице, что уменьшит размер полного имени клиента.*)   
**Ответ: 0.97 ГБ**   

# 2) Если мы хотим занять как можно меньше памяти? 
Если мы хотим занять как можно меньше памяти, то из прошлого пункта нужно убрать поле **final int hash** и  **Node<K,V> next** и оставить только полное имя и телефон пользователя (конечно же мы потеряем в скорости, но по условию данного пункта нам нужно минимизировать затраты памяти, а не скорости).  

Далее стоит задуматься о структуре данных, в которых можно хранить имя и телефон пользователя так, чтобы сделать соответствие между ними, и так как мы хотим минимизировать затраты памяти, то кроме этих данных мы ничего не должны хранить (*поэтому варианты ,например, с деревьями, где в узлах хранятся цифры и в конечных узлах хранятся имена отпадает, так как все телефоны могут быть очень длинными и много памяти уйдёт на хранение указателей, потому что от каждой цифры исходит 10 указателей (10 цифр), а это минимум в 32-bit JVM 40 байт, и даже если хранить каждую цифру в типе данных byte, то номер если состоит из больше чем десяти цифр, то уйдёт > 10 байт, а мы все телефоны покрыли типом long, который занимает 8 байт всего лишь*).   

Поэтому предлагаю завести **первый массив c типом данных long**, где будут храниться номера телефонов пользователей, и он будет размером ровно 18_758_328. Такой массив будет занимать **18_758_328 * 8 Б = 150066624 Б = 146549,4375 КБ = 143,11469 МБ = 0.13976 ГБ**. Поиск по такому массиву по номеру телефона будет заниматься **O(n)**, где n - размер массива (*но если заранее сделать сортировку, то потом можно на запросы отвечать за log(n), используя бинарный поиск*)  
  
И заведём второй **второй массив с типом данных String**, где будут храниться полные имена клиентов (заполним его так, чтобы номеру в массиве номеров с индексом i, в массиве с именами под индексом i соответствовало его имя), и он будет также размером 18_758_328. Полное имя клиента, как мы выяснили в 1 пунктке в худшем случае занимает 40 байт. Значит весь массив будет занимать **18_758_328 * 40 Б = 750333120 Б = 732747,1875 КБ = 715,57343 МБ = 0,698802 ГБ**.  

И алгоритм заключается в следующем - например на вход нам подаётся номер x, мы находим его индекс в массиве с номерами за O(n) (*или же log(n), если делать сортировку*), и по этому индексу за O(1) получаем в массиве с именами полное имя клиента, которому соответствует номер x. И заметим, что при таком подходе мы храним только имена и телефоны, и ничего больше.  

Получается память, которая будет затрачена при таком подходе равняется **0.13976 ГБ + 0,698802 ГБ = 0,838562 ГБ** (*значение на практике может быть меньше, так как мы старались брать в самом худшем случае, чтобы памяти точно хватило. Например если какие-то полные имена клиентов совпадают, то благодаря String Pool будет просто две ссылки на один и тот же объект типа String, или же многие имена будут на латинице, что уменьшит размер полного имени клиента.*).    
**Ответ: 0,838562 ГБ**




