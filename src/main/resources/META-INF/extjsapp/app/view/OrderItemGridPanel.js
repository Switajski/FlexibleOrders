Ext.define('MyApp.view.OrderItemGridPanel', {
			extend : 'MyApp.view.PositionGridPanel',
			alias : 'widget.OrderItemGrid',
			title : "Bestellpositionen",
			customicon : 'images/new_ab.png',
			onActionClick : function(view, a, b, column, event, record, f) {
				console.log('orderItemGrid - customtransitionfunction');
				// var secondStore =
				// Ext.ComponentQuery.query('grid[itemid=secondGrid]')[0].getStore();
				var ocnr = this.getStore().data.items[0].data.orderNumber;
				MyApp.getApplication().getController('MyController').confirm(
						"ok", ocnr, record);

			},
			onPdfClick : function(view, a, b, column, event, record, f){
				var win = window.open('/FlexibleOrders/orders/' + record.data.orderNumber
						+ '.pdf', '_blank');
				win.focus();
			}

		});