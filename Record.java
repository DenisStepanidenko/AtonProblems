package Problem1MicroServices;

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
