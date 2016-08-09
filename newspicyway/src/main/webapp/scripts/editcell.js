$.extend($.fn.datagrid.methods, {
	editCell : function(jq, param) {
		return jq.each(function() {
			var opts = $(this).datagrid('options');
			var fields = $(this).datagrid('getColumnFields', true).concat(
					$(this).datagrid('getColumnFields'));
			for (var i = 0; i < fields.length; i++) {
				var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor1 = col.editor;
				if (fields[i] != param.field) {
					col.editor = null;
				}
			}
			$(this).datagrid('beginEdit', param.index);
			for (var i = 0; i < fields.length; i++) {
				var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor = col.editor1;
			}
		});
	}
});

var editIndex = undefined;
function endEditing() {
	if (editIndex == undefined) {
		return true;
	}
	if ($('#dishList').datagrid('validateRow', editIndex)) {
		$('#dishList').datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}
function onClickCell(index, field) {
	if (endEditing()) {
		$('#dishList').datagrid('selectRow', index).datagrid('editCell', {
			index : index,
			field : field
		});
		editIndex = index;
	}
}
var editIndexSecond = undefined;
function endEditingSecond() {
	if (editIndexSecond == undefined) {
		return true;
	}
	if ($('#dishListSecond').datagrid('validateRow', editIndexSecond)) {
		$('#dishListSecond').datagrid('endEdit', editIndexSecond);
		editIndexSecond = undefined;
		return true;
	} else {
		return false;
	}
}
function onClickCellSecond(index, field) {
	if (endEditingSecond()) {
		$('#dishListSecond').datagrid('selectRow', index).datagrid('editCell', {
			index : index,
			field : field
		});
		editIndexSecond = index;
	}
}