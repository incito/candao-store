package com.candao.www.printer.listener.template;

import java.util.List;

import com.candao.print.entity.PrintObj;
import com.candao.print.entity.Row;

public interface TemplateDefinition {

	public List<Row> parse(PrintObj obj);
}
