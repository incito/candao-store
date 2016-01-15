package kaiying;
 
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
public class Testbj {
	public static void main(String[] args) {
		BigDecimal amount= new BigDecimal("86.688543");
		System.out.println(amount.setScale(2, RoundingMode.HALF_UP));
	}
     
//    public static void main(String[] args) {
//        Set<Integer> result = new HashSet<Integer>();
//        Set<Integer> set1 = new HashSet<Integer>(){{
//            add(1);
//            add(3);
//            add(5);
//        }};
//         
//        Set<Integer> set2 = new HashSet<Integer>(){{
//            add(1);
//            add(2);
//            add(3);
//        }};
//         
//        result.clear();
//        result.addAll(set1);
//        result.retainAll(set2);
//        System.out.println("交集："+result);
//         
//        result.clear();
//        result.addAll(set1);
//        result.removeAll(set2);
//        System.out.println("差集："+result);
//         
//        result.clear();
//        result.addAll(set1);
//        result.addAll(set2);
//        System.out.println("并集："+result);
//        
//        String[] a = {
//                "01", "03", "05", "06"
//        };
//        String[] b = {
//                "03", "03", "05", "05"
//        };
//        //生成不变List
//        List listA = Arrays.asList(a);
//        List listB = Arrays.asList(b);
//        System.out.println("数组a是否包含数组b："+listA.containsAll(listB));
//        List<Integer> A=new ArrayList<>();
//        A.add(1);
//        List<Integer> B=null;
//        System.out.println("数组a是否包含数组b："+A.containsAll(B));
//         
//    }
 
}