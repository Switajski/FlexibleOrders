Ext.define('MyApp.view.ShippingItemGridPanel', {
	extend : 'MyApp.view.PositionGridPanel',
	alias : 'widget.ShippingItemGrid',
	title : "Auftragbestaetigungspositionen - Shipping items",
	onActionClick : function(view, a, b, column, event, record, f) {
				console.log('ShippingItemGrid - customtransitionfunction');
				var inr = this.getStore().data.items[0].data.orderConfirmationNumber;
				MyApp.getApplication().getController('MyController').deliver(
						"ok", inr, record);

			},
	onPdfClick : function(view, a, b, column, event, record, f){
				var win = window.open('/FlexibleOrders/orderconfirmations/' + record.data.orderConfirmationNumber
						+ '.pdf', '_blank');
				win.focus();
			}
});