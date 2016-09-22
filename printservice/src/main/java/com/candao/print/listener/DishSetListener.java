package com.candao.print.listener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;

import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintData;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
import com.candao.print.listener.template.ListenerTemplate;

/**
 * 套餐小票（传菜员使用）监听类
 *
 * @author caicai
 */
@Service
public class DishSetListener extends AbstractQueueListener {

    /**
     * @param message
     * @return
     */
    public String receiveMessage(String message) {
        return null;
    }

    @Override
    protected void printBusinessData(PrintObj object, PrintData socketOut, PrintData writer, ListenerTemplate template)
            throws Exception {
        String billName = object.getBillName();
        List<PrintDish> printDishList = object.getList();

        socketOut.write(PrinterConstant.getFdDoubleFont());

        // 单号
        writer.write("　　　　" + StringUtils.bSubstring2(billName, 6)
                + " \r\n");
        writer.flush();//
        socketOut.write(PrinterConstant.getClear_font());
        writer.write("==========================================\r\n");
        // 一行最多能容下42个字符

        String[] name = {object.getOrderNo(), object.getTimeMsg().substring(0, 10)};
        //最多显示34个字符
        Integer[] len = {22, 10};
        String[] header = StringUtils.getLineFeedText(name, len);
        if (header != null) {
            for (int i = 0; i < header.length; i++) {
                if (i == 0) {
                    writer.write(StringUtils.bSubstring2("账单号:", 4) + header[i] + "\r\n");
                } else {
                    writer.write(StringUtils.getStr(7) + header[i] + "\r\n");
                }
            }
        }

        String[] username = {object.getUserName(), object.getTableArea(), object.getTimeMsg().substring(11)};
        Integer[] length = {12, 10, 8};
        String[] body = StringUtils.getLineFeedText(username, length);
        if (body != null) {
            for (int i = 0; i < body.length; i++) {
                if (i == 0) {
                    writer.write(StringUtils.bSubstring2("服务员:", 4) + body[i] + "\r\n");
                } else {
                    writer.write(StringUtils.getStr(7) + body[i] + "\r\n");
                }
            }
        }

        if (object.getDiscardUserId() != null && !"".equals(object.getDiscardUserId())) {
            writer.write(StringUtils.bSubstring2("授权人:" + object.getDiscardUserId(),
                    9)
                    + "\r\n");
        }

        writer.write("------------------------------------------\r\n");

        writer.flush();//
        socketOut.write(PrinterConstant.getFdDoubleFont());

        String[] tableName = {object.getTableNo()," ", "人数:" + object.getCustnum()};
        Integer[] tableLength = {12,1,8};
        String[] table = StringUtils.getLineFeedText(tableName, tableLength);
        if (table != null) {
            for (int i = 0; i < table.length; i++) {
                writer.write(table[i] + "\r\n");
            }
        }

        writer.flush();//
        socketOut.write(PrinterConstant.getClear_font());

        writer.write("------------------------------------------\r\n");
        //注意格式与客用单保持 一致
        writer.write(StringUtils.bSubstring2("品项 ", 13)
                + StringUtils.bSubstring2("数量", 4)
                + StringUtils.bSubstring2("单位", 2) + "\r\n");

        writer.flush();//
        socketOut.write(PrinterConstant.VerticalFont());

        for (PrintDish it : printDishList) {
            it.setDishName(StringUtils.split2(it.getDishName(), "#"));
            it.setDishUnit(StringUtils.split2(it.getDishUnit(), "#"));
        }

        Object[] text = getPrintText(object, 25, 8, 8);

        for (int i = 0; i < text.length; i++) {
            writer.write(text[i].toString() + "\r\n");
        }

        writer.flush();//

        socketOut.write(PrinterConstant.getClear_font());

        writer.write("------------------------------------------\r\n");

        writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
        socketOut.write(PrinterConstant.VerticalFont());
        writer.write(StringUtils.bSubstring2((printDishList.get(0)
                .getAbbrname() == null ? "　" : printDishList.get(0)
                .getAbbrname()), 4));

        //菜品套餐信息
        String parentDishName = "";
        List<String> buffer = new LinkedList<>();
        for (PrintDish it : printDishList) {
            if (it.getParentDishName() != null && !"".equals(it.getParentDishName())) {
                String parentName = StringUtils.split2(it.getParentDishName(), "#");
                if (!buffer.contains(parentName))
                    buffer.add(parentName);
            }
        }
        for (int i = 0; i < buffer.size(); i++) {
            if (i != 0) {
                parentDishName = parentDishName.concat("，").concat(buffer.get(i));
            } else {
                parentDishName = parentDishName.concat(buffer.get(i));
            }
        }

        // 全单备注
        String totalSpecial = printDishList.get(0).getGlobalsperequire();
        List<String> bufferList = new ArrayList<>();
        if (totalSpecial != null && !totalSpecial.isEmpty()) {
            bufferList.add(totalSpecial);
        }

        String special = "";
        special = StringUtils.bSubstring2(printDishList.get(0)
                .getSperequire() == null ? "" : printDishList.get(0)
                .getSperequire(), 30);
        if (special == null || "null".equals(special)) {
            special = "";
        }

        writer.write(StringUtils.bSubstring3(String.valueOf(object.getOrderseq() == 0 ? "　" : "第" + object
                .getOrderseq() + "张"), 6));
        writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
        socketOut.write(PrinterConstant.getClear_font());
        writer.write(StringUtils.bSubstring2(new SimpleDateFormat("HH:mm:ss")
                .format(Calendar.getInstance().getTime()), 8)
                + "\r\n");

        writer.write("------------------------------------------\r\n");
        writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
        socketOut.write(PrinterConstant.VerticalFont());

        //填写菜品套餐信息
        if (parentDishName != null && !"".equals(parentDishName)) {
            //套餐备注换行
            String[] dishName = {"全单备注：" + parentDishName};
            Integer[] dishLength = {38};
            String[] parentDishNameLineFeed = StringUtils.getLineFeedText(dishName, dishLength);
            parentDishNameLineFeed[0] = parentDishNameLineFeed[0];
            for (int j = 0; j < parentDishNameLineFeed.length; j++) {
                writer.write(parentDishNameLineFeed[j] + "\r\n");
            }
        } else {
            if (bufferList != null && !bufferList.isEmpty()) {
                String temp = bufferList.get(0);
                bufferList.set(0, "全单备注:" + temp);
            }
        }

        // 忌口信息
        if (bufferList != null && !bufferList.isEmpty()) {
            for (String it : bufferList) {
                String[] specialName = {it};
                Integer[] specialLength = {38};
                String[] specialLineFeed = StringUtils.getLineFeedText(specialName, specialLength);
                for (int j = 0; j < specialLineFeed.length; j++) {
                    writer.write(specialLineFeed[j] + "\r\n");
                }
            }
        }
    }

    @Override
    public PrintData receiveMessage(PrintObj object, ListenerTemplate template) throws Exception {
        return prepareData(object, new PrintData(), template);
    }

    private Object[] getPrintText(PrintObj object, int num1, int num2, int num3) throws Exception {
        Object[] res = null;

        List<PrintDish> list = object.getList();

        for (PrintDish it : list) {
            // 校验名称
            String dishName = it.getDishName() == null ? "" : it.getDishName();
            String dishNum = it.getDishNum() == null ? "" : it.getDishNum();
            String dishUnit = it.getDishUnit() == null ? "" : it.getDishUnit();
            String taste = it.getTaste() != null && !it.getTaste().isEmpty() ? "(" + it.getTaste().trim() + ")" : "";
            String Sperequire = it.getSperequire() != null && !it.getSperequire().isEmpty()
                    ? "(" + it.getSperequire().trim() + ")" : "";
            dishName += taste + Sperequire;

            String[] name = {dishName, dishNum, dishUnit};
            Integer[] len = {num1, num2, num3};

            String[] temp = StringUtils.getLineFeedText(name, len);

            res = ArrayUtils.addAll(res, temp);
        }

        return res;
    }
}
