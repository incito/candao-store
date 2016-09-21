package com.candao.print.listener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.candao.print.entity.PrintData;
import org.springframework.stereotype.Service;

import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
import com.candao.print.listener.template.ListenerTemplate;

@Service
public class NormalDishListener extends AbstractQueueListener {

    LoggerHelper logger = LoggerFactory.getLogger(NormalDishListener.class);

    public PrintData receiveMessage(PrintObj object, ListenerTemplate template) throws Exception {
        System.out.println("NormalDishListener receive message");
        return prepareData(object, new PrintData(), template);
    }

    protected void printBusinessData(PrintObj object, PrintData socketOut, PrintData writer, ListenerTemplate template) throws Exception {
        String billName = object.getBillName();
        // TODO 临时处理
        PrintDish printDish = object.getpDish().get(0);
        writer.flush();//
        socketOut.write(PrinterConstant.getFdDoubleFont());
        //居中
        socketOut.write(template.setAlignCenter());
        // 单号
        billName = billName + "(" + object.getPrintName() + ")";
        if (billName.contains("退菜单")) {
            billName = "==" + StringUtils.bSubstring2(billName, billName.length()) + "==";
        } else {
            billName = StringUtils.bSubstring2(billName, billName.length());
        }
        //默认20个长度
        String[] portsMsg = StringUtils.getLineFeedText(new String[]{billName}, new Integer[]{20});
        write(writer, portsMsg, true);

//		// 档口名称
//		Object[] portName = template.getPrinterPortMsg(object);
//		this.write(writer, portName);

        writer.flush();
        socketOut.write(template.setClearfont());
        //左对齐
        socketOut.write(template.setAlignLeft());
        writer.write("==========================================\r\n");
        writer.flush();//

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
        // -----------------------------分割区------------------------------------
        writer.write("------------------------------------------\r\n");
        writer.flush();//
        socketOut.write(template.getBodyFont());

        // 处理送菜的问题 [2-3] 表示从2桌子送菜到3号桌子 ，在桌号前加送字
        String special = printDish.getSperequire();
        // String giftMsg = "(礼)-";

        if (!org.springframework.util.StringUtils.isEmpty(special) && special.contains("[")) {

            String[] table = template.getSpecTableMsg(object);
            if (table != null) {
                for (int i = 0; i < table.length; i++) {
                    writer.write(table[i] + "\r\n");
                }
            }

            if (special.endsWith(",")) {
                special = special.substring(0, special.indexOf("["))
                        .concat(special.substring(special.indexOf("]") + 2, special.length()));
            } else {
                special = special.substring(0, special.indexOf("["))
                        .concat(special.substring(special.indexOf("]") + 1, special.length()));
            }

        } else {

            //居中
            socketOut.write(template.setAlignLeft());
            String[] table = template.getTableMsg(object);
            this.write(writer, table);
            //左对齐
            socketOut.write(template.setAlignLeft());
        }

        writer.flush();//
        socketOut.write(template.setClearfont());
        writer.write("------------------------------------------\r\n");

        writer.write(StringUtils.bSubstring2("品项 ", 13) + StringUtils.bSubstring2("数量", 4));
        writer.write(StringUtils.bSubstring2("单位", 2) + "\r\n");
        writer.flush();//
        logger.info("------------------------", "");
        logger.info("[" + object.getCustomerPrinterIp() + "]打印菜品，订单号：" + object.getOrderNo() + "*菜品数量："
                + (object.getpDish() == null ? 0 : object.getpDish().size()), "");
        socketOut.write(template.getBodyFont());

        for (PrintDish it : object.getpDish()) {
            it.setDishName(StringUtils.split2(it.getDishName(), "#"));
            it.setDishUnit(StringUtils.split2(it.getDishUnit(), "#"));
        }


        Object[] text = template.getBodyMsg(object);

        for (int i = 0; i < text.length; i++) {
            writer.write(text[i].toString() + "\r\n");
        }
        writer.flush();

        socketOut.write(template.setClearfont());
        writer.write("------------------------------------------\r\n");
        writer.flush();//

        //时间，张数等信息
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
        for (PrintDish it : object.getpDish()) {
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
        String totalSpecial = object.getpDish().get(0).getGlobalsperequire();
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
