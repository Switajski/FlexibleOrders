Ext.define('MyApp.view.InvoiceItemGridPanel', {
	extend : 'MyApp.view.PositionGridPanel',
	alias : 'widget.InvoiceItemGrid',
	title : "Rechnungspositionen - Invoice items",
	customicon : 'images/new_ab.png',
	onActionClick : function(view, a, b, column, event, record, f) {
				console.log('invoiceItemGrid - customtransitionfunction');
				var anr = this.getStore().data.items[0].data.invoiceNumber;
				MyApp.getApplication().getController('MyController').complete(
						"ok", anr, record);

			},
	onPdfClick : function(view, a, b, column, event, record, f){
				var win = window.open('/FlexibleOrders/invoices/' + record.data.invoiceNumber
						+ '.pdf', '_blank');
				win.focus();
			}

});