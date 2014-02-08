Ext.define('MyApp.view.InvoiceItemGridPanel', {
	extend : 'MyApp.view.PositionGridPanel',
	alias : 'widget.InvoiceItemGrid',
	title : "Nicht abgeschlossene Rechnungspositionen",
	customicon : '/FlexibleOrders/images/new_ab.png',
	onActionClick : function(view, a, b, column, event, record, f) {
				console.log('invoiceItemGrid - customtransitionfunction');
				var anr = record.data.invoiceNumber;
				
				MyApp.getApplication().getController('MyController').complete(
						"ok", anr, record);

			},
	onPdfClick : function(view, a, b, column, event, record, f){
				var win = window.open('/FlexibleOrders/reports/invoices/' + record.data.documentNumber
						+ '.pdf', '_blank');
				win.focus();
			},
	onRemoveClick: function(view, a, b, column, event, record, f) {
				console.log('invvoiceItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('MyController').withdraw(
						"ok", record);

			}


});