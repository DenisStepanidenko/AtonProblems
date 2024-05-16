package Problem1MicroServices;


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
