package Bot.util;

import java.util.ArrayList;

public class ArrayListHelper {
    public static <T> ArrayList<ArrayList<T>> chunks(ArrayList<T> bigList,int n){
        ArrayList<ArrayList<T>> chunks = new ArrayList<ArrayList<T>>();
    
        for (int i = 0; i < bigList.size(); i += n) {
            ArrayList<T> chunk = new ArrayList<T>(bigList.subList(i, Math.min(bigList.size(), i + n)));         
            chunks.add(chunk);
        }
    
        return chunks;
    }
}