package com.candao.www.printer.listener;

import java.util.List;

import org.springframework.stereotype.Service;
import com.candao.print.entity.PrintData;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.Row;
import com.candao.print.listener.AbstractQueueListener;
import com.candao.print.listener.template.ListenerTemplate;
import com.candao.www.printer.listener.template.XMLTemplateDefinition;

@Service
public class XmlTemplateListener extends AbstractQueueListener {

	@Override
	public PrintData receiveMessage(PrintObj object, ListenerTemplate template) throws Exception {
		if (object == null || template == null) {
			return null;
		}
		System.out.println("XmlTemplateListener receive message");
		return prepareData(object, new PrintData(), template);
	}

	@Override
	protected void printBusinessData(PrintObj object, PrintData socketOut, PrintData writer, ListenerTemplate template)
			throws Exception {
		XMLTemplateDefinition definition = (XMLTemplateDefinition) template;
		List<Row> rows = definition.parse(object);

		for (Row row : rows) {
			writer.flush();
			socketOut.write(definition.getAlign(row.getAlign()));
			socketOut.write(definition.getFont(row.getFont()));
			write(writer, row.getContent());
		}
	}

}
