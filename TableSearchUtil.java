package Starter_App.Starter_Ap;

import java.util.Locale;
import java.util.function.Function;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public final class TableSearchUtil {
    private TableSearchUtil() {
    }

    @SafeVarargs
    public static <T> FilteredList<T> connectSearch(
        TextField searchField,
        TableView<T> tableView,
        ObservableList<T> sourceData,
        Function<T, ?>... searchableFields
    ) {
        FilteredList<T> filteredData = new FilteredList<>(sourceData, row -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String query = normalize(newValue);

            filteredData.setPredicate(row -> {
                if (query.isEmpty()) {
                    return true;
                }

                for (Function<T, ?> field : searchableFields) {
                    Object value = field.apply(row);
                    if (value != null && normalize(String.valueOf(value)).contains(query)) {
                        return true;
                    }
                }

                return false;
            });
        });

        tableView.setItems(filteredData);
        return filteredData;
    }

    private static String normalize(String text) {
        if (text == null) {
            return "";
        }

        return text.trim().toLowerCase(Locale.ROOT);
    }
}
