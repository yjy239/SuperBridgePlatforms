package com.yjy.rnbridge.RnBridge;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.DelayQueue;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/09/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TransformWriter {
    private static class Node{
        public Node(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        String key;
        Object value;
    }

    private Queue<Node> nodes = new ArrayDeque<>();

    public void TransformWriter(){

    }

    public void walk(Object object){
        if(object == null){
            return;
        }
        try {
            Field[] fields =
                    object.getClass().getDeclaredFields();
            if(fields.length == 0){
                return;
            }
            //首先推入
            int max = fields.length - 1;
            for(int i = 0;i<max;i++){
                Node root = new Node(fields[i].getName(),fields[i].get(object));
                nodes.offer(root);
            }

            while(!nodes.isEmpty()){
                int size = nodes.size();
                //
                for(int i = 0;i<size;i++){
                    Node node = nodes.poll();
                    if(node!=null){
                        Object obj = node.value;
                        Field[] childFields
                                = obj.getClass().getDeclaredFields();

                        for(int j = 0;j<childFields.length;j++){
                            nodes.offer(new Node(childFields[j].getName(),
                                    childFields[j].get(node.value)));
                        }
                    }

                }

            }




        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
