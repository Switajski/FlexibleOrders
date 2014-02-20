Ext.define('MyApp.view.OrderItemGridPanel', {
			extend : 'MyApp.view.PositionGridPanel',
			alias : 'widget.OrderItemGrid',
			title : "Bestellungen",
			customicon : '/FlexibleOrders/images/new_ab.png',
			onActionClick : function(view, a, b, column, event, record, f) {
				console.log('orderItemGrid - customtransitionfunction');
				var ocnr = record.data.orderNumber;
				MyApp.getApplication().getController('MyController').confirm(
						"ok", ocnr, record);

			},
			onPdfClick : function(view, a, b, column, event, record, f){
				var win = window.open('/FlexibleOrders/reports/orders/' + record.data.orderNumber
						+ '.pdf', '_blank');
				win.focus();
			}

		});