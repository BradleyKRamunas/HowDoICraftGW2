package data.api.api_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sun.xml.internal.ws.util.StringUtils;

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

    public int getItemId(String requested) {
        if(items != null && items.size() >= 1){
            int mostSimilarPos = 0;
            double mostSimilarSimilarity = similarity(requested, items.get(0).name);
            for(int i = 0; i<items.size(); i++) {
                double sim = similarity(requested, items.get(i).name);
                if(sim >= mostSimilarSimilarity) {
                    mostSimilarPos = i;
                    mostSimilarSimilarity = sim;
                }
            }
            return items.get(mostSimilarPos).id;
        }
        return -1;
    }

    public static double similarity(String requested, String found) {
        String longer = requested;
        String shorter = found;
        if(requested.length() < found.length()) {
            longer = found;
            shorter = requested;
        }
        int longerLength = longer.length();
        if(longerLength == 0) return 1.0;
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    /**
     * Algorithm provided by https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java
     * StackOverflow User ACDCJunior, Edited by BullyWiiPlaza
     */
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
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
