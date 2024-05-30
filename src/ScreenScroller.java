
import java.util.LinkedList;
import java.util.Queue;
public class ScreenScroller{

    public static void Scroll(LinkedList<Thing> LL){
        for (int i=0;i<LL.size();i++){
            Thing temp = LL.get(i);
            int temp2 = temp.getX();
            int temp3 = temp.getS();
            temp.setX(temp2 - temp3);
        }
    }

    public static void ShuffleList(LinkedList<Thing> LL){
        for (int i=0;i<LL.size();i++){
            Thing temp = LL.get(i);
            if((temp.getX() + temp.getW()) < 0){
                LL.remove(i);
            }
        }

    }
}

