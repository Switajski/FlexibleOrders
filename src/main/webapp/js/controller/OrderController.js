Ext.define('MyApp.controller.OrderController', {
	debug : true,
	extend : 'Ext.app.Controller',

	id : 'OrderController',
	models : ['ItemData'],
	stores : ['CreateOrderDataStore'],
	views : ['OrderWindow'],

	init : function(application) {
		this.control({
					'#ErstelleBestellungButton' : {
						click : this.onOrder
					}
				});
	},

	order : function(button, event, option) {
		var form = Ext.getCmp('OrderWindow').down('form').getForm();
		var record = form.getRecord();

		var request = Ext.Ajax.request({
			url : '/FlexibleOrders/transitions/order',
			jsonData : {
				orderNumber : form.getValues().order,
				created : form.getValues().created,
				invoiceNumber : form.getValues().invoiceNumber,
				packageNumber : form.getValues().packageNumber,
				trackNumber : form.getValues().trackNumber,
				customerId : record.data.customerNumber,
				name1 : record.data.name1,
				name2 : record.data.name2,
				street : record.data.street,
				postalCode : record.data.postalCode,
				city : record.data.city,
				country : record.data.country,
				items : Ext.pluck(
						Ext.getCmp('CreateOrderGrid').getStore().data.items,
						'data')
			},
			success : function(response) {
				var text = response.responseText;
				// Sync
				controller = MyApp.getApplication().getController('MyController');
				controller.sleep(500);
				controller.syncAll();
				Ext.getCmp('CreateOrderGrid').getStore().removeAll();
				Ext.getCmp("OrderWindow").close();
			}
		});
	},

	onOrder : function(button, event, option) {
		// check customer is chosen
		var customer = MyApp.getApplication().getController('MyController')
				.retrieveChosenCustomerSavely();
		if (customer == null)
			return;

		var orderWindow = Ext.create('MyApp.view.OrderWindow', {
					id : "OrderWindow",
					record : customer,
					onShow : function() {
						this.down('form').getForm().loadRecord(customer);
					}
				});
		orderWindow.show();
		orderWindow.focus();
	},
	
	deleteOrder : function(orderNumber){
		var request = Ext.Ajax.request({
			url : '/FlexibleOrders/transitions/deleteOrder',
			params : {
				orderNumber : orderNumber
			},
			success : function(response) {
				controller = MyApp.getApplication().getController('MyController');
				controller.sleep(500);
				controller.syncAll();
			}
		});
		
	}

});
