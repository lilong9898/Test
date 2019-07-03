package com.lilong.algorithm.array;

import com.lilong.algorithm.sort.BaseSort;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 找出两个数组中相同的元素
 * (1) 注意不是最长公共字串问题，只是找出相同元素
 * (2) 两个数组中的相同元素， 值一样的可以有多个，比如
 * {1, 2, 2, 1}和{2, 2}的相同元素是{2, 2}
 *
 */
public class GetSamePart {

    private static int[] a = new int[]{1, 2, 2, 1, 4, 5};
    private static int[] b = new int[]{2, 2 , 3, 9, 2, 1};

    public static void main(String[] args) {
        Integer[] result = getSamePart(a, b);
        BaseSort.display(result);
    }

    private static Integer[] getSamePart(int[] a, int[] b) {
        ArrayList<Integer> samePart = new ArrayList<>();
        HashMap<Integer, Integer> map = new HashMap<>();

        /**
         * {@link HashMap}查找一个key对应的value的时间复杂度， 是o(1)-o(n)
         * 所以每遍历a数组中的一个元素， 就来一次， 整体时间复杂度o(n)-o(n^2)
         * */
        for(int i = 0; i < a.length; i++){
            Integer aNumberCount = map.get(a[i]);
            if(aNumberCount == null){
                map.put(a[i], 1);
            }else{
                map.put(a[i], aNumberCount + 1);
            }
        }

        // 同理，整体时间复杂度o(n)-o(n^2)
        for(int j = 0; j < b.length; j++){
            Integer aNumberCount = map.get(b[j]);
            if(aNumberCount == null || aNumberCount == 0){
                //no-op
            }else{
                map.put(b[j], aNumberCount - 1);
                samePart.add(b[j]);
            }
        }

        Integer[] result = new Integer[samePart.size()];
        return samePart.toArray(result);
    }

}
