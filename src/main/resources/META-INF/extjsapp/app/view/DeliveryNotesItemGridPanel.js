Ext.define('MyApp.view.DeliveryNotesItemGridPanel', {
	extend : 'MyApp.view.PositionGridPanel',
	alias : 'widget.DeliveryNotesItemGrid',
	title : "Lieferscheine",
	customicon : '/FlexibleOrders/images/new_ab.png',
	onActionClick : function(view, a, b, column, event, record, f) {
				console.log(record);
				MyApp.getApplication().getController('MyController').invoice(
						"ok", record);

			},
	onPdfClick : function(view, a, b, column, event, record, f){
				var win = window.open('/FlexibleOrders/reports/deliveryNotes/' + record.data.documentNumber
						+ '.pdf', '_blank');
				win.focus();
			},
	onRemoveClick: function(view, a, b, column, event, record, f) {
				console.log('deliveryNotesItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('MyController').deleteReport(
						record.data.deliveryNotesNumber);

			}


});