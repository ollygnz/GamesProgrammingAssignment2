// Creates the illusion of the constantly scrolling screen for infinite movement by scrolling through the list of things

import java.util.LinkedList;
public class ScreenScroller{

    public static void Scroll(LinkedList<Thing> LL){
        //For the length of the list, obtain the item at index i, get the item's x co-ordinate and speed, then subtract the x value by the speed value
        for (int i=0;i<LL.size();i++){
            Thing temp = LL.get(i);
            int temp2 = temp.getX();
            int temp3 = temp.getS();
            temp.setX(temp2 - temp3);
        }
    }

    public static void ShuffleList(LinkedList<Thing> LL){
        //For the length of the list, obtain the item at index i, check whether the item's x co-ordinate + width is less than zero 
        //(i.e not visible on the screen), and remove the item if so
        for (int i=0;i<LL.size();i++){
            Thing temp = LL.get(i);
            if((temp.getX() + temp.getW()) < 0){
                LL.remove(i);
            }
        }

    }
}

