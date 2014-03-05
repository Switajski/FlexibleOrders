Ext.define('MyApp.view.DeliveryNotesItemGridPanel', {
	extend : 'MyApp.view.PositionGridPanel',
	alias : 'widget.DeliveryNotesItemGrid',
	title : "Lieferscheine",
	customicon : '/FlexibleOrders/images/new_ab.png',
	onActionClick : function(view, a, b, column, event, record, f) {
				console.log('deliveryNotesItemGrid - customtransitionfunction');
				var anr = record.data.invoiceNumber;
				
				MyApp.getApplication().getController('MyController').invoice(
						"ok", anr, record);

			},
	onPdfClick : function(view, a, b, column, event, record, f){
				var win = window.open('/FlexibleOrders/reports/deliveryNotes/' + record.data.documentNumber
						+ '.pdf', '_blank');
				win.focus();
			},
	onRemoveClick: function(view, a, b, column, event, record, f) {
				console.log('invoiceItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('MyController').withdraw(
						"ok", record);

			}


});