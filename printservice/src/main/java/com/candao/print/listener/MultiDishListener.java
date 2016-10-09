package com.candao.print.listener;

//import groovy.transform.Synchronized;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintData;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
import com.candao.print.listener.template.ListenerTemplate;

@Service
public class MultiDishListener extends AbstractQueueListener {

    public PrintData receiveMessage(PrintObj object, ListenerTemplate template) throws Exception {
        System.out.println("MultiDishListener receive message");
        return prepareData(object, new PrintData(), template);
    }

    @Override
    protected void printBusinessData(PrintObj object, PrintData socketOut, PrintData writer, ListenerTemplate template)
            throws Exception {
        String billName = object.getBillName();
        List<PrintDish> printDishList = object.getList();

        socketOut.write(PrinterConstant.getFdDoubleFont());
        // 居中
        socketOut.write(template.setAlignCenter());
        // 单号
        billName = billName + "(" + object.getPrintName() + ")";
        //默认20个长度
        String[] portsMsg = StringUtils.getLineFeedText(new String[]{StringUtils.bSubstring2(billName, billName.length())}, new Integer[]{20});
        write(writer, portsMsg, true);
        writer.flush();
//		// 档口名称
//		Object[] portName = template.getPrinterPortMsg(object);
//		this.write(writer, portName);
        // 居左
        socketOut.write(template.setAlignLeft());
        socketOut.write(template.setClearfont());
        writer.write("==========================================\r\n");

        String[] name = {object.getOrderNo(), object.getTimeMsg().substring(0, 10)};
        // 最多显示34个字符
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
            writer.write(StringUtils.bSubstring2("授权人:" + object.getDiscardUserId(), 9) + "\r\n");
        }

        writer.write("------------------------------------------\r\n");
        writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效

        // -------------------------------分割点-----------------------------------
        socketOut.write(template.getBodyFont());

        // 居中
        socketOut.write(template.setAlignLeft());
        String[] table = template.getTableMsg(object);
        this.write(writer, table);
        // 左对齐
        socketOut.write(template.setAlignLeft());

        writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效
        socketOut.write(template.setClearfont());
        writer.write("------------------------------------------\r\n");

        writer.write(StringUtils.bSubstring2("品项 ", 13) + StringUtils.bSubstring2("数量", 4));
        writer.write(StringUtils.bSubstring2("单位", 2) + "\r\n");

        writer.flush();

        socketOut.write(template.getBodyFont());

        for (PrintDish it : printDishList) {
            it.setDishName(StringUtils.split2(it.getDishName(), "#"));
            it.setDishUnit(StringUtils.split2(it.getDishUnit(), "#"));
        }

        Object[] text = template.getBodyMsg(object);

        for (int i = 0; i < text.length; i++) {
            writer.write(text[i].toString() + "\r\n");
        }

        writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效

        socketOut.write(template.setClearfont());

        writer.write("------------------------------------------\r\n");
        writer.flush();// 关键,很重要,不然指令一次性输出,后面指令覆盖前面指令,导致取消放大指令无效

        // 时间，张数等信息
        socketOut.write(template.getBodyFont());

        Object[] tail = template.getTailMsg(object);
        for (int i = 0; i < tail.length; i++) {
            writer.write(tail[i].toString() + "\r\n");
        }
        writer.flush();

        socketOut.write(template.setClearfont());
        writer.write("------------------------------------------\r\n");
        writer.flush();

        // 菜品套餐信息
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
        String totalSpecial = object.getList().get(0).getGlobalsperequire();
        List<String> bufferList = new ArrayList<>();
        if (totalSpecial != null && !totalSpecial.isEmpty()) {
            bufferList.add(totalSpecial);
        }

        socketOut.write(template.getBodyFont());
        // 填写菜品套餐信息
        if (parentDishName != null && !"".equals(parentDishName)) {
            // 套餐备注换行
            String[] dishName = {"全单备注：" + parentDishName};
            Integer[] dishLength = template.getNoteLength();
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
                Integer[] specialLength = template.getNoteLength();
                String[] specialLineFeed = StringUtils.getLineFeedText(specialName, specialLength);
                for (int j = 0; j < specialLineFeed.length; j++) {
                    writer.write(specialLineFeed[j] + "\r\n");
                }
            }
        }
    }

}
