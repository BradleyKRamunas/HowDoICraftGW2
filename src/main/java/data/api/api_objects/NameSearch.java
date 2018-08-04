package data.api.api_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class NameSearch {

    @Expose
    @SerializedName("results")
    List<Item> items;

    public class Item {

        @Expose
        @SerializedName("data_id")
        public int id;

        @Expose
        @SerializedName("name")
        public String name;

        @Override
        public String toString() {
            return "Item{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return id == item.id &&
                    Objects.equals(name, item.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }

    }

    public int getItemId() {
        if(items != null && items.size() >= 1){
            return items.get(0).id;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "NameSearch{" +
                "items=" + items +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameSearch that = (NameSearch) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

}
