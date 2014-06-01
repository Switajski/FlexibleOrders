Ext.define('MyApp.view.ShippingItemGridPanel', {
			extend : 'MyApp.view.PositionGridPanel',
			alias : 'widget.ShippingItemGrid',
			title : "Auftragsbest&auml;tigungen",
			onActionClick : function(view, a, b, column, event, record, f) {
				console.log('ShippingItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('DeliverController').deliver(
						"ok", record);
			},
			onPdfClick : function(view, a, b, column, event, record, f) {
				var win = window
						.open(	'/FlexibleOrders/reports/orderConfirmations/'
										+ record.data.documentNumber + '.pdf',
								'_blank');
				win.focus();
			},
			onRemoveClick : function(view, a, b, column, event, record, f) {
				console.log('orderItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('MyController')
						.deleteReport(record.data.orderConfirmationNumber);
			}
		});