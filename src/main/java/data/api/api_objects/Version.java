package data.api.api_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Version {

    @Expose
    @SerializedName("id")
    public int version;

    @Override
    public String toString() {
        return "Version{" +
                "version=" + version +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version1 = (Version) o;
        return version == version1.version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }

}
