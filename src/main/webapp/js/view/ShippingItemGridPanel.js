Ext.define('MyApp.view.ShippingItemGridPanel', {
			extend : 'MyApp.view.PositionGridPanel',
			alias : 'widget.ShippingItemGrid',
			title : "Auftragsbest&auml;tigungen / Auftr&auml;ge",
			id : 'ShippingItemGrid',
			onActionClick : function(view, a, b, column, event, record, f) {
				if (record.get('agreed') === false)
					MyApp.getApplication().getController('AgreementController').onAgree("ok", record);
				else 
					MyApp.getApplication().getController('DeliverController').deliver("ok", record);
			}
		});