package tech.grasshopper.excel.report.util;

public interface TriConsumer<K, V, S> {

	void accept(K k, V v, S s);

}
