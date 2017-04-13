package cn.imkarl.urlbuilder;

/**
 * Key-Value组合
 * @version imkarl 2017-04
 */
public class Part<K, V> {
    private K key;
    private V value;

    public Part() {
    }
    public Part(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Part<?, ?> part = (Part<?, ?>) o;

        if (key != null ? !key.equals(part.key) : part.key != null) return false;
        return value != null ? value.equals(part.value) : part.value == null;
    }

    @Override
    public String toString() {
        return "Part{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    public K getKey() {
        return key;
    }
    public void setKey(K key) {
        this.key = key;
    }
    public V getValue() {
        return value;
    }
    public void setValue(V value) {
        this.value = value;
    }
}
