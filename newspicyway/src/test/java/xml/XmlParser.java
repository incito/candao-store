package xml;

import org.w3c.dom.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016-7-29.
 */
public class XmlParser {

    /**
     * 返回 Employee 对象 return Object of Employee
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws ParseException
     *
     */
    public static Row parese(Element element, Class cls)
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, ParseException {
        Row emp = (Row) cls.newInstance();
        // new一个cls所代表的employee对象，当然这里改为泛型的话 就可以对付其他的XML 这里是一个列子就不用泛型了
        Method[] methods = cls.getMethods();
        // 得到 Employee对象的所有方法
        for (Method me : methods) {
            String methodName = me.getName();
            // 得到方法名
            if (methodName.startsWith("set")) {
                // 取出set方法 应为是要 new一个对象 然后set属性进对象
                String mName = methodName.substring(3).toLowerCase();
                //方法名取出set之后的 也就是属性名，一致转化为小写
                Class[] paramTypes = me.getParameterTypes();
                String parm = paramTypes[0].getName();
                // 得到参数列表
                if (element.getNodeType() == Node.ELEMENT_NODE) {
                    // 如果element 是属性节点就继续 不是就无视掉

                    NamedNodeMap eleAttr = element.getAttributes();
                    //这里先判断element有没有属性 有的话 开始解析属性值
                    if (eleAttr != null){
                        for(int j=0 ;j<eleAttr.getLength();j++){
                            Attr attr = (Attr)eleAttr.item(j);
                            String attrName = attr.getName();
                            String attrValue = attr.getValue();
                            //虽然列子里employee的属性值就1个 还是String 但是 惯例的判断还是不要少的好，以后好用嘛。
                            if(mName.equals(attrName.toLowerCase())){
                                if (parm.equals("java.lang.String")) {
                                    me.invoke(emp, attrValue);
                                    //这里调用set方法开始放入值
                                } else if (parm.equals("java.lang.Double")) {
                                    me.invoke(emp,Double.parseDouble(attrValue));
                                } else if (parm.equals("java.util.Date")) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    me.invoke(emp, sdf.parse(attrValue));
                                }
                            }
                        }
                    }
                    NodeList epmAttr = element.getChildNodes();
                    //得到element 的子节点
                    for (int i = 0; i < epmAttr.getLength(); i++) {
                        Node ele = epmAttr.item(i);
                        //得到第i个子节点
                        if (ele.getNodeType() == Node.ELEMENT_NODE) {
                            //如果这个子节点是属性节点就继续 不是就无视掉 如果你要处理 注释节点和文本节点就加判断 加解析
                            Node info = ele.getChildNodes().item(0);
                            //得到这个子节点的第一个子节点（也就是文本值 name的 就是 名字）
                            if (info.getNodeType() == Node.TEXT_NODE) {
                                //判断一下子节点是不是文本节点..虽然例子只能是文本，但是多判断一句 留着以后好用.....
                                Text text = (Text) info;
                                String massage = text.getWholeText();
                                //得到子节点的值
                                //这里同加入节点的属性值一样 开始调用set方法
                                if (mName.equals(ele.getNodeName().toLowerCase())) {
                                    if (parm.equals("java.lang.String")) {
                                        me.invoke(emp, massage);
                                    } else if (parm.equals("java.lang.Double")) {
                                        me.invoke(emp,Double.parseDouble(massage));
                                    } else if (parm.equals("java.util.Date")) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        me.invoke(emp, sdf.parse(massage));
                                    }
                                }
                            }

                            NamedNodeMap nnmAttr = ele.getAttributes();
                            //如果这个子节点 又属性节点 就在写入属性值 这个列子 就是unit 属性
                            if (nnmAttr != null){
                                for(int j=0 ;j<nnmAttr.getLength();j++){
                                    Attr attr = (Attr)nnmAttr.item(j);
                                    String attrName = attr.getName();
                                    String attrValue = attr.getValue();
                                    if(mName.equals(attrName.toLowerCase())){
                                        if (parm.equals("java.lang.String")) {
                                            me.invoke(emp, attrValue);
                                        } else if (parm.equals("java.lang.Double")) {
                                            me.invoke(emp,Double.parseDouble(attrValue));
                                        } else if (parm.equals("java.util.Date")) {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            me.invoke(emp, sdf.parse(attrValue));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return emp;
    }
}
