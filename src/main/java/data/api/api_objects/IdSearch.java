package data.api.api_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class IdSearch {

    @Expose
    @SerializedName("id")
    public int id;

    @Expose
    @SerializedName("chat_link")
    public String chatLink;

    @Expose
    @SerializedName("name")
    public String name;

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdSearch idSearch = (IdSearch) o;
        return id == idSearch.id &&
                Objects.equals(chatLink, idSearch.chatLink) &&
                Objects.equals(name, idSearch.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatLink, name);
    }

    @Override
    public String toString() {
        return "IdSearch{" +
                "id=" + id +
                ", chatLink='" + chatLink + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
