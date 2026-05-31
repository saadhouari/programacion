package views;

final class ComboItem<T> {

    private final T value;
    private final String label;

    ComboItem(T value, String label) {
        this.value = value;
        this.label = label;
    }

    T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return label;
    }
}
