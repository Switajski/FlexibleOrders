Ext.define('MyApp.view.ShippingItemGridPanel', {
			extend : 'MyApp.view.PositionGridPanel',
			alias : 'widget.ShippingItemGrid',
			title : "Zu liefernde Auftragspositionen",
			onActionClick : function(view, a, b, column, event, record, f) {
				console.log('ShippingItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('MyController').deliver(
						"ok", record);

			},
			onPdfClick : function(view, a, b, column, event, record, f) {
				var win = window.open('/FlexibleOrders/reports/orderConfirmations/'
								+ record.data.documentNumber + '.pdf',
						'_blank');
				win.focus();
			},
			onRemoveClick : function(view, a, b, column, event, record, f) {
				console.log('orderItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('MyController').deconfirm(
						"ok", record.data.documentNumber, record);

			}
		});